/**
 * This class is a subclass of the Hand class, and is used to model a hand of Pair. 
 * 
 * @author Tratik Dixit
 *
 */
public class Pair extends Hand{

	private static final long serialVersionUID = 1L;

	/**
	 * A constructor for building a hand with the specified player and list of cards(Here: Pair). 
	 * 
	 * @param player 
	 * 			An object of 'Player' it refers to the active player
	 * @param cards 
	 * 			An object of 'CardList' that stores the active hand
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * This is a getter method. It is used for retrieving the top card of this hand.
	 * 
	 * @return the top card
	 * 			 
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(1);	
	}
	
	/**
	 * A method for checking if current calling object is a valid hand.
	 * 
	 * @return a boolean false 
	 *         if hand is valid then true else false
	 */
	public boolean isValid() {
			return ((this.size() == 2) && (this.getCard(0).getRank() == this.getCard(1).getRank())) ;
	}
	
	/**
	 * A method for returning a string specifying the type of this hand of the current calling object
	 * 
	 * @return  A string value
	 *        The name of the type of hand of the current calling object
	 */
	public String getType() {
		return "Pair";
	}
}
