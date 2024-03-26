import pygame #import the required libraries
import random

class Star (pygame.sprite.Sprite):
    def __init__(self, x, y):
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    __init__
    
        Method parameters    :    self, x, y
    
        Method return        :    none
    
        Synopsis             :    This method is a constructor for the Star object.
    
        References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Initialization
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='''
        super().__init__()
        try:
            self.image = pygame.image.load("assets/bigStar.png") # set the image to the star sprite
        except pygame.error as e:
            print(f"Error loading star image: {e}") #print a message if there was an error loading the image
        self.rect = self.image.get_rect() #define a rect for the star sprite
        self.rect.topleft = (x, y) #define the rect's position for the star sprite
        self.noPreviousCollision = True #set the noPreviousCllision variable to true

    def update(self, game, object_speed):
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    update
    
        Method parameters    :    self, game, object_speed
    
        Method return        :    none
    
        Synopsis             :    This method updates the star objects.
    
        References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Update
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='''  
        no_new_position = True #set the noNewPosition variable to true

        self.rect.move_ip(-(object_speed + 4), 0) #update the rect's position
        if  self.rect.x < -40: #star is completely off-screen
            self.noPreviousCollision = True #set no_previous_collision to true
            new_lane = game.lanes[random.randint(0,2)]
            while no_new_position:
                new_x_pos = random.randint(950, 1250) #determine a new random position for the obstacle
                for x_pos in game.obstacles_x_positions:
                    if not (new_x_pos in range(x_pos - 40, x_pos + 60)):
                        no_new_position = False #set no_new_positon to false
                        
            self.rect.topleft = (new_x_pos, new_lane) #update the rect with it's new random position
            game.obstacles_x_positions[3] = new_x_pos #store the new x position, offset by 3 to take cars into account
            self.image = pygame.image.load("assets/bigStar.png") #set the star's sprite to the big star
        if game.dot.rect.colliderect(self.rect) and self.noPreviousCollision: #using images, pygame.getrect()
            self.noPreviousCollision = False #set no_previous_collision to false
            self.image = pygame.image.load("assets/starHit.png") #set the star's sprite to the hit star
            game.dot.score += 1;    #if it collides with a star, they get a point
