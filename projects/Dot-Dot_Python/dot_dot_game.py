import pygame                                                       # import the necessary libraries
from enum import Enum
from pygame.examples.grid import Game
import Dot                                                          # import the Dot file
import Cars                                                         # import the Cars file
import Star                                                         # import the Star file
import File                                                         # import the File file

pygame.init()                                                       # initialize Pygame

class Game_state(Enum):                                             # declare an enum class to switch between game states
    MENU = 1                                                        # declare and enumerate the game states 
    GAME = 2
    NAME = 3
    HIGHSCORES = 4

WIDTH = 900                                                         # declare a game constant for the Width of the window
HEIGHT = 600                                                        # declare a game constant for the Height of the window
FPS = 60                                                            # declare a game constant for the refresh rate
white = (255, 255, 255)                                             # declare a game constant to store the RGB value of the color white
black = (0,0,0)                                                     # declare a game constant to store the RGB value of the color black

window = pygame.display.set_mode((WIDTH, HEIGHT))                   # create the application window 
pygame.display.set_caption("Dot-Dot")                               # set the caption of the application window
game_font = pygame.font.Font('assets/NotoSansGeorgian-SemiBold.ttf', 21)            # declare the fonts to be used in the game, for the game,
titles_font = pygame.font.Font('assets/NotoSansGeorgian-SemiBold.ttf', 34)          # subtitles and titles
game_title_font = pygame.font.Font('assets/NotoSansGeorgian-SemiBold.ttf', 84)

timer = pygame.time.Clock()                                                         # declare the timer to control the frame rate

def menu_screen(window):
    '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    Method               :    menu_screen

    Method parameters    :    window

    Method return        :    none

    Synopsis             :    This function draws the menu screen.

    References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
                              Ankthon (2022) Python | Display text to PyGame window. 
                              https://www.geeksforgeeks.org/python-display-text-to-pygame-window/

    Modifications        :
                          Date            Developer                Notes
                          ----            ---------                -----
                         2023-12-04        V. Arias                Menu Screen

    =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=''' 
    image = pygame.image.load("assets/menus_bg.png")                                            # load the image for the background of the screen
    window.blit(image, (0,0))                                                                   # draw the background image

    menu_game_title = game_title_font.render(f'Dot-Dot', True, black)                           # render the game title text using a formatted string
    menu_start_text = titles_font.render(f'Press S to START...', True, black)                   # render the menu start text using a formatted string
    menu_highscore_text = titles_font.render(f'Press H to see the HIGH SCORES...', True, black) # render the high score text using a formatted string
    window.blit(menu_game_title, (285, 100))                                                    # draw the game title
    window.blit(menu_start_text, (300, 340))                                                    # draw the menu start text
    window.blit(menu_highscore_text, (180, 440))                                                # draw the high score text             
    pygame.display.flip()                                                                       # update the pygame display
    
def enter_name_screen(window, input_rect, player_input, error):
    '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    Method               :    enter_name_screen

    Method parameters    :    window, input_rect, player_input, error

    Method return        :    none

    Synopsis             :    This function draws the enter name screen.

    References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
                              Ankthon (2022) Python | Display text to PyGame window. 
                              https://www.geeksforgeeks.org/python-display-text-to-pygame-window/
                              ChetanJha888 (2021) How to create a text input box with Pygame?
                              https://www.geeksforgeeks.org/how-to-create-a-text-input-box-with-pygame/ 

    Modifications        :
                          Date            Developer                Notes
                          ----            ---------                -----
                         2023-12-04        V. Arias                Enter Name Screen

    =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='''  
    image = pygame.image.load("assets/menus_bg.png")                                                                # load the image for the background of the screen
    window.blit(image, (0,0))                                                                                       # draw the background image

    name_text = titles_font.render(f'Please enter your name:', True, black)                                         # render the name text using a formatted string
    name_next_text = titles_font.render(f'Press ENTER to continue...', True, black)                                 # render the name next text using a formatted string
    error_message1 = game_font.render(f'Enter a maximum of 10 letters, numbers and/or underscores.', True, black)   # render the first part of the error message text using a formatted string
    error_message2 = game_font.render(f'Do not start with a number.', True, black)                                  # render the second part of the error message text using a formatted string
    window.blit(name_text, (250, 100))                                                                              # draw the game title
    window.blit(name_next_text, (240, 500))                                                                         # draw the name next (press enter) text
    if(error):
        window.blit(error_message1, (150, 400))                                                                     # if there was an error in the input, display the error message part1
        window.blit(error_message2, (300, 425))                                                                     # and part 2
    pygame.draw.rect(window, (black), input_rect, 4)                                                                # draw a rectangle for the input
    text_surface = game_font.render(player_input, True, black)                                                      # render the text that the user has entered 
    width = max(200, text_surface.get_width() + 10)                                                                 # determine the width of the input rect according to the entry of the player
    input_rect.w = width                                                                                            # set the input rect width equal to the previously calculated width
    window.blit(text_surface, (input_rect.x + 5, input_rect.y + 5))                                                 # draw the inputed text
    pygame.display.flip()                                                                                           # update the pygame display
    
def highscores_screen(window, file_content, gameOver):
    '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    Method               :    highscores_screen

    Method parameters    :    window, file_content, gameOver

    Method return        :    none

    Synopsis             :    This function draws the high scores screen and orders the high score values obtained from the file.

    References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
                              Ankthon (2022) Python | Display text to PyGame window. 
                              https://www.geeksforgeeks.org/python-display-text-to-pygame-window/
                              w3schools (2023) Python List insert() Method. https://www.w3schools.com/python/ref_list_insert.asp
                              Kolade C. (2023) Lambda Sorted in Python – How to Lambda Sort a List. https://www.freecodecamp.org/news/lambda-sort-list-in-python/

    Modifications        :
                          Date            Developer                Notes
                          ----            ---------                -----
                         2023-12-04        V. Arias                Menu Screen

    =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='''  
    highscores_list = []                                                                                            # declare a list to store the high score values

    image = pygame.image.load("assets/menus_bg.png")                                                                # load the image for the background of the screen
    window.blit(image, (0,0))                                                                                       # draw the background image

    highscore_title_text = titles_font.render(f'HIGH SCORES', True, black)                                          # render the high score text using a formatted string
    window.blit(highscore_title_text, (330, 50))                                                                    # draw the high score title text  
    
    if not gameOver:
        highscore_next_text = titles_font.render(f'Press M to go back to the menu...', True, black)                 # if the game is running, render the go back to menu text
        window.blit(highscore_next_text, (190, 500))                                                                # draw the go back to menu text
    else:
        highscore_next_text = titles_font.render(f'Thank you for playing! Press ESC to exit...', True, black)       # if game over, render the thank you for playing text
        window.blit(highscore_next_text, (120, 500))                                                                # draw the than you for playing text    
     
    sorted_scores = sorted(file_content, key = lambda item: int(item[1]))                                           # convert item to a numeric type so that it sorts properly by value
                                                                                                                    # and sort they items using a lambda function to take the second index
                                                                                                                    # of each row in the file's data (which is the score)
    if len(sorted_scores) < 5:
        upper_limit = 5                                                                                             # if the list has less than 5 elements, set the upper limit to 5
    else:
        upper_limit = len(sorted_scores)                                                                            # otherwise set the limit to the size of the list
    
    for entry in range(upper_limit):
        if entry < len(sorted_scores):
            highscores_list.append(sorted_scores[entry])                                                            # Print the first five items of the list, or 'none' if they don't exist
        else:
            highscores_list.insert(0,['none', '0'])                                                                 # insert none if the list is no long enough   
             
    index_counter = len(highscores_list) - 1                                                                        # declare an index variable thst's the length of the high scores list minus one
    for index in range(5):
        name = highscores_list[index_counter][0]                                                                    # get the name of the player
        score = highscores_list[index_counter][1]                                                                   # get the score of the player
        highscore_entry_text = game_font.render(f'{name} . . . . . . . . . . . . . . . . . . . {score}', True, black)   # render the high score entry text
        window.blit(highscore_entry_text, (290, 150 + 50 * index))                                                      # draw the high score entry text
        index_counter -= 1                                                                                              # decrease the index counter by one
    pygame.display.flip()                                                                                               # update the pygame display

class Game():
    def __init__(self):
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    __init__
    
        Method parameters    :    self
    
        Method return        :    none
    
        Synopsis             :    This method is a constructor for the game object.
    
        References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
                                  Ankthon (2022) Python | Display text to PyGame window. 
                                  https://www.geeksforgeeks.org/python-display-text-to-pygame-window/
                                  Bailey C. (2023) Sprite Groups. https://realpython.com/lessons/sprite-groups/ 
                                  w3schools (2023) Python zip() Function. https://www.w3schools.com/python/ref_func_zip.asp
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Initialization
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=''' 
        self.gravity = 0.9                                                              # declare a gravity variable to handle jumps        
        self.lanes = [180, 330, 480]                                                    # declare a lanes list to store the positions of each lane
        self.object_speed = 2                                                           # declare a variable to initialize the object speed
        self.current_lane = 1                                                           # declare a variable to determine in which lane the player will start
        self.all_sprites = pygame.sprite.Group()                                        # declare a sprite group for all the sprites of the game
        self.cars_group = pygame.sprite.Group()                                         # declare a sprite group for the cars 
        self.stars_group = pygame.sprite.Group()                                        # declare a sprite group for all the stars
        self.obstacles_x_positions = []                                                 # declare a list to store the x positions of the obstacles in the game
        self.dot = Dot.Dot(50,350)                                                      # initialize the Dot object
        self.all_sprites.add(self.dot)                                                  # add the dot object to the all sprites list
        self.image = pygame.image.load("assets/bg.png")                                 # load the backgrounf image for the game screen    

        for pos_x, lane in zip([900, 1000, 1100], [180, 330, 480]):
            car = Cars.Cars(pos_x, lane)                                                # initialize the car objects with their x position and the lane
            self.cars_group.add(car)                                                    # add the car object to the cars group
            self.all_sprites.add(car)                                                   # add the car to the all sprites group
            self.obstacles_x_positions.append(pos_x)                                    # store the x position of the car created
        
        star = Star.Star(950, 330)                                                      # initialize the star object
        self.stars_group.add(star)                                                      # add the star object to the stars group
        self.all_sprites.add(star)                                                      # add the star object to the all sprites group
        self.obstacles_x_positions.append(950)                                          # add the x position to the x positions list
         
    def update(self):
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    update
    
        Method parameters    :    self
    
        Method return        :    void
    
        Synopsis             :    This method updates the game objects.
    
        References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Update
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=''' 
        if (self.object_speed < 12):
            self.object_speed += 0.0005                                 # gradually increase the object speed every update  
        self.dot.update(self.gravity, self.lanes, self.current_lane)    # update the dot object by calling its update method
        self.cars_group.update(self, self.object_speed, window)         # update the cars sprite group
        self.stars_group.update(self, self.object_speed)                # update the stars sprite group
    
    def draw_game_window(self):
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    draw_game_window
    
        Method parameters    :    self
    
        Method return        :    none
    
        Synopsis             :    This method draws the game screen and all the game objects.
    
        References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
                                  Ankthon (2022) Python | Display text to PyGame window. 
                                  https://www.geeksforgeeks.org/python-display-text-to-pygame-window/
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Draw Game Window
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=''' 
        line = pygame.draw.rect(window, (97,97,125), [0, 220, WIDTH, 8])            # declare and draw the first lane's rect
        line2 = pygame.draw.rect(window, (91,91,125), [0, 370, WIDTH, 8])           # declare and draw the second lane's rect
        line3 = pygame.draw.rect(window, (97,97,125), [0, 520, WIDTH, 8])           # declare and draw the third lane's rect
        score_text = game_font.render(f'Score: {self.dot.score}', True, white)      # render the score text using a formatted string
        lives_text = game_font.render(f'Lives: {self.dot.lives}', True, white)      # render the lives text using a formatted string
        instructions_text = game_font.render('SPACE: Jump in lane    ARROW UP: Go up 1 lane    ARROW DOWN: Go down 1 lane', True, white) # render the instructions text using a formatted string
        window.blit(score_text, (40, 20))                                           # draw the score text
        window.blit(lives_text, (780, 20))                                          # draw the lives text
        window.blit(instructions_text, (40, 560))                                   # draw the instructions text
        self.all_sprites.draw(window)                                               # use the all sprites group to draw all sprites 
        pygame.display.flip()                                                       # update the pygame display
    
def main(window):    
    '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    Method               :    main

    Method parameters    :    window

    Method return        :    none

    Synopsis             :    This function contains the application and the game loops, and it handles the user's input.

    References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
                              LeMaster Tech (2022) Create an Endless Runner Game Using Python and Pygame! Infinite Obstacle Jumping, Full Game Tutorial.
                              https://www.youtube.com/watch?v=ZV8TNrwqG1Y
                              Yasoob M. (2017) Ternary Operators. https://book.pythontips.com/en/latest/ternary_operators.html
                              

    Modifications        :
                          Date            Developer                Notes
                          ----            ---------                -----
                         2023-12-04        V. Arias                Main

    =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=''' 
    current_state = Game_state.MENU.name                                        # set the current game state to the menu
    game_file = File.File()                                                     # initialize the file object
    game_file.load_file()                                                       # using the file object, load the game file
    gameOver = False                                                            # set the game over variable to false

    run_app = True                                                              # set the run app variable to true to run the game app
    while run_app:
        match current_state:
            case 'MENU':
                menu_screen(window)                                             # call the menu screen function to draw the main menu
                
                for event in pygame.event.get():
                    if event.type == pygame.QUIT: 
                        run_app = False                                         # set the run app variable to false if the player quits the app
                        break
                    if event.type == pygame.KEYDOWN:
                        if event.key == pygame.K_s:
                            current_state = Game_state.GAME.name                # change the game state to Game if the player presses the s key
                        if event.key == pygame.K_h:
                            current_state = Game_state.HIGHSCORES.name          # change the current state to high score if the player presses the h key
            case 'GAME':
                game = Game()                                                   # initialize the game object            
                run_game = True                                                 # set the run game variable to true to run the game cycle
                while run_game:
                    timer.tick(FPS)                                             # runs at 60 frames per second to regulate the frame rate
                    window.blit(game.image, (0,0))                              # draw the game background                      
                    for event in pygame.event.get():
                        if event.type == pygame.QUIT:                   
                            run_game = False                                    # set the run game variable to false if the player quits the app
                            run_app = False                                     # set the run app variable to false if the player quits the app
                        if event.type == pygame.KEYDOWN:
                            if event.key == pygame.K_UP and game.dot.delta_y == 0:
                                game.dot.delta_y = 10                               # if the arrow up key is pressed, set the dot's delta y variable to 10
                                game.current_lane -= 1                              # subtract 1 from the current lane
                                if game.current_lane < 0: 
                                    game.current_lane = 0                           # if the current lane goes below 0, set it back to 0
                            if event.key == pygame.K_DOWN and game.dot.delta_y == 0:
                                game.dot.delta_y = 4                                # if the arrow down key is pressed, set the dot's delta y variable to 4
                                game.current_lane += 1                              # add 1 to the current lane
                                if game.current_lane > 2: 
                                    game.current_lane = 2                           # if the current lane goes above 2, set it back to 2
                            if event.key == pygame.K_SPACE and game.dot.delta_y == 0:
                                game.dot.delta_y = 14                               # if the space bar is pressed, set the dot's delta y to 14
                                  
                    game.update()                                                   # update all objects
                    game.draw_game_window()                                         # draw the game window
                           
                    if game.dot.lives == 0: 
                        game.dot.kill()                                             # if the player has zero lives, kill the dot object
                        current_state = Game_state.NAME.name                        # set the current game state to enter Name
                        run_game = False                                            # set run game to false
                if run_app == False:
                    break                                                           # break if the player has quit the app
            case 'NAME':
                player_input = ""                                                   # declare a player input variable to store the players input
                input_rect = pygame.Rect(350, 300, 500, 50)                         # declare a rect object to receive the input
                error = False                                                       # set the error variable to false
                invalid_input = True                                                # set the invalid input to true
                gameOver = True                                                     # set the game over variable to true
                
                while invalid_input and run_app:
                    for event in pygame.event.get():
                        if event.type == pygame.QUIT:
                            run_app = False                                         # set the run app variable to false if the player quits the app
                            break
                        elif event.type == pygame.KEYDOWN:
                            if event.key == pygame.K_RETURN:
                                invalid_input = game_file.validate_name_input(player_input)     # set the invalid input boolean equal to the velue returned by the input validation method
                                if not invalid_input:
                                    player_found = False                                        # if the input is valid, set the player found variable to false
                                    for row in game_file.content:
                                        if row[0] == player_input:
                                            player_found = True                                 # if the input matches a player's name, set player foun to true
                                            player_highscore = row[1]                           # set the player's high score to the player's previous score
                                            if game.dot.score > int(player_highscore):
                                                row[1] = str(game.dot.score)                    # if the current score is larger than the previous score, set the player's current score as their new high score
                                    if not player_found:
                                        game_file.content.append([player_input, game.dot.score])    # if the player was not found, add the new player and their score to the list
                                    current_state = Game_state.HIGHSCORES.name                  # set the current state to the high score table
                                    game_file.save_file()                                       # using the game file object, save the information to the file
                                else:
                                    error = True                                                # if the input is not valid, set the error variable to true
                                    player_input = ""                                           # empty the player input variable
                            elif event.key == pygame.K_BACKSPACE:
                                player_input = player_input[:-1]                                # if the backspace key is pressed, eliminate the last character from the input string
                            else:
                                player_input += event.unicode if event.unicode else ""          # or else add the player_input or nothing depending on the entry in the update
                
                    enter_name_screen(window, input_rect, player_input, error)                  # draw the enter name screen
                    
            case 'HIGHSCORES':
                highscores_screen(window, game_file.content, gameOver)                          # draw the high score screen
                for event in pygame.event.get():
                    if event.type == pygame.QUIT: 
                        run_app = False                                         # set the run app variable to false if the player quits the app
                        break
                    if event.type == pygame.KEYDOWN:
                        if event.key == pygame.K_m and not gameOver:
                            current_state = Game_state.MENU.name                # if not game over, change the state to the menu screen when M is pressed
                        if event.key == pygame.K_ESCAPE and gameOver:
                            run_app = False                                     # if game over, quit app when ESC is pressed
    pygame.quit()                                                               # quit pygame
    quit()                                                                      # quit python program


if __name__ == "__main__": 
    main(window)                                                                # only call the main if we run this file directly



