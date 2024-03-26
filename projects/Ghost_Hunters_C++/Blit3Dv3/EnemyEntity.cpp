#include "EnemyEntity.h"

extern b2World* world;
extern std::vector<Sprite*> enemySprites;

EnemyEntity* MakeEnemy( b2Vec2 pixelCoords,	float angleInDegrees, int maximumHP, int ghostSprite)
{
	EnemyEntity* enemyEntity = new EnemyEntity();

	b2BodyDef bodyDef;

	bodyDef.type = b2_dynamicBody; //make it a dynamic body i.e. one moved by physics
	bodyDef.position = Pixels2Physics(pixelCoords); //set its position in the world
	bodyDef.angle = deg2rad(angleInDegrees);

	bodyDef.angularDamping = 1.8f;

	bodyDef.userData.pointer = reinterpret_cast<uintptr_t> (enemyEntity);

	enemyEntity->body = world->CreateBody(&bodyDef); //create the body and add it to the world

	b2FixtureDef fixtureDef;

	// Define a shape for our body.
	b2PolygonShape polygon;

	// This defines a square in CCW order.
	polygon.SetAsBox(35 / PTM_RATIO, 35 / PTM_RATIO);

	fixtureDef.shape = &polygon;

	fixtureDef.density = 0.4f;
	fixtureDef.restitution = 0.01;
	fixtureDef.friction = 0.2;

	enemyEntity->body->CreateFixture(&fixtureDef);

	//add sprite
	enemyEntity->sprite = enemySprites[ghostSprite];

	enemyEntity->maxHP = enemyEntity->hp = maximumHP;

	return enemyEntity;
}