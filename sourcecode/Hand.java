
import java.util.* ;

/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards
 * 
 * @author Tratik Dixit
 *
 */

public abstract class Hand extends CardList {

	private static final long serialVersionUID = 1L;
	private CardGamePlayer player; //A CardGamePlayer object that plays the hand.
	
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player 
	 * 			An object of 'Player' it refers to the active player
	 * @param cards 
	 * 			An object of 'CardList' that stores the active hand
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = new CardGamePlayer();
		this.player = player;
		for(int i=0;i<cards.size();i++) {
			this.addCard(cards.getCard(i));
		}
	}
	
	/**
	 * This is a getter method. It returns the current player.
	 * 
	 * @return player 
	 *     A CardGamePlayer object i.e player of the current object.
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	
	/**
	 * This is a getter method. It is used for retrieving the top card of this hand.
	 * 
	 * @return The top card
	 * 			 
	 */
	public Card getTopCard() {
		return null;
	}
	
	/**
	 * This method is used for checking if this hand beats a specified hand.
	 * 
	 * @param hand The hand that is currently dealt and is to be compared with the specified hand
	 * 
	 * @return a boolean value 
	 *          If the given hands beats the specified hand then true else  false
	 */
	public boolean beats(Hand hand) {
		// comparing Singles, pairs and triples
		for (int i = 1 ; i< 4; i++)
               {	
             if(hand.size() == i && this.size() == i && this.isValid() && hand.isValid()) {
	                    if (this.getTopCard().compareTo(hand.getTopCard()) == 1) 
	                    	return true;
	              }
             }
		ArrayList<String> Order = new ArrayList<>(Arrays.asList("Straight", "Flush","FullHouse","Quad", "StraightFlush"));
		// checking hand with 5 cards
		if(hand.size() == 5 && this.size()==5) 
		{
			// if cards have the same hand type (eg. both are flush)	
					int r1 = Order.indexOf(this.getType());
					int r2 = Order.indexOf(hand.getType());
					if (r1==r2)
							return (this.getTopCard().compareTo(hand.getTopCard())==1) ;
					return (r1>r2);
		}
		return false;
	}
	
	
	/**
	 * A method for checking if current calling object is a valid hand.
	 * 
	 * @return a boolean false 
	 *         if hand is valid then true else false
	 */
	public abstract boolean isValid();

	/**
	 *  A method for returning a string specifying the type of this hand of the current calling object
	 * 
	 * @return  A string value
	 *        The name of the type of hand of the current calling object
	 */
    public abstract String getType();

}
