#pragma once
#include<Blit3D.h>

class Choco
{
public:
	std::vector<Sprite*> spriteList; //4 frame animation for running
	int x, y; //player's coordinates
	float height = 425.f; //height of sprite
	float width = 190.f; //width of sprite
	int frameNumber = 0; //what frame of the character spritesheet is displayed
	float elapsedTime = 0;
	const float frameTime = 1.f / 6.f;
	const float animationTotalDuration = 1.f; //how long an animation takes to complete
	float currentAnimationDuration = 0.000001f;
	glm::vec2 positionOffset; //our current offset, calculated as a ratio of the total offset below
	glm::vec2 positionTotalOffset; //stores the total distance from one tile to the next
	bool movingLeft = false; //controls horizontal movement
	bool movingRight = false;
	float immunityTimer = 0;
	int lives = 3;

	void Draw();
	void Update(float seconds);
};

enum class Movement { LEFT, RIGHT, NONE };

void InitializeRNG();
enum class ObstacleType { HAMMER, TORCH };

class Obstacle
{
public: 
	std::vector<Sprite*> spriteList; //4 frame animation of obstacles
	glm::vec2 velocity, position;
	float angle = 0;
	float height, width; //height and width of sprite
	float durationTimer = 0;
	int frameNumber = 0; //what frame of the character spritesheet is displayed
	int cycle = 0;
	ObstacleType type;

	void Draw();
	bool Update(float seconds);
};

Obstacle ObstacleFactory(ObstacleType type, std::vector<Obstacle> obstacleList, int yOffset = 0); //yOffset optional parameter (a default value for a parameter) must be at the end
ObstacleType MakeRandomObstacle();

// returns true if there was a collision
bool CollidePlayerWithObstacle(Choco* choco, Obstacle& o);

extern std::vector<Sprite*> explosionSpriteList;

class Explosion
{
public:
	int frameNum = 0; //which frame to display
	float frameSpeed = 1.f / 10.f; //how long a frame is displayed for
	float frameTimer = 0.f;
	glm::vec2 position;
	float scale = 5.f;

	void Draw();
	bool Update(float seconds);//return false if explosion should be removed

	Explosion(glm::vec2 location, float size);
};