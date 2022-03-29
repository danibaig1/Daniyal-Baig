package tripletriad;

import java.io.FileNotFoundException;
import java.util.*;

public class Cards
{
	public static void main(String[] args) throws FileNotFoundException
	{
		Board b = new Board();
		player Dani = new player(card.getRandomCards(players.Player_1.getOwnerCode()));
		DumbAI Dai = new DumbAI(card.getRandomCards(players.AI_Player.getOwnerCode()));
		b.runGame(Dani, Dai);
//				while(true)
//				{
//					Dai.inHand();
//					System.out.println();
//					b.printBoard();
//					Dani.inHand();
//					System.out.println("Player: "+Dani.score);
//					System.out.println("AI: "+Dai.score);
//					Dani.playerMove(b);
//					Dai.playerMove(b);
//				}
	}
	
}

//assigning cards to a specific player
enum players 
{
	Player_1(0),
	AI_Player(1);
	
	private final int owner;
	
	private players(int owner) {
		this.owner = owner;
	}
	
	public int getOwnerCode() {
		return this.owner;
	}
}
enum card
{	
	//generating cards for the deck
	//Character A is represented by 11
	Bahamut(" _________\n",
			"|    9    |\n",
			"|         |\n",
			"|6       5|\n",
			"|         |\n",
			"|    9    |\n",
			"|_________|", 
				'9','5','9','6'),
	
	Bartz ( " _________\n",
			"|    4    |\n",
			"|         |\n",
			"|A       4|\n",
			"|         |\n",
			"|    A    |\n",
			"|_________|",
				'4','4','A','A'),
	
	Cecil(  " _________\n",
			"|    4    |\n",
			"|         |\n",
			"|A       A|\n",
			"|         |\n",
			"|    4    |\n",
			"|_________|",
				'4','A','4','A'),
	
	Cloud(	" _________\n",
			"|    9    |\n",
			"|         |\n",
			"|8       3|\n",
			"|         |\n",
			"|    9    |\n",
			"|_________|",
				'9','3','9','8'),
	
	CoD(	" _________\n",
			"|    7    |\n",
			"|         |\n",
			"|6       B|\n",
			"|         |\n",
			"|    7    |\n",
			"|_________|",
				'7','B','7','6'),
	
	Firion(	 "_________\n",
			"|    A    |\n",
			"|         |\n",
			"|1       5|\n",
			"|         |\n",
			"|    A    |\n",
			"|_________|",
				'A','5','A','1'),
	
Lightning (	" _________\n",
			"|    9    |\n",
			"|         |\n",
			"|A       1|\n",
			"|         |\n",
			"|    7    |\n",
			"|_________|",
				'9','1','7','A'),
	
Noctis (	" _________\n",
			"|    7    |\n",
			"|         |\n",
			"|1       B|\n",
			"|         |\n",
			"|    9    |\n",
			"|_________|",
				'7','B','9','1'),
	
OnionKnight(" _________\n",
			"|    8    |\n",
			"|         |\n",
			"|A       2|\n",
			"|         |\n",
			"|    8    |\n",
			"|_________|",
				'8','2','8','A'),
	
	Squall(	" _________\n",
			"|    6    |\n",
			"|         |\n",
			"|1       A|\n",
			"|         |\n",
			"|    A    |\n",
			"|_________|",
				'6','A','A','1'),
	
	Terra(	" _________\n",
			"|    A    |\n",
			"|         |\n",
			"|5       A|\n",
			"|         |\n",
			"|    2    |\n",
			"|_________|",
				'A','A','2','5'),
	
	Tidus(	" _________\n",
			"|    A    |\n",
			"|         |\n",
			"|9       7|\n",
			"|         |\n",
			"|    1    |\n",
			"|_________|",
				'A','7','1','9'),
	
	Vaan(	" _________\n",
			"|    1    |\n",
			"|         |\n",
			"|9       7|\n",
			"|         |\n",
			"|    A    |\n",
			"|_________|",
				'1','7','A','9'),
	
	WoL(	" _________\n",
			"|    A    |\n",
			"|         |\n",
			"|A       2|\n",
			"|         |\n",
			"|    5    |\n",
			"|_________|",
				'A','2','5','A'),
	
	Zidane(	" _________\n",
			"|    5    |\n",
			"|         |\n",
			"|8       A|\n",
			"|         |\n",
			"|    8    |\n",
			"|_________|",
				'5','A','6','8'),

//insert Russian national theme 
Chungus(	" _________\n",
			"|    A    |\n",
			"|         |\n",
			"|A       A|\n",
			"|         |\n",
			"|    A    |\n",
			"|_________|",
				'A','A','A','A');

	String First;
	String Second;
	String Third;
	String Fourth;
	String Fifth;
	String Sixth;
	String Seventh;
	char Top = 0;
	char Right = 0;
	char Bottom = 0;
	char Left = 0;
	
	int owner;
	//prints line by line of the string of cards
	private card(String First, String Second, String Third, String Fourth, String Fifth, String Sixth, String Seventh, char Top, char Right, char Bottom, char Left)
	{
		this.First = First;
		this.Second = Second;
		this.Third = Third;
		this.Fourth = Fourth;
		this.Fifth = Fifth;
		this.Sixth = Sixth;
		this.Seventh = Seventh;
		this.Top = Top;
		this.Right = Right;
		this.Bottom = Bottom;
		this.Left = Left;
		this.owner = -1; //belongs to no one yet
	}
	public void setOwner(int owner)
	{
		this.owner = owner;
	}
	public String toString()
	{
		return First+Second+Third+Fourth+Fifth+Sixth+Seventh;
	}
	//randomly generates 5 cards to give to a player
	public static card[] getRandomCards(int owner)
	{
		Random rand = new Random();
		card cards[] = new card[5];
		for (int j = 0; j < 5; j++)
		{
			int cardIndex = rand.nextInt(15)+1;
			cards[j] = mapCard(cardIndex);
			cards[j].setOwner(owner);
		}
		return cards;
	}
	//setting cards to a value so when the number generator generates a number it will choose it
	private static card mapCard(int cardIndex)
	{
		switch (cardIndex)
		{
		case 1: return card.Bahamut;
		case 2: return card.Bartz;
		case 3: return card.Cecil;
		case 4: return card.Chungus;
		case 5: return card.Cloud;
		case 6: return card.CoD;
		case 7: return card.Firion;
		case 8: return card.Lightning;
		case 9: return card.Noctis;
		case 10: return card.OnionKnight;
		case 11: return card.Squall;
		case 12: return card.Terra;
		case 13: return card.Tidus;
		case 14: return card.Vaan;
		case 15: return card.WoL;
		default: return card.Zidane;
		}
	}
}
class Board 
{
    private static final int WIDTH = 3;
    private static final int HEIGHT = 3;
    private int numslots = 0;
    
    private card[][] slots;

    public Board() 
    {
        slots = new card[WIDTH][HEIGHT];
    }

    public card[][] getSlots()
    {
        return slots;
         //return the matrix of the cards
    }

    public void increaseNumSlots()
    {
    	this.numslots++;
    }
    public void setSlot(card c, int X, int Y) 
    {
        this.slots[X][Y] = c;
    }
    public card getSlot(int X, int Y)
    {
    	return this.slots[X][Y];
    }
    public boolean isBoardFilled()
    {
    	if (this.numslots < 9)
    		return false;
    	else
    		return true;
    }
    private boolean validSlot(int X, int Y) 
    {
    	if (this.slots[X][Y].Top == 0)
    		return true;
    	else
    		return false; //valid slot
    }
    public void printBoard()
    {
    	
    	for (int y = 0; y < HEIGHT; y++) 
    	{
    		char top[] = new char[3];
    		char right[] = new char [3];
    		char bottom[] =  new char[3];
    		char left[] = new char[3];
    		
    		//parse cards
    		for (int x = 0; x < WIDTH; x++)
    		{
    			card slot = slots[y][x];
	    		if(slot == null)
	    		{
	    			top[x] = ' ';
	    			left[x] = ' ';
	    			bottom[x] = ' ';
	    			right[x] = ' ';
	    		}
	    		else
	    		{
	    			top[x] = (char) slot.Top;
	    			right[x] = (char) slot.Right;
	    			bottom[x] = (char) slot.Bottom;
	    			left[x] = (char) slot.Left;
	    			
	    		}
    		}
    		System.out.println(" _________   _________   _________");
    		System.out.println("|    "+top[0]+"    | |    "+top[1]+"    | |    "+top[2]+"    |");
    		System.out.println("|         | |         | |         |");
    		System.out.println("|"+left[0]+"       "+right[0]+"| |"+left[1]+"       "+right[1]+"| |"+left[2]+"       "+right[2]+"|");
    		System.out.println("|         | |         | |         |");
    		System.out.println("|    "+bottom[0]+"    | |    "+bottom[1]+"    | |    "+bottom[2]+"    |");
    		System.out.println("|_________| |_________| |_________|");
    		
    	}
    }
    
    public void runGame(player p, player AI)
    {
    	//cast to exact type, DumbAi and SmartAI should have the same functions
    	if (AI instanceof DumbAI)
    	{
    		AI = (DumbAI) AI;
    	}
    	else
    	{
    		AI = (SmartAI) AI;
    	}
    	
    	//running game
    	while(!isBoardFilled()) //looping until board is full
    	{
    		//display current state of the board
			System.out.println("AI: "+AI.score);
    		AI.inHand();
			System.out.println();
			printBoard();
			p.inHand();
			System.out.println("Player: "+p.score);

			
			//player turn and their score
			int playerTurn = updateBoard(p.playerMove(this));
			p.addScore(playerTurn);
			AI.minusScore(playerTurn);
//			System.out.println("Player: "+p.score);
//			System.out.println("AI: "+AI.score);
			
			//AI turn
			int AITurn = updateBoard(AI.playerMove(this));
			AI.addScore(AITurn);
			p.minusScore(AITurn);
//			System.out.println("Player: "+p.score);
//			System.out.println("AI: "+AI.score);
    	}
		System.out.println("AI: "+AI.score);
    	printBoard();
		System.out.println("Player: "+p.score);
    	//identifies the winner
    	if(p.score >= 6 && AI.score <= 4)
    	{
    		//player is the winner
    		System.out.println("You Win");
    	}
    	else if (p.score <= 4 && AI.score >=6)
    	{
    		//AI is winner
    		System.out.println("AI wins");
    	}
    	else
    	{
    		//Draw
    		System.out.println("Draw");
    	}
    }
    private int updateBoard(int[] slot)
    {
    	int score = 0;
    	int cardVal = 0;
    	int nextCardVal = 0;
    	int X = slot[0];
    	int Y = slot[1];
    	//check top
    	if((X-1) >= 0 && this.slots[X-1][Y] != null)
    	{
    		//Flip card if values exists
    		cardVal = (int)this.slots[X][Y].Top;
    		nextCardVal = (int)this.slots[X - 1][Y].Bottom;
    		score += flipcard(X,Y,X - 1,Y, cardVal, nextCardVal);
    	}
    	
    	//check right
    	if ((Y + 1) < WIDTH && this.slots[X][Y + 1] != null)
    	{
    		cardVal = (int)this.slots[X][Y].Right;
    		nextCardVal = (int)this.slots[X][Y + 1].Left;
    		score += flipcard(X,Y,X,Y + 1, cardVal, nextCardVal);
    	}
    	
    	//check bottom
    	if((X + 1) < HEIGHT && this.slots[X + 1][Y] != null)
    	{
    		cardVal = (int)this.slots[X][Y].Bottom;
    		nextCardVal = (int)this.slots[X + 1][Y].Top;
    		score += flipcard(X,Y,X + 1,Y, cardVal, nextCardVal);
    	}
    	
    	//check left
    	if ((Y-1) >= 0 && this.slots[X][Y-1] != null)
    	{
//        	System.out.println("Left compares "+ this.slots[X][Y].Left);
    		cardVal = (int)this.slots[X][Y].Left;
    		nextCardVal = (int)this.slots[X][Y-1].Right;
    		score += flipcard(X,Y,X,Y-1, cardVal, nextCardVal);
    	}
    	return score;
    }
    private int flipcard(int X, int Y, int nextX, int nextY, int cardVal, int nextCardVal)
    {
//    	System.out.println("X + "+X+" Y "+Y+" = "+cardVal);
//   	System.out.println("New X + "+ nextX +" next Y " + nextY +" = "+ nextCardVal);

    	if(cardVal > nextCardVal && this.slots[nextX][nextY].owner != (this.slots[X][Y].owner))
    	{
    		this.slots[nextX][nextY].owner = this.slots[X][Y].owner;
    		return 1;
    	}
    	else
    		return 0;
    }
}
class player
{
	ArrayList<card> getRandomCards;
	int score;
	
	public player(card[] fiveRandomCard)
	{
		this.getRandomCards = new ArrayList<card>(Arrays.asList(fiveRandomCard));
		this.score = 5;
	}
	public void inHand()
	{
		Iterator itr = this.getRandomCards.iterator();
		for(card c : this.getRandomCards)
			System.out.print(" _________  ");
		System.out.println();
		for(card c : this.getRandomCards)
			System.out.print("|    " + c.Top + "    | ");
		System.out.println();
		for(card c : this.getRandomCards)
			System.out.print("|         | ");
		System.out.println();
		for(card c : this.getRandomCards)
			System.out.print("|"+c.Left+"       "+c.Right+"| ");
		System.out.println();
		for(card c : this.getRandomCards)
			System.out.print("|         | ");
		System.out.println();
		for(card c : this.getRandomCards)
		System.out.print("|    "+c.Bottom+"    | ");
		System.out.println();
		for(card c : this.getRandomCards)
		System.out.print("|_________| ");
	
	}
	
	public void addScore(int n)
	{
		this.score += n;
	}
	
	public void minusScore(int n)
	{
		this.score -= n;
	}
	
	public int[] playerMove(Board b)
	{
		Scanner s = new Scanner(System.in);
		int cardNum = 0;
		int X = 0, Y = 0;
		while(true)
		{
			System.out.println("Which card would you like to use within " + this.getRandomCards.size() + " cards? (starts at 0)");
			cardNum = s.nextInt();
			if(cardNum > this.getRandomCards.size())
				continue;
			else
				break;
		}
		boolean valid = true;
		while (true) //prompt to place the card at the proper slot
		{
			System.out.println("Where do you want to place your card? (7, 8, 9, 4, 5, 6, 1, 2, 3)");
			int slot = s.nextInt();
			System.out.println("You gave me: "+slot+" move");
			//Check proper slot with switch statements
			switch(slot)
			{
			//placing the card in the square coordinate
			case 7:
				X = 0;
				Y = 0;
				break;
			case 8:
				X = 0;
				Y = 1;				
				break;
			case 9:
				X = 0;
				Y = 2;
				break;
			case 1:
				X = 2;
				Y = 0;
				break;
			case 2:
				X = 2;
				Y = 1;
				break;
			case 3:
				X = 2;
				Y = 2;
				break;
			case 4:
				X = 1;
				Y = 0;
				break;
			case 5:
				X = 1;
				Y = 1;
				break;
			case 6:
				X = 1;
				Y = 2;
				break;
			default:
				System.err.println("Bad move");
				valid = false;
			}
			
			if(!valid)
			{
//				System.out.println("Invalid");
				continue;
			}			
			
			//if empty slot then place card in it
			if (b.getSlot(X,Y) == null) 
			{
//				System.out.println("Good move");
				b.setSlot(this.getRandomCards.remove(cardNum), X, Y); //places card in it
				b.increaseNumSlots();
				break; //finishing turn by exiting the loop
			}
			else if (b.isBoardFilled())
			{
				break;
			}
			
//			System.out.println("???");
			
		}	
		System.out.println("End of your turn");
		
		return new int[] {X,Y};
		//end of player move
	}
}
class DumbAI extends player
{

	public DumbAI(card[] fiveRandomCard)
	{
		super(fiveRandomCard);	
		
	}
	public int[] playerMove(Board b)
	{
		int cardNum = (int)(Math.random());
		int X = 0, Y = 0;
//		while(true)
//		{
//			if(cardNum > this.getRandomCards.size())
//				continue;
//			else
//				break;
//		}
		boolean valid = true;
		while (true) //prompt to place the card at the proper slot
		{
			int slot = (int)(Math.random()*8)+1;
			//Check proper slot with switch statements
			switch(slot)
			{
			//placing the card in the square coordinate
			case 7:
				X = 0;
				Y = 0;
				break;
			case 8:
				X = 0;
				Y = 1;				
				break;
			case 9:
				X = 0;
				Y = 2;
				break;
			case 1:
				X = 2;
				Y = 0;
				break;
			case 2:
				X = 2;
				Y = 1;
				break;
			case 3:
				X = 2;
				Y = 2;
				break;
			case 4:
				X = 1;
				Y = 0;
				break;
			case 5:
				X = 1;
				Y = 1;
				break;
			case 6:
				X = 1;
				Y = 2;
				break;
			default:
				System.err.println("Bad move");
				valid = false;
			}
			
			if(!valid)
				continue;
			
			if (b.getSlot(X,Y) == null) 
			{
				b.setSlot(this.getRandomCards.remove(cardNum), X, Y);
				b.increaseNumSlots();
				break; //finishing turn by exiting the loop
			}
			else if (b.isBoardFilled())
			{
				break;
			}
			
		}	
		return new int[] {X,Y};
		//end of player move
		
	}
}
class SmartAI extends player
{
	
	public SmartAI(card[] fiveRandomCard) 
	{
		super(fiveRandomCard);
	}
	
	public int[] playerMove(Board b)
	{
		int cardNum = (int)(Math.random());
		int X = 0, Y = 0;
//		while(true)
//		{
//			if(cardNum > this.getRandomCards.size())
//				continue;
//			else
//				break;
//		}
		boolean valid = true;
		while (true) //prompt to place the card at the proper slot
		{
			int slot = (int)(Math.random()*8)+1;
			//Check proper slot with switch statements
			switch(slot)
			{
			//placing the card in the square coordinate
			case 7:
				X = 0;
				Y = 0;
				break;
			case 8:
				X = 0;
				Y = 1;				
				break;
			case 9:
				X = 0;
				Y = 2;
				break;
			case 1:
				X = 2;
				Y = 0;
				break;
			case 2:
				X = 2;
				Y = 1;
				break;
			case 3:
				X = 2;
				Y = 2;
				break;
			case 4:
				X = 1;
				Y = 0;
				break;
			case 5:
				X = 1;
				Y = 1;
				break;
			case 6:
				X = 1;
				Y = 2;
				break;
			default:
				System.err.println("Bad move");
				valid = false;
			}
			
			if(!valid)
				continue;
			
			if (b.getSlot(X,Y) == null) 
			{
				b.setSlot(this.getRandomCards.remove(cardNum), X, Y);
				break; //finishing turn by exiting the loop
			}
			
		}	
		return new int[] {X,Y};
	}
}


