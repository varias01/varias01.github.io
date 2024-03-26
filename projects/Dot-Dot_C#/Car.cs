using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Dot_Dot
{
    public class Car
    {
        private Texture2D sprite;   //create a texture for the sprite
        public Vector2 Position { get; private set; }  //create a vector for the position, public getter, private setter
        private Rectangle carBounds; //declare a rectangle to use for block collisions
        private bool noPreviousCollision; // declare a boolean to store if there was a previous collision 

        public Car(Texture2D carSprite, int x, int y)
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	Car
            //
            // Method parameters	:	carSprite, x, y
            //
            // Method return		:	void
            //
            // Synopsis				:   This method is a constructor for the car object. 
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Car (constructor)
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            sprite = carSprite; // declare the car sprite
            Position = new Vector2(x, y); // initialize the position vector
            carBounds = new Rectangle((int)Position.X, (int)Position.Y, sprite.Width, sprite.Height); // initialize the rectangle for collisions
            noPreviousCollision = true; // set the no previous collisions variable to false
        }

        public void Update(byte index, List<short> obstaclesXPositions, Dot dot, Texture2D carSprite, Texture2D carHitSprite, float objectSpeed, GraphicsDeviceManager graphics)
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	Update
            //
            // Method parameters	:	index, obstaclesXPositions, dot, carSprite, carHitSprite, objectSpeed, graphics
            //
            // Method return		:	void
            //
            // Synopsis				:   This method updates the car object.
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Update
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            Random random = new Random(); //declare and initilize the random object 
            bool noNewPosition = true; // set the neNewPosition boolean to true
            int newXPos = 0; // set the newXPos variable to zero
            byte count; //declare a counter variable for looping

            Position = new Vector2(Position.X - objectSpeed, Position.Y); //update the objects position
            carBounds = new Rectangle((int)Position.X, (int)Position.Y, sprite.Width, sprite.Height); //update rectangle

            if (Position.X < -80) // Note: completely off-screen
            {
                noPreviousCollision = true; //set noPreviousCollison to true

                while (noNewPosition)
                {
                    newXPos = random.Next(950, 1350); //determine a new random x position for the obstacle
                    for (count = 0; count < obstaclesXPositions.Count; count++)
                    {
                        if (!(newXPos >= obstaclesXPositions[count] - 60 && newXPos <= obstaclesXPositions[count] + 60))
                        {
                            noNewPosition = false; //if the new position doesn't overlap, set the noNewPosition variable to false
                        }
                    }
                }

                Position = new Vector2(newXPos, Position.Y); // Select a new random position for an obstacle/change to make them not overlap
                obstaclesXPositions[index] = (short)newXPos; // Store the new x position
                sprite = carSprite; //set the sprite equal to the car sprite
            }
            
            if (dot.DotBounds.Intersects(carBounds) && noPreviousCollision)
            {
                noPreviousCollision = false; //set noPreviousCollison to false
                sprite = carHitSprite; //set the sprite equal to the car hit sprite
                dot.lives -= 1; // If the dot collides with a car, the player loses a life
            }
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
            // Synopsis				:   This method draws the car object.
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Draw
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            spriteBatch.Draw(sprite, Position, Color.White);                // draw rhe car sprite in the corresponding position
        }
    }
}