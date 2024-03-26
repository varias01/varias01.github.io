using System; 
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;

namespace Dot_Dot
{
    public class Game1 : Game
    {
        private GraphicsDeviceManager graphics; // declare a graphics decive manager as graphics
        private SpriteBatch spriteBatch; // declare the spritebach to use for drawing graphics
        KeyboardState previousKeyboardState = new KeyboardState(); // declare a previousKeyboardState variable to keep track of keys pressed

        const short WIDTH = 900; //declare a constant to store the width of the window
        const short HEIGHT = 600; // declare a constant to store the height of the window

        Texture2D dotSprite; //declare all the necessary sprites as Texture2D objects
        Texture2D carSprite;
        Texture2D carHitSprite;
        Texture2D starSprite;
        Texture2D starHitSprite;
        Texture2D background;
        Texture2D menusBackground;
        SpriteFont gameFont; //declare all the necessary fonts as SpriteFont objects
        SpriteFont titlesFont;
        SpriteFont gameTitleFont;

        Dot dot; //Declare the dot object
        Star star; //Declare the star object
        float gravity = 0.9F; //Set the gravity of the game
        float objectSpeed = 2.5F; //set the initial object speed
        short currentLane = 1; //set the initial lane
        List<Car> cars = new List<Car>(); //declare a car sprites list to update and draw them together
        List<short> obstaclesXPositions = new List<short>(); //declare an obstacle x positions list to keep track of them
        short[] lanes = { 180, 330, 480 }; //declare an array to store the positions of the lanes

        File file = new File(); //declare and initialize the file object
        private StringBuilder player_input = new StringBuilder(); // declare a stringBuilder object to handle the interface input
        string playerInput = ""; //declare a string to store and display the player's input
        bool inputError = false; //declare a boolean inputError and set it to false
        List<List<string>> highscoresList = new List<List<string>>(); //Declare a list of lists to store the highscores data
        Random random = new Random(); //instantiation of the random object

        bool gameOver = false; //declare and initialize the game over variable to false
        bool runOnce = true; //declare and initialize the runOnce variable to true

        enum GameState  //enumerate the game states to switch between them
        {
            MENU,
            GAME, 
            NAME, 
            HIGHSCORES
        }
        GameState currentGameState; //declare a currentGameState game state variable to keep track of the game state

        public Game1()
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	Game1
            //
            // Method parameters	:	none
            //
            // Method return		:	void
            //
            // Synopsis				:   This method is a constructor for the game settings and mouse settings. 
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Game1 (constructor)
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            graphics = new GraphicsDeviceManager(this); // instantiate the graphics object
            Content.RootDirectory = "Content"; //set the root directory of the content
            IsMouseVisible = true; //Set the mouse to visible
        }

        protected override void LoadContent()
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	LoadContent 
            //
            // Method parameters	:	none
            //
            // Method return		:	void
            //
            // Synopsis				:   This method loads the graphics and fonts for the game.
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Load Content 
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            spriteBatch = new SpriteBatch(GraphicsDevice); //initialize the spriteBatch object
            dotSprite = Content.Load<Texture2D>("dot");  //Load all the sprites to be used in the game by linking them to their corresponding images
            carSprite = Content.Load<Texture2D>("bigCar");
            carHitSprite = Content.Load<Texture2D>("bigCarHit");
            starSprite = Content.Load<Texture2D>("bigStar");
            starHitSprite = Content.Load<Texture2D>("blank_star");
            background = Content.Load<Texture2D>("bg");
            menusBackground = Content.Load<Texture2D>("menus_bg");
            gameFont = Content.Load<SpriteFont>("NotoSansGeorgian-SemiBold");  //Load all the fonts to be used in the game by linking them to their correpsonding SpriteFont file
            titlesFont = Content.Load<SpriteFont>("NotoSansGeorgian-Titles");
            gameTitleFont = Content.Load<SpriteFont>("NotoSansGeorgian-GameTitle");
        }

        protected override void Initialize()
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	Initialize 
            //
            // Method parameters	:	none
            //
            // Method return		:	void
            //
            // Synopsis				:   This method initializes the game vartiables and objects.
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Initialize
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            graphics.PreferredBackBufferWidth = WIDTH;  // Set the desired width for the application window
            graphics.PreferredBackBufferHeight = HEIGHT; // Set the desired height for the application window
            graphics.ApplyChanges(); //apply the changes to the graphics

            base.Initialize(); //intialize the monogame base

            short[] initialXPos = { 900, 1000, 1100 }; //set the initial positions for the cars

            dot = new Dot(dotSprite, 50, 350); //initialize dot
            for (byte counter = 0; counter < 3; counter++)
            {
                Car car = new Car(carSprite, initialXPos[counter], lanes[counter]); // create intitial cars
                cars.Add(car); //add the new cars to the cars list
                obstaclesXPositions.Add((short)car.Position.X); //add the car's x position to the x positions list, position is a float so it has to be converted to short
            }
            star = new Star(starSprite, 950, 330);  // create initial star
            obstaclesXPositions.Add((short)star.Position.X);  // add the star's x position to the x positions list
            
            file.LoadFile(); // Load File

            currentGameState = GameState.MENU; // set the intial game state to the menu

            Window.TextInput += TextInputHandler; // Subscribe to the TextInput event
        }

        protected override void Update(GameTime gameTime)
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	Update 
            //
            // Method parameters	:	gameTime
            //
            // Method return		:	void
            //
            // Synopsis				:   This method updates the game objects.
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            //                          Windows (2023) Keyboard.IsKeyDown(Key) Method. 
            //                          https://learn.microsoft.com/en-us/dotnet/api/system.windows.input.keyboard.iskeydown?view=windowsdesktop-7.0
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Update
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            //runs 60 fps by default, game loop, 
            if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed || Keyboard.GetState().IsKeyDown(Keys.Escape))
                Exit(); //If the player closes the application, or presses ESC, exit

            KeyboardState keyboardState = Keyboard.GetState(); // declare a KeyboardState to read the keyboard states
 
            switch (currentGameState)
            {
                case GameState.MENU:
                    if (keyboardState.IsKeyDown(Keys.S))
                    {
                        currentGameState = GameState.GAME; //Set the game state to Game
                    }
                    if (keyboardState.IsKeyDown(Keys.H))
                    {
                        currentGameState = GameState.HIGHSCORES; //set the game state to Highscores
                    }
                    break;
                case GameState.GAME:
                    if (objectSpeed < 12)
                    {
                        objectSpeed += 0.005F; //increase object speed every update
                    }
                    if (keyboardState.IsKeyDown(Keys.Up) && dot.deltaY == 0)
                    {
                        dot.deltaY = 10; // set the dot's delta y to 10
                        currentLane -= 1; //decrease teh current lane by one
                        if (currentLane <= 0)
                        {
                            currentLane = 0; //if the current lane falls below zero, maintain it at zero
                        }
                    }
                    if (keyboardState.IsKeyDown(Keys.Down) && dot.deltaY == 0)
                    {
                        dot.deltaY = 4; // set the dot's delta y to 4
                        currentLane += 1; //increase the current lane by one
                        if (currentLane >= 2)
                        {
                            currentLane = 2;  //if the current lane falls above two, maintain it at two
                        }
                    }
                    if (keyboardState.IsKeyDown(Keys.Space) && dot.deltaY == 0)
                    {
                        dot.deltaY = 14; // set the dot's delta y to 14
                    }
                    dot.Update(gravity, lanes, currentLane); //update the dot object
                    star.Update(dot, starSprite, starHitSprite, objectSpeed, obstaclesXPositions, lanes); //update the star object
                    for (byte counter = 0; counter < cars.Count; counter++)
                    {
                        cars[counter].Update(counter, obstaclesXPositions, dot, carSprite, carHitSprite, objectSpeed, graphics); // update all the car objects
                    }

                    if (dot.lives == 0)
                    {
                        currentGameState = GameState.NAME; //set the game state to Name
                    }
                    break;
                case GameState.NAME:
                    List<string> newPlayer = new List<string>(); //Declare a string list to store the name and score of a new player
                    int playerHighscore = 0; //declare and initialize the player high score
                    int currentScore; //declare the current score integer
                    bool playerFound; //declare the playerfound boolean
                    bool invalidInput = true; //set invalid input boolean to true
                    playerInput = (player_input).ToString(); //set the player input string, euqal to the player_input string builder

                    if (keyboardState.IsKeyDown(Keys.Enter))
                    {
                        invalidInput = file.ValidateNameInput(playerInput);
                        if (!invalidInput)
                        {
                            playerFound = false; //set player found to false
                            for (short row = 0; row < file.content.Count; row++)
                            {
                                if (file.content[row][0] == playerInput)
                                {
                                    playerFound = true; //set player found to true
                                    playerHighscore = int.Parse(file.content[row][1]); //get the saved player's high score
                                    currentScore = dot.score; //get the current game's score
                                    if (currentScore > playerHighscore)
                                    {
                                        file.content[row][1] = currentScore.ToString(); //update the player's high score
                                    }
                                }
                            }
                            if (!playerFound)
                            {
                                newPlayer.Add(playerInput); //set the new player's name
                                newPlayer.Add(dot.score.ToString()); //set the new player's score
                                file.content.Add(newPlayer); //Add the new player to the file content 
                            }
                            currentGameState = GameState.HIGHSCORES; //set the game state to High scores 
                        }
                        else
                        {
                            inputError = true; // set input error to true
                            player_input.Clear(); //clear the player input string builder
                            playerInput = player_input.ToString(); //set the player input string queal to the string builder input variable
                        }
                    }
                    else if (keyboardState.IsKeyDown(Keys.Back) && !(previousKeyboardState.IsKeyDown(Keys.Back)))
                    {
                        int charToErase; //declare a character to erase integer variable
                        if (player_input.Length - 1 >= 0)
                        {
                            charToErase = player_input.Length - 1; //set the char to erase equal to the last character in teh string
                            player_input.Remove(charToErase, 1); //remove the char to erase
                        }
                        playerInput = player_input.ToString(); //set the player input string queal to the string builder input variable
                    }
                    gameOver = true; // set the game over variable to true
                    break;
                case GameState.HIGHSCORES:
                    if (!gameOver && keyboardState.IsKeyDown(Keys.M))
                    {
                        currentGameState = GameState.MENU; //set the game state to MENU
                    }
                    break;
                default:
                    break; //if default, break
            }
            previousKeyboardState = keyboardState; //set teh previous keyboard state equal to the current keyboard state
            base.Update(gameTime); //update the base of the game
        }
        private void TextInputHandler(object sender, TextInputEventArgs args)
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	TextInputHandler 
            //
            // Method parameters	:   sender, args
            //
            // Method return		:	void
            //
            // Synopsis				:   This method handles the text input for the program.
            //
            // References			:   Microsoft (2023) Char.IsControl Method. 
            //                          https://learn.microsoft.com/en-us/dotnet/api/system.char.iscontrol?view=net-8.0
            //                        
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Text Input Handler
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            if (currentGameState == GameState.NAME)
            {
                char character = args.Character; // get the character of they key being pressed
                if (!char.IsControl(character)) // Check if the character is valid (not a control character)
                {
                    player_input.Append(character); // if the character is valid, append the character to player_input
                }
            }
        }
        protected override void Draw(GameTime gameTime)
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	Draw
            //
            // Method parameters	:	gameTime
            //
            // Method return		:	void
            //
            // Synopsis				:   This method draws the game objects.
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            //                          Jjagg (2018) Monogame. How to write text with keyboard. https://community.monogame.net/t/how-to-write-text-with-keyboard/10501
            //                          Willmotil (2019) How Do I Create A User Input Text Box That Stores Input Into Variables?
            //                          https://community.monogame.net/t/how-do-i-create-a-user-input-text-box-that-stores-input-into-variables/11621
            //                          
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Draw
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

            spriteBatch.Begin(); //call spritebatch to begin drawing

            switch (currentGameState)
            {
                case GameState.MENU:
                    spriteBatch.Draw(menusBackground, new Vector2(0, 0), Color.White); //Draw the menu's background
                    spriteBatch.DrawString(gameTitleFont, "Dot-Dot", new Vector2(245, 100), Color.Black); //Draw the game's title
                    spriteBatch.DrawString(titlesFont, "Press S to START...", new Vector2(310, 340), Color.Black); //Draw the start game text
                    spriteBatch.DrawString(titlesFont, "Press H to see the HIGH SCORES...", new Vector2(170, 440), Color.Black); //Draw the access highscores text
                    break;
                case GameState.GAME:
                    spriteBatch.Draw(background, new Vector2(0, 0), Color.White); //Draw the game's background
                    DrawRectangle(spriteBatch, new Rectangle(0, 220, WIDTH, 8), Color.LightSlateGray); //Draw the lanes
                    DrawRectangle(spriteBatch, new Rectangle(0, 370, WIDTH, 8), Color.LightSlateGray);
                    DrawRectangle (spriteBatch, new Rectangle(0, 520, WIDTH, 8), Color.LightSlateGray);
                    
                    spriteBatch.DrawString(gameFont, "Score: " + dot.score, new Vector2(40, 20), Color.White); //Drawe the score
                    spriteBatch.DrawString(gameFont, "Lives: " + dot.lives, new Vector2(780, 20), Color.White); //Draw the lives
                    string instructions = "SPACE: Jump in lane    ARROW UP: Go up 1 lane    ARROW DOWN: Go down 1 lane"; //declare the instructions string
                    spriteBatch.DrawString(gameFont, instructions, new Vector2(40, 560), Color.White); // draw the instructions of the game at the bottom of the screen
                    
                    for (byte counter = 0; counter < cars.Count; counter++)
                    {
                        cars[counter].Draw(spriteBatch); //Draw the cars
                    }
                    star.Draw(spriteBatch); //Draw the star
                    dot.Draw(spriteBatch); //Draw the dot
                    break;
                case GameState.NAME:
                    spriteBatch.Draw(menusBackground, new Vector2(0, 0), Color.White); //Draw the name screen background
                    spriteBatch.DrawString(titlesFont, "Please enter your name:", new Vector2(250, 100), Color.Black); //Draw the enter your name string
                    spriteBatch.DrawString(titlesFont, "Press ENTER to continue...", new Vector2(240, 500), Color.Black); //Draw the press enter to continue message
                    if(inputError)
                    {
                        string errorMessage = "Enter a maximum of 10 letters and/or numbers."; //declare a string to temporarily store the error message string
                        spriteBatch.DrawString(gameFont, errorMessage, new Vector2(210, 400), Color.Black); // draw the input error message
                    }
                    //text box
                    DrawRectangle(spriteBatch, new Rectangle(380, 300, 140, 40), Color.Black); // Draw the outter box of the textbox
                    DrawRectangle(spriteBatch, new Rectangle(385, 305, 130, 30), Color.White); // Draw the inner box of the text box
                    spriteBatch.DrawString(gameFont, playerInput, new Vector2(390, 305), Color.Black); // Draw the player's input
                    break;
                case GameState.HIGHSCORES:
                    string name; //declare a string to store the name
                    string score; //declare a string to store the score 
                    spriteBatch.Draw(menusBackground, new Vector2(0, 0), Color.White); //draw the background for the high scores screen
                    if (runOnce)  
                    {
                        file.SaveFile(); // save the new information to the file
                        OrderScores(); //order the scores obtained from the file 
                        runOnce = false; //set the runOnce variable to false
                    }
                    spriteBatch.DrawString(titlesFont, "HIGH SCORES", new Vector2(330, 50), Color.Black); //Draw the high scores title 
                    if (!gameOver)
                    {
                        spriteBatch.DrawString(titlesFont, "Press M to go back to the menu...", new Vector2(190, 500), Color.Black); // Draw the go back to menu text
                    }
                    else
                    {
                        spriteBatch.DrawString(titlesFont, "Thank you for playing! Press ESC to exit...", new Vector2(120, 500), Color.Black); //Draw the thank you for playing text
                    }
                    short indexCounter = (short)(highscoresList.Count - 1); //declare and initialize a short vaiable to work as an index counter
                    for (short index = 0; index < 5; index++)
                    {
                        name = highscoresList[indexCounter][0]; // get the player's name
                        score = highscoresList[indexCounter][1]; // get the player's score data
                        spriteBatch.DrawString(gameFont, name + " . . . . . . . . . . . . . . . . . . . " + score, new Vector2(290, 150 + 50 * index), Color.Black); // draw the line on the high scores screen
                        indexCounter--; //decrease the index counter by one
                    }
                    break;
                default:
                    break; //break if it falls in the default case
            }
            spriteBatch.End(); //call spritebach to end drawing
            base.Draw(gameTime); //draw the monogame base
        }

        private void OrderScores()
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	OrderScores
            //
            // Method parameters	:	none
            //
            // Method return		:	void
            //
            // Synopsis				:   This method orders the scores from largest to smallest.
            //
            // References			:   Micorsoft (2023) Enumerable.OrderBy Method. 
            //                          https://learn.microsoft.com/en-us/dotnet/api/system.linq.enumerable.orderby?view=net-8.0
            //                          
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Order Scores
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            short upperLimit; // declare an upper limit variable 
            short entry; // declare an entry short
            
            var sortedScores = file.content.OrderBy(item => int.Parse(item[1])).ToList(); // Order the entries by score

            if (sortedScores.Count < 5)
            {
                upperLimit = 5; // if the stored scores are less than 5, set the upper limit to 5
            }
            else
            {
                upperLimit = (short)sortedScores.Count; // if not, set the upper limit to the number of scores on the list
            }

            for (entry = 0; entry < upperLimit; entry++)
            {
                if (entry < sortedScores.Count)
                {
                    highscoresList.Add(sortedScores[entry]); //Add the ordered entries to the highscores list
                } 
                else
                {
                    highscoresList.Insert(0, new List<string> { "none", "0" }); // put at the beginning of the list so it gets drawn last
                                                                                // insert none if the list is not long enough
                }
            }
        }

        private void DrawRectangle(SpriteBatch spriteBatch, Rectangle rectangle, Color color)
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	DrawRectangle
            //
            // Method parameters	:	spriteBatch, rectangle, color
            //
            // Method return		:	void
            //
            // Synopsis				:   This method draws rectangles that work as the lanes for the game.
            //
            // References			:   Challacade (2021) Make Games with MonoGame - Installation and Development Fundamentals. 
            //                          https://www.youtube.com/watch?v=sPH-sNTSrhw&ab_channel=Challacade
            //                          
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Draw Rectangle
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            Texture2D pixel = new Texture2D(GraphicsDevice, 1, 1); // Create a 1x1 texture for drawing the rectangle
            pixel.SetData(new[] { Color.White }); // set the pixel data
            spriteBatch.Draw(pixel, rectangle, color);  // Draw the rectangle using the pixel texture
        }
    }
}