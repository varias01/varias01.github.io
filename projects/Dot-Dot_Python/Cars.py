import pygame #import the required libraries
import random

class Cars(pygame.sprite.Sprite):
    def __init__(self, x, y):
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    __init__
    
        Method parameters    :    self, x, y
    
        Method return        :    none
    
        Synopsis             :    This method is a constructor for the Cars object.
    
        References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Initialization
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='''
        super().__init__() 
        try:
            self.image = pygame.image.load("assets/bigCar.png") # set the image to the car sprite
        except pygame.error as e:
            print(f"Error loading car image: {e}") #print a message if there was an error loading the image
        self.rect = self.image.get_rect() #define a rect for the car sprite
        self.rect.topleft = (x, y) #define the rect's position for the car sprite
        self.noPreviousCollision = True #set the noPreviousCllision variable to true

    def update(self, game, object_speed, window):
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    update
    
        Method parameters    :    self, game, object_speed, window
    
        Method return        :    none
    
        Synopsis             :    This method updates the car objects.
    
        References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Update
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='''    
        no_new_position = True  #set the noNewPosition variable to true
        index = 0 #set the index variable to zero

        for car in game.cars_group.sprites():
            car.rect.move_ip(-object_speed, 0) #update the rect's position
            if car.rect.x < -80: #car is completely off-screen
                car.noPreviousCollision = True  #set no_previous_collision to true
                while no_new_position:
                    new_x_pos = random.randint(950, 1350) #determine a new random position for the obstacle
                    for x_pos in game.obstacles_x_positions:
                        if not (new_x_pos in range(x_pos - 60, x_pos + 60)):
                            no_new_position = False  #set no_new_positon to false
                            
                car.rect.x = new_x_pos #update the rect position
                game.obstacles_x_positions[index] = new_x_pos #store the new x position
                car.image = pygame.image.load("assets/bigCar.png") #set the car's sprite to the big car
            if game.dot.rect.colliderect(car.rect) and car.noPreviousCollision: #using images, pygame.getrect()
                car.noPreviousCollision = False #set no_previous_collision to false
                car.image = pygame.image.load("assets/bigCarHit.png") #set the car's sprite to the big hit car
                game.dot.lives -= 1; #if they collide with a car, they lose a life
            index += 1 # increase the index by one