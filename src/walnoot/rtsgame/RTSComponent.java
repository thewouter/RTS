package walnoot.rtsgame;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;

import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.Screen;

public class RTSComponent extends Canvas implements Runnable {
	private static final long serialVersionUID = -1401470770875975471L;

	public static final double MS_PER_TICK = 1000.0 / 60.0;
	
	private Screen screen;
	private boolean running = true;
	public static final int SCALE = 2;
	private FullScreenManager fullScreenManager;
	private InputHandler input;
	private Container container;
	private long fps;
	private BufferedImage screenImage;
	
	public RTSComponent(Container container){
		this.container = container;
		fullScreenManager = new FullScreenManager(this, container);
		
		input = InputHandler.getInputHandler(this);
		
		screen = new GameScreen(this, input);
		
		setIgnoreRepaint(true);
		
		if(!new File(Options.fileName).exists()) Options.writeOptions();
		Options.loadOptions();
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
		
		if(Options.startFullScreen) fullScreenManager.setFullScreen();
		
		while(running){
			long timePassed = System.nanoTime() - totalTime; //hoeveel tijd er verstreken is sinds de vorige update
			totalTime += timePassed;
			tickTime += timePassed / 1000000.0;
			boolean shouldRender = true;
			
			while(tickTime > MS_PER_TICK){
				tickTime -= MS_PER_TICK;
				shouldRender = true;
				
				numTicks++;
				
				screen.update();
				input.update();
				
				if(input.escape.isPressed()) stop();
				if(input.fullScreen.isTapped()) fullScreenManager.switchFullScreen();
				
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
	}
	
	public Screen getScreen(){
		return screen;
	}
	
	public void stop(){
		running = false;
		
		Options.window_width = container.getSize().width;
		Options.window_height = container.getSize().height;
		Options.startFullScreen = fullScreenManager.isFullScreen();
		
		Options.writeOptions();
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
			}catch(InterruptedException e){
			}
		}
	}
}