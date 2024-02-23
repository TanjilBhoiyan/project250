package states;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import gfx.Assets;
import gfx.Text;
import main.Window;
import ui.Button;
import ui.Click;

public class MenuState extends State{
	
	private Button button;
	
	private ArrayList<Button> buttons = new ArrayList<Button>();
	
	public MenuState(Window window){
		super(window);
		int width = window.getContentPane().getWidth();
		int height = window.getContentPane().getHeight();
		
		buttons.add(new Button("PLAY", width/2, height/2 - 50, new Click(){

			@Override
			public void onClick() {
				State.currentState = window.getLevelSelectorState();
			}}, Assets.font48));
		buttons.add(new Button("CREDITS", width/2, height/2 + 50, new Click(){

			@Override
			public void onClick() {
				State.currentState = window.getLevelSelectorState();
			}}, Assets.font48));
		buttons.add(new Button("EXIT", width/2, height/2 + 150, new Click(){

			@Override
			public void onClick() {
				System.exit(1);
			}}, Assets.font48));
	}
	
	@Override
	public void update() {
		for(int i = 0; i < buttons.size(); i++)
			buttons.get(i).update();
	}

	@Override
	public void render(Graphics g) {
		int width = window.getContentPane().getWidth();
		int height = window.getContentPane().getHeight();
		g.setFont(Assets.font48);
		Text.drawString(g, "SOKOBAN", width/2, height/2 - 200, true, Color.DARK_GRAY);
		
		buttons.get(0).render(g, width/2, height/2 - 50);
		buttons.get(1).render(g, width/2, height/2 + 50);
		buttons.get(2).render(g, width/2, height/2 + 150);
//		for(int i = 0; i < buttons.size(); i++)
//			buttons.get(i).render(g);
	}

}
