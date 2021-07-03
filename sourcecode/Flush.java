import java.util.* ;

/**
 * This class is a subclass of the Hand class, and is used to model a hand of Flush.
 * 
 * @author Tratik Dixit
 *
 */
public class Flush extends Hand{
		
	private static final long serialVersionUID = 1L;

	/**
	 * A constructor for building a hand with the specified player and list of cards(Here: Single). 
	 * 
	 * @param player 
	 * 			An object of 'Player' it refers to the active player
	 * @param cards 
	 * 			An object of 'CardList' that stores the active hand
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * This is a getter method. It is used for retrieving the top card of this hand.
	 * 
	 * @return the top card
	 * 			 
	 */
	public Card getTopCard() {
		ArrayList<Integer> orderOfCards = new ArrayList<Integer>(5);
		for(int i=0;i<5;i++) {
			if(this.getCard(i).getRank()==0) 
				orderOfCards.add(13);
			else if(this.getCard(i).getRank()==1) 
				orderOfCards.add(14);
			else 
				orderOfCards.add(this.getCard(i).getRank());
		}
		Collections.sort(orderOfCards);
		if(orderOfCards.get(4)>=13) {
			orderOfCards.set(4, orderOfCards.get(4)-13);
		}
		int ans = 0;
		for(int i=1;i<5;i++) {
			if(this.getCard(i).getRank() == orderOfCards.get(4)) {
				ans = i;
			}
		}
		return this.getCard(ans);

	}
	/**
	 * A method for checking if current calling object is a valid hand.
	 * 
	 * @return a boolean false if hand is valid then true else false
	 */
	public boolean isValid() {
		if(this.size() == 5) {
			for(int i=0;i<4;i++) {
				if(this.getCard(i).getSuit() != this.getCard(i+1).getSuit()) {
					return false;
				}
			}
			return true;
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
		return "Flush";	
	}
	
	

}

