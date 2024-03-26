import pygame        # import the pygame library

class Dot(pygame.sprite.Sprite):
    def __init__(self, x, y):
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    __init__
    
        Method parameters    :    self, x, y
    
        Method return        :    none
    
        Synopsis             :    This method is a constructor for the Dot object.
    
        References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Initialization
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=''' 
        super().__init__()
        try:
            self.image = pygame.image.load("assets/dot.png") # set the image to the dot sprite
        except pygame.error as e:
            print(f"Error loading dot image: {e}") #print an error message if there is an issue with the sprite loading
        self.rect = self.image.get_rect() #define a rect for the dot sprite
        self.rect.topleft = (x, y + 20) #define the rect's position for the dot sprite
        self.lives = 3 #set the player's lives to three
        self.score = 0 #set the initial score to zero
        self.delta_x = 0 #set delta x to zero
        self.delta_y = 0 #set delta y to zero
        
    def update(self, gravity, lanes, current_lane):
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    update
    
        Method parameters    :    self, gravity, lanes, current_lane
    
        Method return        :    none
    
        Synopsis             :    This method updates the dot object.
    
        References           :    Pygame (2023) Pygame Documentation. https://www.pygame.org/docs/
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Update
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=''' 
        if self.rect.x < 205:
            self.delta_x = 2 # move 2 units every update
            self.rect.x += self.delta_x #update the rects position as well
        if self.rect.x > 205: 
            self.rect.x = 205 #if somehow it goes beyond the center of the screen, bring it back to the center 

        if self.delta_y > 0 or self.rect.y < lanes[current_lane] + 20:  
            self.rect.y -= self.delta_y #update the dot's rect's y position
            self.delta_y -= gravity #subtract gravity from the jump, so you jump less and fall every update

        if self.rect.y > lanes[current_lane]+20:    
            self.rect.y = lanes[current_lane]+20 #make sure the player stays on the floor
            
        if self.rect.y == lanes[current_lane]+20 and self.delta_y < 0:
            self.delta_y = 0 #if the dot is back in the lane, set the delta y back to zero
            