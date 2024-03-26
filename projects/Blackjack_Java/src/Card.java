
public class Card {

	private String image;																									// Define a data member to store the card
																															// image path
	private char suit;																										// Define a data member to store the card
																															// suit
	private byte value;																										// Define a data member to store the card
																															// value
	
	public Card(){
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	Card (Constructor)
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is a constructor for the card class, it initializes the values for the card 
		//							image, suit and value.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Initial Card setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		this.image = "";																									// Set the card image to a empty string
		this.suit = ' ';																									// Set the card suit to an empty suit
		this.value = 0;																										// Set the card value to zero
	}
	
	 public void setCardImage(String imageName){
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void setCardImage
		//
		// Method parameters	:	imageName
		//
		// Method return		:	void
		//
		// Synopsis				:   This method allows other classes to set the images of the cards.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Set image
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		 
		 this.image = imageName; 																							// Set the card image to the imageName
	 }
	 
	 public String getCardImage(){
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void getCardImage
		//
		// Method parameters	:	none
		//
		// Method return		:	String
		//
		// Synopsis				:   This method allows other classes to get the images of the cards.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Get image
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		 
		 return(this.image);																								// Return the card's image name
	 }
	 
	 public void setCardSuit(char cardSuit) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void setCardSuit
		//
		// Method parameters	:	cardSuit
		//
		// Method return		:	void
		//
		// Synopsis				:   This method allows other classes to set the card suits.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Set card suit
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		 
		 this.suit = cardSuit;																								// Set the card suit to the suit parameter 
	 }
	 
	 public char getCardSuit() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void getCardSuit
		//
		// Method parameters	:	none
		//
		// Method return		:	char
		//
		// Synopsis				:   This method allows other classes to get the card suits.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Get card suit
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		 
		 return(this.suit);																									// Return the suit of the card
	 }	
	 
	 public void setCardValue(byte numericValue){
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void setCardValue
		//
		// Method parameters	:	numericValue
		//
		// Method return		:	void
		//
		// Synopsis				:   This method allows other classes to set the card values.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Set card value
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		 
		 this.value = numericValue;																							// Set the numeric value of the card
	 }
	 
	 public byte getCardValue(){
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void getCardValue
		//
		// Method parameters	:	none
		//
		// Method return		:	byte
		//
		// Synopsis				:   This method allows other classes to set the card values.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Get card value
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		 
		 return(this.value);																								// Return the value of the card
	 }
}
