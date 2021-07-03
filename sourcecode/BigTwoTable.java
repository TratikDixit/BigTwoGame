import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import java.util.*; 

/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build a GUI for     
 * the Big Two card game and handle all user actions. 
 * 
 * @author Tratik Dixit
 */
public class BigTwoTable implements CardGameTable{
	
	private BigTwoClient game;  // A card game associates with this table
	private boolean[] selected;   // A boolean array indicating which cards are being selected
	private int activePlayer;  // An integer specifying the index of the active player
	private JFrame frame;   //  The main window of the application
	private JPanel bigTwoPanel; // A panel for showing the cards of each player and the cards played on the table
	private JButton playButton; // A Play button for the active player to play the selected cards
	private JButton passButton; // A Pass button for the active player to pass his/her turn to the next player
	private JTextArea msgArea; // A text area for showing the current game status as well as end of game messages
	private Image[][] cardImages; // A 2D array storing the images for the faces of the cards
	private Image cardBackImage;  // An image for the backs of the cards
	private Image[] avatars; // An array storing the images for the avatars
	private JTextArea chatBox;   // A chat box for the showing the messages sent by players
	
    // This is a private method used to set up the GUI.
	private void go() {		
		
		//creating a new panel
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setPreferredSize(new Dimension(900,800));
		
		//setting up the Jframe object
		frame = new JFrame();
		frame.setTitle("Big Two Game Assignment 5");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
		// Creating the two drop down menus and adding them to the frame 		
		JMenuBar menuBarDropDown = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenu messageMenu = new JMenu("Message");
		frame.setJMenuBar(menuBarDropDown); 		
		menuBarDropDown.add(gameMenu);
		menuBarDropDown.add(messageMenu);
		
		//adding the respective options to the menus
		JMenuItem connectGame= new JMenuItem("Connect Game");
		JMenuItem quitGame = new JMenuItem("Quit Game");
		JMenuItem clearBoard = new JMenuItem("Clear Information Board");
		JMenuItem clearChat = new JMenuItem("Clear Chat Screen");
		connectGame.addActionListener(new ConnectMenuItemListener());
		gameMenu.add(connectGame);
		quitGame.addActionListener(new QuitMenuItemListener());
		gameMenu.add(quitGame);		
		clearBoard.addActionListener(new InfoBoardListener());
		messageMenu.add(clearBoard);
		clearChat.addActionListener(new ClearChatListener());
		messageMenu.add(clearChat);
		
	    // creating JPanel for chatbox
	    JPanel messagePanel= new JPanel();
	    messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.PAGE_AXIS));
	    msgArea = new JTextArea(35,40);
	    msgArea.setEnabled(false);
	    DefaultCaret caret = (DefaultCaret) msgArea.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	    JScrollPane scrollPaneMsg= new JScrollPane(msgArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

	    //creating chat Text Area
	    chatBox = new JTextArea(40,40);
	    chatBox.setEnabled(false);
	    DefaultCaret caretChat = (DefaultCaret) chatBox.getCaret();
	    caretChat.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	    JScrollPane scrollPaneChat = new JScrollPane(chatBox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    	    
	    JPanel chat = new JPanel();
	    chat.setLayout(new FlowLayout());
	    chat.add(new JLabel("Message :"));
	    
	    JTextField chatInput= new MyTextField(30);
	    chatInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
	    chatInput.setPreferredSize(new Dimension(180, 40));
	    chat.add(chatInput);
	    
	    messagePanel.add(scrollPaneMsg);
	    messagePanel.add(scrollPaneChat);
	    messagePanel.add(chat);
	   
	    JPanel buttons = new JPanel();
	    playButton = new JButton("Play");
	    passButton = new JButton("Pass");
	    playButton.addActionListener(new PlayButtonListener());
	    passButton.addActionListener(new PassButtonListener());
	    buttons.add(playButton);
		buttons.add(Box.createHorizontalStrut(35));
	    buttons.add(passButton);
	    
	    boolean flag = true;
	    if(game.getCurrentIdx()!=activePlayer) {
	    	flag = false;
	    }
	    buttons.setEnabled(flag);
		playButton.setEnabled(flag);
		passButton.setEnabled(flag);
	    
		frame.add(messagePanel, BorderLayout.EAST);
	    frame.add(bigTwoPanel,BorderLayout.WEST);
	    frame.add(buttons, BorderLayout.SOUTH);		
	    
	    frame.setSize(1600,730);
	    frame.setVisible(true);
		frame.setResizable(false);
				
	}
	
	/**
	 * A constructor for creating a BigTwoTable. The parameter game is a reference to a card game associates with this table.
	 * 
	 * @param game This is a CardGame object which holds the reference of the current game.
	 */
	public BigTwoTable(BigTwoClient game) {
		//loading images
		avatars = new Image[4];
		for (int i = 0 ; i < 4 ; i++)
		{
			String path = "../resources/playerAvatars/"+String.valueOf(i)+".png";
			avatars[i] = new ImageIcon(path).getImage();
		}
		cardBackImage = new ImageIcon("../resources/cards/back.gif").getImage();
		char[] suit = {'d','c','h','s'};
		char[] rank = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
		cardImages = new Image[4][13];
		for(int rankIdx=0;rankIdx<13;rankIdx++) {
			for(int suitIdx=0;suitIdx<4;suitIdx++) {
				String path ="../resources/cards/" + rank[rankIdx] + suit[suitIdx] + ".gif";
				cardImages[suitIdx][rankIdx] = new ImageIcon(path).getImage();
			}
		}
		this.game = game;
		selected = new boolean[13];
		setActivePlayer(game.getPlayerID()); 
		// calling the GUI Setup function
		go();	
	}
	
	/**
	 * This is a setter method used for setting the index of the active player (i.e., the current player). 
	 * 
	 * @param activePlayer An integer value that store the current index of the active player
	 * 
	 */
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}
	
	/**
	 * This is a getter method used for getting an array of indices of the cards selected.
	 * 
	 * @return Integer Array 
	 * 				It returns an array of indices of the selected cards
	 */
	public int[] getSelected() {
		int ctr = 0;
		ArrayList<Integer> ans = new ArrayList<Integer>();
		for(int i=0;i<selected.length;i++)
		{
			if(selected[i]==true) {
				ans.add(i);
				ctr++;
			}
		}
		if(ctr==0) {
			return null;
		}
		int[] result =  ans.stream().mapToInt(i -> i).toArray();;
		return result;	
	}
	
	/**
	 * This method is used for resetting the list of selected cards.
	 * 
	 */
	public void resetSelected() {
		for(int i=0;i<selected.length;i++) {
            selected[i] = false;
		}
	}
	
	/**
	 * This method is used for repainting the GUI. 
	 * 
	 */
	public void repaint() 
	{
		/*resetSelected();
		frame.repaint();*/
		// Setting message area


	
	resetSelected();
	Color [] color_codes = new Color[4];
	color_codes[0] = new Color(255, 204, 204);
	color_codes[1] = new Color(229, 255, 204);
	color_codes[2] = new Color(204, 239, 255);
	color_codes[3] = new Color(249, 255, 204);
	Color x = new Color(255, 182, 193);
	bigTwoPanel.setBackground( (0 <= activePlayer && activePlayer < 4 ) ? color_codes[activePlayer] :x);

	// Redraw the bigTwoPanel

	frame.repaint();

	}
	
	/**
	 * This method is used for printing the specified string to the message area of the GUI.
	 * 
	 * @param msg This is the string message that we want to print 
	 */
	public void printMsg(String msg) {
		msgArea.append(msg+"\n");
	}
	
	/**
	 * This method is used for clearing the message area of the GUI. 
	 * 
	 */
	public void clearMsgArea() {
		msgArea.setText("");
	}
	/**
	 * A method that prints the specified string to the chat message area of the card game table.
	 * 
	 * @param msg the string to be printed to the chat message area of the card game table.
	 */
	public void printChatMsg(String msg) {
		chatBox.append(msg+"\n");
	}
	/**
	 * 
	 */
	public void clearChatMsgArea() {
		chatBox.setText("");
	}
	/**
	 * This method is used for resetting the GUI.
	 * 
	 */
	public void reset() {
		this.resetSelected();
		this.clearMsgArea();
		this.enable();
	}
	
	/**
	 * This method is used for enabling user interactions with the GUI.
	 * 
	 */
	public void enable() {
		bigTwoPanel.setEnabled(true);
		playButton.setEnabled(true);
		passButton.setEnabled(true);
	}
	
	/**
	 * This method is used for disabling user interactions with the GUI.
	 */
	public void disable() {
		bigTwoPanel.setEnabled(false);
		playButton.setEnabled(false);
		passButton.setEnabled(false);
	}
	
	/**
	 * This is an inner class that extends the JPanel class and implements the MouseListener interface. 
	 * 
	 * 
	 * @author Tratik Dixit
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
	
		private static final long serialVersionUID = 1L;
		// draw the card decks
		private int cardXc, cardWidth, cardMove;
		// to facilitate the drawing of player avatar and player sections
		private int avatarXc, avatarYc;
		// to draw section breaks
		private int sectionXc, sectionYc;
		// to print player [0-3]
		private int playerNameXc, playerNameYc; 
		//to facilitate up-down movement
		private int downCardYCoord,upCardYCoord;
		/**
		 * A constructor for BigTwoPanel class.
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
			// draw the card decks
			cardXc = 120;
			cardWidth = 30;
			cardMove = 160;
			// to facilitate the drawing of player avatar and player sections
			avatarXc = 15;
			avatarYc = 35;
			// to draw section breaks
			sectionXc = 160;
			sectionYc = 1550;
			// to print player [0-3]
			playerNameXc = 30;
			playerNameYc = 30;
			//to facilitate up-down movement
			downCardYCoord = 40;
			upCardYCoord = 30;
		}
		
		/**
		 * This is an over-ridden method. It is used to paint the entire GUI (i.e player images, cards and game console)
		 * 
		 * @param g is a Graphics object 
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
	
			g.setColor(Color.black);

            // loop to draw the green portion(player avatars and deck)
			ArrayList<String> playerNameList = new ArrayList<String>(4);
			
			for (int ctr = 0 ; ctr<4; ctr+=1)
			{
			    playerNameList.add(ctr, game.getPlayerList().get(ctr).getName());
			    String update="";	
				if(game.getPlayerID()==ctr)
				{
					update = "[You]";
					g.setColor(Color.magenta);
				}
		       if (ctr==game.getCurrentIdx()) {
						g.setColor(Color.BLUE);
				}
				g.drawString(game.getPlayerList().get(ctr).getName()+update, playerNameXc , playerNameYc + 160*ctr);					
				g.setColor(Color.black);
				g.drawImage(avatars[ctr], avatarXc, avatarYc + 160*ctr, this);
			    g2.drawLine(0, sectionXc*(ctr+1), sectionYc, sectionXc*(ctr+1));
				if(game.getPlayerID() == ctr) 
				{
						// setting the color back for non-active players
						for (int i = 0; i < game.getPlayerList().get(ctr).getNumOfCards(); i++) 
				    	{
				    		int suit = game.getPlayerList().get(ctr).getCardsInHand().getCard(i).getSuit();
				    		int rank = game.getPlayerList().get(ctr).getCardsInHand().getCard(i).getRank();
				    		int y = downCardYCoord+cardMove*ctr;
				    		// drawn normally
					   		if (selected[i])
					   			y = upCardYCoord+cardMove*ctr;
					   		// drawn upwards   			
				    		g.drawImage(cardImages[suit][rank], cardXc+cardWidth*i, y, this);		
					   	}
				}
				else {
						for (int i = 0; i < game.getPlayerList().get(ctr).getCardsInHand().size(); i++)
				    	{
							// all drawn normally (not raised) as this is a non-active player
				    		g.drawImage(cardBackImage, cardXc+cardWidth*i, downCardYCoord+cardMove*ctr, this);
				    	}
				}
			}
			//g.drawString(update, 10,10);
				// to ensure that atleast one hand has been played
				if (!game.getHandsOnTable().isEmpty() && !game.endOfGame() ){
				   	Hand currentHand = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
				   	String playerString = game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName();
				   	int playerInt= playerNameList.indexOf(playerString);
				   	g.drawString("Last Hand on Table", 640, (160*playerInt)+20);
				   	g.drawString("Hand Type:\n " + game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getType(), 640, (160*playerInt)+32);
				   	g.drawString("Played by: " + playerString, 640, 45+(160*playerInt));
				   	// printing the cards
				   	for (int i = 0; i < currentHand.size(); i++){
			    		int suit = currentHand.getCard(i).getSuit();
			    		int rank = currentHand.getCard(i).getRank();
		    			g.drawImage(cardImages[suit][rank], 640 + 30*i, (160*playerInt)+50, this);
		    		}
			    }
			    repaint();
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}	
		/**
		 * This implements the mouseClicked() method from the MouseListener interface to handle mouse click events. 
		 * 
		 * @param e It is a MouseEvent object it stores the co-ordinates of the mouse click
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			boolean ctr = false; 
			int nextCard = game.getPlayerList().get(activePlayer).getNumOfCards()-1;
			boolean b[] = new boolean[2]; b[0]=true; b[1]= false;
			int y[] = new int[2]; y[0]= downCardYCoord; y[1]=upCardYCoord;
			
			if ( e.getX() <= (cardXc+nextCard*30+70) && e.getX() >= (cardXc+nextCard*30)) {
				for (int i = 0 ; i<2; ++i ){
					if(selected[nextCard]!=b[i] && e.getY() <= (y[i] + cardMove*activePlayer+97) && e.getY() >= (y[i] + cardMove*activePlayer)){
						ctr= true;
						selected[nextCard] =b[i];
						break;
					}
				}
			}
			for (nextCard = game.getPlayerList().get(activePlayer).getNumOfCards()-2; !ctr && nextCard >= 0 ; nextCard--) 
			{
				if ( e.getX() <= (cardXc+(nextCard+1)*cardWidth)  &&  e.getX() >= (cardXc+nextCard*cardWidth) ) {
					for (int i = 0 ; i<2; ++i ){
						if(selected[nextCard]!=b[i] && e.getY() >= (y[i] + cardMove*activePlayer) && e.getY() <= (y[i] + cardMove*activePlayer+97)){
							ctr= true;
							selected[nextCard] =b[i];
							break;
						}
					}		
				}
				else
				{
					for (int i = 0 ; i<2; ++i ){
					  if (e.getX() >= (cardXc+(nextCard+1)*cardWidth) && e.getX() <= (cardXc+nextCard*cardWidth+70) && e.getY() >= (y[i]+cardMove*activePlayer) && e.getY() <= (y[i]+cardMove*activePlayer+97)) 
					  {
						  if (selected[nextCard+1]==b[i] && selected[nextCard]!=b[i]) {
								ctr = true;
								selected[nextCard] = b[i];
								break;
							}// end of inner if
					  }// end of outer if
					} // end of for loop
				}//end of if else construct	
			}// end of outer for
			this.repaint();
		}
	}
	/**
	 * This is an inner class that implements the ActionListener interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle button-click events for the "Play" button. 
	 * 
	 * @author Tratik Dixit
	 */
	class PlayButtonListener implements ActionListener{	
		/**
		 * This implements the actionPerformed() method from the MouseListener interface to handle mouse click events. 
		 * 
		 * @param e It is a 'ActionEvent' object
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(game.getPlayerID()==game.getCurrentIdx())
	       {
			if (getSelected() == null)
				printMsg("No cards are selected");
			else 
				game.makeMove(activePlayer, getSelected());
	       }
			repaint();
		}
	}
	/**
	 * This is an inner class implements the ActionListener interface. 
	 *
	 * @author Tratik Dixit
	 **/
	class PassButtonListener implements ActionListener{
		/**
		 * This is an overridden function that is used to handle button-click events for the "Pass" button. 
		 * 
		 * @param e It is a 'ActionEvent' object that deals with clicks on pass button.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(game.getPlayerID()==game.getCurrentIdx()) {
				game.makeMove(activePlayer, null);
			}
			repaint();
		}	
	}
	/**
	 * This inner class implements the actionListener interface for the Restart Menu Item in the JMenuBar to restart the game on click.
	 * 
	 * @author Tratik Dixit
	 */
	class ConnectMenuItemListener implements ActionListener{
		/**
		 * The function overrides the ActionPerformed function in ActionListener interface to detect 
		 * the user interaction on the object and carry out necessary functions.
		 * 
		 * @param e This is a ActionEvent object to detect if some user interaction was performed on the given object.
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(game.isConnected()){
				printMsg("Already connected");
			}
			else
			{
				reset();
				game.makeConnection();
			}
		}
	}
	/**
	 * It is an inner class that implements the actionListener interface.
	 * 
	 * @author Tratik Dixit
	 */
	class QuitMenuItemListener implements ActionListener{
		/**
		 * This is an overridden function that is used to handle button-click events for the "Restart" button. 
		 *  
		 *  @param e It is a 'ActionEvent' object that deals with clicks on reset button.
		 *  
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
            // exits(quits) program
			System.exit(0);	
		}
	}
	/**
	 * This inner class implements the actionListener interface for the Clear Information Board item in the JMenuBar to quit the game on click. 
	 * 
	 * @author Tratik Dixit
	 */
	class InfoBoardListener implements ActionListener{
		/**
		 * The function overrides the ActionPerformed function in ActionListener interface to detect 
		 * the user interaction on the object and carry out necessary functions.
		 *  
		 * @param e This is a ActionEvent object to detect if some user interaction was performed on the given object.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			clearMsgArea();	
		}
	}
	/**
	 * This inner class implements the actionListener interface for the Clear Chat Board item in the JMenuBar to quit the game on click. 
	 * 
	 * @author Tratik Dixit
	 */
	class ClearChatListener implements ActionListener{
		
		/**
		 * The function overrides the ActionPerformed function in ActionListener interface to detect 
		 * the user interaction on the object and carry out necessary functions.
		 *  
		 * @param e This is a ActionEvent object to detect if some user interaction was performed on the given object.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			clearChatMsgArea();
		}
	}

	/**
	 * This inner class extends the JTextField and implements the actionListener interface for the message field in the game.
	 * 
	 * @author Tratik Dixit
	 *
	 */
	class MyTextField extends JTextField implements ActionListener{
		private static final long serialVersionUID = 1L;

		/*
		 * The constructor for this class.
		 */
		public MyTextField(int i) {
			super(i);
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String chatMsg = getText();
			CardGameMessage message = new CardGameMessage(CardGameMessage.MSG,-1,chatMsg);
			game.sendMessage(message);
			this.setText("");			
		}
	}
}
	