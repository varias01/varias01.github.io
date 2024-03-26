using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;

namespace Dot_Dot
{
    public class Dot
    {
        private Texture2D sprite;   //create a texture for the sprite
        public Vector2 Position { get; private set; } //create a vector for the position, public getter, private setter
        public Rectangle DotBounds { get; private set; } //declare a rectangle to use for block collisions
        public float deltaX { get; set; } //declare a float to store the change in position in x
        public float deltaY { get; set; } //declare a float to store the change in position in y
        public int lives { get; set; } // declare an integer to store the player's lives
        public int score { get; set; } // declare an integer to store the player's score

        public Dot(Texture2D dotSprite, int x, int y)
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	Dot
            //
            // Method parameters	:	dotSprite, x, y
            //
            // Method return		:	void
            //
            // Synopsis				:   This method is a constructor for the dot object. 
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Dot (constructor)
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            sprite = dotSprite; // set the sprite of the dot object
            Position = new Vector2(x, y + 20); //set the position of the dot, +20 to account for the sprite size difference 
            DotBounds = new Rectangle((int)Position.X, (int)Position.Y, sprite.Width, sprite.Height);  // initilize the rectangle of the dot for collisions
            lives = 3; // intialize the player's lives to three
            score = 0; // set the initial score to 0
            deltaX = 0; // set delta x and delta y to 0
            deltaY = 0;
        }

        public void Update(float gravity, short[] lanes, short currentLane)
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	Update
            //
            // Method parameters	:	gravity, lanes, currentLane
            //
            // Method return		:	void
            //
            // Synopsis				:   This method updates the dot object.
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Update
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            if (Position.X < 205)
            {
                deltaX = 2; // move 2 units every update
                Position = new Vector2(Position.X + deltaX, Position.Y);    //update the position of the dot
                                                                            // The reason why a new Vector2 is created is that Vector2 is a struct in C#, and structs are value types. When you modify a property of a struct,
                                                                            // you get a new copy of the struct with the modified value.
            }
            if (Position.X > 205) 
            {
                Position = new Vector2(205, Position.Y); // if somehow it goes beyond the center of the screen, bring it back to the center
            }

            if (deltaY > 0 || Position.Y < lanes[currentLane] + 20) 
            {
                Position = new Vector2(Position.X, Position.Y - deltaY); //update the position of the dot
                deltaY -= gravity; // subtract gravity from the jump, so you jump less and fall every update
            }

            if (Position.Y > lanes[currentLane] + 20)
            {
                Position = new Vector2(Position.X, lanes[currentLane] + 20); // make sure the player stays on the floor
            }
            if (Position.Y == lanes[currentLane] + 20 && deltaY < 0)
            {
                deltaY = 0; // if the player is back in the lane, set the deltaY back to zero
            }

            DotBounds = new Rectangle((int)Position.X, (int)Position.Y, sprite.Width, sprite.Height); // update the dot's rectangle for collisions
        }

        public void Draw(SpriteBatch spriteBatch)
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	Draw
            //
            // Method parameters	:	spriteBatch
            //
            // Method return		:	void
            //
            // Synopsis				:   This method draws the dot object.
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Draw
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            spriteBatch.Draw(sprite, Position, Color.White);                // draw the dot sprite in the corresponding position
        }
    }
}