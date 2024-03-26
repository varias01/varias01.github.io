import csv

class File():
    def __init__(self):
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    __init__
    
        Method parameters    :    self
    
        Method return        :    none
    
        Synopsis             :    This method is a constructor for the file object.
    
        References           :    none
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Initialization
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=''' 
        self.file_path = 'assets/highscores.csv' #create a variable to store the file's path
        self.content = [] #declare a list to store the file data
        
    def load_file(self):
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    load_file
    
        Method parameters    :    self
    
        Method return        :    none
    
        Synopsis             :    This method loads the game information from the file.
    
        References           :    W3Schools (2023) Python Try Except. https://www.w3schools.com/python/python_try_except.asp
                                  Python documentation 3.12.0 (2023) CSV File Reading and Writing. 
                                  https://docs.python.org/3/library/csv.html
                                  
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Load File
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=''' 
        try:
            with open(self.file_path, 'r') as file:
                file_reader = csv.reader(file) #read the file
                for row in file_reader:
                    self.content.append(row) #save the row to the content list 
        except:
            print('File Loading Error') # If there is an error, print error message
                                
                
    def save_file(self):
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    save_file
    
        Method parameters    :    self
    
        Method return        :    none
    
        Synopsis             :    This method saves the game information to the file.
    
        References           :    W3Schools (2023) Python Try Except. https://www.w3schools.com/python/python_try_except.asp
                                  Python documentation 3.12.0 (2023) CSV File Reading and Writing. 
                                  https://docs.python.org/3/library/csv.html
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Save File
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=''' 
        try:
            with open(self.file_path, 'w',  newline='') as file: 
                file_writer = csv.writer(file) #intialize file writer
                for row in self.content:
                    file_writer.writerow(row)  #write row to the file
        except:
            print('File Writing Error') # If there is an error, print error message
                
    def validate_name_input(self, input): 
        '''=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        Method               :    validate_name_input
    
        Method parameters    :    self, input
    
        Method return        :    none
    
        Synopsis             :    This method validates if the name of the user is a valid entry.
    
        References           :    w3schools (2023) Python String isidentifier() Method. 
                                  https://www.w3schools.com/python/ref_string_isidentifier.asp
    
        Modifications        :
                              Date            Developer                Notes
                              ----            ---------                -----
                             2023-12-04        V. Arias                Validate Name Input
    
        =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=''' 
        if not input.isidentifier() or len(input) > 10: #returns true if the string only contains alphanumeric letters (a-z) and (0-9), or underscores (_).
            return True
        return False #return false if string is not valid
    