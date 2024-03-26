import java.util.ArrayList;

public class Player {

	private Integer wallet;																										// Declare a data member wallet to 
																																// store the wallet of a player
	private ArrayList<Card> playerCards;																						// Declare a data member array list
																																// to store the player's cards
	
	public Player(){
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	Player (Constructor)
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is a constructor for the player class, it initializes the player's wallet and 
		//							an array list for the player's cards. 
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Initial Player setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		this.wallet = 0;																										// Initialize the wallet to zero
		playerCards = new ArrayList<Card>();																					// Initialize the playerCards to an 
																																// array list of cards
	}
	
	 public void setWallet(Integer funds){
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	setWallet
		//
		// Method parameters	:	funds
		//
		// Method return		:	void
		//
		// Synopsis				:   This method allows other classes to set the funds for the wallet.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Set Wallet
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		 	
		 this.wallet = funds;																									// Set the wallet to a certain amount
	 }
	 
	 public Integer getWallet(){
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	getWallet
		//
		// Method parameters	:	none
		//
		// Method return		:	Integer
		//
		// Synopsis				:   This method allows other classes to set the funds for the wallet.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Get Wallet
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=	
		 
		 return(this.wallet);																									// Return the amount that's in the wallet
	 }
	 
	 public void setCards(Card deckCard){
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	setCards
		//
		// Method parameters	:	deckCard
		//
		// Method return		:	void
		//
		// Synopsis				:   This method allows other classes to add cards to the player's deck.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Set Player Cards
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		 
		playerCards.add(deckCard);																								// Add a card from the deck to the player's 
																																// array
	}
	 
	public void emptyCards() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	emptyCards
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method removes all the cards from the player's deck.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Erase Player Cards
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		playerCards.clear();																									// Erase all cards from the player's deck
	}
	 
	public ArrayList<Card> getCards() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	getCards
		//
		// Method parameters	:	none
		//
		// Method return		:	ArrayList
		//
		// Synopsis				:   This method returns the array of cards of the player.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Get Array of Player Card
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		return(playerCards);																									// Returns the player's cards in an array
	}
	 
	public byte getNumOfCards() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	getNumOfCards
		//
		// Method parameters	:	none
		//
		// Method return		:	byte
		//
		// Synopsis				:   This method returns the number of cards in the player deck.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Get number of cards in the player deck
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=	
		
		byte numberOfCards;																										// Declare a byte to store the number of cards
		
		numberOfCards = (byte)playerCards.size();																				// Set the numberOfCards to the size of the
																																// player cards array
		return numberOfCards;																									// Return the number of cards
	}
	 
	public byte getHandValue() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	getHandValue
		//
		// Method parameters	:	none
		//
		// Method return		:	byte
		//
		// Synopsis				:   This method returns the combined value of the cards in the player's deck.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Get the hand value in the player deck
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		Card currentCard;																										// Define a card to store the current card being 
																																// evaluated
		byte numberOfCards;																										// Define a byte to store the number of cards 
		byte aces;																												// Define a byte to track the number of aces
		byte cardValue;																											// Define a byte to store each card's value 
		byte handValue;																											// Define a byte to store the hand's value
		
		numberOfCards = (byte)playerCards.size();																				// Set number of cards to the size of the player's
																																// deck
		aces = 0;																												// Set aces to zero
		cardValue = 0;																											// set cardValue to zero
		handValue = 0;																											// Set handValue to zero
		
		for(byte counter = 0; counter < numberOfCards; counter++) {
			
			currentCard = playerCards.get(counter);																				// Set the current card to the card in the player's
																																// array in the position of the counter
			cardValue = currentCard.getCardValue();																				// Set the cardValue to the current card value
		
			if (cardValue == 1) {
				aces++;																											// If the value of the card is 1, increase the aces
			}
			
			handValue += cardValue;																								// Update the hand value
		}
		
		if ((handValue + 10 * aces) <= 21) {
			handValue += 10 * aces;																								// If the hand value is less than 21, make the aces
																																// worth 11 points
		}
		return(handValue);																										// Return the final hand value
	} 
}