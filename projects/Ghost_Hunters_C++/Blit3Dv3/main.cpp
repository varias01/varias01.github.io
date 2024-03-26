/*
	"Angry Birds" style example
*/
#include "Blit3D.h"
#include <random>

#include "Box2d/Box2d.h"
#include "GroundEntity.h"
#include "ShotEntity.h"
#include "Particle.h"
#include "Physics.h"
#include "MyContactListener.h" 
#include "EdgeEntity.h"
#include "Cannon.h"
#include "Meter.h"
#include "Camera.h"
#include "BlockEntity.h"
#include "EnemyEntity.h"

#include "CollisionMask.h"

Blit3D *blit3D = NULL;

//memory leak detection
#define CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>

//GLOBAL DATA
b2World *world;
// Prepare for simulation. Typically we use a time step of 1/60 of a
// second (60Hz) and ~10 iterations. This provides a high quality simulation
// in most game scenarios.
int32 velocityIterations = 8;
int32 positionIterations = 3;
float timeStep = 1.f / 60.f; //one 60th of a second
float elapsedTime = 0; //used for calculating time passed
float settleTime = 0;

//contact listener to handle collisions between important objects
MyContactListener *contactListener;

enum GameState { START, PLAYING, SETTLING, GAMEOVER };
GameState gameState = START;
bool attachedShot = true;

std::vector<Entity *> blockEntityList; //blocks go here
std::vector<Entity *> shotEntityList; //track the balls seperately from everything else
std::vector<Entity *> entityList; //other entities in our game go here
std::vector<Entity *> deadEntityList; //dead entities

std::vector<Particle *> particleList;

//Sprites
Sprite* start = NULL;
Sprite* gameOver = NULL;
Sprite* background = NULL;
Sprite *cannonballSprite = NULL;
Sprite *groundSprite = NULL;
Sprite *tsSprite = NULL;
std::vector<Sprite*> enemySprites;

Cannon cannon;

bool fireShotNow = false;
bool followingShot = false; //is the camera tracking the shot?

Meter meter;

Camera2D *camera; //pans the view

std::vector<Sprite *> blockSprites;

std::vector<Sprite *> debrisList;

std::vector<Sprite*> cloudList;

//simple class to hold graphics-only elements of the level
class GraphicElement
{
public:
	Sprite* sprite;
	glm::vec2 pos;
	void Draw()
	{
		sprite->Blit(pos.x, pos.y);
	};
};

std::vector<GraphicElement> elementList;

std::mt19937 rng;

//ensures that entities are only added ONCE to the deadEntityList
void AddToDeadList(Entity *e)
{
	bool unique = true;

	for (auto ent : deadEntityList)
	{
		if (ent == e)
		{
			unique = false;
			break;
		}
	}

	if (unique) deadEntityList.push_back(e);
}

std::uniform_real_distribution<float> randomCircle(0, 360);
std::uniform_real_distribution<float> randomRotationSpeed(-90.f, 90.f);
std::uniform_real_distribution<float> randomSpeed(0.f, 200.f);
std::uniform_real_distribution<float> randomSizeStart(0.1f, 0.5f);
std::uniform_real_distribution<float> randomSizeEnd(0.05f, 0.15f);
std::uniform_int_distribution<int> randomZeroToTwo(0,2);

//Function that applies damage etc when a block collides with anything
void BlockCollide(Entity *A, float maxImpulseAB)
{
	BlockEntity *blockEntity = (BlockEntity *)A;
	//damage?
	if (maxImpulseAB > 0.5f) //cutoff for no damage
	{
		//apply some damage
		if (blockEntity->Damage((int)(maxImpulseAB *30.f)))
		{
			//Damage() returned true, need to kill this block
			AddToDeadList(A);

			//spawn particles here
			//debrisList
			for (int particleCount = 0; particleCount < 10; ++particleCount)
			{

				Particle *p = new Particle();
				p->coords = Physics2Pixels(A->body->GetPosition());
				p->angle = randomCircle(rng);
				p->direction = deg2vec(randomCircle(rng));
				p->rotationSpeed = randomRotationSpeed(rng);
				p->startingSpeed = randomSpeed(rng);
				p->targetSpeed = randomSpeed(rng);
				p->totalTimeToLive = 0.3f;

				p->startingScaleX = randomSizeStart(rng);
				p->startingScaleY = randomSizeStart(rng);
				p->targetScaleX = randomSizeEnd(rng);
				p->targetScaleY = randomSizeEnd(rng);

				p->spriteList.push_back(debrisList[randomZeroToTwo(rng) + ((int)blockEntity->materialType) * 3]);
				particleList.push_back(p);
			}
		}
	}
}

//Function that applies damage when an enemy collides with anything
void EnemyCollide(Entity* A, float maxImpulseAB)
{
	EnemyEntity* enemyEntity = (EnemyEntity*)A;
	//damage?
	if (maxImpulseAB > 0.25f) //cutoff for no damage
	{
		//apply some damage
		if (enemyEntity->Damage((int)(maxImpulseAB * 10.f)))
		{
			//Damage() returned true, need to kill this block
			AddToDeadList(A);
		}
	}
}

void Init()
{
	//seed our rng
	std::random_device rd;
	rng.seed(rd());

	blit3D->ShowCursor(false);

	//make a camera
	camera = new Camera2D();

	//set it's valid pan area
	camera->minX = blit3D->screenWidth / 2;
	camera->minY = blit3D->screenHeight / 2;
	camera->maxX = blit3D->screenWidth * 2 - blit3D->screenWidth/2;
	camera->maxY = blit3D->screenHeight / 2 + 400;

	camera->PanTo(blit3D->screenWidth / 2, blit3D->screenHeight / 2);

	//load the sprites
	start = blit3D->MakeSprite(0, 0, 1920, 1080, "Media\\StartScreen.png");
	background = blit3D->MakeSprite(0, 0, 3840, 1540, "Media\\Sky.png");
	gameOver = blit3D->MakeSprite(0, 0, 1920, 1080, "Media\\Victory.png");

	cannonballSprite = blit3D->MakeSprite(0, 0, 36, 36, "Media\\Plasmaball.png");
	cannon.sprite = blit3D->MakeSprite(0, 0, 440, 128, "Media\\Plasmacannon.png");

	cannon.positionPixels = b2Vec2(100, 150);

	meter.sprite = blit3D->MakeSprite(0, 0, 100, 100, "Media\\Plasmameter.png");
	meter.positionPixels = cannon.positionPixels;

	//ground block + tombstone
	groundSprite = blit3D->MakeSprite(0, 0, 70, 70, "Media\\Ground.png");
	tsSprite = blit3D->MakeSprite(0, 0, 45, 59, "Media\\tombstone.png");

	//enemies
	for (int i = 0; i < 2; i++)
	{
		enemySprites.push_back(blit3D->MakeSprite(70 * i, 0, 70, 70, "Media\\ghosts.png"));
	}

	//load the block sprites (WOOD, METAL, ROCK)
	//Large Triangle - WOOD
	blockSprites.push_back(blit3D->MakeSprite(581, 1841, 138, 68, "Media\\spritesheet_elements.png"));
	blockSprites.push_back(blit3D->MakeSprite(941, 791, 138, 68, "Media\\spritesheet_elements.png"));
	blockSprites.push_back(blit3D->MakeSprite(581, 1911, 138, 68, "Media\\spritesheet_elements.png"));

	//fill up the list with dummy sprites for the materials I am skipping: 
	//no METAL, ROCK for large triangle, no WOOD for small circles 
	for (int i = 0; i < 3 * 3; ++i)
		blockSprites.push_back(tsSprite);
	//Small Circle - METAL
	blockSprites.push_back(blit3D->MakeSprite(1700, 1550, 70, 70, "Media\\spritesheet_elements.png"));
	blockSprites.push_back(blit3D->MakeSprite(1700, 1130, 70, 70, "Media\\spritesheet_elements.png"));
	blockSprites.push_back(blit3D->MakeSprite(1500, 430, 70, 70, "Media\\spritesheet_elements.png"));

	//fill up the list with dummy sprites for the materials I am skipping: 
	//no ROCK for small circles, no WOOD, METAL for squares 
	for (int i = 0; i < 3 * 3; ++i)
		blockSprites.push_back(tsSprite);
	//Square - ROCK
	blockSprites.push_back(blit3D->MakeSprite(1280, 1860, 140, 140, "Media\\spritesheet_elements.png"));
	blockSprites.push_back(blit3D->MakeSprite(800, 440, 140, 140, "Media\\spritesheet_elements.png"));
	blockSprites.push_back(blit3D->MakeSprite(1080, 0, 140, 140, "Media\\spritesheet_elements.png"));

	//load debris sprites
	//WOOD debris
	debrisList.push_back(blit3D->MakeSprite(0, 57, 64, 55, "Media\\spritesheet_debris.png"));
	debrisList.push_back(blit3D->MakeSprite(0, 168, 64, 52, "Media\\spritesheet_debris.png"));
	debrisList.push_back(blit3D->MakeSprite(0, 446, 68, 51, "Media\\spritesheet_debris.png"));
	//METAL debris
	debrisList.push_back(blit3D->MakeSprite(0, 112, 64, 56, "Media\\spritesheet_debris.png"));
	debrisList.push_back(blit3D->MakeSprite(0, 220, 58, 53, "Media\\spritesheet_debris.png"));
	debrisList.push_back(blit3D->MakeSprite(0, 512, 56, 61, "Media\\spritesheet_debris.png"));
	//ROCK debris
	debrisList.push_back(blit3D->MakeSprite(0, 112, 64, 56, "Media\\spritesheet_debris.png"));
	debrisList.push_back(blit3D->MakeSprite(0, 220, 58, 53, "Media\\spritesheet_debris.png"));
	debrisList.push_back(blit3D->MakeSprite(0, 512, 56, 61, "Media\\spritesheet_debris.png"));

	//load cloud sprites
	cloudList.push_back(blit3D->MakeSprite(0, 0, 174, 157, "Media\\cumulus-big1.png"));
	cloudList.push_back(blit3D->MakeSprite(0, 0, 190, 118, "Media\\cumulus-big2.png"));
	cloudList.push_back(blit3D->MakeSprite(0, 0, 512, 211, "Media\\cumulus-huge.png"));
	cloudList.push_back(blit3D->MakeSprite(0, 0, 76, 51, "Media\\cumulus-small2.png"));
	cloudList.push_back(blit3D->MakeSprite(0, 0, 64, 41, "Media\\cumulus-small3.png"));


	//add the graphical elements to the level
	GraphicElement ge;
	//add some cactii
	ge.sprite = tsSprite;
	ge.pos = glm::vec2(500, 90);
	elementList.push_back(ge);
	ge.pos = glm::vec2(1500, 90);
	elementList.push_back(ge);
	ge.pos = glm::vec2(2000, 90);
	elementList.push_back(ge);
	ge.pos = glm::vec2(2150, 90);
	elementList.push_back(ge);
	ge.pos = glm::vec2(3000, 90);
	elementList.push_back(ge);

	//add some clouds
	std::uniform_real_distribution<float> distCloudsx(0, 4000);
	std::uniform_real_distribution<float> distCloudsy(700, 2000);
	std::uniform_int_distribution<int> distCloudsIndex(0, cloudList.size() - 1);

	for (int numClouds = 0; numClouds < 150; ++numClouds)
	{
		ge.sprite = cloudList[distCloudsIndex(rng)];
		ge.pos.x = distCloudsx(rng);
		ge.pos.y = distCloudsy(rng);
		elementList.push_back(ge);
	}

	// Define the gravity vector.
	b2Vec2 gravity(0.f, -9.8f);

	// Construct a world object, which will hold and simulate the rigid bodies.
	world = new b2World(gravity);
	//world->SetGravity(gravity); <-can call this to change gravity at any time
	world->SetAllowSleeping(true); //set true to allow the physics engine to 'sleep" objects that stop moving

								   //_________GROUND OBJECT_____________
								   //make an entity for the ground
	GroundEntity *groundEntity = new GroundEntity();
	//A bodyDef for the ground
	b2BodyDef groundBodyDef;
	// Define the ground body.
	groundBodyDef.position.Set(0, 0);

	//add the userdata
	groundBodyDef.userData.pointer = reinterpret_cast<uintptr_t>(groundEntity);

	// Call the body factory which allocates memory for the ground body
	// from a pool and creates the ground box shape (also from a pool).
	// The body is also added to the world.
	groundEntity->body = world->CreateBody(&groundBodyDef);

	//an EdgeShape object, for the ground
	b2EdgeShape groundBox;

	// Define the ground as 1 edge shape at the bottom of the screen.
	b2FixtureDef boxShapeDef;

	boxShapeDef.shape = &groundBox;

	//collison masking
	boxShapeDef.filter.categoryBits = CMASK_GROUND;  //this is the ground
	boxShapeDef.filter.maskBits = CMASK_SHOT | CMASK_BLOCK | CMASK_ENEMY;		//it collides wth balls and powerups

																				//bottom
	groundBox.SetTwoSided(b2Vec2(0, 70 / PTM_RATIO), b2Vec2(blit3D->screenWidth * 2 / PTM_RATIO, 70 / PTM_RATIO));
	//Create the fixture
	groundEntity->body->CreateFixture(&boxShapeDef);
	
	//add to the entity list
	entityList.push_back(groundEntity);

	//now make the other 3 edges of the screen on a seperate entity/body
	EdgeEntity * edgeEntity = new EdgeEntity();

	groundBodyDef.userData.pointer = reinterpret_cast<uintptr_t>(edgeEntity);
	edgeEntity->body = world->CreateBody(&groundBodyDef);

	boxShapeDef.filter.categoryBits = CMASK_EDGES;  //this is the ground
	boxShapeDef.filter.maskBits = CMASK_SHOT | CMASK_BLOCK | CMASK_ENEMY;		//it collides wth balls

																				//left
	groundBox.SetTwoSided(b2Vec2(0, blit3D->screenHeight * 2 / PTM_RATIO), b2Vec2(0, 70 / PTM_RATIO));
	edgeEntity->body->CreateFixture(&boxShapeDef);

	//top
	groundBox.SetTwoSided(b2Vec2(0, blit3D->screenHeight * 2 / PTM_RATIO),
		b2Vec2(blit3D->screenWidth * 2 / PTM_RATIO, blit3D->screenHeight * 2 / PTM_RATIO));
	edgeEntity->body->CreateFixture(&boxShapeDef);

	//right
	groundBox.SetTwoSided(b2Vec2(blit3D->screenWidth * 2 / PTM_RATIO, 70 / PTM_RATIO),
		b2Vec2(blit3D->screenWidth * 2 / PTM_RATIO, blit3D->screenHeight * 2 / PTM_RATIO));
	edgeEntity->body->CreateFixture(&boxShapeDef);

	entityList.push_back(edgeEntity);

	// Create contact listener and use it to collect info about collisions
	contactListener = new MyContactListener();
	world->SetContactListener(contactListener);

	BlockEntity * block = MakeBlock(BlockType::LARGE_TRIANGLE, MaterialType::WOOD, b2Vec2(1850.f, 100.f), 0.f, 200);
	blockEntityList.push_back(block);
	
	block = MakeBlock(BlockType::LARGE_TRIANGLE, MaterialType::WOOD, b2Vec2(1850.f, 300.f), 0.f, 200);
	blockEntityList.push_back(block);

	block = MakeBlock(BlockType::LARGE_TRIANGLE, MaterialType::WOOD, b2Vec2(1600.f, 240.f), -135.f, 200);
	blockEntityList.push_back(block);

	block = MakeBlock(BlockType::LARGE_TRIANGLE, MaterialType::WOOD, b2Vec2(1600.f, 290.f), 45.f, 200);
	blockEntityList.push_back(block);

	block = MakeBlock(BlockType::SMALL_CIRCLE, MaterialType::METAL, b2Vec2(1400.f, 100.f), 0.f, 50);
	blockEntityList.push_back(block);

	block = MakeBlock(BlockType::SMALL_CIRCLE, MaterialType::METAL, b2Vec2(1480.f, 210.f), 0.f, 50);
	blockEntityList.push_back(block);

	block = MakeBlock(BlockType::SQUARE, MaterialType::ROCK, b2Vec2(1450.f, 140.f), 0.f, 250);
	blockEntityList.push_back(block);

	block = MakeBlock(BlockType::SQUARE, MaterialType::ROCK, b2Vec2(1590.f, 140.f), 0.f, 250);
	blockEntityList.push_back(block);

	EnemyEntity* enemy = MakeEnemy(b2Vec2(1720.f, 100.f), 0.f, 10, 0);
	entityList.push_back(enemy);

	enemy = MakeEnemy(b2Vec2(1600.f, 350.f), 0.f, 10, 1);
	entityList.push_back(enemy);

	enemy = MakeEnemy(b2Vec2(2000.f, 350.f), 0.f, 10, 1);
	entityList.push_back(enemy);
}

void DeInit(void)
{
	if (camera != NULL) delete camera;
	
	//delete all particles
	for (auto &p : particleList) delete p;
	particleList.clear();

	//delete all the entities
	for (auto &e : entityList) delete e;
	entityList.clear();

	for (auto &s : shotEntityList) delete s;
	shotEntityList.clear();

	for (auto &b : blockEntityList) delete b;
	blockEntityList.clear();

	//delete the contact listener
	delete contactListener;

	//Free all physics game data we allocated
	delete world;
	//any sprites still allocated are freed automatcally by the Blit3D object when we destroy it
}

void Update(double seconds)
{
	switch (gameState)
	{
	case SETTLING:
	{
		camera->Update((float)seconds);
		elapsedTime += seconds;
		while (elapsedTime >= timeStep)
		{
			//update the physics world
			world->Step(timeStep, velocityIterations, positionIterations);

			// Clear applied body forces. 
			world->ClearForces();

			settleTime += timeStep;
			elapsedTime -= timeStep;
		}

		if (settleTime > 3) gameState = PLAYING;
	}
	break;
	case PLAYING:
	{
		elapsedTime += seconds;

		if (fireShotNow)
		{

			fireShotNow = false;
			followingShot = true;

			ShotEntity* shot = MakeShot();
			b2Vec2 pos = Pixels2Physics(cannon.positionPixels);

			b2Vec2 dirCannon = Pixels2Physics(deg2vec(cannon.angle, 220));

			shot->body->SetTransform(pos + dirCannon, 0);

			b2Vec2 dir = deg2vec(cannon.angle, meter.scale * 3 + 1);

			shot->body->ApplyLinearImpulse(dir, shot->body->GetPosition(), true);
			shotEntityList.push_back(shot);

		}

		//don't apply physics unless at least a timestep worth of time has passed
		while (elapsedTime >= timeStep)
		{
			//update the physics world
			world->Step(timeStep, velocityIterations, positionIterations);

			// Clear applied body forces. 
			world->ClearForces();

			elapsedTime -= timeStep;

			//update game logic/animation
			for (auto &e : entityList) e->Update(timeStep);
			for (auto &b : shotEntityList) b->Update(timeStep);
			for (auto &b : blockEntityList) b->Update(timeStep);

			//update shot meter
			meter.Update(timeStep);

			//update cannon
			cannon.Update(timeStep);

			//update camera
			if (followingShot)
			{
				int size = shotEntityList.size();
				if (size > 0)
				{
					//last shot on list is the current active shot,
					//so follow it
					b2Vec2 pos = shotEntityList[size - 1]->body->GetPosition();
					pos = Physics2Pixels(pos);
					camera->PanTo(pos.x, pos.y);
				}
			}
			camera->Update(timeStep);

			//update the particle list and remove dead particles
			for (int i = particleList.size() - 1; i >= 0; --i)
			{
				if (particleList[i]->Update(timeStep))
				{
					//time to die!
					delete particleList[i];
					particleList.erase(particleList.begin() + i);
				}
			}

			//loop over contacts
			for (int pos = 0; pos < contactListener->contacts.size(); ++pos)
			{
				MyContact contact = contactListener->contacts[pos];

				//fetch the entities from the body userdata
				Entity *A = (Entity *)contact.fixtureA->GetBody()->GetUserData().pointer;
				Entity *B = (Entity *)contact.fixtureB->GetBody()->GetUserData().pointer;

				if (A != NULL && B != NULL) //if there is an entity for these objects...
				{
					if (A->typeID == EntityTypes::ENTITYBLOCK)
					{
						BlockCollide(A, contact.maxImpulseAB);
					}

					if (B->typeID == EntityTypes::ENTITYBLOCK)
					{
						BlockCollide(B, contact.maxImpulseAB);
					}

					if (A->typeID == EntityTypes::ENTITYENEMY)
					{
						EnemyCollide(A, contact.maxImpulseAB);
					}

					if (B->typeID == EntityTypes::ENTITYENEMY)
					{
						EnemyCollide(B, contact.maxImpulseAB);
					}
				}
			}

			//clean up dead entities
			for (auto &e : deadEntityList)
			{
				//remove body from the physics world and free the body data
				world->DestroyBody(e->body);
				//remove the entity from the appropriate entityList
				if (e->typeID == ENTITYSHOT)
				{
					for (int i = 0; i < shotEntityList.size(); ++i)
					{
						if (e == shotEntityList[i])
						{
							delete shotEntityList[i];
							shotEntityList.erase(shotEntityList.begin() + i);
							break;
						}
					}
				}
				else if (e->typeID == ENTITYBLOCK)
				{
					for (int i = 0; i < blockEntityList.size(); ++i)
					{
						if (e == blockEntityList[i])
						{
							delete blockEntityList[i];
							blockEntityList.erase(blockEntityList.begin() + i);
							break;
						}
					}
				}
				else
				{
					for (int i = 0; i < entityList.size(); ++i)
					{
						if (e == entityList[i])
						{
							delete entityList[i];
							entityList.erase(entityList.begin() + i);
							break;
						}
					}
				}
			}

			deadEntityList.clear();

			//check if player won
			if (entityList.size() < 3)
			{
				gameState = GAMEOVER;
			}
		}
	}
	break;
	case START:
		camera->StopShaking();
		gameState = SETTLING;
		break;
	case GAMEOVER:
		break;
	default:
		//do nada here
		break;
	}//end switch(gameState)
}

void Draw(void)
{
	glClearColor(0.5f, 0.5f, 1.0f, 0.0f);	//clear colour: r,g,b,a 	
											// wipe the drawing surface clear
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	//draw stuff here
	switch (gameState)
	{
	case START:
		camera->UnDraw();
		break;
	case SETTLING:
		start->Blit(blit3D->screenWidth / 2, blit3D->screenHeight / 2);
		break;
	case PLAYING:
		//MUST draw camera first!
		camera->Draw();

		//Draw the level background
		background->Blit(blit3D->screenWidth, (blit3D->screenHeight / 2) + 200);

		for (int i = 0; i < ((1920 * 2) / 70) + 2; ++i)
		{
			groundSprite->Blit(i * 70.f - 35.f, 35.f);
		}

		//throw in a few graphical things, clouds and cactii
		for (auto& e : elementList) e.Draw();

		//loop over all entities and draw them
		for (auto &e : entityList) e->Draw();
		for (auto &b : blockEntityList) b->Draw();
		for (auto &b : shotEntityList) b->Draw();
		for (auto &p : particleList) p->Draw();
		cannon.Draw();
		meter.Draw();

		break;
	case GAMEOVER:
		camera->UnDraw();
		gameOver->Blit(blit3D->screenWidth / 2, blit3D->screenHeight / 2);
		break;
	}
}

//the key codes/actions/mods for DoInput are from GLFW: check its documentation for their values
void DoInput(int key, int scancode, int action, int mods)
{
	if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
		blit3D->Quit(); //start the shutdown sequence

	if (key == GLFW_KEY_D && (action == GLFW_PRESS || action == GLFW_REPEAT))
		cannon.rotateDir = -1.f;

	if (key == GLFW_KEY_A && (action == GLFW_PRESS || action == GLFW_REPEAT))
		cannon.rotateDir = 1.f;

	if ((key == GLFW_KEY_A || key == GLFW_KEY_D) && action == GLFW_RELEASE)
		cannon.rotateDir = 0;

	if (key == GLFW_KEY_SPACE && action == GLFW_PRESS)
	{
		meter.shooting = true;
		followingShot = false;
		camera->PanTo(0, 0);
	}

	if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE)
	{
		meter.shooting = false;
		fireShotNow = true;
		followingShot = true;
	}

	if (key == GLFW_KEY_RIGHT && (action == GLFW_PRESS || action == GLFW_REPEAT))
	{
		camera->Pan(1, 0);
		followingShot = false;
	}

	if (key == GLFW_KEY_LEFT && (action == GLFW_PRESS || action == GLFW_REPEAT))
	{
		camera->Pan(-1, 0);
		followingShot = false;
	}

	if ((key == GLFW_KEY_LEFT || key == GLFW_KEY_RIGHT) && action == GLFW_RELEASE)
		camera->Pan(0, 0);

	if (key == GLFW_KEY_UP && (action == GLFW_PRESS || action == GLFW_REPEAT))
	{
		camera->Pan(0, 1);
		followingShot = false;
	}

	if (key == GLFW_KEY_DOWN && (action == GLFW_PRESS || action == GLFW_REPEAT))
	{
		camera->Pan(0, -1);
		followingShot = false;
	}

	if ((key == GLFW_KEY_DOWN || key == GLFW_KEY_UP) && action == GLFW_RELEASE)
		camera->Pan(0, 0);
}

int main(int argc, char *argv[])
{
	//memory leak detection
	_CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);

	blit3D = new Blit3D(Blit3DWindowModel::BORDERLESSFULLSCREEN_1080P, 720, 480);

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