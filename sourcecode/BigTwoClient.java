import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 * The BigTwo class is used to model a Big Two card game. It has private instance variables for 
 * storing a deck of cards, a list of players, a list of hands played on the table, an index of the
 * current player, and a console for providing the user interface.
 * 
 * @author Tratik Dixit
 * 
 */
public class BigTwoClient implements CardGame, NetworkGame{

	private int numOfPlayers; //an integer specifying the number of players
	private Deck deck; // a deck of cards.
	private ArrayList<CardGamePlayer> playerList;  // a list of players
	private ArrayList<Hand> handsOnTable; //  a list of hands played on the table
	private int playerID; //an integer specifying the playerID (i.e., index) of the local player
	private String playerName; //a string specifying the name of the local player
	private String serverIP; //a string specifying the IP address of the game server
	private int serverPort;  //an integer specifying the TCP port of the game server
	private Socket sock; //a socket connection to the game server
	private ObjectOutputStream oos; //an ObjectOutputStream for sending messages to the server
	private int currentIdx; // an integer specifying the index of the current player
	private BigTwoTable bigTwoTable; // a Big Two table which builds the GUI for the game and handles all user actions
	
	/** 
	 *  A constructor to initialise the BigTwoClient object.
	 */
	public BigTwoClient()
	{
		currentIdx  = -1;
		playerList  = new ArrayList<CardGamePlayer> ();
		handsOnTable= new ArrayList<Hand>();
		for(int i = 0; i < 4;i++)
			playerList.add(new CardGamePlayer());
		numOfPlayers= playerList.size();
		bigTwoTable = new BigTwoTable(this);
		
		String name = (String) JOptionPane.showInputDialog("Enter the player name: ");

		while(name.equals("")||name==null) 
			name = (String) JOptionPane.showInputDialog("Please enter a valid name: ");
		
		setPlayerName(name);
		
		setServerIP("127.0.0.1");
		setServerPort(2396);
		makeConnection();
		bigTwoTable.disable();
		bigTwoTable.repaint();
	}
	/**
	 * This is a getter method and is used for getting the number of players
	 *  
	 * @return An integer value
	 * 
	 */	
	public int getNumOfPlayers() {
		return numOfPlayers;
	}
	/**
	 * This is a getter method and is used for retrieving the deck of cards being used.
	 *  
	 * @return A deck object ('deck') which stores the card of decks of the current player
	 */
	public Deck getDeck()
	{
		return deck;
	}
	
	/**
	 * This is a getter method. It is used for for retrieving the list of players.
	 * 
	 * @return An ArrayList ('playerList') that stores the list of players.
	 */
	public ArrayList<CardGamePlayer> getPlayerList()
	{
		return playerList;		
	}
	
	/**
	 * This is a getter method. It is used for retrieving the list of hands played on the table.
	 * 	  
	 * @return An ArrayList ('handsOnTable') that stores the hand dealt by previous player.
	 */
	public ArrayList<Hand> getHandsOnTable()
	{
		return handsOnTable;
	}
	
	/**
	 * This is a getter method. It is used for retrieving the index of the current player.
	 * 
	 * @return An integer('currentIdx') which stores the index of the player 
	 */
	public int getCurrentIdx()
	{
		return currentIdx;	
	}
    /**
     * This is a getter method. It is used for retrieving the ID of the local player.
     * 
     * @return An integer('currentIdx') 
     */
	public int getPlayerID() {
		return playerID;
	}
	
    /**
     * This is a setter method. It is used to assign a value to 'playerID'.
     * 
     * @param playerID
     *             ID of the local player 
     */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	 /**
     * This is a getter method. It is used for retrieving the ID of the local player.
     * 
     * @return A string that stores the player name
     */
	public String getPlayerName() {
		return playerName;
	}
	
	/**
     * This is a setter method. It is used to assign the playerName.
     * 
     * @param playerName
     *             The name of the playerName
     */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	 /**
     * This is a getter method. It is used for retrieving the serverIP.
     * 
     * @return A String that stores the serverIP
     */
	public String getServerIP() {
		return serverIP;
	}
	
	/**
     * This is a setter method. It is used to assign the serverIP.
     * 
     * @param serverIP
     *             The serverIP
     */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	
	 /**
     * This is a getter method. It is used for retrieving the serverPort.
     * 
     * @return An integer that stores the serverPort
     */
	public int getServerPort() {
		return serverPort;
	}
	
	/**
     * This is a setter method. It is used to assign a value to 'playerID'.
     * 
     * @param serverPort
     *             The ID of the player.
     */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	/**
	 * A method for starting/restarting the game with a given shuffled deck of cards
	 * 
	 * @param Deck (It is a BigTwoDeck object that stores the deck that plays the game)
	 */
	public void start(Deck Deck)
	{
		// Clearing the table of cards
		handsOnTable.clear();
		for(int i=0;i<4;i++) {
			playerList.get(i).getCardsInHand().removeAllCards();
		}//removing all cards of the players
		
		// distributing the to all four players
		for(int i = 0; i <52; i++)   
		{
			playerList.get(i%4).addCard(Deck.getCard(i));
		}
		
		// Since we know that first card always have to be 3 of diamonds (0 is the suit(diamond)  2 represents rank (3))
		for (int i = 0 ; i<4; i++)
		{
				  if(playerList.get(i).getCardsInHand().contains(new Card(0,2))){
						bigTwoTable.setActivePlayer(i);
						currentIdx = i;
				  }	
				  playerList.get(i).getCardsInHand().sort();
					// sorting the cards for each player	
		}  
		bigTwoTable.setActivePlayer(getPlayerID());
		bigTwoTable.printMsg("All players have joined. Game starts now.");
		bigTwoTable.printMsg(getPlayerList().get(currentIdx).getName() + "'s Turn");
		bigTwoTable.repaint();
	}
	
	/**
	 * A method for making a move by a player with the specified playerID using the cards specified by the list of indices. 
	 * 
	 * @param playerID 
	 * 				Returns an integer value ('playerID') which is the player who makes the move.
	 * @param cardIdx 
	 * 				Returns an array value ('cardIdx') which stores the indices of the cards selected by the player.
	 */
	public void makeMove(int playerID, int [] cardIdx)
	{
		CardGameMessage move = new CardGameMessage(CardGameMessage.MOVE, playerID, cardIdx);
		sendMessage(move);
	}
	/**
	 * A method for checking a move made by a player.
	 * 
	 * @param playerID 
	 * 				Returns an integer value ('playerID') which is the player who makes the move.
	 * @param cardIdx 
	 * 				Returns an array value ('cardIdx') which stores the indices of the cards selected by the player.
	 */
	public void checkMove(int playerID, int [] cardIdx)
	{
		boolean cardIsValid = true;
		if(cardIdx==null)
		{
				// If the player inputs nothing and chooses to pass
				if(handsOnTable.isEmpty()==false && handsOnTable.get(handsOnTable.size()-1).getPlayer() != playerList.get(playerID)) 
				{
					currentIdx = (currentIdx+1)%4;
					bigTwoTable.setActivePlayer(currentIdx);
					bigTwoTable.printMsg("{Pass}\nPlayer " + currentIdx + "'s Turn");  
					cardIsValid = true;
				}
				else
				{
					// this deals with illegal moves where a player might try to play incompatible types for instance a Straight for a Triple
					bigTwoTable.printMsg("Not a legal move!!!");
					cardIsValid= false;
				} 
		}
		else
			{
			CardList listOfCards = playerList.get(playerID).play(cardIdx);
			Hand hand = composeHand(playerList.get(playerID), listOfCards);
			if(hand==null) {
				bigTwoTable.printMsg(listOfCards +" <= Not a legal move!!!");
			}
			else{	
				if(handsOnTable.isEmpty())
				{
					cardIsValid = (hand.contains(new Card(0,2)) && !hand.isEmpty() && hand.isValid());
					// checks if the first player has made the right move
				}
				else
				{
					if(handsOnTable.get(handsOnTable.size() - 1).getPlayer() != playerList.get(playerID)) 
					{
						if(!hand.isEmpty()) {
						cardIsValid = hand.beats(handsOnTable.get(handsOnTable.size() - 1));}
						// checks if the hand beats it
						else
						cardIsValid = false;
					}
					else
						cardIsValid = !hand.isEmpty();
					    // if the previous highest move was made by the current player all valid moves are acceptable
				}
				if(cardIsValid && hand.isValid())
				{
					for(int i=0;i<listOfCards.size();i++)
					{
						playerList.get(playerID).getCardsInHand().removeCard(listOfCards.getCard(i)); 
					}
					bigTwoTable.printMsg("{" + hand.getType() + "} " + hand);
					handsOnTable.add(hand);
					currentIdx = (currentIdx + 1) % 4;
					bigTwoTable.setActivePlayer(currentIdx);
					bigTwoTable.printMsg("Player " + (currentIdx) + "'s Turn");
				}
				else
				{
					bigTwoTable.printMsg(listOfCards +" <= Not a legal move!!!");
				}	
				}
			}
			
		  	bigTwoTable.repaint();
		
		// if the game is over
		   if(endOfGame()) {
			   // reset controls
			bigTwoTable.setActivePlayer(-1);
			bigTwoTable.repaint();
			String finalAlert = "Game Over!!!\n";
			
			//loop to print the leader board
			for(int i = 0; i < playerList.size();i++)
			{
					if(playerList.get(i).getCardsInHand().size() == 0)
					{
						finalAlert = finalAlert+"Player " + i + " wins the game\n"; 
					}	
				    else
				     {
					   finalAlert= finalAlert+"Player " + i + " has " + playerList.get(i).getCardsInHand().size() + " cards in hand\n"; 
					   // list the number of cards left in the other players' hand
				     }
			}
			bigTwoTable.disable();
			for (int i=0; i<4; ++i)
	        {
	          getPlayerList().get(i).removeAllCards();
	        }
			JOptionPane.showMessageDialog(null,finalAlert);
			CardGameMessage ready = new CardGameMessage(CardGameMessage.READY,-1,null);
			sendMessage(ready);
		}
	}
	/**
	 * A method for checking if the game ends.
	 * 
	 * @return a boolean value
	 * 					The function returns true if one of the player's deck is empty else false 
	 */
	public boolean endOfGame() {
		for (CardGamePlayer element : playerList) {
			if (element.getNumOfCards() == 0)
				return true;
		}
		return false;
	}
	/**
	 * 
	 * @return a boolean value
	 */
	public boolean isConnected() {
		return !(sock.isClosed()) ;
	}
	/**
	 *  This method is used for making a socket connection with the game server.
	 *  
	 */
	public void makeConnection() {
		try {
			sock = (new Socket(getServerIP(), getServerPort()));
			oos = new ObjectOutputStream(sock.getOutputStream());
			Thread thread = new Thread(new ServerHandler());
			thread.start();
			CardGameMessage joinGameObj = new CardGameMessage(CardGameMessage.JOIN, -1, this.getPlayerName());
			sendMessage(joinGameObj);
			CardGameMessage readyGameObj = new CardGameMessage(CardGameMessage.READY, -1, null);
			sendMessage(readyGameObj);
			bigTwoTable.repaint();
		}
		catch(Exception e) {
			e.printStackTrace();
		}	
	}
	/**
	 * Parses the specified message received from the server.
	 * 
	 * @param message
	 *            the specified message received from the server
	 */
	public void parseMessage(GameMessage message) {
		if(message.getType()==CardGameMessage.FULL) {
			bigTwoTable.printMsg("The server is already full and cannot be joined!");
			try {
				sock.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		else if(message.getType()==CardGameMessage.JOIN) {
			getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
			bigTwoTable.repaint();
			bigTwoTable.printMsg(playerList.get(message.getPlayerID()).getName() + " joined the game!");
		}
		
		else if(message.getType()==CardGameMessage.MOVE) {
			checkMove(message.getPlayerID(),(int[])message.getData());
			bigTwoTable.repaint();
		}
		else if(message.getType()==CardGameMessage.MSG) {
			bigTwoTable.printChatMsg((String)message.getData());
		}
		else if(message.getType()==CardGameMessage.PLAYER_LIST) {
			setPlayerID(message.getPlayerID());
			bigTwoTable.setActivePlayer(message.getPlayerID());
			for(int i=0;i<getNumOfPlayers();i++)	
				if(((String[])message.getData())[i]!=null) {
					getPlayerList().get(i).setName(((String[])message.getData())[i]);
				}
		}
		else if(message.getType()==CardGameMessage.QUIT) {
			bigTwoTable.printMsg("Player " + message.getPlayerID() + " " + playerList.get(message.getPlayerID()).getName() + " left the game.");
			getPlayerList().get(message.getPlayerID()).setName("");
			if(!endOfGame()) {
				bigTwoTable.disable();
				CardGameMessage msg = new CardGameMessage(CardGameMessage.READY, -1, null);
				sendMessage(msg);
				for(int i=0;i<4;i++) {
					getPlayerList().get(i).removeAllCards();
				}
			}
			bigTwoTable.repaint();
		}
		else if(message.getType()==CardGameMessage.READY) {
			bigTwoTable.printMsg("Player " + message.getPlayerID() + " is ready.");
		}
		else if(message.getType()==CardGameMessage.START) {
			deck = (BigTwoDeck) message.getData();
			start(deck);
			bigTwoTable.enable();
			bigTwoTable.repaint();
		}
	}
	
	@Override
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * An inner class that implements the Runnable interface.
	 * 
	 * @author Tratik Dixit
	 *
	 */
	class ServerHandler implements Runnable{

		@Override
		public void run() {	
			ObjectInputStream inputObj;
			CardGameMessage message = null;
			try
			{
				inputObj = new ObjectInputStream(sock.getInputStream());
				while(!sock.isClosed()) {
					message = (CardGameMessage) inputObj.readObject();
					if(message != null)
						parseMessage(message);	
				}
				inputObj.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			bigTwoTable.repaint();
		}
	}
	/**
	 * 
	 * A method for returning a valid hand from the specified list of cards of the player
	 * 
	 * @param player 
	 * 				It is a CardGamePlayer object that stores the list of players.
	 * @param cards 
	 * 				It is a CardList object that stores the cards of the active player.
	 * 
	 * @return object of the type of hand dealt (Straight, Flush and so on) or null
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards)
	{
		StraightFlush obj1 = new StraightFlush(player,cards); 
		if(obj1.isValid())
			return obj1; 
		
		Quad obj2 = new Quad(player,cards); 
		if(obj2.isValid())
		 	return obj2; 
		
		FullHouse obj3 = new FullHouse(player,cards); 
		if(obj3.isValid())
			return obj3; 
		
		Flush obj4 = new Flush(player,cards); 
		if(obj4.isValid())
			return obj4; 
		
		Straight obj5 = new Straight(player,cards); 
		if(obj5.isValid())
			return obj5; 
		
		Triple obj6 = new Triple(player,cards); 		
		if(obj6.isValid())
			return obj6; 
	
		Pair obj7 = new Pair(player,cards); 
		if(obj7.isValid())
			return obj7; 
		
		Single obj8 = new Single(player,cards); 
		if(obj8.isValid())
			return obj8; 
		
		return null;
	   // default return value is null
	}
	 
	/**
	 * Main (driver) method It starts the game
	 * 
	 * @param args (unused)
	 */
	public static void main(String [] args)
	{
		BigTwoClient game = new BigTwoClient();
	}
}