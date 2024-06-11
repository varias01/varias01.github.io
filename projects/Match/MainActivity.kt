package com.example.matchgame
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchgame.ui.theme.MatchGameTheme

var imageSelected = false                                                                           // boolean variable to keep track of if an image has been selected
var textSelected = false                                                                            // boolean variable to keep track of if a word has been selected
var selectedValues = arrayOf(0,0)                                                                   // an array to store the ID of the buttons selected
val arrayOfMatchStates = arrayOf( mutableStateOf(false),  mutableStateOf(false),        // an array to keep track of what matches has been made
    mutableStateOf(false),  mutableStateOf(false),  mutableStateOf(false))
val failedToMatch = mutableStateOf(false)                                                     // a boolean variable to kee track if the match was successful
var numOfMatches = 0                                                                                // a counter to track the number of matches made

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    // Method				:	onCreate
    //
    // Method parameters	:	savedInstanceState: Bundle?
    //
    // Method return		:	void
    //
    // Synopsis				:   This method loads the application and sets-up the layout.
    //
    // References			:   Android Developers (2024) ScrollState. https://developer.android.com/reference/kotlin/androidx/compose/foundation/ScrollState
    //                          Android Developers (2024) Compose layout basics. https://developer.android.com/develop/ui/compose/layouts/basics
    //
    // Modifications		:
    //							Date			Developers				Notes
    //							----			---------				-----
    //							2024-05-21		V. Arias                On App Creation
    //
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()                                                                          // enable the layout to be edge to edge of the screen
        setContent {
            MatchGameTheme {
                val scrollState = rememberScrollState()                                             // setup a scroll state variable to allow scrolling in the app
                Box(
                    modifier = Modifier
                        .fillMaxSize()                                                              // set the box component to fill the entire screen
                        .verticalScroll(scrollState)                                                // enable the app to have vertical scrolling
                        .background(Color(0xFFE97823))                                        // set the background color of the box object that holds all the elements of the app
                ) {
                    Column{
                        GreetingText(
                            title = "\nMatch the animal to its name!",                              // set the text to be displayed by the GreetingText composable
                            modifier = Modifier                                                     // set the default modifier of the column
                        )
                        ButtonsGrid(
                            modifier = Modifier                                                     // set the default modifier of the ButtonsGrid composable
                        )
                    }
                }
            }
        }
    }
}

fun playSoundEffect(context: Context, sound: Int, nextSound: Int) {
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    // Method				:	playSoundEffect
    //
    // Method parameters	:	context, sound, nextSound
    //
    // Method return		:	void
    //
    // Synopsis				:   This method plays a sound when called.
    //
    // References			:   Android Developers (2024) Using MediaPlayer. https://developer.android.com/media/platform/mediaplayer
    //
    // Modifications		:
    //							Date			Developers				Notes
    //							----			---------				-----
    //							2024-05-21		V. Arias                Play Sound Effect
    //
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    val soundEffect = MediaPlayer.create(context, sound)                                            // create a media player sound object to play the sound effects
    soundEffect.start()                                                                             // start playing the sound

    if (nextSound != 0) {
        val secondSoundEffect = MediaPlayer.create(context,nextSound)                               // if there is another sound to be played, create a second media player object
        secondSoundEffect.start()                                                                   // start playing the second sound
    }
}

fun compareSelections(image: Int, text: Int, context: Context){
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    // Method				:	compareSelections
    //
    // Method parameters	:	image, text, context
    //
    // Method return		:	void
    //
    // Synopsis				:   This method compares the selected buttons to determine if they match.
    //
    // References			:   GeeksForGeeks (2023) Kotlin when expression. https://www.geeksforgeeks.org/kotlin-when-expression/
    //                          GeeksForGeeks (2021) How to Add Audio Files to Android App in Android Studio?
    //                          https://www.geeksforgeeks.org/how-to-add-audio-files-to-android-app-in-android-studio/
    //
    // Modifications		:
    //							Date			Developers				Notes
    //							----			---------				-----
    //							2024-05-21		V. Arias                Compare Selections
    //
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    var soundID = 0                                                                                 // create a variable to store the ID of the sound to be played
    var nextSoundID = 0                                                                             // variable to store the ID of a secondary sound to be played
    if (image == text){
        when (image){                                                                               // switch on the ID of the buttons selected if they match
            1-> soundID = R.raw.cat_meow                                                            // set-up the sound ID corresponding to the audio file to be played
            2-> soundID = R.raw.elephant_trumpet
            3-> soundID = R.raw.horse_neigh
            4-> soundID = R.raw.bird_chirp
            5-> soundID = R.raw.dog_bark
        }
        arrayOfMatchStates[image-1].value = true                                                    // in the array of match states, change the state of the position corresponding
                                                                                                    // to the ID of the buttons that matched
        numOfMatches++                                                                              // increase the total number of matches
        if (numOfMatches == 5) nextSoundID = R.raw.game_over                                        // if the total number of matches is 5, the game is over, play game over sound
    }
    else{
        failedToMatch.value = true                                                                  // if there was no match, set failed to match to true
        soundID = R.raw.error_message                                                               // set the ID of the sound file to the error message
    }
    playSoundEffect(context, sound = soundID,  nextSound = nextSoundID)                             // call on the playSoundEffect method to play the corresponding sounds
}


@Composable
fun GreetingText(title: String, modifier: Modifier = Modifier) {
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    // Method				:	GreetingText (Composable)
    //
    // Method parameters	:	title, modifier
    //
    // Method return		:	void
    //
    // Synopsis				:   This method sets the layout of the instructions at the top of the app.
    //
    // References			:   Android Developers (2024) Compose layout basics. https://developer.android.com/develop/ui/compose/layouts/basics
    //
    // Modifications		:
    //							Date			Developers				Notes
    //							----			---------				-----
    //							2024-05-21		V. Arias                Greeting text
    //
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,                                         // set the alignment of the column component to center
        modifier = Modifier                                                                         // set the modifier to the default
            .fillMaxWidth()                                                                         // set it to fill the width of the screen
            .background(Color(0xFFE97823))                                                    // set the background color to orange
    ){
        Text(
            text = title,                                                                           // set the text to the parameter passed
            fontSize = 25.sp,                                                                       // configure the UI settings of the text
            lineHeight = 60.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ButtonsGrid(modifier: Modifier) {
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    // Method				:	ButtonsGrid (Composable)
    //
    // Method parameters	:	modifier
    //
    // Method return		:	void
    //
    // Synopsis				:   This method creates a table layout for the game buttons.
    //
    // References			:   Android Developers (2024) Compose layout basics. https://developer.android.com/develop/ui/compose/layouts/basics
    //
    // Modifications		:
    //							Date			Developers				Notes
    //							----			---------				-----
    //							2024-05-21		V. Arias                Buttons Grid
    //
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    Row(
        horizontalArrangement = Arrangement.Center,                                                 // set the alignment of the row component to center
        modifier = Modifier                                                                         // set the modifier to the default
            .fillMaxSize()                                                                          // set-up the row to fill the max size
            .padding(5.dp)                                                                          // add padding to the rows
            .background(Color(0xFFF5D087))                                                    // set the background color to a light yellow
    ){
        Column(
            modifier = Modifier.padding(8.dp)                                                       // add padding to the columns
        ){
            ImageButton(modifier = Modifier, animal = R.drawable.cat, buttonID = 1, match = arrayOfMatchStates[0], noMatch = failedToMatch)             // create the image buttons by passing their parameters
            ImageButton(modifier = Modifier, animal = R.drawable.elephant, buttonID = 2, match = arrayOfMatchStates[1], noMatch = failedToMatch)        // to the composable method that creates them
            ImageButton(modifier = Modifier, animal = R.drawable.horse, buttonID = 3, match = arrayOfMatchStates[2], noMatch = failedToMatch)
            ImageButton(modifier = Modifier, animal = R.drawable.bird, buttonID = 4, match = arrayOfMatchStates[3], noMatch = failedToMatch)
            ImageButton(modifier = Modifier, animal = R.drawable.dog, buttonID = 5, match = arrayOfMatchStates[4], noMatch = failedToMatch)
        }
        Column(
            modifier = Modifier.padding(8.dp)                                                       // add padding to the columns
        ) {
            TextButton(modifier = Modifier, animal = "Horse", buttonID = 3, match = arrayOfMatchStates[2], noMatch = failedToMatch)                     // create the word buttons by passing their parameters
            TextButton(modifier = Modifier, animal = "Cat", buttonID = 1, match = arrayOfMatchStates[0], noMatch = failedToMatch)                       // to the composable method that creates them
            TextButton(modifier = Modifier, animal = "Dog", buttonID = 5, match = arrayOfMatchStates[4], noMatch = failedToMatch)
            TextButton(modifier = Modifier, animal = "Elephant", buttonID = 2, match = arrayOfMatchStates[1], noMatch = failedToMatch)
            TextButton(modifier = Modifier, animal = "Bird", buttonID = 4, match = arrayOfMatchStates[3], noMatch = failedToMatch)
        }
    }
}

@Composable
fun ImageButton(modifier: Modifier, animal: Int, buttonID: Int, match: MutableState<Boolean>, noMatch: MutableState<Boolean>) {
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    // Method				:	ImageButton (Composable)
    //
    // Method parameters	:	modifier, animal, buttonID, match, noMatch
    //
    // Method return		:	void
    //
    // Synopsis				:   This method builds the image buttons and controls their functionality.
    //
    // References			:   Android Developers (2024) State and Jetpack Compose. https://developer.android.com/develop/ui/compose/state
    //                          Android Developers (2024) Loading images. https://developer.android.com/develop/ui/compose/graphics/images/loading
    //                          Android Developers (2024) Side-effects in Compose. https://developer.android.com/develop/ui/compose/side-effects
    //
    // Modifications		:
    //							Date			Developers				Notes
    //							----			---------				-----
    //							2024-05-21		V. Arias                Image Button
    //
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    val height = 150.dp                                                                             // declare the height of the button
    val width = 150.dp                                                                              // declare the width of the button
    val buttonEnabled = remember { mutableStateOf(true)}                                      // declare a mutable boolean to disable the buttons when a match is made
    val context = LocalContext.current                                                              // store the local context in order to play sounds
    Button(
        onClick = {
            if (!imageSelected){                                                                    // if the user clicks on a button, and it has not been previously selected
                imageSelected = true                                                                // set the image selected boolean to true
                selectedValues[0] = buttonID                                                        // change the corresponding value of the selectedValues array equal to the
                                                                                                    // ID of the button selected
                if (textSelected){
                    compareSelections(selectedValues[0], selectedValues[1], context)                // if the text button has already been selected, compare the values of the selected buttons
                }
                else playSoundEffect(context, sound = R.raw.select_word,  nextSound = 0)            // if not, play the instructional audio
            }
                  },
        modifier = Modifier                                                                         // set the modifier settings of the button
            .size(height, width)
            .padding(2.dp),
        shape = RectangleShape,                                                                     // set the shape of the button to a rectangle
        colors = ButtonDefaults.buttonColors(Color.Cyan),                                           // set the color of the button to cyan
        enabled = buttonEnabled.value                                                               // set the button enabled equal to a mutable state, to disable it when necessary
    ){
        Image(
            painter = painterResource(id = animal),                                                 // load the corresponding image for each button
            contentDescription = "Button Image"                                                     // set the description for the content of the image
        )
    }
    LaunchedEffect(key1 = match.value, key2 = noMatch.value) {                     // LaunchedEffect to update the state when the condition is met
        if (match.value) {
            buttonEnabled.value = false                                                             // if there was a match, disable the button
            imageSelected = false                                                                   // set the selections back to false and zeroes
            textSelected = false
            selectedValues = arrayOf(0,0)
        }
        if(noMatch.value) {
            if (!imageSelected && !textSelected) failedToMatch.value = false                        // if there was no match and no buttons are selected, set failedToMatch to false
            else{
                imageSelected = false                                                               // else, set the image selected to false
                selectedValues = arrayOf(0,0)                                                       // and reset the selected values back to zero
            }
        }
    }
}

@Composable
fun TextButton(modifier: Modifier, animal: String, buttonID: Int, match: MutableState<Boolean>, noMatch: MutableState<Boolean>) {
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    // Method				:	TextButton (Composable)
    //
    // Method parameters	:	modifier, animal, buttonID, match, noMatch
    //
    // Method return		:	void
    //
    // Synopsis				:   This method builds the text buttons and controls their functionality.
    //
    // References			:   Android Developers (2024) State and Jetpack Compose. https://developer.android.com/develop/ui/compose/state
    //                          Android Developers (2024) Side-effects in Compose. https://developer.android.com/develop/ui/compose/side-effects
    //
    // Modifications		:
    //							Date			Developers				Notes
    //							----			---------				-----
    //							2024-05-21		V. Arias                Text Button
    //
    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    val height = 150.dp                                                                             // declare the height of the button
    val width = 150.dp                                                                              // declare the width of the button
    val buttonEnabled = remember { mutableStateOf(true)}                                      // declare a mutable boolean to disable the buttons when a match is made
    val context = LocalContext.current                                                              // store the local context in order to play sounds
    Button(
        onClick = {
            if (!textSelected){                                                                     // if the user clicks on a button, and it has not been previously selected
                textSelected = true                                                                 // set the text selected boolean to true
                selectedValues[1] = buttonID                                                        // change the corresponding value of the selectedValues array equal to the
                                                                                                    // ID of the button selected
                if (imageSelected){
                    compareSelections(selectedValues[0], selectedValues[1], context)                // if the image button has already been selected, compare the values of the selected buttons
                }
                else playSoundEffect(context, sound = R.raw.select_picture,  nextSound = 0)         // if not, play the instructional audio
            }
                  },
        modifier = Modifier                                                                         // set the modifier settings of the button
            .size(height, width)
            .padding(2.dp),
        shape = RectangleShape,                                                                     // set the shape of the button to a rectangle
        colors = ButtonDefaults.buttonColors(Color.Blue),                                           // set the color of the button to blue
        enabled = buttonEnabled.value                                                               // set the button enabled equal to a mutable state, to disable it when necessary
    ) {
        Text(
            text = animal,                                                                          // set the text of the button
            fontSize = 22.sp,                                                                       // configure the UI text settings
            lineHeight = 30.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
    LaunchedEffect(key1 = match.value, key2 = noMatch.value) {                     // LaunchedEffect to update the state when the condition is met
        if (match.value) {
            buttonEnabled.value = false                                                             // if there was a match, disable the button
            imageSelected = false                                                                   // set the selections back to false and zeroes
            textSelected = false
            selectedValues = arrayOf(0,0)
        }
        if(noMatch.value) {
            if (!imageSelected && !textSelected) failedToMatch.value = false                        // if there was no match and no buttons are selected, set failedToMatch to false
            else{
                textSelected = false                                                                // else, set the text selected to false
                selectedValues = arrayOf(0,0)                                                       // and reset the selected values back to zero
            }
        }
    }
}