package walnoot.rtsgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class RTSFont {
	public static final int HEIGHT = 9;
	private final Char[] chars = new Char[256];
	
	public RTSFont(BufferedImage sourceImage){
		int width = sourceImage.getWidth() / 16;
		int height = sourceImage.getHeight() / 16;
		
		for(int i = 0; i < chars.length; i++){
			int x = i % 16;
			int y = i / 16;
			
			chars[i] = new Char(sourceImage.getSubimage(x * width, y * height, width, height));
		}
	}
	
	public void drawLine(Graphics g, String text, int xPos, int yPos){
		int x = 0;
		
		for(int i = 0; i < text.length(); i++){
			Char currentChar = chars[(int) text.charAt(i)];
			
			currentChar.render(g, xPos + x, yPos);
			x += currentChar.getWidth();
		}
	}
	
	public void drawLine(Graphics g, String text, int xPos, int yPos, Color color){
		g.setColor(color);
		drawLine(g, text, xPos, yPos);
	}
	
	public void drawLineAndShadow(Graphics g, String text, int xPos, int yPos, Color shadowColor){
		Color oldColor = g.getColor();
		
		g.setColor(shadowColor);
		drawLine(g, text, xPos + 1, yPos + 1);
		
		g.setColor(oldColor);
		drawLine(g, text, xPos, yPos);
	}
	
	public void drawBoldLine(Graphics g, String text, int xPos, int yPos, Color backgroundColor){
		Color oldColor = g.getColor();
		
		g.setColor(backgroundColor);
		drawLine(g, text, xPos + 1, yPos);
		drawLine(g, text, xPos, yPos + 1);
		drawLine(g, text, xPos - 1, yPos);
		drawLine(g, text, xPos, yPos - 1);
		
		g.setColor(oldColor);
		drawLine(g, text, xPos, yPos);
	}
	
	public int getLineWidth(String text){
		int width = 0;
		
		for(int i = 0; i < text.length(); i++){
			width += chars[i].getWidth();
		}
		
		return width;
	}
	
	private class Char {
		private boolean[][] pixels;
		
		private Char(BufferedImage charImage){
			//kijken hoe breed een char is
			int charWidth = charImage.getWidth() - 1;//standaardlengte, voor spatie en onbekende letters
			for(int x = 0; x < charImage.getWidth(); x++){
				boolean rowIsEmpty = false;
				
				for(int y = 0; y < charImage.getHeight(); y++){
					if(charImage.getRGB(x, y) != Color.WHITE.getRGB()) rowIsEmpty = true;
				}
				
				if(rowIsEmpty) charWidth = x + 2;
			}
			pixels = new boolean[charWidth][charImage.getHeight()];
			
			//pixels[][] invullen, kijken of het zwart of wit is op x/y
			for(int x = 0; x < pixels.length; x++){
				for(int y = 0; y < pixels[x].length; y++){
					pixels[x][y] = charImage.getRGB(x, y) != Color.WHITE.getRGB();
				}
			}
		}
		
		private void render(Graphics g, int xPos, int yPos){
			for(int x = 0; x < pixels.length; x++){
				for(int y = 0; y < pixels[x].length; y++){
					if(pixels[x][y]) g.fillRect(xPos + x, yPos + y, 1, 1);
				}
			}
		}
		
		private int getWidth(){
			return pixels.length;
		}
	}
}
