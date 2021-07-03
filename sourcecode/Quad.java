/**
 * This class is a subclass of the Hand class, and is used to model a hand of Quad.
 * 
 * @author Tratik Dixit
 *
 */
public class Quad extends Hand {

	private static final long serialVersionUID = 1L;
	/**
	 * A constructor for building a hand with the specified player and list of cards(Here: Quad). 
	 * 
	 * @param player 
	 * 			An object of 'Player' it refers to the active player
	 * @param cards 
	 * 			An object of 'CardList' that stores the active hand
	 */
	public Quad(CardGamePlayer player, CardList cards) {
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
		if(this.getCard(0).getRank() == this.getCard(3).getRank()) {
			return this.getCard(0);
		}
		else if(this.getCard(1).getRank() == this.getCard(4).getRank()) {
			return this.getCard(1);
		}
		else 
		    return null;
	}
	/**
	 * A method for checking if current calling object is a valid hand.
	 * 
	 * @return a boolean false 
	 *         if hand is valid then true else false
	 */
// check if this function works correctly
	public boolean isValid() {
		if(this.size() == 5) {
			this.sort();
			if((this.getCard(0).getRank() == this.getCard(1).getRank()) && (this.getCard(1).getRank() == this.getCard(2).getRank()) && (this.getCard(2).getRank() == this.getCard(3).getRank())) {
				return true;
			}
			else if((this.getCard(1).getRank() == this.getCard(2).getRank()) && (this.getCard(2).getRank() == this.getCard(3).getRank()) && (this.getCard(3).getRank() == this.getCard(4).getRank())) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;	
	}
	
	/**
	 * A method for returning a string specifying the type of this hand of the current calling object
	 * 
	 * @return  A string value
	 *        The name of the type of hand of the current calling object
	 */
	public String getType() {
		return "Quad";	
	}
}