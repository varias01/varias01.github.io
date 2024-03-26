import java.awt.EventQueue;

import javax.swing.JDialog;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import javax.swing.JEditorPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BlackjackApp extends JDialog {
	
	private static BlackjackHand newHand;																								// Declare data member to create a new hand 
																																		// of blackjack
	private static Player dealer = new Player();																							// Declare data member to initialize the 
																																		// dealer
	private static Player user = new Player();																							// Declare data member to initialize the 
																																		// player/user
	private static char winner = ' ';																									// Declare data member to store the character 
																																		// that identifies the winner of each hand and 
																																		// the game
	
	public static void main(String[] args) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	main
		//
		// Method parameters	:	String[] args
		//
		// Method return		:	void
		//
		// Synopsis				:   This is the main method of the blackjack application, it launches the app and 
		// 							initializes the player and dealer wallets, as well as the blackjack hand.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Blackjack main
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		EventQueue.invokeLater(new Runnable() {																								// Declare and launch the blackjack app's 
																																			// graphic interface
			public void run() {
				try {
					BlackjackApp dialog = new BlackjackApp();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		dealer.setWallet(1000);																												// Set the dealer's wallet to their permitted
																																			// funds 
		user.setWallet(1000);																												// Set the player's wallet to their permitted 
																																			// funds
		newHand = new BlackjackHand();																										// Initialize the starting blackjack hand
	}

	public BlackjackApp() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	BlackjackApp
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method contains all the app's graphic elements and their functionality.
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Blackjack graphic interface
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		getContentPane().setBackground(new Color(102, 153, 102));																			// Set the background of the application and the
																																			// window size
		getContentPane().setForeground(new Color(0, 0, 0));																					
		setBounds(100, 100, 1453, 822);
		getContentPane().setLayout(null);
		setTitle("Blackjack");
		
		JLabel lblBlackjack = new JLabel("BLACKJACK");																						// Add a title label for blackjack with its 
																																			// corresponding settings
		lblBlackjack.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlackjack.setForeground(new Color(130, 36, 87));
		lblBlackjack.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 35));
		lblBlackjack.setBounds(1173, 95, 264, 35);
		getContentPane().add(lblBlackjack);
		
		JLabel lblPot = new JLabel("POT: " + newHand.getPot());																				// Declare a label to display the pot in each hand
																																			// with its corresponding settings
		lblPot.setForeground(Color.BLACK);
		lblPot.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));
		lblPot.setBounds(48, 374, 206, 35);
		getContentPane().add(lblPot);
		
		JLabel lblWinner = new JLabel("");																									// Declare a label to display the winner each hand
																																			// with its corresponding settings
		lblWinner.setHorizontalAlignment(SwingConstants.CENTER);
		lblWinner.setForeground(new Color(130, 36, 87));
		lblWinner.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 25));
		lblWinner.setBounds(358, 374, 457, 35);
		getContentPane().add(lblWinner);
		
		JLabel lblPlayerWallet = new JLabel("Player Wallet: " + user.getWallet());															// Declare a label to display the player's wallet
																																			// with its corresponding settings
		lblPlayerWallet.setForeground(new Color(0, 0, 0));
		lblPlayerWallet.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));
		lblPlayerWallet.setBounds(383, 725, 206, 35);
		getContentPane().add(lblPlayerWallet);
		
		JLabel lblPlayerHandValue = new JLabel("Hand Value: " + user.getHandValue());														// Declare a label to display the player's hand
																																			// value with its corresponding settings
		lblPlayerHandValue.setForeground(Color.BLACK);
		lblPlayerHandValue.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));
		lblPlayerHandValue.setBounds(653, 725, 206, 35);
		getContentPane().add(lblPlayerHandValue);
		
		JLabel lblDealerWallet = new JLabel("Dealer Wallet: " + dealer.getWallet());														// Declare a label to display the dealer's wallet
																																			// with its corresponding settings
		lblDealerWallet.setForeground(Color.BLACK);
		lblDealerWallet.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));
		lblDealerWallet.setBounds(383, 23, 206, 35);
		getContentPane().add(lblDealerWallet);
		
		JLabel lblDealerHandValue = new JLabel("Hand Value: " + dealer.getHandValue());														// Declare a label to display the dealer's hand
																																			// value its corresponding settings
		lblDealerHandValue.setForeground(Color.BLACK);
		lblDealerHandValue.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));
		lblDealerHandValue.setBounds(653, 23, 206, 35);
		getContentPane().add(lblDealerHandValue);
		
		JLabel lblPlayerCard1 = new JLabel("");																								// Declare a label to display the player's card 1
		lblPlayerCard1.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
		lblPlayerCard1.setBounds(34, 515, 180, 257);
		getContentPane().add(lblPlayerCard1);
		
		JLabel lblPlayerCard2 = new JLabel("");																								// Declare a label to display the player's card 2
		lblPlayerCard2.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));											
		lblPlayerCard2.setBounds(267, 445, 180, 257);
		getContentPane().add(lblPlayerCard2);
		
		JLabel lblPlayerCard3 = new JLabel("");																								// Declare a label to display the player's card 3
		lblPlayerCard3.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));											
		lblPlayerCard3.setBounds(498, 445, 180, 257);
		getContentPane().add(lblPlayerCard3);
		
		JLabel lblPlayerCard4 = new JLabel("");																								// Declare a label to display the player's card 4
		lblPlayerCard4.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));											
		lblPlayerCard4.setBounds(733, 445, 180, 257);
		getContentPane().add(lblPlayerCard4);
		
		JLabel lblPlayerCard5 = new JLabel("");																								// Declare a label to display the player's card 5
		lblPlayerCard5.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));											
		lblPlayerCard5.setBounds(968, 515, 180, 257);
		getContentPane().add(lblPlayerCard5);
		
		JLabel lblDealerCard1 = new JLabel("");																								// Declare a label to display the dealer's card 1
		lblDealerCard1.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));											
		lblDealerCard1.setBounds(34, 11, 180, 257);
		getContentPane().add(lblDealerCard1);
		
		JLabel lblDealerCard2 = new JLabel("");																								// Declare a label to display the dealer's card 2
		lblDealerCard2.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
		lblDealerCard2.setBounds(267, 83, 180, 257);
		getContentPane().add(lblDealerCard2);
		
		JLabel lblDealerCard3 = new JLabel("");																								// Declare a label to display the dealer's card 3
		lblDealerCard3.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
		lblDealerCard3.setBounds(498, 83, 180, 257);
		getContentPane().add(lblDealerCard3);
		
		JLabel lblDealerCard4 = new JLabel("");																								// Declare a label to display the dealer's card 4
		lblDealerCard4.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
		lblDealerCard4.setBounds(733, 83, 180, 257);
		getContentPane().add(lblDealerCard4);
		
		JLabel lblDealerCard5 = new JLabel("");																								// Declare a label to display the dealer's card 5
		lblDealerCard5.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
		lblDealerCard5.setBounds(968, 11, 180, 257);
		getContentPane().add(lblDealerCard5);
		
		
		JButton btnBet = new JButton("Bet");																								// Declare a button for betting 
		JButton btnDeal = new JButton("Deal");																								// Declare a button for dealing the first two cards
		JButton btnHit = new JButton("Hit");																								// Declare a button to hit
		JButton btnStand = new JButton("Stand");																							// Declare a button to stand 
		JButton btnNewHand = new JButton("New Hand");																						// Declare a button to get a new hand
		
		btnDeal.setEnabled(false);																											// Disable the deal, hit, stand and new hand buttons
		btnHit.setEnabled(false);																											// at the beginning of the game
		btnStand.setEnabled(false);
		btnNewHand.setEnabled(false);
		
		
		btnBet.addActionListener(new ActionListener() {																						// Declare the action listener for the bet button
			public void actionPerformed(ActionEvent e) {
				newHand.bet(user, dealer);																									// Call the betting method and pas the user and dealer
																																			// as parameters
				lblPot.setText("POT: " + newHand.getPot());																					// Set the pot label to the new pot value after betting
				lblPlayerWallet.setText("Player Wallet: " + user.getWallet());																// Set the player wallet label to the new wallet value
				lblDealerWallet.setText("Dealer Wallet: " + dealer.getWallet());															// Set the dealer wallet label to the new wallet value
				if (user.getWallet() == 0 || dealer.getWallet() == 0) {
					btnBet.setEnabled(false);																								// If the player or dealer have no more funds, disable 
																																			// the bet button
				}
				btnDeal.setEnabled(true);																									// Enable the dealing button
			}
		});
		btnBet.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));																		//Set characteristics of the bet button
		btnBet.setBounds(1215, 191, 170, 56);
		getContentPane().add(btnBet);
		
		
		btnDeal.addActionListener(new ActionListener() {																					// Declare the action listener for the deal button
			public void actionPerformed(ActionEvent e) {
				btnBet.setEnabled(false);																									// Disable the bet button
				newHand.deal(user);																											// Call the deal method and pass the user as a parameter
				Card firstCard = user.getCards().get(0);																					// Get the first card the user was dealt to display it
				Card secondCard = user.getCards().get(1);																					// Get the second card the user was dealt to display it
				lblPlayerCard1.setIcon(new ImageIcon(BlackjackApp.class.getResource(firstCard.getCardImage())));							// Set the Card1 label to the first card the user was dealt
				lblPlayerCard2.setIcon(new ImageIcon(BlackjackApp.class.getResource(secondCard.getCardImage())));							// Set the Card2 label to the second card the user was dealt
				lblPlayerHandValue.setText("Hand Value: " + user.getHandValue());															// Set the hand value label to the new hand value of 
																																			// the player
				btnDeal.setEnabled(false);																									// Disable the deal button
				btnHit.setEnabled(true);																									// Enable the hit button
				btnStand.setEnabled(true);																									// Enable the stand button
			}
		});
		btnDeal.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));																		//Set characteristics of the deal button
		btnDeal.setBounds(1215, 279, 170, 56);
		getContentPane().add(btnDeal);
		
		
		btnHit.addActionListener(new ActionListener() {																						// Declare the action listener for the hit button
			public void actionPerformed(ActionEvent e) {				
				winner = newHand.hit(user);																									// Call the hit method and save the character it 
																																			// returns into the winner data member
				int userCards = user.getNumOfCards();																						// Declare an integer userCards to store the users
																																			// number of cards
				Card hitCard = user.getCards().get(userCards - 1);																			// Declare a card called hitCard that stores the last
																																			// card added into the player's array of cards
				if ((userCards - 1) == 2 ) {
					lblPlayerCard3.setIcon(new ImageIcon(BlackjackApp.class.getResource(hitCard.getCardImage())));							// If the position of the last card in the user's array
																																			// is 2, then set the label Card3 to that card
				}
				if ((userCards - 1) == 3) {
					lblPlayerCard4.setIcon(new ImageIcon(BlackjackApp.class.getResource(hitCard.getCardImage())));							// If the position of the last card in the user's array
																																			// is 3, then set the label Card4 to that card
				}
				if ((userCards - 1) == 4) {
					lblPlayerCard5.setIcon(new ImageIcon(BlackjackApp.class.getResource(hitCard.getCardImage())));							// If the position of the last card in the user's array
																																			// is 4, then set the label Card5 to that card
					btnHit.setEnabled(false);																								// Disable the hit button when all cards have been dealt
				}
				lblPlayerHandValue.setText("Hand Value: " + user.getHandValue());															// Set the hand value label to the new hand value
				 
				if (winner == 'U' || winner == 'D') {
					someoneWon(winner, user, dealer, lblWinner, lblPot, lblPlayerWallet, lblDealerWallet, btnNewHand);						// If someone won, call the someoneWon method and pass the 
																																			// winner, the players and the required labels
					btnHit.setEnabled(false);																								// Disable the hit button
					btnStand.setEnabled(false);																								// Disable the stand button
				}
			}
		});
		btnHit.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));																		//Set characteristics of the hit button
		btnHit.setBounds(1215, 368, 170, 56);
		getContentPane().add(btnHit);
		
		
		btnStand.addActionListener(new ActionListener() {																					// Declare the action listener for the stand button
			public void actionPerformed(ActionEvent e) {
				btnHit.setEnabled(false);																									// Disable the hit button
				winner = newHand.stand(user, dealer);																						// Call the stand method and save the character it 
																																			// returns into the winner data member
				int numOfCards = dealer.getNumOfCards();																					// Declare an integer numOfCards to store the dealer's
																																			// number of cards 
				Card dealtCard;																												// Declare a card called dealtCard to sort trough the 
																																			// cards that were dealt to the dealer
				for(int counter = 0; counter < numOfCards; counter++) {
					
					dealtCard = dealer.getCards().get(counter);																				// Get the card in the position of the counter and save
																																			// it in dealtCard
					if (counter == 0 ) {
						lblDealerCard1.setIcon(new ImageIcon(BlackjackApp.class.getResource(dealtCard.getCardImage())));					// Set the Card1 label to the first card in the dealer's
																																			// array
					}
					if (counter == 1) {
						lblDealerCard2.setIcon(new ImageIcon(BlackjackApp.class.getResource(dealtCard.getCardImage())));					// Set the Card2 label to the second card in the dealer's
																																			// array
					}
					if (counter == 2 ) {
						lblDealerCard3.setIcon(new ImageIcon(BlackjackApp.class.getResource(dealtCard.getCardImage())));					// Set the Card3 label to the third card in the dealer's
																																			// array
					}
					if (counter == 3) {
						lblDealerCard4.setIcon(new ImageIcon(BlackjackApp.class.getResource(dealtCard.getCardImage())));					// Set the Card4 label to the fourth card in the dealer's
																																			// array
					}
					if (counter == 4) {
						lblDealerCard5.setIcon(new ImageIcon(BlackjackApp.class.getResource(dealtCard.getCardImage())));					// Set the Card5 label to the fifth card in the dealer's
																																			// array
					}
				}
				lblDealerHandValue.setText("Hand Value: " + dealer.getHandValue());															// set the hand value label to the new hand value
				
				if (winner == 'U' || winner == 'D') {
					someoneWon(winner, user, dealer, lblWinner, lblPot, lblPlayerWallet, lblDealerWallet, btnNewHand);						// If someone won, call the someoneWon method and pass the 
																																			// winner, the players and the required labels
					btnHit.setEnabled(false);																								// Disable the hit button
					btnStand.setEnabled(false);																								// Disable the stand button
				}
				else if (winner == 'N') {
					winner = newHand.evaluateHand(user, dealer);																			// If not winner is determined before, call the evaluateHand
																																			// method and find a winner 
					someoneWon(winner, user, dealer, lblWinner, lblPot, lblPlayerWallet, lblDealerWallet, btnNewHand);						// Call the someoneWon method and pass the winner, the
																																			// players and the required labels
					btnHit.setEnabled(false);																								// Disable the hit button
					btnStand.setEnabled(false);																								// Disable the stand button
					
				}
			}
		});
		btnStand.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));																	//Set characteristics of the stand button
		btnStand.setBounds(1215, 456, 170, 56);
		getContentPane().add(btnStand);	
		
		
		btnNewHand.addActionListener(new ActionListener() {																					// Declare the action listener for the new hand button
			public void actionPerformed(ActionEvent e) {
				newHand.newDeck();																											// Call the newDeck method to create a new deck
				newHand.setPot(0);																											// Call the setPot method and set it to zero

				lblDealerCard1.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));									// Set all cards labels to the back of the card icon
				lblDealerCard2.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
				lblDealerCard3.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
				lblDealerCard4.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
				lblDealerCard5.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
				lblPlayerCard1.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
				lblPlayerCard2.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
				lblPlayerCard3.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
				lblPlayerCard4.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
				lblPlayerCard5.setIcon(new ImageIcon(BlackjackApp.class.getResource("/cards/Back.png")));
				
				btnBet.setEnabled(true);																									// Enable the bet button
				btnDeal.setEnabled(false);																									// Disable the deal button
				btnHit.setEnabled(false);																									// Disable the hit button
				btnStand.setEnabled(false);																									// Disable the stand button
				btnNewHand.setEnabled(false);																								// Disable the new hand button
				
				user.emptyCards(); 																											// Empty the payer's card array
				dealer.emptyCards();																										// Empty the dealer's card array
				lblPlayerHandValue.setText("Hand Value: " + user.getHandValue());															// Set the player's hand value label to the new hand
																																			// value
				lblDealerHandValue.setText("Hand Value: " + dealer.getHandValue());															// Set the dealer's hand value label to the new hand
																																			// hand value
				lblWinner.setText("");																										// Set the winner message label back to empty
			}
		});
		btnNewHand.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));																	//Set characteristics of the new hand button
		btnNewHand.setBounds(1215, 545, 170, 56);
		getContentPane().add(btnNewHand);
	}
	
	public static void someoneWon(char winner, Player user, Player dealer, JLabel lblWinner, JLabel lblPot, JLabel lblPlayerWallet,
			JLabel lblDealerWallet, JButton btnNewHand) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	someoneWon
		//
		// Method parameters	:	winner, user, dealer, lblWinner, lblPot, lblPlayerWallet, lblDealerWallet, btnNewHand
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is called if someone has won the hand or the game, and displays the winner message. 
		//
		// References			:   none
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-03-31		V. Arias				Winner sequence
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		Integer playerFunds = user.getWallet();																								// Declare an integer playerFunds that stores the player's
																																			// wallet
		Integer dealerFunds = dealer.getWallet();																							// Declare an integer dealerFunds that stores the dealer's
																																			// wallet
		int bets = newHand.getPot()/2;																										// Declare an integer bets that stores how much each player 
																																			// has bet in the hand

		switch (winner) {																													
			case 'U':	if (dealerFunds > 0) {																								
							lblWinner.setText("Player Won!");																				// If the winner is the player and the dealer's funds are not 
																																			// zero, display the player won
							btnNewHand.setEnabled(true);																					// Enable the new hand button
						}
						else {
							lblWinner.setText("GAME OVER! Player Wins.");																	// If the winner is the player and the dealer's funds are 
																																			// zero, display the game over message
							btnNewHand.setEnabled(false);																					// Disable the new hand button
						}
						user.setWallet(user.getWallet() + newHand.getPot());																// Set the player's wallet to the previous wallet value
																																			// plus the pot
						lblPlayerWallet.setText("Player Wallet: " + user.getWallet());														// Set the player's wallet label to the new wallet value
						lblPot.setText("POT: 0");																							// Set the pot label back to zero
						break;
						
			case 'D':	if (playerFunds > 0) {
							lblWinner.setText("Dealer Won!");																				// If the winner is the dealer and the player's funds are not 
																																			// zero, display the dealer won
							btnNewHand.setEnabled(true);																					// Enable the new hand button
						}
						else {
							lblWinner.setText("GAME OVER! Dealer Wins.");																	// If the winner is the dealer and the player's funds are 
																																			// zero, display the game over message
							btnNewHand.setEnabled(false);																					// Disable the new hand button
						}
						dealer.setWallet(dealer.getWallet() + newHand.getPot());															// Set the dealer's wallet to the previous wallet value
																																			// plus the pot
						lblDealerWallet.setText("Dealer Wallet: " + dealer.getWallet());													// Set the dealer's wallet label to the new wallet value
						lblPot.setText("POT: 0");																							// Set the pot label back to zero
						break;
						
			case 'T':	lblWinner.setText("It's a Tie!");																					// If its a tie, display It's a tie message
						btnNewHand.setEnabled(true);																						// Enable the new hand button
						user.setWallet(user.getWallet() + bets);																			// Set the player's wallet to the previous wallet value
																																			// plus the amount they bet
						dealer.setWallet(dealer.getWallet() + bets);																		// Set the dealer's wallet to the previous wallet value
																																			// plus the amount they bet
						lblPlayerWallet.setText("Player Wallet: " + user.getWallet());														// Set the player's wallet label to the new wallet value
						lblDealerWallet.setText("Dealer Wallet: " + dealer.getWallet());													// Set the dealer's wallet label to the new wallet value
						lblPot.setText("POT: 0");																							// Set the pot label back to zero
						break;
		}

	}
}
