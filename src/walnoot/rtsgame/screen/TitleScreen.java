package walnoot.rtsgame.screen;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import walnoot.rtsgame.Images;
import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.RTSComponent;
import walnoot.rtsgame.screen.menus.MainMenu;

//laat het logo paar seconden zien, switcht daarna naar menu
public class TitleScreen extends Screen {
	private int totalTime;
	private BufferedImage grass;
	private BufferedImage grass2;
	private BufferedImage logo;
	private float logoTransparancy = 0.2f; //hoe transparant het logo is, max. 1.0
	private boolean[][] tiles = new boolean[20][20];
	
	public TitleScreen(RTSComponent game, InputHandler input){
		super(game, input);
		try{
			//grass = ImageIO.read(this.getClass().getResource("/grass.png"));
			//grass2 = ImageIO.read(this.getClass().getResource("/grass2.png"));
			grass2 = Images.terrain[0][0];
			grass = Images.terrain[1][0];
			logo = ImageIO.read(this.getClass().getResource("/res/logo2.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		Random random = new Random();
		
		for(int x = 0; x < 20; x++){
			for(int y = 0; y < 20; y++){
				boolean b = random.nextBoolean();
				tiles[x][y] = b;
			}
		}
	}
	
	public void render(Graphics g){
		for(int x = 0; x < tiles.length; x++){
			for(int y = 0; y < tiles[x].length; y++){
				if(tiles[x][y]){
					g.drawImage(grass, x * 32, y * 16, null);
					g.drawImage(grass, x * 32 - 16, y * 16 - 8, null);
				}else{
					g.drawImage(grass2, x * 32, y * 16, null);
					g.drawImage(grass2, x * 32 - 16, y * 16 - 8, null);
				}
			}
			
		}
		super.makeTransparant(g, logoTransparancy);
		
		g.drawImage(logo, (super.component.getWidth() / RTSComponent.SCALE - logo.getWidth()) / 2, 30, null);
		
		//g.drawString(""+totalTime, 30, 30);
	}
	
	public void update(){
		totalTime++;
		if(totalTime > 200 && !(component.getScreen() instanceof MainMenu)) setScreen(new MainMenu(component, this, input));
		
		boolean switchScreen = false;
		
		if(logoTransparancy < 1.0f){
			logoTransparancy += (float) RTSComponent.MS_PER_TICK / 2000;
		}else{
			logoTransparancy = 1.0f;
			switchScreen = true;
		}
		
		if(switchScreen){
			if(!(component.getScreen() instanceof MainMenu)) setScreen(new MainMenu(component, this, input));
		}
	}
}
