using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework;
using System;
using System.Collections.Generic;

namespace Dot_Dot
{
    public class Star
    {

        private Texture2D sprite;   //create a texture for the sprite
        public Vector2 Position { get; private set; }  //create a vector for the position, public getter, private setter
        private Rectangle starBounds; //declare a rectangle to use for block collisions
        private bool noPreviousCollision; // declare a boolean to store if there was a previous collision 

        public Star(Texture2D starSprite, int x, int y)
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	Star
            //
            // Method parameters	:	starSprite, x, y
            //
            // Method return		:	void
            //
            // Synopsis				:   This method is a constructor for the star object. 
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Star (constructor)
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            sprite = starSprite; // declare the star sprite
            Position = new Vector2(x, y); // initialize the position vector
            starBounds = new Rectangle((int)Position.X, (int)Position.Y, sprite.Width, sprite.Height); // initialize the rectangle for collisions
            noPreviousCollision = true; // set the no previous collisions variable to false
        }

        public void Update(Dot dot, Texture2D starSprite, Texture2D starHitSprite, float objectSpeed, List<short> obstaclesXPositions, short[] lanes)
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	Update
            //
            // Method parameters	:	obstaclesXPositions, dot, starSprite, starHitSprite, objectSpeed, lanes
            //
            // Method return		:	void
            //
            // Synopsis				:   This method updates the star object.
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
            short newLane; //declare a new lane short to get a random new lane for the star
            byte count; //declare a counter variable for looping

            Position = new Vector2(Position.X - objectSpeed, Position.Y); //update the objects position
            starBounds = new Rectangle((int)Position.X, (int)Position.Y, sprite.Width, sprite.Height); //update rectangle

            if (Position.X < -40) // completely off-screen
            {
                noPreviousCollision = true; //set noPreviousCollison to true
                newLane = lanes[random.Next(0, 2)]; //determine a new random lane for the star to be in
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

                Position = new Vector2(newXPos, newLane); // Select a new random position for an obstacle/change to make them not overlap
                obstaclesXPositions[3] = (short)newXPos; // Store the new x position
                sprite = starSprite; //set the sprite equal to the star sprite
            }
           
            if (dot.DotBounds.Intersects(starBounds) && noPreviousCollision)
            {
                noPreviousCollision = false; //set noPreviousCollison to false
                sprite = starHitSprite; //set the sprite equal to the star hit sprite
                dot.score += 1; // If the dot collides with a star, the player earns a point
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
            // Synopsis				:   This method draws the star object.
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Draw
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            spriteBatch.Draw(sprite, Position, Color.White);                // draw the star sprite in the corresponding position
        }
    }
}