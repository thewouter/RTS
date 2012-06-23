package walnoot.rtsgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	private ArrayList<Key> keylist = new ArrayList<Key>();

	public Key left = new Key(KeyEvent.VK_LEFT, KeyEvent.VK_A);
	public Key fullScreen = new Key(KeyEvent.VK_F11);
	public Key right = new Key(KeyEvent.VK_RIGHT, KeyEvent.VK_D);
	public Key up = new Key(KeyEvent.VK_UP, KeyEvent.VK_W);
	public Key down = new Key(KeyEvent.VK_DOWN, KeyEvent.VK_S);
	public Key use = new Key(KeyEvent.VK_ENTER, KeyEvent.VK_E);
	public Key enter = new Key(KeyEvent.VK_ENTER);
	public Key space = new Key(KeyEvent.VK_SPACE);
	public Key escape = new Key(KeyEvent.VK_ESCAPE);
	public Key increase = new Key(KeyEvent.VK_PAGE_UP, KeyEvent.VK_E);
	public Key decrease = new Key(KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_Q);
	public Key debug = new Key(KeyEvent.VK_F3);
	
	private boolean LMBDown = false, LMBWasDown = false;
	private boolean RMBDown = false, RMBWasDown = false;
	private int mouseX, mouseY, mouseWheelChange;
	private char charTyped;
	
	private InputHandler(){
		//Only InputHandler can initialize an InputHandler object
	}
	
	public void update(){
		LMBWasDown = LMBDown;
		RMBWasDown = RMBDown;
		mouseWheelChange = 0;
		
		for(Key key: keylist){
			key.update();
		}
		
		charTyped = KeyEvent.CHAR_UNDEFINED;
	}
	
	public boolean typedChar(){
		return charTyped != KeyEvent.CHAR_UNDEFINED;
	}
	
	public char getCharTyped(){
		return charTyped;
	}
	
	public boolean LMBDown(){
		return LMBDown;
	}
	
	public boolean LMBTapped(){
		return LMBDown && !LMBWasDown;
	}
	
	public boolean RMBDown(){
		return RMBDown;
	}
	
	public boolean RMBTapped(){
		return RMBDown && !RMBWasDown;
	}
	
	public int getMouseX(){		//coordinats in mapcoordinats!
		return mouseX;
	}
	
	public int getMouseY(){		//coordinats in mapcoordinats!
		return mouseY;
	}
	
	public int getMouseWheelChange(){
		return mouseWheelChange;
	}
	
	public void mouseClicked(MouseEvent e){
	}
	
	public void mouseEntered(MouseEvent e){
	}
	
	public void mouseExited(MouseEvent e){
	}
	
	public void mousePressed(MouseEvent e){
		if(e.getButton() == MouseEvent.BUTTON1) LMBDown = true;
		else RMBDown = true;
	}
	
	public void mouseReleased(MouseEvent e){
		if(e.getButton() == MouseEvent.BUTTON1) LMBDown = false;
		else RMBDown = false;
	}
	
	public void mouseDragged(MouseEvent e){
		mouseX = e.getX() / RTSComponent.SCALE;
		mouseY = e.getY() / RTSComponent.SCALE;
	}
	
	public void mouseMoved(MouseEvent e){
		mouseX = e.getX() / RTSComponent.SCALE;
		mouseY = e.getY() / RTSComponent.SCALE;
	}
	
	public void mouseWheelMoved(MouseWheelEvent e){
		mouseWheelChange += e.getWheelRotation();
	}
	
	public void keyPressed(KeyEvent e){
		for(Key key: keylist){
			key.press(e.getKeyCode());
		}
		
		if(e.getKeyChar() != KeyEvent.CHAR_UNDEFINED) charTyped = e.getKeyChar();
	}
	
	public void keyReleased(KeyEvent e){
		for(Key key: keylist){
			key.release(e.getKeyCode());
		}
	}
	
	public void keyTyped(KeyEvent e){
	}
	
	public static InputHandler getInputHandler(RTSComponent comp){
		InputHandler input = new InputHandler();
		
		comp.addKeyListener(input);
		comp.addMouseListener(input);
		comp.addMouseMotionListener(input);
		comp.addMouseWheelListener(input);
		
		return input;
	}
	
	public class Key {
		private final int[] keys;
		private boolean pressed = false, wasPressed = false, wasWasPressed;
		private int timePressed = 0;
		
		public Key(int...keys){
			this.keys = keys;
			keylist.add(this);
		}
		
		public void update(){
			wasWasPressed = wasPressed;
			wasPressed = pressed;
			
			if(pressed) timePressed++;
			else timePressed = 0;
		}
		
		public void press(int key){
			if(has(key)) pressed = true;
		}
		
		public void release(int key){
			if(has(key)) pressed = false;
		}
		
		/**
		 * @return for how long the key has been pressed, in ticks
		 */
		public double getTimePressed(){
			return timePressed;
		}
		
		public boolean has(int key){
			for(int i = 0; i < keys.length; i++){
				if(keys[i] == key) return true;
			}
			return false;
		}
		
		public boolean isTapped(){
			return pressed && !wasWasPressed;
		}
		
		public boolean isPressed(){
			return pressed;
		}
	}
}
