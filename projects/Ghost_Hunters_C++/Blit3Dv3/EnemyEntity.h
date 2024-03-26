#pragma once

#include "Entity.h"

class EnemyEntity : public Entity
{
public:
	int hp;
	int maxHP;

	EnemyEntity()
	{
		typeID = ENTITYENEMY;
		maxHP = hp = 10;
	}

	//Damage() returns true if we should kill this object
	bool Damage(int damage)
	{
		hp -= damage;
		if (hp < 1) return true;

		return false;
	}
};

EnemyEntity* MakeEnemy( b2Vec2 pixelCoords, float angleInDegrees, int maximumHP, int ghostSprite);