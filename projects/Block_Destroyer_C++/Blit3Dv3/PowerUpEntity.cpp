#include "PowerUpEntity.h"
#include "CollisionMask.h"

extern Sprite *multiBallSprite;
extern Sprite* slowBallSprite;

extern b2World *world;

PowerUpEntity::PowerUpEntity()
{
	typeID = ENTITYPOWERUP;
	powerUpType = PowerUpType::MULTIBALL;
	sprite = multiBallSprite;

	typeID = ENTITYPOWERUP;
	powerUpType = PowerUpType::SLOWBALL;
	sprite = slowBallSprite;
}

PowerUpEntity * MakePowerUp(PowerUpType type, float xpos, float ypos)
{
	PowerUpEntity *powerUpEntity = new PowerUpEntity();
	powerUpEntity->powerUpType = type;

	//set the sprites to draw with
	switch(type)
	{
	case PowerUpType::MULTIBALL:
		powerUpEntity->sprite = multiBallSprite;
		break;
	case PowerUpType::SLOWBALL:
		powerUpEntity->sprite = slowBallSprite;
		break;
	default:
		break;
	}

	//make the physics body
	b2BodyDef powerUpBodyDef;

	//set the position of the center of the body, 
	//converting from pxel coords to physics measurements
	powerUpBodyDef.position.Set(xpos / PTM_RATIO, ypos / PTM_RATIO);
	powerUpBodyDef.type = b2_dynamicBody; //make it a dynamic body, so it can collide with the kinematic paddle

	//make the userdata point back to this entity
	powerUpBodyDef.userData.pointer = reinterpret_cast<uintptr_t>(powerUpEntity);

	powerUpEntity->body = world->CreateBody(&powerUpBodyDef); //create the body and add it to the world

	// Define a box shape for our dynamic body.
	b2PolygonShape boxShape;
	//SetAsBox() takes as arguments the half-width and half-height of the box
	boxShape.SetAsBox(49.0f / (2.f*PTM_RATIO), 16.0f / (2.f*PTM_RATIO));

	b2FixtureDef powerUpFixtureDef;
	powerUpFixtureDef.shape = &boxShape;
	powerUpFixtureDef.density = 1.0f; 
	powerUpFixtureDef.restitution = 0;
	powerUpFixtureDef.friction = 0.1f;

	//collison masking
	powerUpFixtureDef.filter.categoryBits = CMASK_POWERUP;  //this is a power up
	powerUpFixtureDef.filter.maskBits = CMASK_PADDLE | CMASK_GROUND;		//it collides wth paddle and ground

	powerUpEntity->body->CreateFixture(&powerUpFixtureDef);	

	//make the powerup drop
	
	powerUpEntity->body->SetLinearVelocity(b2Vec2(0.f, -200.f/PTM_RATIO)); //apply velocity to kinematic object

	return powerUpEntity;
}