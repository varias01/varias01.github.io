#pragma once

#include "Entity.h"

enum class PowerUpType { MULTIBALL, SLOWBALL};

class PowerUpEntity : public Entity
{
public:
	PowerUpType powerUpType;
	PowerUpEntity();
};

PowerUpEntity * MakePowerUp(PowerUpType type, float xpos, float ypos);