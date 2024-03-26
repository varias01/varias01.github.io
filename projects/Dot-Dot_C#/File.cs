using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using System.IO;
using System.Linq;
using Microsoft.Xna.Framework.Content;

namespace Dot_Dot
{
    public class File
    {
        private string filePath = "highscores.txt"; //Store the path to the file in a private string
        public List<List<string>> content = new List<List<string>>(); // declare a list of lists to store all the information from the game [name][score]

        public void LoadFile()
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	LoadFile
            //
            // Method parameters	:	none
            //
            // Method return		:	void
            //
            // Synopsis				:   This method reads the contents of the game file. 
            //
            // References			:   Microsoft (2023) StreamWriter Class. https://learn.microsoft.com/en-us/dotnet/api/system.io.streamwriter?view=net-8.0
            //
            //
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Load File
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            try
            {
                using (StreamReader reader = new StreamReader(filePath)) //declare and initialize the reader object 
                {
                    string line = "";                                    // declare a string to store each line of the file
                    string[] values;                                     // declare a string array to store the values of the lines in the file

                    while ((line = reader.ReadLine()) != null)
                    {
                        values = line.Split(','); // while the next line is not null, load the line and split it on each comma

                        content.Add(values.ToList()); //add the retrieved values to the file content 
                    }
                }
            }
            catch (Exception e)
            {
                return; //If there was an error return
            }
        }

        public void SaveFile()
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	SaveFile
            //
            // Method parameters	:	none
            //
            // Method return		:	void
            //
            // Synopsis				:   This method writes the game data onto the game file.
            //
            // References			:   Microsoft (2023) StreamWriter Class. https://learn.microsoft.com/en-us/dotnet/api/system.io.streamwriter?view=net-8.0
            //
            //
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				Save File
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            try
            {
                using (StreamWriter writer = new StreamWriter(filePath)) //declare and initialize the writer object 
                {
                    foreach (List<string> row in content)
                    {
                        writer.WriteLine(string.Join(",", row)); //write each of the rows in the content list and append them with commas
                    }
                }
            }
            catch(Exception e) 
            { 
                return; //If there was an error return
            }

        }

        public bool ValidateNameInput(string input) //NOT WORKING :(
        {
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
            // Method				:	LoadFile
            //
            // Method parameters	:	none
            //
            // Method return		:	void
            //
            // Synopsis				:   This method is a constructor for the game settings and mouse settings. 
            //
            // References			:   none
            //                          
            //
            // Modifications		:
            //							Date			Developer				Notes
            //							----			---------				-----
            //							2023-12-04		V. Arias				LoadFile
            //
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=


            if (!string.IsNullOrWhiteSpace(input) && input.Length < 10) //check that the input is max 10 characters, and not empty or white space
            { 
                for (byte counter = 0; counter < input.Length; counter++)
                {
                    try
                    {
                        char currentCharacter = Char.Parse(input[counter].ToString());  //store every character in the input string one by one
                        if (!Char.IsLetterOrDigit(currentCharacter)) 
                        {
                            return true; // if the character is not a letter or digit, return true
                        }
                    }
                    catch (Exception e)
                    {
                        return true; // if there is an error reading or parsing the character, return true
                    }
                }
                return false; //if no characters caused a return, they are correct, so return false
            }
            return true; //If the string is null, white space or more than 10 characters, return true
        }
    }
}

