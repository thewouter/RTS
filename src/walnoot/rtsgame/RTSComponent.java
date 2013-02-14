package walnoot.rtsgame;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import javax.swing.JFrame;

import walnoot.rtsgame.multiplayer.host.MPHost;
import walnoot.rtsgame.popups.screenpopup.ScreenPopup;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupButton;
import walnoot.rtsgame.popups.screenpopup.ScreenPopupTextField;
import walnoot.rtsgame.popups.screenpopup.TextInput;
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
		
		loadBackgroundMusic();
		
		
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
		if(titleScreen == null) {
			titleScreen = new TitleScreen(this, input);
		}
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
	
	private void loadBackgroundMusic(){
		final ArrayList<String> fileNames = new ArrayList<String>();
		
		Path startDir = Paths.get("src/res/Sounds/Background music");
		String pattern = "*.mp3";
		FileSystem fs = FileSystems.getDefault();
		
		final PathMatcher matcher = fs.getPathMatcher("glob:" + pattern);
		
		FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
		    @Override
		    public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
		        Path name = file.getFileName();
		        if (matcher.matches(name)) {
		            fileNames.add(file.toString());
		        }
		        return FileVisitResult.CONTINUE;
		    }
		};
		try {
			Files.walkFileTree(startDir, matcherVisitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		backgroundSound = new Sound(fileNames);
		
	}
	
	public boolean isMember(String name){
		try {
			URL source = new URL("http://www.tribe.net84.net/users.nothin"); //super secret database of registered users!
			InputStreamReader input = new InputStreamReader(source.openStream());
			BufferedReader in = new BufferedReader(input);
			String inputLine;
				while((inputLine = in.readLine())!= null){
					if(name.equals(inputLine)) return true;
					System.out.println(inputLine);
				}
		} catch (FileNotFoundException e) {
			return true;
		} catch (MalformedURLException e) {
			return true;
		} catch (IOException e) {
			return true;
		}
		
		return false;
	}
}