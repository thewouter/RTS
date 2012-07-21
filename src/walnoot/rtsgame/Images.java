package walnoot.rtsgame;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images {
	public static BufferedImage[][] terrain = split(load("/res/terrain2.png"), 16, 16);
	public static BufferedImage[][] gui = split(load("/res/gui.png"), 3, 3);
	public static BufferedImage structures = load("/res/structures.png");
	public static BufferedImage font = load("/res/font.png");
	public static BufferedImage[][] sheep = split(load("/res/animations/sheep.png"),7,1);
	public static BufferedImage[][] buttons = split(load("/res/menubar.png"),4,4);
	public static BufferedImage mines = load("/res/mines.png");
	
	public static BufferedImage load(String fileName){
		try{
			return ImageIO.read(Images.class.getResource(fileName));
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage[][] split(BufferedImage image, int xAmount, int yAmount){
		BufferedImage[][] result = new BufferedImage[xAmount][yAmount];
		
		int width = image.getWidth() / xAmount;
		int height = image.getHeight() / yAmount;
		
		for(int x = 0; x < xAmount; x++){
			for(int y = 0; y < yAmount; y++){
				result[x][y] = image.getSubimage(x * width, y * height, width, height);
			}
		}
		
		return result;
	}
	
	public static BufferedImage[] split(BufferedImage image, int xAmount){
		BufferedImage[] result = new BufferedImage[xAmount];
		int width = image.getWidth() / xAmount;
		
		for(int x = 0; x < xAmount; x++){
			result[x] = image.getSubimage(x * width, 0, width, image.getHeight());
		}
		return result;
		
	}
}
