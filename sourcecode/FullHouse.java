/**
 * This class is a subclass of the Hand class, and is used to model a hand of Full House. 
 * 
 * @author Tratik Dixit
 *
 */
public class FullHouse extends Hand{
	private static final long serialVersionUID = 4622772461907486829L;
	/**
	 * A constructor for building a hand with the specified player and list of cards(Here: Single). 
	 * 
	 * @param player 
	 * 			An object of 'Player' it refers to the active player
	 * @param cards 
	 * 			An object of 'CardList' that stores the active hand
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	/**
	 * This is a getter method. It is used for retrieving the top card of this hand.
	 * 
	 * @return the top card
	 * 			 
	 */
	public Card getTopCard() {
		this.sort();
		if(this.getCard(0).getRank()== this.getCard(2).getRank()) {
			return this.getCard(0);
		}
		else if(this.getCard(2).getRank() == this.getCard(4).getRank()) {
			return this.getCard(2);
		}
		return null;
		
	}
	/**
	 * A method for checking if current calling object is a valid hand.
	 * 
	 * @return a boolean false 
	 *         if hand is valid then true else false
	 */
	public boolean isValid() {
		this.sort();
		if(this.size() == 5) {
			if((this.getCard(0).getRank() == this.getCard(1).getRank()) && (this.getCard(1).getRank() == this.getCard(2).getRank()) && (this.getCard(3).getRank() == this.getCard(4).getRank())){
				return true;
			}
			else if((this.getCard(0).getRank() == this.getCard(1).getRank()) && (this.getCard(2).getRank() == this.getCard(3).getRank()) && (this.getCard(3).getRank() == this.getCard(4).getRank())) {
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
		return "FullHouse";	
	}
}