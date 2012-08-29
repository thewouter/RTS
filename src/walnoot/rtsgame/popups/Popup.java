package walnoot.rtsgame.popups;

import java.awt.Graphics;

import walnoot.rtsgame.Images;

public abstract class Popup {
	
	public abstract void render(Graphics g);
	public abstract void update(int mouseX, int mouseY);
	public abstract void onLeftClick(int mouseX, int mouseY);
	public abstract boolean isInPopup(int x, int y);
	
	protected void drawBox(Graphics g, int width, int height, int screenX, int screenY){
		int imageWidth = Images.gui[0][0].getWidth();
		int imageHeight = Images.gui[0][0].getHeight();
		
		//renderen achtergrond
		
		g.setClip(screenX + imageWidth / 2, screenY + imageHeight / 2, width - imageWidth, height - imageHeight);
		//g.setClip(screenX, screenY, width, height);
		
		for(int x = 0; x < width / imageWidth + 1; x++){
			for(int y = 0; y < height / imageHeight + 1; y++){
				g.drawImage(Images.gui[1][1], screenX + x * imageWidth, screenY + y * imageHeight, null);
			}
		}
		
		//renderen rand linkerbovenhoek
		
		g.setClip(screenX, screenY, width - imageWidth, height - imageHeight);
		
		for(int x = 0; x < width / imageWidth; x++){
			for(int y = 0; y < height / imageHeight; y++){
				int imageX = x, imageY = y;
				if(x > 0) imageX = 1;
				if(y > 0) imageY = 1;
				
				if(imageX == 1 && imageY == 1) continue;
				
				g.drawImage(Images.gui[imageX][imageY], screenX + x * imageWidth, screenY + y * imageHeight, null);
			}
		}
		
		g.setClip(screenX, screenY, width - imageWidth, height);
		
		for(int x = 0; x < width / imageWidth; x++){
			int imageX = x, imageY = 2;
			if(x > 0) imageX = 1;
			
			g.drawImage(Images.gui[imageX][imageY], screenX + x * imageWidth, screenY + height - imageHeight, null);
		}
		
		g.setClip(screenX, screenY, width, height - imageHeight);
		
		for(int y = 0; y < width/* / imageWidth*/; y++){
			int imageX = 2, imageY = y;
			if(y > 0) imageY = 1;
			
			g.drawImage(Images.gui[imageX][imageY], screenX + width - imageWidth, screenY + y * imageHeight, null);
		}
		
		g.setClip(null);
		
		g.drawImage(Images.gui[2][2], screenX + width - imageWidth, screenY + height - imageHeight, null);
	}

}
