/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a Big Two card game.
 * It overrides the compareTo() method it inherited from the Card class to reflect the ordering of cards used in a Big Two card game. 
 * 
 * @author Tratik Dixit
 *
 */

public class BigTwoCard extends Card{
	

	private static final long serialVersionUID = 1L;

	/**
	 * A constructor for building a card with the specified suit and rank
	 * 
	 * @param suit
	 *            an integer between 0 and 3 representing the suit of a card 
	 * @param rank
	 *            an integer value between 0 and 12 representing the rank of a card
	 *            
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit,rank);
	}
	
	/**
	 * This is a a method for comparing the order of this card with a specified card.
	 * 
	 * @param card 
	 *          A negative integer, zero, or a positive integer as this card is less than, equal to, or greater than the specified card.
	 *      
	 */
	public int compareTo(Card card) {
	    int tocheck[]= new int[4];
		tocheck[0]=this.rank;
		tocheck[1]=card.rank;
		tocheck[2]=this.getSuit();
		tocheck[3]=card.getSuit();

	    for (int i = 0 ; i<2; ++i)	
	    {
		if(tocheck[i] ==0) 
			tocheck[i] = 13;
		else if(tocheck[i] == 1) 
			tocheck[i] = 14;
      	}
	    for (int i= 0; i<4;i+=2 )
	    {
	    	if(tocheck[i] < tocheck[i+1]) 
				return -1;
			else if(tocheck[i] > tocheck[i+1]) 
				return 1;
	    }
	   // if the code above does not return it means both cards have same value so we return 0
		return 0;
	}
}
