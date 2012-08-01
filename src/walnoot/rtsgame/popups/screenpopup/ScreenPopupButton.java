package walnoot.rtsgame.popups.screenpopup;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import walnoot.rtsgame.InputHandler;
import walnoot.rtsgame.screen.Screen;

public abstract class ScreenPopupButton extends ScreenPopupPart {
	private BufferedImage button;
	private String text;
	Boolean isInRect = false;
	
	public ScreenPopupButton(String text, ScreenPopup owner, InputHandler input){
		try {
			button = ImageIO.read(this.getClass().getResource("/res/Pictures/buttonsmall.png"));
		} catch (IOException e){
			System.out.println("no pic?");
		}
		this.input = input;
		this.text = text;
		this.owner = owner;
		height = button.getHeight();
	}
	
	public void render(Graphics g) {
		g.drawImage(button,  xPos , yPos, null);
		
		g.setColor(new Color(0x6B6B6B));
		Screen.font.drawLine(g, text, xPos + 11, yPos + (height / 2));
		Screen.font.drawLine(g, text, xPos + 10, yPos + (height / 2) + 1);
		
		g.setColor(new Color(0x4C4C4C));
		Screen.font.drawLine(g, text, xPos + 9, yPos + (height / 2));
		Screen.font.drawLine(g, text, xPos + 10, yPos + (height / 2) - 1);
		
		g.setColor(new Color(0x5B5B5B));
		Screen.font.drawLine(g, text, xPos + 10, yPos + (height / 2));

		g.setColor(new Color(128, 128, 255, 32));
		
		if(isInRect) g.fillRect(xPos, yPos, width, height);
		
	}
	
	public abstract void onLeftClick();
	
	public void update(int xPos, int yPos, int width) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = button.getWidth();
		if(input.LMBTapped() && isInRect(input.mouseX, input.mouseY)){
			onLeftClick();
		}
		isInRect = isInRect(input.mouseX, input.mouseY);
		
	}
	
	private boolean isInRect(int x, int y){
		if(x > xPos && x < xPos + width && y > yPos && y < yPos + height) return true;
		return false;
	}
	
}
