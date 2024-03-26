import java.util.ArrayList;
import java.util.Random;

public class Deck {
	
	private static ArrayList<Card> cardDeck;																					// Declare a data member array list to store
																																// the deck of cards
	
	public Deck(){
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	Deck (Constructor)
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is a constructor for the deck class, it initializes the cardDeck array list. 
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Initial Deck setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		cardDeck = new ArrayList<Card>();																						// Set the cardDeck to a new array list of 
																																// cards
	}
	
	private void addCard(String image, char suit, byte value){
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void addCard
		//
		// Method parameters	:	image, suit, value
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is creates and adds a card to the card deck. 
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Create cards
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		Card newCard = new Card();																								// Define a new card to create each card
		
		newCard.setCardImage(image);																							// Set the new card's image 
		newCard.setCardSuit(suit);																								// Set the new card's suit
		newCard.setCardValue(value);																							// Set the new card's value
		
		cardDeck.add(newCard);																									// Add the new card to the cardDeck
	}
	
	public void fillDeck() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void fillDeck
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method creates the deck, it calls the addCard method and creates a full deck of cards. 
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Create the deck
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		byte counter;																											// Define a counter for the loop structures
		byte value = 0;																											// Define a byte value and set it to zero
		String imageName = "";																									// Define a String imageName and set it to empty
		
		for (counter = 1; counter <= 13; counter ++) {																			// Create heart suit
			
			imageName = "/cards/" + counter + "H.png";																			// Set imageName to the path of the location of
																																// the image file of the card
			
			if (counter<10) {
				value = counter;																								// If the counter is less than 10, set the card
																																// value to the counter
			}
			else {
				value = 10;																										// If the counter is greater than 10, set the 
																																// card value to 10
			}
			
			addCard(imageName,'H',value);																						// Call the addCard method with the imageName, 
																																// suit and value parameters 
		}
	
		for (counter = 1; counter <= 13; counter ++) {																			// Create Diamond suit
					
			imageName = "/cards/" + counter + "D.png";																			// Set imageName to the path of the location of
																																// the image file of the card
			if (counter<10) {
				value = counter;																								// If the counter is less than 10, set the card
																																// value to the counter
			}
			else {
				value = 10;																										// If the counter is greater than 10, set the 
																																// card value to 10
			}
			addCard(imageName,'D',value);																						// Call the addCard method with the imageName, 
																																// suit and value parameters
		}
	
		for (counter = 1; counter <= 13; counter ++) {																			// Create club suit
			
			imageName = "/cards/" + counter + "C.png";																			// Set imageName to the path of the location of
																																// the image file of the card
			if (counter<10) {
				value = counter;																								// If the counter is less than 10, set the card
																																// value to the counter
			}
			else {
				value = 10;																										// If the counter is greater than 10, set the 
																																// card value to 10
			}
			addCard(imageName,'C',value);																						// Call the addCard method with the imageName, 
																																// suit and value parameters
		}
	
		for (counter = 1; counter <= 13; counter ++) {																			// Create spade suit
			
			imageName = "/cards/" + counter + "S.png";																			// Set imageName to the path of the location of
																																// the image file of the card
			if (counter<10) {
				value = counter;																								// If the counter is less than 10, set the card
																																// value to the counter
			}
			else {
				value = 10;																										// If the counter is greater than 10, set the 
																																// card value to 10
			}
			addCard(imageName,'S',value);																						// Call the addCard method with the imageName, 
																																// suit and value parameters
		}
	}
	
	public static Card deal(){
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	deal
		//
		// Method parameters	:	none
		//
		// Method return		:	Card
		//
		// Synopsis				:   This method takes a random number, selects the card from the deck in the position of the 
		//							random number and returns the card.
		//
		// References			:   Oracle (2023) Class Random. Oracle. https://docs.oracle.com/javase/8/docs/api/java/util/Random.html
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Select a random card from the deck
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		int sizeOfDeck;																											// Define an integer sizeOfDeck to store the size
																																// of the deck array
		int randCard;																											// Define an integer randCard to store a random
																																// card selection number
		Card dealtCard;																											// Define a card to store the dealt card
		Random randNum;																											// Define a random number generator randNum
		
		sizeOfDeck = cardDeck.size();																							// Set the size of the deck to the size of the 
																																// cardDeck
		
		randNum = new Random();																									// Instantiate the random object
		randCard = randNum.nextInt(sizeOfDeck);																					// Generate a random number from 0 to the size 
																																// of the deck
		dealtCard = cardDeck.get(randCard);																						// Set the dealtCard to the card in the cardDeck
																																// that's in the position of the random number
		
		cardDeck.remove(randCard);																								// Remove Card from the deck to avoid repetition
		
		return dealtCard;																										// Return the dealtCard
	}
}
