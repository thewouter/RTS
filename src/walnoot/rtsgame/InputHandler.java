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

	public Key left = new Key("", KeyEvent.VK_LEFT, KeyEvent.VK_A);
	public Key fullScreen = new Key("", KeyEvent.VK_F11);
	public Key right = new Key("", KeyEvent.VK_RIGHT, KeyEvent.VK_D);
	public Key up = new Key("", KeyEvent.VK_UP, KeyEvent.VK_W);
	public Key down = new Key("", KeyEvent.VK_DOWN, KeyEvent.VK_S);
	public Key use = new Key("", KeyEvent.VK_ENTER, KeyEvent.VK_E);
	public Key enter = new Key("", KeyEvent.VK_ENTER);
	public Key space = new Key(" ", KeyEvent.VK_SPACE);
	public Key escape = new Key("", KeyEvent.VK_ESCAPE);
	public Key increase = new Key("", KeyEvent.VK_PAGE_UP, KeyEvent.VK_E);
	public Key decrease = new Key("", KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_Q);
	public Key debug = new Key("", KeyEvent.VK_F3);
	public Key a = new Key ("a", KeyEvent.VK_A);
	public Key b = new Key ("b", KeyEvent.VK_B);
	public Key c = new Key ("c", KeyEvent.VK_C);
	public Key d = new Key ("d", KeyEvent.VK_D);
	public Key e = new Key ("e", KeyEvent.VK_E);
	public Key f = new Key ("f", KeyEvent.VK_F);
	public Key g = new Key ("g", KeyEvent.VK_G);
	public Key h = new Key ("h", KeyEvent.VK_H);
	public Key i = new Key ("i", KeyEvent.VK_I);
	public Key j = new Key ("j", KeyEvent.VK_J);
	public Key k = new Key ("k", KeyEvent.VK_K);
	public Key l = new Key ("l", KeyEvent.VK_L);
	public Key m = new Key ("m", KeyEvent.VK_M);
	public Key n = new Key ("n", KeyEvent.VK_N);
	public Key o = new Key ("o", KeyEvent.VK_O);
	public Key p = new Key ("p", KeyEvent.VK_P);
	public Key q = new Key ("q", KeyEvent.VK_Q);
	public Key r = new Key ("r", KeyEvent.VK_R);
	public Key s = new Key ("s", KeyEvent.VK_S);
	public Key t = new Key ("t", KeyEvent.VK_T);
	public Key u = new Key ("u", KeyEvent.VK_U);
	public Key v = new Key ("v", KeyEvent.VK_V);
	public Key w = new Key ("w", KeyEvent.VK_W);
	public Key x = new Key ("x", KeyEvent.VK_X);
	public Key y = new Key ("y", KeyEvent.VK_Y);
	public Key z = new Key ("z", KeyEvent.VK_Z);
	public Key n1 = new Key ("1", KeyEvent.VK_1, KeyEvent.VK_NUMPAD1);
	public Key n2 = new Key ("2", KeyEvent.VK_2, KeyEvent.VK_NUMPAD2);
	public Key n3 = new Key ("3", KeyEvent.VK_3, KeyEvent.VK_NUMPAD3);
	public Key n4 = new Key ("4", KeyEvent.VK_4, KeyEvent.VK_NUMPAD4);
	public Key n5 = new Key ("5", KeyEvent.VK_5, KeyEvent.VK_NUMPAD5);
	public Key n6 = new Key ("6", KeyEvent.VK_6, KeyEvent.VK_NUMPAD6);
	public Key n7 = new Key ("7", KeyEvent.VK_7, KeyEvent.VK_NUMPAD7);
	public Key n8 = new Key ("8", KeyEvent.VK_8, KeyEvent.VK_NUMPAD8);
	public Key n9 = new Key ("9", KeyEvent.VK_9, KeyEvent.VK_NUMPAD9);
	public Key n0 = new Key ("0", KeyEvent.VK_0, KeyEvent.VK_NUMPAD0);
	public Key backspace = new Key ("backspace", KeyEvent.VK_BACK_SPACE);
	public Key dot = new Key (".", KeyEvent.VK_PERIOD);
	
	private boolean LMBDown = false, LMBWasDown = false;
	private boolean RMBDown = false, RMBWasDown = false;
	private boolean isDragging = false;
	public int mouseX, mouseY, mouseWheelChange, mouseXOnClick, mouseYOnClick;
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
	
	public boolean isDragging(){
		return isDragging;
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
		isDragging = false;
	}
	
	public void mouseDragged(MouseEvent e){
		mouseX = e.getX() / RTSComponent.SCALE;
		mouseY = e.getY() / RTSComponent.SCALE;
		if(!isDragging){
			mouseXOnClick = mouseX;
			mouseYOnClick = mouseY;
		}
		if(LMBDown)isDragging = true;
		
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
		private boolean pressed = false, wasPressed = false;
		private int timePressed = 0;
		private String chars;
		
		public Key(String chars,int...keys){
			this.chars = chars;
			this.keys = keys;
			keylist.add(this);
		}
		
		public void update(){
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
			return pressed && !wasPressed;
		}
		
		public boolean isPressed(){
			return pressed;
		}
		
		public String getChars(){
			return chars;
		}
	}
}
