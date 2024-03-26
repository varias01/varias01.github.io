#pragma once

#include "Entity.h"

enum class BrickColour { BLUE = 0, GREEN, GREY, PURPLE, RED, YELLOW};

//externed sprites
extern Sprite *redBrickSprite;
extern Sprite *yellowBrickSprite;
extern Sprite *blueBrickSprite;
extern Sprite *purpleBrickSprite;
extern Sprite* greenBrickSprite;
extern Sprite* greyBrickSprite;

class BrickEntity : public Entity
{
public:
	BrickColour colour;
	BrickEntity()
	{
		typeID = ENTITYBRICK;
		colour = BrickColour::RED;
	}

	bool HandleCollision();
};

BrickEntity * MakeBrick(BrickEntity* brickEntity);

void LoadMap(std::string fileName, std::vector<Entity*>& brickList);
