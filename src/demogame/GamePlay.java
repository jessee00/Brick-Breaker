package demogame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePlay extends JPanel implements ActionListener, KeyListener
{
	private boolean play = false;
	private int score = 0;
	private int totalBricks = 21;
	private Timer timer;
	private int delay = 8;
	private int ballposX = 120;
	private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    private int playerX = 350;
    private MapGenerator map;
    
    public GamePlay()
    {
    	addKeyListener(this);
    	setFocusable(true);
    	setFocusTraversalKeysEnabled(true);
    	
    	timer = new Timer(delay,this);
    	timer.start();
    	
    	map= new MapGenerator(3,7);
    }
    
    public void paint(Graphics g)
    {
    	//black canvas
    	g.setColor(Color.BLACK);
    	g.fillRect(0, 0, 692, 592);
    	
    	//border 
    	g.setColor(Color.YELLOW);
    	g.fillRect(0, 0, 692, 3);
    
    		
    	//paddle
    	g.setColor(Color.GREEN);
    	g.fillRect(playerX, 550, 100, 8);
    	
    	//bricks
    	map.draw((Graphics2D)g);
    	
    	//ball
    	g.setColor(Color.WHITE);
    	g.fillOval(ballposX, ballposY, 20, 20);
    	
    	//score
    	g.setColor(Color.cyan);
    	g.setFont(new Font ("serif", Font.BOLD, 20));
    	g.drawString("Score : "+score, 550, 30);
    	
    	//game over
    	if(ballposY>=570)
    	{
    		play = false;
    		ballXdir = 0;
    		ballYdir = 0;
    				
    		g.setColor(Color.YELLOW);
    		g.setFont( new Font("sherif", Font.BOLD, 30));
    		g.drawString("Game Over", 250, 300);
    		
    		g.setFont( new Font("sherif", Font.BOLD, 30));
    		g.drawString("Press Enter to Restart!!", 200, 350);
    	}
    	
    	//player won
    	if(totalBricks <= 0)
    	{
    		play = false;
    		ballXdir = 0;
    		ballYdir = 0;
    				
    		g.setColor(Color.YELLOW);
    		g.setFont( new Font("sherif", Font.BOLD, 30));
    		g.drawString("You Won!!", 250, 300);
    		
    		g.setFont( new Font("sherif", Font.BOLD, 30));
    		g.drawString("Press Enter to Restart!!", 200, 350);
    	}
    }

    private void moveLeft() {
    	play=true;
    	playerX-=20;
    }
    
    private void moveRight() {
    	play=true;
    	playerX+=20;
    }
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==KeyEvent.VK_LEFT)
		{
			if(playerX<=0) playerX=0;
			else moveLeft();
		}
			
			

		if(e.getKeyCode()==KeyEvent.VK_RIGHT) 
		{
			if(playerX >= 600) playerX=600;
			else moveRight();	
		}
		
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
		{
			if(!play) 
			{
				score=0;
				totalBricks=21;
				ballposX=120;
				ballposY=350;
				ballXdir=-1;
				ballYdir=-2;
				playerX=320;
				
				map=new MapGenerator(3,7);
			}
		}
		
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(play)
		{
			if(ballposX <= 0) ballXdir = -ballXdir;
			if(ballposX >=670) ballXdir = -ballXdir;
			
			if(ballposY <= 0) ballYdir = -ballYdir;	
			
			Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
			Rectangle paddleRect = new Rectangle(playerX, 550, 100, 8);
			
			if(ballRect.intersects(paddleRect)) ballYdir = -ballYdir;
			
			
			for(int i=0; i<map.map.length; i++)
			{
				for(int j=0; j<map.map[0].length; j++) 
				{
					if(map.map[i][j]>0)
					{
						int width=map.brickWidth;
						int height=map.brickHeight;
						int brickposX = 80+j*width;
						int brickposY = 50+i*height;
						
						Rectangle brickRect = new Rectangle(brickposX, brickposY, width, height);
						
						if(ballRect.intersects(brickRect))
						{
							map.setBrick(0, i, j);
							totalBricks--;
							score += 10;
							
							if(ballposX+19 <= brickposX || ballposX+1 >= brickposX+width) {
								ballXdir = -ballXdir;
							}
							else ballYdir = - ballYdir;
							
							break;
						}
					}
				}
			}
			
			ballposX += ballXdir;
			ballposY += ballYdir;
		}	
		repaint();
	}
	
	

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	
	}


}
