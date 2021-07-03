/** 
 * The BigTwoDeck class is a subclass of the Deck class, and is used to model a deck of cards used in a Big Two card game.
 *  
 * @author Tratik Dixit
 *
 */
public class BigTwoDeck extends Deck{

	private static final long serialVersionUID = 1L;

	/**
	 * This is a method for initializing a deck of Big Two cards. It should remove all cards from the deck, create 52 Big Two cards and add them to the deck.
	 */
	public void initialize() {
		removeAllCards();
		for (int suit= 0; suit< 4; suit++) {
			for (int pCard = 0; pCard < 13; pCard++) {
				addCard(new BigTwoCard(suit, pCard));
				// adding card
			}
		}
	}
}