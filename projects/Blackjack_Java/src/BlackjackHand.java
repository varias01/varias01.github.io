
public class BlackjackHand {

	private Integer pot;																										// Declare a data member called pot to 
																																// store the amount in the pot
	private static Deck gameDeck;																								// Declare a data member called gameDeck
																																// to store the deck of cards each hand
	
	public BlackjackHand() {		
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	BlackjackHand (Constructor)
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is a constructor for the Blackjack Hand class, it initializes the game's deck
		//							fills it with cards and initializes the pot. 
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Initial Blackjack Hand setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		gameDeck = new Deck();																									// Initialize the game deck as a new Deck
		gameDeck.fillDeck();																									// Call the fillDeck method to fill the deck
																																// with cards
		this.pot = 0; 																											// Set the pot to zero
	}
	
	public void newDeck() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	newDeck
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method creates and fills a new deck. 
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Create a new deck
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		gameDeck = new Deck();																									// Initialize the game deck as a new Deck
		gameDeck.fillDeck();																									// Call the fillDeck method to fill the deck
	}
	
	public Integer getPot() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	getPot
		//
		// Method parameters	:	none
		//
		// Method return		:	Integer
		//
		// Synopsis				:   This method returns the value of the pot.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Get pot
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
				
		return(this.pot);																										// Return the pot value
	}
	
	public void setPot(Integer emptyPot) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	setPot
		//
		// Method parameters	:	emptyPot
		//
		// Method return		:	void
		//
		// Synopsis				:   This method lets other classes set the value of the pot.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Set pot 
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		 this.pot = emptyPot;																									// Set the pot value to an Integer called 
		 																														// emptyPot
	 }
	
	public void bet(Player theUser, Player theDealer) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	bet
		//
		// Method parameters	:	theUser, theDealer
		//
		// Method return		:	void
		//
		// Synopsis				:   This method sets a standard bet amount, subtracts the amount from the player and dealer
		//							wallets, and adds both amounts to the pot.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				betting mechanics
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		byte bet = 10;																											// Declare a byte called bet, that establishes
																																// the bet amount
		Integer userFunds = theUser.getWallet();																				// Declare an integer to store the user's funds
		Integer dealerFunds = theDealer.getWallet();																			// Declare an integer to store the dealer's funds
		
		theUser.setWallet(userFunds - bet);																						// Set the user's wallet to their funds minus 
																																// their bet
		theDealer.setWallet(dealerFunds - bet);																					// Set the dealer's wallet to their funds minus
																																// their bet
		this.pot += bet*2;																										// Set the pot to the bet amount times two, to 
																																// account for the player and the dealer's bet
	}
	
	public void deal(Player theUser) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	deal
		//
		// Method parameters	:	theUser
		//
		// Method return		:	void
		//
		// Synopsis				:   This method deals two cards to a player and saves them in their card deck.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				dealing mechanics
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		Card cardDealt1 = Deck.deal();																							// Declare a card and store the first card dealt
		theUser.setCards(cardDealt1);																							// Add the dealt card to the user's deck
		
		Card cardDealt2 = Deck.deal();																							// Declare a card and store the second card dealt
		theUser.setCards(cardDealt2);																							// Add the dealt card to the user's deck
	}
	
	public char hit(Player theUser) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	hit
		//
		// Method parameters	:	theUser
		//
		// Method return		:	char
		//
		// Synopsis				:   This method deals a card to a player and checks if anyone has won the hand by going
		//							over 21 or with a five card monty.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				hit mechanics
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		byte userHand;																											// Declare a byte called userHand to store the 
																																// value of the user's hand
		byte userCards;																											// Declare a byte userCards to store the number
																																// of cards the user has
		Card cardDealt = Deck.deal();																							// Declare a card and store the card dealt
		theUser.setCards(cardDealt);																							// Add the dealt card to the user's deck
		
		userHand = theUser.getHandValue();																						// Set the user hand variable to its new value
		userCards = theUser.getNumOfCards();																					// Set the user cards number to its new value
		
		if (userHand > 21) {
			return('D');																										// Return D if the player goes over 21
		}
		else if (userCards == 5) {
			return('U');																										// Return U if the player has a 5 card monty
		}
		else {
			return('N'); 																										// Return N if no one has won
		}
	}
	
	public char stand(Player theUser, Player theDealer) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	stand
		//
		// Method parameters	:	theUser, theDealer
		//
		// Method return		:	char
		//
		// Synopsis				:   This method deals the dealer's cards and checks if anyone has won the hand by going
		//							over 21 or with a five card monty.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				stand mechanics
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		byte dealerHand = theDealer.getHandValue();																				// Declare a byte called dealerHand to store the 
																																// value of the dealer's hand
		byte dealerCards = theDealer.getNumOfCards();																			// Declare a byte dealerCards to store the number
																																// of cards the dealer has
		byte userHand = theUser.getHandValue();																					// Declare a byte called userHand to store the 
																																// value of the user's hand
		
		Card cardDealt1 = Deck.deal();																							// Declare a card and store the first card dealt
		theDealer.setCards(cardDealt1);																							// Add the dealt card to the deale's deck
		
		Card cardDealt2 = Deck.deal();																							// Declare a card and store the second card dealt
		theDealer.setCards(cardDealt2);																							// Add the dealt card to the deale's deck
		
		dealerHand = theDealer.getHandValue();																					// Set the dealer hand variable to its new value
		dealerCards = theDealer.getNumOfCards();																				// Set the dealer cards number to its new value
		
		if (dealerHand <= userHand && dealerCards < 5) {
			if (userHand == 21) {
				while(dealerHand < userHand && dealerCards < 5) {
					Card cardDealt = Deck.deal();																				// If the user's hand value is 21, the dealer's hand    
																																// value is less than the user and the dealer has less
																																// than 5 cards, deal a card and save it to cardDealt
					theDealer.setCards(cardDealt);																				// Add the dealt card to the deale's deck
					dealerHand = theDealer.getHandValue();																		// Set the dealer hand variable to its new value
					dealerCards = theDealer.getNumOfCards();																	// Set the dealer cards number to its new value
				}
			}
			else {
				while(dealerHand <= userHand && dealerCards < 5) {													
					Card cardDealt = Deck.deal();																				// If the dealer's hand value is less than the user   
																																// and the dealer has less than 5 cards, deal a card
																																// and save it to cardDealt
					theDealer.setCards(cardDealt);																				// Add the dealt card to the deale's deck
					dealerHand = theDealer.getHandValue();												 						// Set the dealer hand variable to its new value
					dealerCards = theDealer.getNumOfCards();																	// Set the dealer cards number to its new value
				}
			}
		}
		
		if (dealerHand > 21) {
			return('U'); 																										// Return U if the dealer goes over 21
		}
		else if (dealerCards == 5) {
			return('D'); 																										// Return D if the dealer has a 5 card monty
		}
		else {
			return('N');																										// Return N if no one has won
		}
		
	}
	
	public char evaluateHand(Player theUser, Player theDealer) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	evaluateHand
		//
		// Method parameters	:	theUser
		//
		// Method return		:	char
		//
		// Synopsis				:   This method deals the dealer's cards and checks if anyone has won the hand by going
		//							over 21 or with a five card monty.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				stand mechanics
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		byte userHand = theUser.getHandValue();																						// Declare a byte called userHand to store the 
																																	// value of the user's hand
		byte dealerHand = theDealer.getHandValue();																					// Declare a byte called dealerHand to store the 
																																	// value of the dealer's hand
		if (userHand > dealerHand) {
			return('U');																											// Return U if the player's hand value is greater 
																																	// than the dealer's hand value
		}
		else if (dealerHand > userHand) {
			return('D');																											// Return D if the dealer's hand value is greater
																																	// than the player's hand value
		}
		else {
			return('T'); 																											// Return T if the player's and the dealer's hand
																																	// value are both 21
		}

	}
	
}

