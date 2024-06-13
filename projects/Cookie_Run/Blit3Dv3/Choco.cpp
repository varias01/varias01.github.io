#include "Choco.h"
#include <random>
extern Blit3D* blit3D;

std::mt19937 rng;
extern Movement playerInput;

void Choco::Draw()
{
	//Draw choco animation 
	spriteList[frameNumber]->Blit(x, y);
}

void Choco::Update(float seconds)
{
	//handle moving side to side
	elapsedTime += seconds;
	currentAnimationDuration += seconds;
	switch (playerInput)
	{
	case Movement::LEFT:				//move the player LEFT
		if (x - width > 0)
		{
			x -= width/2;
			//make our offset vector that will "walk" us to the new location
			positionTotalOffset = positionOffset = glm::vec2(-width/2, 0);
		}
		break;
	case Movement::RIGHT:				//move the player RIGHT
		if (x + width < 1920)
		{
			x += width/2;
			//make our offset vector that will "walk" us to the new location
			positionTotalOffset = positionOffset = glm::vec2(width/2, 0);
		}
		break;
	default:
		//player movement must be NONE to get here
		break;
	}//end switch

	if (playerInput != Movement::NONE)
	{
		playerInput = Movement::NONE;
	}

	//calculate new offset
	float percentDone = 1.f - currentAnimationDuration / animationTotalDuration;
	//positionOffset = positionTotalOffset * percentDone;

	if (currentAnimationDuration >= animationTotalDuration)
	{
		//we have finished animating this walk cycle
		frameNumber = 1;
		currentAnimationDuration = 0.000001f;
		positionOffset = glm::vec2(0, 0); //reset our walk offset
		positionTotalOffset = glm::vec2(0, 0); //reset our walk offset
	}
	else if (elapsedTime >= frameTime)
	{
		elapsedTime -= frameTime;
		//time to advance the animation frame
		frameNumber++;
		if (frameNumber > 3)
		{
			frameNumber = 1;
		}
	}

	//handle inmunity timer
	if (immunityTimer > 0) immunityTimer -= seconds;
}

void InitializeRNG()
{
	std::random_device rd;
	rng.seed(rd());
}

void Obstacle::Draw()
{
	spriteList[frameNumber]->Blit(position.x, position.y);
}

bool Obstacle::Update(float seconds)
{
	switch (type)
	{
	case ObstacleType::HAMMER:
		if (position.y > 307)
		{
			position += velocity * seconds;
		}
		break;
	case ObstacleType::TORCH:
		if (position.y > 698)
		{
			position += velocity * seconds;
		}
		break;
	default:
		assert(false);
		break;
	}

	cycle++;
	if (cycle > 12)
	{
		frameNumber ++;
		cycle = 0;
	}
	if (frameNumber > 3)
		frameNumber = 0;

	//handle obstacle timer
	if (durationTimer > 0) durationTimer -= seconds;
	else return false;
}

extern std::vector<Sprite*> hammerSpriteList;
extern std::vector<Sprite*> torchSpriteList;

std::uniform_real_distribution<float> posDistX(120.f, 1800.f);
std::uniform_real_distribution<float> speedDist(200.f, 300.f);
std::uniform_int_distribution<int> obstacleTypeDist(0, 1);


Obstacle ObstacleFactory(ObstacleType type, std::vector<Obstacle> obstacleList, int yOffset)
{
	Obstacle O;

	O.angle = 270.f; //going down

	O.velocity.x = cos(glm::radians(O.angle));
	O.velocity.y = sin(glm::radians(O.angle));

	O.type = type;

	switch (type)
	{
	case ObstacleType::HAMMER:
		O.spriteList = hammerSpriteList;
		O.velocity *= speedDist(rng);
		O.height = 278;
		O.width = 220;
		break;
	case ObstacleType::TORCH:
		O.spriteList = torchSpriteList;
		O.velocity *= speedDist(rng);
		O.height = 765;
		O.width = 140;
		break;
	default:
		assert(false);
		break;
	}

	boolean done = false;

	while (!done)
	{
		O.position.x = posDistX(rng);
		done = true;

		for (auto A : obstacleList)
		{
			if (!(O.position.x < A.position.x - 200 || O.position.x > A.position.x + 200))
			{
				done = false;
			}
		}
	}
	 
	O.position.y = float(O.height + 1080) + yOffset; 
	O.durationTimer = 20.f;

	return O;
}


ObstacleType MakeRandomObstacle()
{
	int type = obstacleTypeDist(rng);

	switch (type)
	{
	case 0:
		return ObstacleType::HAMMER;
		break;
	case 1:
		return ObstacleType::TORCH;
		break;
	}
}


bool CollidePlayerWithObstacle(Choco* choco, Obstacle& o)
{
	float chocoHalfWidth = choco->width / 2;
	float chocoHalfHeight = choco->height / 2;
	float oHalfWidth = o.width / 2;
	float oHalfHeight = o.height / 2;

	float verticalDistance = chocoHalfHeight + oHalfHeight;
	float horizontalDistance = chocoHalfWidth + oHalfWidth;

	if (choco->x > o.position.x)
	{
		//Check for collision to the left
		if ((choco->x - o.position.x < horizontalDistance) && (o.position.y - choco->y < verticalDistance))
		{
			if (choco->immunityTimer > 0) return false;
			else return true;
		}
		else return false;
	}
	else 
	{
		//Check for collision to the right
		if ((o.position.x - choco->x < horizontalDistance) && (o.position.y - choco->y < verticalDistance))
		{
			if (choco->immunityTimer > 0) return false;
			else return true;
		}
		else return false;
	}

}

void Explosion::Draw()
{
	explosionSpriteList[frameNum]->Blit(position.x, position.y, scale, scale);
}

bool Explosion::Update(float seconds)
{
	//update frame timer
	frameTimer += seconds;
	//see if we should advance the frame counter
	if (frameTimer >= frameSpeed)
	{
		if (frameNum > explosionSpriteList.size() - 2) return false;
		frameNum++; //advance to the next frame

		frameTimer -= frameSpeed;
	}

	return true;
}

Explosion::Explosion(glm::vec2 location, float size)
{
	position = location;
	scale = size;
}