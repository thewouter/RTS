package walnoot.rtsgame.multiplayer.client;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import walnoot.rtsgame.InputHandler.Key;
import walnoot.rtsgame.rest.RTSFont;
import walnoot.rtsgame.screen.GameScreen;
import walnoot.rtsgame.screen.MPGameScreen;
import walnoot.rtsgame.screen.Screen;

public class Chat {
	public static int MAX_LINE_WIDTH = 100;
	public static int X_POS = 0, Y_POS_FROM_BOTTOM = 100;
	private ArrayList<Message> messages = new ArrayList<>();
	private GameScreen screen;
	private ArrayList<Message> messagesToRemove = new ArrayList<>();
	private LinkedList<String> text = new LinkedList<>();
	private boolean isSelected = false;
	
	public Chat(GameScreen screen){
		this.screen = screen;
	}
	
	public synchronized void render(Graphics g){
		int size = messages.size() + 1;
		int yPos = screen.getHeight() - Y_POS_FROM_BOTTOM - size * RTSFont.HEIGHT;
		int xPos = X_POS;
		int height = size * RTSFont.HEIGHT;
		g.setColor(new Color(0, 0, 0, 35));
		g.fillRect(xPos, yPos, MAX_LINE_WIDTH, height);
		for(Message m:messages){
			String message = m.getMessage();
			Screen.font.drawLine(g, message, xPos, yPos + RTSFont.HEIGHT * messages.indexOf(m), Color.BLACK);
		}
		
		Screen.font.drawLine(g, getText() + (isSelected ? "_" : ""), xPos, yPos + height - RTSFont.HEIGHT, Color.BLACK);
	}
	
	public synchronized void update(){
		for(Message m:messages){
			m.update();
		}
		if(screen.input.LMBTapped()){
			if(isInRect(screen.input.mouseX, screen.input.mouseY)){
				isSelected = true;
				screen.pause = true;
			}else{
				isSelected = false;
				screen.pause = false;
			}
		}
		if(isSelected){
			for(Key a:screen.input.inputKeys){
				if(a.isTapped()){
					text.add(a.getChars());
				}
			}
			if(screen.input.backspace.isTapped() && !text.isEmpty()){
				 text.removeLast();
			}
			if(screen.input.enter.isTapped()){
				if(screen instanceof MPGameScreen) {
					((MPGameScreen) screen).sendMessage(getText());
					text.clear();
				}
			}
		}
		
		messages.removeAll(messagesToRemove);
		messagesToRemove.clear();
	}
	
	private boolean isInRect(int x, int y){
		if(x > X_POS && x < MAX_LINE_WIDTH && y < screen.getHeight() - Y_POS_FROM_BOTTOM && y > screen.getHeight() - Y_POS_FROM_BOTTOM - RTSFont.HEIGHT){
			return true;
		}
		return false;
	}
	
	private String getText(){
		String textToRender = "";
		for(String s:text){
			textToRender = textToRender + s;
		}
		return textToRender;
	}
	
	public synchronized void removeMessage(Message message){
		messagesToRemove.add(message);
	}
	
	public synchronized void messageReceived(String message){
		messages.add(new Message(message.split(" ", 3)[2], message.split(" ", 3)[1], 600, this));
	}
}
