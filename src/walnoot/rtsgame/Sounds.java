package walnoot.rtsgame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import sun.audio.AudioStream;



public class Sounds {
	public static AudioStream RISING_SUN = load("src/res/Sounds/risingsun.wav") ;
	public static AudioStream GUN = load("src/res/Sounds/guns.wav") ;
	
	
	
	public static AudioStream load(String fileName){
	    try {
	    	return new AudioStream(new FileInputStream(fileName));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return null;
	    }
	}
}
