package sokoban;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import gfx.Assets;
import input.KeyBoard;
import main.Window;
import states.LevelSelectorState;
import states.State;
import ui.Button;
import ui.Click;

public class Level {
	
	public static final int TILESIZE = 48;
	
	private int[][] maze;
	
	private int[][] copy;
	
	private int player_row, player_col;
	
	private Image texture;
	
	private int xOffset, yOffset;
	
	private long time, lastTime;
	
	private final int DELAY = 150;
	
	private Button restart, back, special, showTime;
	

	
	private boolean solved;
	
	private int plaStartRow, plaStartCol;
	
	private LevelSelectorState levelSelectorState;
	
	
	public static int ID = 0;
	
	public boolean useSpecialPower = false;
	public boolean takenSpecialPower = false;
	private int id;
	
	int powerTime = 10;
	Timer timer;
	
	public Level(int[][] maze, int player_row, int player_col, LevelSelectorState levelSelectorState){
		this.levelSelectorState = levelSelectorState;
		this.maze = maze;
		ID ++;
		id = ID;
		
		copy = new int[maze.length][maze[0].length];
		for(int row = 0; row < maze.length; row++){
			for(int col = 0; col < maze[row].length; col ++)
				copy[row][col] = maze[row][col];
		plaStartRow = player_row;
		plaStartCol = player_col;
		this.player_row = player_row;
		this.player_col = player_col;
		if(ID == 1)
			solved = true;
		else
			solved = false;
		xOffset = (Window.WIDTH - maze[0].length*TILESIZE)/2;
		yOffset = (Window.HEIGHT - maze.length*TILESIZE)/2;
		texture = Assets.PlayerFront;
		restart = new Button("RESTART", 100, Window.HEIGHT/2, new Click(){

			@Override
			public void onClick() {
				resetPower();
				reset();
				
			}},
				Assets.font30);
		back = new Button("BACK", Window.WIDTH - 100, Window.HEIGHT/2, new Click(){

			@Override
			public void onClick() {
				
				State.currentState = levelSelectorState;
				
			}},
				Assets.font30);
		
		special = new Button("TAKE POWER", Window.WIDTH - 100,  25 , new Click(){

			@Override
			public void onClick() {
				if(!takenSpecialPower) {
					takenSpecialPower = true;
					useSpecialPower = true;
					scheduleTimerForSpecialPower();
				}
				
			}},
				Assets.font22);
		
		showTime = new Button("", Window.WIDTH / 2,  25 , new Click(){

			@Override
			public void onClick() {
				
				
			}},
				Assets.font22, false);
		
		time = 0;
		lastTime = System.currentTimeMillis();
		
		}
	}
	
	public void resetPower() {
		useSpecialPower = false;
		takenSpecialPower = false;
		powerTime = 10;
		setSpecialButtonHovering(true);
		setShowTimeButtonText("");
	}
	private void scheduleTimerForSpecialPower(){
        timer = new Timer();
        int begin = 0;
        int timeInterval = 1000;
       
        setSpecialButtonText("RUNNING");
        setSpecialButtonHovering(false);
        setShowTimeButtonText(String.valueOf(powerTime));
        
        useSpecialPower = true;
        timer.schedule(new TimerTask() {
            int counter = 10;
            
            @Override
            public void run() {
                //call the method
                counter--;
                powerTime--;
                if (powerTime < 0){
                   useSpecialPower = false;
                    setSpecialButtonText("POWER TAKEN");
                    setSpecialButtonHovering(false);
                    setShowTimeButtonText("");
                    checkPlayerPosition();
                    timer.cancel();
                }
                else {
                	setShowTimeButtonText(String.valueOf(powerTime));
                }
              
//                updateLocation();
            }
        }, begin, timeInterval);

    }
	private void checkPlayerPosition() {
		if(maze[player_row][player_col] == 1 ||
				maze[player_row][player_col] == 2 ||
				maze[player_row ][player_col] == 4) {
		
			setShowTimeButtonText("STAYING WRONG POSITION");
		}
	}
	
	private void setSpecialButtonText(String text) {
		special.setText(text);
	}
	private void setSpecialButtonHovering(boolean isEnable) {
		special.setHoveringEnable(isEnable);
	}
	private void setShowTimeButtonText(String text) {
		showTime.setText(text);
	}
	
	private void reset(){
		for(int row = 0; row < maze.length; row++)
			for(int col = 0; col < maze[row].length; col ++)
				maze[row][col] = copy[row][col];
		
		player_row = plaStartRow;
		player_col = plaStartCol;
		texture = Assets.PlayerFront;
	}
	
	
	public void update(){
		time += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
		
		if(KeyBoard.UP && time > DELAY){
			move(-1, 0);
			texture = Assets.playerBack;
		}
		if(KeyBoard.LEFT && time > DELAY){
			move(0, -1);
			texture = Assets.playerLeft;
		}
		if(KeyBoard.DOWN && time > DELAY){
			move(1, 0);
			texture = Assets.PlayerFront;
		}
		if(KeyBoard.RIGHT && time > DELAY){
			move(0, 1);
			texture = Assets.playerRight;
		}
		
		
		
		restart.update();
		back.update();
		special.update();
		showTime.update();
		// check answer
		
		for(int row = 0; row < maze.length; row++)
			for(int col = 0; col < maze[row].length; col ++)
				if(maze[row][col] == 2)
					return;
		
		levelSelectorState.getLevels()[id].setSolved(true);
		State.currentState = levelSelectorState;
		
	}
	
	private void move(int row, int col){
		
		
		if(player_row + row < maze.length && player_row + row >= 0 &&
				player_col + col < maze[0].length && player_col + col >= 0) {
			
			if(!useSpecialPower) {
				if(maze[player_row][player_col] == 1 ||
						maze[player_row][player_col] == 2 ||
						maze[player_row ][player_col] == 4) {
				resetPower();
				reset();
				State.currentState = levelSelectorState;
				}
			}
			
			
			if(maze[player_row + row][player_col + col] != 1){
				if(maze[player_row + row][player_col + col] == 2 || maze[player_row + row][player_col + col] == 4){
					if(maze[player_row + row*2][player_col + col*2] == 1 ||
							maze[player_row + row*2][player_col + col*2] == 2 ||
							maze[player_row + row*2][player_col + col*2] == 4) {
						
						if(useSpecialPower) {
							player_row += row;
							player_col += col;
						}
						
						return;
					}
						
					if(maze[player_row + row][player_col + col] == 4){
						maze[player_row + row][player_col + col] = 3;			
						if(maze[player_row + row*2][player_col + col*2] == 3)
							maze[player_row + row*2][player_col + col*2] = 4;
						else
							maze[player_row + row*2][player_col + col*2] = 2;
					}else{
						maze[player_row + row][player_col + col] = 0;
						if(maze[player_row + row*2][player_col + col*2] == 3)
							maze[player_row + row*2][player_col + col*2] = 4;
						else
							maze[player_row + row*2][player_col + col*2] = 2;
						
					}
					
					
				}
				player_row += row;
				player_col += col;
			}
			else {
				if(useSpecialPower) {
					player_row += row;
					player_col += col;
				}
				else {
					
					
				}
				
			}
		}
		
		time = 0;
	}
	
	public void render(Graphics g){
		
		
		
		restart.render(g, 100, Window.HEIGHT / 2);
		back.render(g, Window.WIDTH - 100, Window.HEIGHT / 2);
		special.render(g, Window.WIDTH - 100,  25);
		showTime.render(g, Window.WIDTH / 2,  25 );
		
		xOffset = (Window.WIDTH - maze[0].length*TILESIZE)/2;
		yOffset = (Window.HEIGHT - maze.length*TILESIZE)/2;
		for(int row = 0; row < maze.length; row++){
			for(int col = 0; col < maze[row].length; col ++){
				g.drawImage(Assets.floor, xOffset + col*TILESIZE, yOffset + row*TILESIZE, null);
				if(maze[row][col] == 1)
					g.drawImage(Assets.wall, xOffset + col*TILESIZE, yOffset + row*TILESIZE, null);
				if(maze[row][col] == 2)
					g.drawImage(Assets.boxOff, xOffset + col*TILESIZE, yOffset + row*TILESIZE, null);
				if(maze[row][col] == 3)
					g.drawImage(Assets.spot, xOffset + col*TILESIZE, yOffset + row*TILESIZE, null);
				if(maze[row][col] == 4)
					g.drawImage(Assets.boxOn, xOffset + col*TILESIZE, yOffset + row*TILESIZE, null);
			}
		}
		
		g.drawImage(texture, xOffset + player_col*TILESIZE, yOffset + player_row*TILESIZE, null);
		
		
	}
	
	public boolean isSolved(){return solved;}
	public void setSolved(boolean bool){solved = bool;}
}
