package DK;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.geom.Ellipse2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DonkeyKongGame extends JFrame
{
	
	public static void main(String args[]) throws IOException, ClassNotFoundException 
	{
		DonkeyKongGame DK = new DonkeyKongGame();
		DK.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		DK.setSize(448,536);
		DK.setVisible(true);
		DK.setup();
		DK.paintscenes();
		DK.draw();
	}
	
	private Image raster;
//	private Image coolBackground;
	private Graphics rasterGraphics;
	private int[] ladder =
		{
			(int)(Math.random() * 330 + 70),
			(int)(Math.random() * 330 + 45),
			(int)(Math.random() * 330 + 70),
			(int)(Math.random() * 330 + 45),
			(int)(Math.random() * 330 + 70)
		};
	GameState state = GameState.RUNNING;
	ArrayList<Block> blocks = new ArrayList<Block>();
	ArrayList<Ladder> ladders = new ArrayList<Ladder>();
	boolean CLICK = false;
	
	public void setup()
	{
		raster = this.createImage(448,536);
		rasterGraphics = raster.getGraphics();
//		coolBackground = new ImageIcon("aurora.jpg").getImage();;
		
		addKeyListener(new KeyListener()
			{
				public void keyPressed(KeyEvent e) {}
				public void keyReleased(KeyEvent e)
				{
					if(e.getKeyCode() == KeyEvent.VK_P)
					{
						if(state == GameState.RUNNING)
						{
							state = GameState.PAUSED;
						}
						else
							state = GameState.RUNNING;
					}
				}
				public void keyTyped(KeyEvent e) {}	
			});
		
		addMouseListener(new MouseListener()
				{
			public void mouseClicked(MouseEvent m) {}
			public void mouseEntered(MouseEvent m) {}
			public void mouseExited(MouseEvent m) {}
			public void mousePressed(MouseEvent m) 
			{
				CLICK = true;
			}
			public void mouseReleased(MouseEvent m)
			{
				CLICK = false;
			}
				});
			
	}
	
	enum GameState
	{
		RUNNING,
		OVER,
		PAUSED;
	}
	
	public void draw() throws IOException, ClassNotFoundException
	{
		
		Me player = new Me(60,455, Color.BLUE, 3);
		barrel barrels[] = {new barrel(105,80, Color.RED, 5, 5),
							new barrel(150,160, Color.RED, 5,5)};
		Rectangle Score = new Rectangle(30,40,70,40);
		
		this.addKeyListener(player);
		while(true)
		{
			long time = System.currentTimeMillis();
			if(state == GameState.RUNNING) 
			{
				DrawBackground(rasterGraphics);
				for (Block p : blocks)
				{
					p.draw(rasterGraphics);
				}
				for (Ladder l : ladders)
				{
					l.draw(rasterGraphics);
				}
				player.draw(rasterGraphics);
				for(barrel b : barrels)
					b.draw(rasterGraphics);
				//draw scores
                rasterGraphics.setColor(Color.RED);
                ((Graphics2D) rasterGraphics).fill(Score);
                rasterGraphics.setColor(Color.BLUE);
                rasterGraphics.setFont(new Font("Arial", Font.PLAIN, 12));
                String scoreString = "Scores: " + player.getScore();
                rasterGraphics.drawString(scoreString, 30, 60);
				//checking collision with blocks for player and barrels
				for (Block p : blocks)
				{
					for(int j = 0; j <barrels.length; j++)
					{
						player.checkCollision(p);
						barrels[j].checkCollision(p);
					}
				}
				//checks collision with ladders for players
				for (Ladder l : ladders)
					player.atLadder(l);
				
				//if barrels collides with player you lose a life until you reach 0 then game ends
				for(int i = 0; i < barrels.length; i++)
				{
					if(player.hitBarrel(barrels[i]))
					{
						player.Location= new Vector2D(60,455);
						--player.Lives;
						barrels[i] = new barrel(105,80, Color.RED, 5, 5);
						if(player.Lives == 0)
							state = GameState.OVER;
					}
					  //if not hit, check pass barrel
                    else if(player.updateScore(barrels[i]))
                        barrels[i].updatePassed();
				}
				
				getGraphics().drawImage(raster,0,0,getWidth(),getHeight(),null);			
				long deltaTime = System.currentTimeMillis() - time;
				try{Thread.sleep(5-deltaTime);}catch(Exception e){}
			}
			//pause menu
			else if (state == GameState.PAUSED)
			{
				//creating save and load button
				Rectangle Save = new Rectangle(200,100,180,100);
				Rectangle Load = new Rectangle(200,220,180, 100);
				
				rasterGraphics.setColor(Color.RED);
				((Graphics2D) rasterGraphics).fill(Save);
				rasterGraphics.setColor(Color.BLUE);
				rasterGraphics.setFont(new Font("Arial", Font.PLAIN, 34));
				rasterGraphics.drawString("Save", 220, 150);
				
				rasterGraphics.setColor(Color.RED);
				((Graphics2D) rasterGraphics).fill(Load);
				rasterGraphics.setColor(Color.BLUE);
				rasterGraphics.setFont(new Font("Arial", Font.PLAIN, 34));
				rasterGraphics.drawString("Load", 220, 270);
				
				getGraphics().drawImage(raster,0,0,getWidth(),getHeight(),null);
				
				//uses the mouse to select the options
				if(CLICK)
				{
					if (Save.contains(MouseInfo.getPointerInfo().getLocation()))
					{
						FileOutputStream fos = new FileOutputStream("SaveFile.sav");
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						oos.writeObject(player);
						oos.writeObject(ladders);						
						oos.writeObject(barrels);
						
					}
						
					if (Load.contains(MouseInfo.getPointerInfo().getLocation()))
					{
						FileInputStream fis = new FileInputStream("SaveFile.sav");
						ObjectInputStream ois = new ObjectInputStream(fis);
						player = (Me)ois.readObject();
						ladders.clear();
						ladders = (ArrayList<Ladder>)ois.readObject();
						barrels = (barrel[])ois.readObject();
						state = GameState.RUNNING;
						this.addKeyListener(player);
						
					}
					CLICK = false;
				}
			}
			//game over
			else if (state == GameState.OVER)
			{
				Rectangle rect = new Rectangle(0,0,418,536);
				rasterGraphics.setColor(Color.BLACK);
				((Graphics2D) rasterGraphics).fill(rect);
				rasterGraphics.setColor(Color.BLUE);
				rasterGraphics.setFont(new Font("Arial", Font.PLAIN, 60));
				String scoreString = "Score: " + player.getScore();
                rasterGraphics.drawString(scoreString, 30, 90);
                
                Rectangle Y = new Rectangle(30, 130, 180, 100);
				rasterGraphics.setColor(Color.RED);
				((Graphics2D) rasterGraphics).fill(Y);
				rasterGraphics.setColor(Color.BLUE);
				rasterGraphics.setFont(new Font("Arial", Font.PLAIN, 60));
				rasterGraphics.drawString("Yes", 30, 180);
				
				Rectangle N = new Rectangle(30, 270, 180, 100);
				rasterGraphics.setColor(Color.RED);
				((Graphics2D) rasterGraphics).fill(N);
				rasterGraphics.setColor(Color.BLUE);
				rasterGraphics.setFont(new Font("Arial", Font.PLAIN, 60));
				rasterGraphics.drawString("No", 30, 320);
				
				rasterGraphics.setColor(Color.BLUE);
				rasterGraphics.setFont(new Font("Arial", Font.PLAIN, 60));
				rasterGraphics.drawString("Retry?", 30, 480);
				
				getGraphics().drawImage(raster,0,0,getWidth(),getHeight(),null);

				if (CLICK) 
				{
					if(Y.contains(MouseInfo.getPointerInfo().getLocation()))
					{
						state = GameState.RUNNING;
						player.Lives = 3;
						for(barrel b : barrels)
						{
							if(!b.getPassed())
							{
								b.updatePassed();
							}
						}
					}
					if(N.contains(MouseInfo.getPointerInfo().getLocation()))
					{
						System.exit(1);
					}
				}
				CLICK = false;
			}
		}
	}
	
	private void DrawBackground(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 448, 536);
	}
	
	//creates the floors walls and ladders for player and barrel to interact with
	private void paintscenes()
	{
		//floor
		blocks.add(new Block(25, 100, 360, 20, Color.BLACK));
		blocks.add(new Block(60, 175, 370, 20, Color.BLACK));
		blocks.add(new Block(25, 250, 360, 20, Color.BLACK));
		blocks.add(new Block(60, 325, 370, 20, Color.BLACK));
		blocks.add(new Block(25, 400, 360, 20, Color.BLACK));
		blocks.add(new Block(0, 475, 420, 20, Color.BLACK));
		
		//wall
		blocks.add(new Block(0, 0, 25, 536, Color.BLACK));
		blocks.add(new Block(418, 0, 25, 536, Color.BLACK));
	
		//ladders to climb
		ladders.add(new Ladder(ladder[0], 400, 20, 75, Color.GREEN));
		ladders.add(new Ladder(ladder[1], 325, 20, 75, Color.GREEN));
		ladders.add(new Ladder(ladder[2], 250, 20, 75, Color.GREEN));
		ladders.add(new Ladder(ladder[3], 185, 20, 75, Color.GREEN));
		ladders.add(new Ladder(ladder[4], 100, 20, 75, Color.GREEN));
	
	}
}

interface saveFeature extends Serializable
{
}

interface Collidable
{
	public Rectangle getCollision();
}

abstract class ScreenObj
{
	public Vector2D Location;
	public Vector2D Size;
}

class Block extends ScreenObj implements Collidable

{
	public Color C;
	public Vector2D Location;
	public Vector2D Size;

	public Block (int X, int Y, int Xsize, int Ysize, Color c) 
	{
		Location = new Vector2D(X,Y);
		Size = new Vector2D(Xsize,Ysize);
		C = c;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(C);
		g.fillRect((int)Location.getX(),(int)Location.getY(),(int)Size.getX(),(int)Size.getY());
		g.setColor(Color.BLACK);
		g.drawRect((int)Location.getX(),(int)Location.getY(),(int)Size.getX(),(int)Size.getY());

	}

	public Rectangle getCollision()
	{
		return new Rectangle((int)Location.getX(),(int)Location.getY(),(int)Size.getX(),(int)Size.getY());
	}
}

class Ladder extends ScreenObj implements Collidable, saveFeature
{
	public Color C;
	public Vector2D Location;
	public Vector2D Size;

	public Ladder (int X, int Y, int Xsize, int Ysize, Color c) 
	{
		Location = new Vector2D(X,Y);
		Size = new Vector2D(Xsize,Ysize);
		C = c;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(C);
		g.fillRect((int)Location.getX(),(int)Location.getY(),(int)Size.getX(),(int)Size.getY());
		g.setColor(Color.BLACK);
		g.drawRect((int)Location.getX(),(int)Location.getY(),(int)Size.getX(),(int)Size.getY());

	}

	public Rectangle getCollision()
	{
		return new Rectangle((int)Location.getX(),(int)Location.getY(),(int)Size.getX(),(int)Size.getY());
	}

}

class Me implements KeyListener, saveFeature
{
	public Color C;
	public float X, Y;
	public int Lives = 3;
	private int score;
	public Vector2D Velocity;
	public Vector2D Location;
	float speedX, speedY, speed = .2f;
	public final Vector2D GRAVITY = new Vector2D(0,1);
	boolean up, left, right, space, ground, atLadder;
	
	//constructing you, the player
	public Me(int a, int b, Color c, int lives)
	{
		speed = .1f;
		Velocity = new Vector2D();
		Location = new Vector2D(a,b);
		Lives = lives;
		C = c;
	}
	
	public int getScore()
	{
		return this.score;
	}
	
	 public boolean updateScore(barrel b) {
	        boolean passed = false;
	        //barrel at the same (range +- 3) or below player
	        if(b.Location.getY() >= (Location.getY() + 3) && b.Location.getY() >= (Location.getY() - 3))
	            //if at the same floor as player
	            if(b.Location.getX() > Location.getX() && b.Velocity.getX() > 0) //behind the player
	                passed = true;

	            else if(b.Location.getX() < Location.getX() && b.Velocity.getX() < 0) //before the player
	                passed = true;

	        if(passed && !b.getPassed()) {//if getPassed() return false => update score else no
	            score++; //by default, increase by 1
	            return true;
	        }
	        return false;
	    }
	
	//movevment and climbing 
	private void update()
	{	
		//update the ball's current location
		Location = Location.add(Velocity.multiply(speed));
			if (Velocity.getY() < 0) //checks to see if the player is in the air
				ground = false;
			
			//Check boundaries for the ball
			if (space && ground)
				Velocity = Velocity.add(new Vector2D(0,-80));
				ground = false;
			//moving left
			if (left)
			{
				if(ground)
					Velocity = Velocity.add(new Vector2D(-2,0));					
				else
					Velocity = Velocity.add(new Vector2D(-1.5f,0));
			}
			//moving right
			if (right)
			{
				if(ground)
					Velocity = Velocity.add(new Vector2D(2,0));
				else
					Velocity = Velocity.add(new Vector2D(1.5f,0));
			}
			//checks to see if we can move up when there is a ladder nearby 
			if(up && atLadder)
			{
					Velocity = Velocity.add(new Vector2D(0,-2));
					
			}
			else
			{
				ground = true;
				atLadder = false;
				Velocity = Velocity.add(GRAVITY);
			}
			
			//if on the ground
			if(ground)
				Velocity = Velocity.multiply(.9f); //friction
			if(atLadder && !(space && up))
			{
				Velocity = Velocity.add(new Vector2D(0,0));
			}
			else
				Velocity = Velocity.add(GRAVITY);
				ground = false;
				atLadder = false;
	}
	
	//hit detection for barrel to player
	public boolean hitBarrel(Collidable c)
	{
		Ellipse2D.Float colliding = new Ellipse2D.Float((int)Location.getX()-20,(int)Location.getY()-20,40,40);
		if(colliding.intersects(c.getCollision()))
			return true;
		else
			return false;
	}
	
	//hit detection for ladder to player
	public void atLadder(Collidable c)
	{
		Ellipse2D.Float colliding = new Ellipse2D.Float((int)Location.getX()-20,(int)Location.getY()-20,40,40);
		if(colliding.intersects(c.getCollision()))
		{
			//hit on something
			Ellipse2D.Float topCollision = new Ellipse2D.Float((int)Location.getX()-10,(int)Location.getY()+15,20,5);
			if(topCollision.intersects(c.getCollision()))
			{
				//something above
				if(Velocity.getY() < 2)
				{
					atLadder = true;
				}
			}
			Ellipse2D.Float bottomCollision = new Ellipse2D.Float((int)Location.getX()-10,(int)Location.getY()+15,20,5);
			if(bottomCollision.intersects(c.getCollision()))
			{
				//standing on something
				if(Velocity.getY() > 2)
				{
					atLadder = false;
				}
			}
		}
	}
	//hit detection for walls and ground to player
	public void checkCollision(Collidable c)
	{
		Ellipse2D.Float colliding = new Ellipse2D.Float((int)Location.getX()-20,(int)Location.getY()-20,40,40);
		if(colliding.intersects(c.getCollision()))
		{
			Ellipse2D.Float bottomCollision = new Ellipse2D.Float((int)Location.getX()-10,(int)Location.getY()+15,20,5);
			if(bottomCollision.intersects(c.getCollision()))
			{
				//standing on something
				if(Velocity.getY() > 0)
				{
					Velocity.setY(0);
					ground = true;
				}
			}
			Ellipse2D.Float rightCollision = new Ellipse2D.Float((int)Location.getX()+15,(int)Location.getY()-10,5,20);
			if(rightCollision.intersects(c.getCollision()))
			{
				//right collision with wall
				if(Velocity.getX() > 0)
				{
					Velocity.setX(0);
				}
			}
			Ellipse2D.Float leftCollision = new Ellipse2D.Float((int)Location.getX()-20,(int)Location.getY()-10,5,20);
			if(leftCollision.intersects(c.getCollision()))
			{
				//left collision with wall
				if(Velocity.getX() < 0)
				{
					Velocity.setX(0);
				}
			}
		}
	}
	//draws the player
	public void draw(Graphics g)
	{
		g.setColor(C);
		drawOval(g, (int)Location.getX(), (int)Location.getY(), 25);
		update();
	}
	
	public void drawOval(Graphics g, int X, int Y, int R)
	{
		g.fillOval(X, Y, R, R);
	}
	
	//allows the usage of keyboard to do something
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			space=true;
		if(e.getKeyCode() == KeyEvent.VK_W)
			up=true;
		if (e.getKeyCode() == KeyEvent.VK_D)
			right=true;
		if (e.getKeyCode() == KeyEvent.VK_A)
			left=true;
	}
	
	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
			space=false;
		if (e.getKeyCode() == KeyEvent.VK_W)
			up=false;
		if (e.getKeyCode() == KeyEvent.VK_D)
			right=false;
		if (e.getKeyCode() == KeyEvent.VK_A)
			left=false;

	}

	public void keyTyped(KeyEvent e) {}
}

class barrel extends ScreenObj implements Collidable, saveFeature
{
	int X, Y;
	public Color C;
	private boolean passed = false;
	public Vector2D Velocity;
	public Vector2D Location;
	public Vector2D Size;
	float speed =.1f;
	boolean direction = true; //default moving to right
	public final Vector2D GRAVITY = new Vector2D(0,1);
	
	//creates barrel
	public barrel(int a, int b, Color c, int Xsize, int Ysize)
	{
		X = a;
		Y = b;
		speed = .1f;
		Velocity = new Vector2D();
		Location = new Vector2D(a,b);
		C = c;
		Size = new Vector2D(Xsize, Ysize);
		
	}
	
	public boolean getPassed() 
	{
        return this.passed;
    }
    public void updatePassed()
    {
        if(this.passed)
            this.passed = false;
        else
            this.passed = true;
    }
	
	//draws barrel
	public void draw(Graphics g)
	{
		g.setColor(C);
		drawOval(g, (int)Location.getX(), (int)Location.getY(), 25);
		update();
	}
	
	public void drawOval(Graphics g, int X, int Y, int R)
	{
		g.fillOval(X, Y, R, R);
	}
	
	//logic for the barrel
	public void update()
	{
		Location = Location.add(Velocity.multiply(speed));
		if(direction)
			Velocity = Velocity.add(new Vector2D(.125f,0));
		else
			Velocity = Velocity.add(new Vector2D(-.125f,0));
		
		Velocity = Velocity.add(GRAVITY);
		
		if(Location.getX() <= 50 && Location.getY() >= 450)
		{
			Location = new Vector2D(X,Y);
			updatePassed();
		}
	}
	
	public Rectangle getCollision()
	{
		return new Rectangle((int)Location.getX(),(int)Location.getY(),(int)Size.getX(),(int)Size.getY());
	}
	
	
	public void checkCollision(Collidable c)
	{
		Ellipse2D.Float colliding = new Ellipse2D.Float((int)Location.getX()-20,(int)Location.getY()-20,40,40);
		if(colliding.intersects(c.getCollision()))
		{
			Ellipse2D.Float bottomCollision = new Ellipse2D.Float((int)Location.getX()-10,(int)Location.getY()+15,20,5);
			if(bottomCollision.intersects(c.getCollision()))
			{
				if(Velocity.getY() > 0)
				{
				Velocity.setY(0);
				}
			}
			Ellipse2D.Float rightCollision = new Ellipse2D.Float((int)Location.getX()+15,(int)Location.getY()-10,5,20);
			if(rightCollision.intersects(c.getCollision()))
			{
				if(Velocity.getX() > 0)
				{
					Velocity.setX(0);
					Velocity = Velocity.add(new Vector2D(-.125f,0));
					direction = false;
				}
			}
			Ellipse2D.Float leftCollision = new Ellipse2D.Float((int)Location.getX()-20,(int)Location.getY()-10,5,20);
			if(leftCollision.intersects(c.getCollision()))
			{
				if(Velocity.getX() < 0)
				{
					Velocity.setX(0);
					Velocity = Velocity.add(new Vector2D(.125f,0));
					direction = true;
				}
			}
		}
	}
	
}

class Vector2D implements Serializable
{
    private float x;
    private float y;

    public Vector2D() 
    {
        this.setX(0);
        this.setY(0);
    }

    public Vector2D(float x, float y) 
    {
        this.setX(x);
        this.setY(y);
    }
    
    public Vector2D(Vector2D v) 
    {
        this.setX(v.getX());
        this.setY(v.getY());
    }

    public static double Distance(Vector2D position2, Vector2D position3) 
	{
		return Math.sqrt(Math.pow(position2.getX()-position3.getX(),2) + Math.pow(position2.getY()-position3.getY(),2));
	}

    public double Distance(Vector2D position3) 
	{
		return Math.sqrt(Math.pow(getX()-position3.getX(),2) + Math.pow(getY()-position3.getY(),2));
	}

    public void set(float x, float y) 
    {
        this.setX(x);
        this.setY(y);
    }

    public void setX(float x) 
    {
        this.x = x;
    }

    public void setY(float y) 
    {
        this.y = y;
    }

    public float getX() 
    {
        return x;
    }

    public float getY() 
    {    	
        return y;
    }
    
    public void rotate(double angle) 
    {
    	Vector2D newVect = new Vector2D(this);
		newVect.setX(getX() * (float)Math.cos(Math.toRadians(angle)) + 
				getY() * (float)Math.sin(Math.toRadians(angle)));
		newVect.setY(-getX() * (float)Math.sin(Math.toRadians(angle)) + 
				getY() * (float)Math.cos(Math.toRadians(angle)));
		this.set(newVect.getX(),newVect.getY());
    }

    //U x V = Ux*Vy-Uy*Vx
    public static float Cross(Vector2D U, Vector2D V)
    {
    	return U.x * V.y - U.y * V.x;
    }
    
    public float dot(Vector2D v2) 
    {
    	float result = 0.0f;
        result = this.getX() * v2.getX() + this.getY() * v2.getY();
        return result;
    }

    public float getLength() 
    {
        return (float) Math.sqrt(getX() * getX() + getY() * getY());
    }

    public Vector2D add(Vector2D v2) 
    {
        Vector2D result = new Vector2D();
        result.setX(getX() + v2.getX());
        result.setY(getY() + v2.getY());
        return result;
    }
    
    public Vector2D subtract(Vector2D v2) 
    {
        Vector2D result = new Vector2D();
        result.setX(this.getX() - v2.getX());
        result.setY(this.getY() - v2.getY());
        return result;
    }
    
    public Vector2D multiply(float scaleFactor) 
    {
        Vector2D result = new Vector2D();
        result.setX(this.getX() * scaleFactor);
        result.setY(this.getY() * scaleFactor);
        return result;
    }
    
    //Specialty method used during calculations of ball to ball collisions.
    public Vector2D normalize() 
    {
    	float length = getLength();
        if (length != 0.0f) 
        {
            this.setX(this.getX() / length);
            this.setY(this.getY() / length);
        } 
        else 
        {
            this.setX(0.0f);
            this.setY(0.0f);
        }
        return this;
    }
    public String toString()
    {
    	return "("+x+", "+y+")";
    }
}