package walnoot.rtsgame;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import walnoot.rtsgame.multiplayer.host.MPHost;
import walnoot.rtsgame.rest.Options;
import walnoot.rtsgame.rest.Sound;
import walnoot.rtsgame.screen.SPGameScreen;
import walnoot.rtsgame.screen.Screen;
import walnoot.rtsgame.screen.TitleScreen;

public class RTSComponent extends Canvas implements Runnable {
	private static final long serialVersionUID = -1401470770875975471L;

	public static final double MS_PER_TICK = 1000.0 / 60.0;
	
	private Screen screen;
	private SPGameScreen gameScreen;
	private TitleScreen titleScreen;
	boolean running = true;
	public static final int SCALE = 2;
	private FullScreenManager fullScreenManager;
	private InputHandler input;
	private Container container;
	long fps;
	private BufferedImage screenImage;
	private Sound backgroundSound;
	public boolean backgroundSoundOn = false;
	
	public RTSComponent(Container container){
		this.container = container;
		fullScreenManager = new FullScreenManager(this, container);
		backgroundSound = new Sound("/res/Sounds/risingsun.wav");
		
		input = InputHandler.getInputHandler(this);
		
		setTitleScreen();
		
		setIgnoreRepaint(true);
		
		//if(!new File(Options.fileName).exists()) Options.writeOptions();
		
		//Options.loadOptions();
		
		
	}
	
	public void render(){
		if(screenImage.getWidth() != getWidth() / SCALE) screenImage = new BufferedImage(getWidth() / SCALE, getHeight() / SCALE, BufferedImage.TYPE_INT_RGB);
		if(screenImage.getHeight() != getHeight() / SCALE) screenImage = new BufferedImage(getWidth() / SCALE, getHeight() / SCALE, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g = (Graphics2D) screenImage.getGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		screen.render(g);
		
		
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(2);
			return;
		}
		
		Graphics graphics = bs.getDrawGraphics();
		graphics.drawImage(screenImage, 0, 0, screenImage.getWidth() * SCALE, screenImage.getHeight() * SCALE, null);
		bs.show();
	}
	
	public void run(){
		requestFocus();
		
		screenImage = new BufferedImage(getWidth() / SCALE, getHeight() / SCALE, BufferedImage.TYPE_INT_RGB);
		
		long startTime = System.nanoTime();
		long totalTime = startTime;
		double tickTime = 0;
		
		int numTicks = 0, numFrames = 0;

		//if(Options.SOUND_ON)amplifybackground();
		
		//if(Options.startFullScreen) fullScreenManager.setFullScreen();

		//fullScreenManager.setFullScreen();
		
		while(running){
			long timePassed = System.nanoTime() - totalTime; //hoeveel tijd er verstreken is sinds de vorige update
			totalTime += timePassed;
			tickTime += timePassed / 1000000.0;
			boolean shouldRender = true;
			
			while(tickTime > MS_PER_TICK){
				tickTime -= MS_PER_TICK;
				shouldRender = true;
				
				numTicks++;
				
				if(input.fullScreen.isTapped())fullScreenManager.switchFullScreen();
				
				screen.update();
				input.update();
				
			}
			
			if(isShowing() && shouldRender){
				render();
				numFrames++;
			}
			
			if(numTicks >= 60){
				fps = numFrames;
				
				numTicks = 0;
				numFrames = 0;
			}
			
			fullScreenManager.update();
		}
		System.exit(-1); //stopt het hele programma
	}
	
	public void setScreen(Screen s){
		screen = s;
		if(s instanceof SPGameScreen && backgroundSoundOn){
			backgroundSound.play();
		}else{
			backgroundSound.stop();
		}
	}
	
	public void setGameScreen(boolean newScreen){
		if(gameScreen == null || newScreen == true) {
			gameScreen = null;
			gameScreen = new SPGameScreen(this, input);
		}
		setScreen(gameScreen);
		
		if(backgroundSoundOn) backgroundSound.loop();
	}
	
	public void setTitleScreen(){
		if(titleScreen == null) titleScreen = new TitleScreen(this, input);
		setScreen(titleScreen);
		backgroundSound.stop();
	}
	
	public void setHostedGame(int port){
		screen = new MPHost(this, input, port);
	}
	
	public Screen getScreen(){
		return screen;
	}
	
	public void muteBackground(){
		backgroundSound.stop();
		backgroundSoundOn = false;
	}
	
	public void amplifybackground(){
		if(!backgroundSoundOn){
			backgroundSoundOn = true;
			backgroundSound.play();
		}
	}
	
	public void stop(){
		running = false;
		
		//Options.window_width = container.getSize().width;
		//Options.window_height = container.getSize().height;
		//Options.startFullScreen = fullScreenManager.isFullScreen();
		//Options.SOUND_ON = backgroundSoundOn;
		
		//Options.writeOptions();
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame("Tribe");
		RTSComponent comp = new RTSComponent(frame);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(Options.window_width, Options.window_height);
		frame.setLayout(new BorderLayout(0, 0));
		frame.add(comp, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		new Thread(comp, "Tribe main").start();
		while(comp.running){
			frame.setTitle("Tribe (" + comp.fps + ")");
			try{
				Thread.sleep(500L);
			}catch(InterruptedException e){}
		}
	}
}