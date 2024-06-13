/*
	Simple example of loading/rotating/displaying sprites in Blit3D
*/
//memory leak detection
#define CRTDBG_MAP_ALLOC
#ifdef _DEBUG
	#ifndef DBG_NEW
		#define DBG_NEW new ( _NORMAL_BLOCK , __FILE__ , __LINE__ )
		#define new DBG_NEW
	#endif
#endif  // _DEBUG

#include <stdlib.h>
#include <crtdbg.h>
#include <random>

#include "Choco.h"
#include "Blit3D.h"

Blit3D *blit3D = NULL;

//GLOBAL DATA
extern std::mt19937 rng;
double elapsedTime = 0;
float timeSlice = 1.f / 120.f;

//Sprites 
Sprite* backgroundSprite = NULL;
Choco* choco = NULL;
Sprite* lifeSprite = NULL;
Sprite* titleGraphic = NULL;
Sprite* endingGraphic = NULL; //Changes depending on win / loose
std::vector<Sprite*> hammerSpriteList;
std::vector<Sprite*> torchSpriteList;
std::vector<Sprite*> explosionSpriteList;
std::vector<Obstacle> obstacleList;
std::vector<Explosion> explosionList;

//Fonts
AngelcodeFont* WinterWishes70 = NULL;
AngelcodeFont* WinterWishes100 = NULL;

//Initialize level
int level = 0;

//Movement and Game states
Movement playerInput = Movement::NONE;
enum class GameState { TITLESCREEN, START, PLAYING, GAMEOVER };
GameState gameState = GameState::TITLESCREEN;


void MakeLevel()
{
	level++;
	
	//turn on invulnerability
	choco->immunityTimer = 2.f;

	//cleanup old obstacles
	if (!obstacleList.empty())
		obstacleList.clear();

	if (level < 4)
	{
		for (int i = 0; i < level; ++i)
		{
			obstacleList.push_back(ObstacleFactory(MakeRandomObstacle(), obstacleList, i*100));
		}
	}
	else
	{
		for (int i = 0; i < 4; ++i)
		{
			obstacleList.push_back(ObstacleFactory(MakeRandomObstacle(), obstacleList, i*100));
		}
	}

}


void Init()
{
	//seed our RNGs
	InitializeRNG();

	//turn cursor off
	blit3D->ShowCursor(false);

	//load a font
	WinterWishes70 = blit3D->MakeAngelcodeFontFromBinary32("Media\\WinterWishes70.bin");
	WinterWishes100 = blit3D->MakeAngelcodeFontFromBinary32("Media\\WinterWishes100.bin");

	//load our small ship life counter
	lifeSprite = blit3D->MakeSprite(0, 0, 68, 84, "Media\\Bow.png");

	//load our background image: the arguments are upper-left corner x, y, width to copy, height to copy, and file name.
	backgroundSprite = blit3D->MakeSprite(0, 0, 1920, 1080, "Media\\back.png");

	//create a new player
	choco = new Choco;
	for (int i = 0; i < 4; ++i)
		choco->spriteList.push_back(blit3D->MakeSprite(i * 190, 0, 190, 425, "Media\\ChocoSprite.png"));
	choco->x = 960.f;
	choco->y = 380.f;

	//load torch graphics
	for (int i = 0; i < 4; ++i)
		torchSpriteList.push_back(blit3D->MakeSprite(i * 200, 0, 200, 765, "Media\\TorchSprite.png"));

	//load knife graphic
	for (int i = 0; i < 4; ++i)
		hammerSpriteList.push_back(blit3D->MakeSprite(i * 240, 0, 240, 278, "Media\\HammerSprite.png"));

	//set the clear colour
	glClearColor(05.0f, 0.0f, 1.0f, 0.0f);	//clear colour: r,g,b,a 

	//load the title graphic
	titleGraphic = blit3D->MakeSprite(0, 0, 1920, 1080, "Media\\start.png");

	//load explosion graphics
	for (int i = 0; i < 7; ++i)
	{
		explosionSpriteList.push_back(blit3D->MakeSprite(91 + i * 34, 298, 33, 33, "Media\\M484ExplosionSet1.png"));
	}
}

void DeInit(void)
{
	if (choco != NULL) delete choco;
	//any sprites/fonts still allocated are freed automatically by the Blit3D object when we destroy it
}

void Update(double seconds)
{
	//GAME STATES
	switch (gameState)
	{
	case GameState::PLAYING:
		//only update time to a maximun amount - prevents big jumps in 
		//the simulation if the computer "hiccups"
		if (seconds < 0.15)
			elapsedTime += seconds;
		else elapsedTime += 0.15;

		//update by a full timeslice when it's time
		while (elapsedTime >= timeSlice)
		{
			elapsedTime -= timeSlice;
			choco->Update(timeSlice);

			//iterate backwards through the obstacle list,
			//so we can erase shots without messing up the vector
			//for the next loop
			
			for (int i = obstacleList.size() - 1; i >= 0; --i)
			{
				//obstacles Update() returns false when it should be killed off
				if (!obstacleList[i].Update(timeSlice))
					obstacleList.erase(obstacleList.begin() + i);
			}
			
			//iterate backwards through the vector to safely remove explotions
			for (int i = explosionList.size() - 1; i >= 0; --i)
			{
				if (!explosionList[i].Update(timeSlice))
				{
					explosionList.erase(explosionList.begin() + i);
				}
			}
			
			for (auto& O : obstacleList) O.Update(timeSlice);

			
			if (obstacleList.empty())
			{
				MakeLevel();
				break;
			}
			
			for (auto O : obstacleList)
			{
				//check for collision with an obstacle
				if (CollidePlayerWithObstacle(choco, O))
				{
					//take away a life
					choco->lives--;
					//check for game over
					if (choco->lives < 1)
					{
						gameState = GameState::GAMEOVER;
						break;
					}

					//turn on invulnerability
					choco->immunityTimer = 2.f;

					glm::vec2 location = glm::vec2(choco->x, choco->y);

					//make an explosion
					Explosion e(location, 5.f);
					explosionList.push_back(e);
					
				}
			}

		}
		break; //end PLAYING state

	case GameState::START:
		level = 0;
		choco->lives = 3;
		MakeLevel();
		gameState = GameState::PLAYING;
		break;

	case GameState::GAMEOVER:
	case GameState::TITLESCREEN:
	default:
		break;

	}//end gameState switch
}

void Draw(void)
{
	// wipe the drawing surface clear
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	//draw stuff here
	switch (gameState)
	{

	case GameState::TITLESCREEN:
	{
		titleGraphic->Blit(1920.f / 2, 1080.f / 2);
		std::string titleString = "COOKIE RUN!";
		float widthTitle = WinterWishes100->WidthText(titleString);
		WinterWishes100->BlitText((1920.f / 2) - (widthTitle / 2) + 20.f, (1080.f / 2.f) + 200.f, titleString);
		std::string titleString2 = "Pass as many levels as possible!";
		float widthTitle2 = WinterWishes70->WidthText(titleString2);
		WinterWishes70->BlitText(1920.f / 2.f - widthTitle2 / 2.f + 20.f, (1080.f / 2.f), titleString2);
		std::string titleString3 = "A - Move left  D - Move right";
		float widthTitle3 = WinterWishes70->WidthText(titleString3);
		WinterWishes70->BlitText(1920.f / 2.f - widthTitle3 / 2.f + 20.f, (1080.f / 2.f) - 100.f, titleString3);
	}
	break;
	case GameState::GAMEOVER:
	{
		backgroundSprite->Blit(1920.f / 2, 1080.f / 2);

		//draw the player
		choco->Draw();

		//draw our life HUD
		for (int i = 0; i < choco->lives; ++i)
		{
			lifeSprite->Blit(1690 + i * 75, 1080 - 84);
		}

		std::string gameOver = "GAME OVER!";
		float widthText = WinterWishes100->WidthText(gameOver);
		WinterWishes100->BlitText(1920.f / 2.f - widthText / 2.f + 10.f, (1080.f / 2.f) + 140.f, gameOver);
		std::string gameOver2 = "Press E to exit...";
		float widthText2 = WinterWishes70->WidthText(gameOver2);
		WinterWishes70->BlitText(1920.f / 2.f - widthText2 / 2.f + 10.f, (1080.f / 2.f) - 40.f, gameOver2);
		//draw the level
		std::string levelString = std::to_string(level);
		levelString = "Level: " + levelString;
		WinterWishes70->BlitText(50, 1060, levelString);
	}
	break;
	case GameState::START:
	case GameState::PLAYING:
		//draw the background in the middle of the screen
		//the arguments to Blit(0 are the x, y pixel coords to draw the center of the sprite at, 
		//starting as 0,0 in the bottom-left corner.
		backgroundSprite->Blit(1920.f / 2, 1080.f / 2);

		//draw the player
		choco->Draw();

		//draw the obstacles
		for (auto& O : obstacleList) O.Draw();

		//draw explosions
		for (auto& E : explosionList) E.Draw();

		//draw our life HUD
		for (int i = 0; i < choco->lives; ++i)
		{
			lifeSprite->Blit(1690 + i * 75, 1080 - 84);
		}

		//draw the level
		std::string levelString = std::to_string(level);
		levelString = "Level: " + levelString;
		WinterWishes70->BlitText(50, 1060, levelString);
		break;
	}
}

//the key codes/actions/mods for DoInput are from GLFW: check its documentation for their values
void DoInput(int key, int scancode, int action, int mods)
{
	if(key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
		blit3D->Quit(); //start the shutdown sequence

	switch (gameState)
	{
	case GameState::TITLESCREEN:
		if (action == GLFW_RELEASE)
			gameState = GameState::START;
		break;
	case GameState::GAMEOVER:
		if (key == GLFW_KEY_E && action == GLFW_RELEASE)
			blit3D->Quit();
		break;
	case GameState::PLAYING:
		if (key == GLFW_KEY_A && action == GLFW_RELEASE)
			playerInput = Movement::LEFT;
		if (key == GLFW_KEY_D && action == GLFW_RELEASE)
			playerInput = Movement::RIGHT;
		break;
	default:
		break;
	}//end gameState switch
}

int main(int argc, char *argv[])
{
	//memory leak detection
	_CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);

	//set X to the memory allocation number in order to force a break on the allocation:
	//useful for debugging memory leaks, as long as your memory allocations are deterministic.
	//_crtBreakAlloc = X;

	blit3D = new Blit3D(Blit3DWindowModel::BORDERLESSFULLSCREEN_1080P, 640, 400);

	//set our callback funcs
	blit3D->SetInit(Init);
	blit3D->SetDeInit(DeInit);
	blit3D->SetUpdate(Update);
	blit3D->SetDraw(Draw);
	blit3D->SetDoInput(DoInput);

	//Run() blocks until the window is closed
	blit3D->Run(Blit3DThreadModel::SINGLETHREADED);
	if (blit3D) delete blit3D;
}