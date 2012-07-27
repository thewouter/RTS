package walnoot.rtsgame.popups.screenpopup;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.InputHandler.Key;
import walnoot.rtsgame.RTSFont;
import walnoot.rtsgame.screen.Screen;

public class TextInput extends ScreenPopupPart {
	private LinkedList<String> text = new LinkedList<String>();
	private static int EMPTY_SPACE = 5;
	LinkedList<Key> inputKeys = new LinkedList<Key>();
	public boolean isActive = true;
	
	public TextInput(ScreenPopup owner,InputHandler input){
		height = RTSFont.HEIGHT + 2 * EMPTY_SPACE;
		this.owner = owner;
		this.input = input;
		inputKeys.add(input.a);
		inputKeys.add(input.b);
		inputKeys.add(input.c);
		inputKeys.add(input.d);
		inputKeys.add(input.e);
		inputKeys.add(input.f);
		inputKeys.add(input.g);
		inputKeys.add(input.h);
		inputKeys.add(input.i);
		inputKeys.add(input.j);
		inputKeys.add(input.k);
		inputKeys.add(input.l);
		inputKeys.add(input.m);
		inputKeys.add(input.n);
		inputKeys.add(input.o);
		inputKeys.add(input.p);
		inputKeys.add(input.q);
		inputKeys.add(input.r);
		inputKeys.add(input.t);
		inputKeys.add(input.s);
		inputKeys.add(input.u);
		inputKeys.add(input.v);
		inputKeys.add(input.w);
		inputKeys.add(input.x);
		inputKeys.add(input.y);
		inputKeys.add(input.z);
		inputKeys.add(input.space);
		inputKeys.add(input.n0);
		inputKeys.add(input.n1);
		inputKeys.add(input.n2);
		inputKeys.add(input.n3);
		inputKeys.add(input.n4);
		inputKeys.add(input.n5);
		inputKeys.add(input.n6);
		inputKeys.add(input.n7);
		inputKeys.add(input.n8);
		inputKeys.add(input.n9);
	}
	

	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(xPos, yPos, width, height);
		g.setColor(Color.GRAY);
		g.drawRect(xPos, yPos, width, height);
		String result = "";
		for(String s:text){
			result += s;
		}
		
		Screen.font.drawLine(g, result, xPos + EMPTY_SPACE, yPos + EMPTY_SPACE);
	}
	
	public void update(int xPos,int yPos, int width) {
		if(isActive){
			for(Key a:inputKeys){
				if(a.isTapped()){
					text.add(a.getChars());
				}
			}
			if(input.backspace.isTapped() && !text.isEmpty()){
				text.removeLast();
			}
		}
		
		if(input.LMBTapped()){
			if(isInBox(input.mouseX, input.mouseY)){
				isActive = true;
			}else{
				isActive = false;
			}
		}
		
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
	}
	
	public boolean isInBox(int x, int y){
		if(x > xPos + EMPTY_SPACE && x < xPos +EMPTY_SPACE + width && y > yPos + EMPTY_SPACE && y < yPos + EMPTY_SPACE + height) return true;
		return false;
	}
	
	public String getOutput(){
		String uitput = "";
		for(String s:text){
			uitput += s;
		}
		return uitput;
	}
}
