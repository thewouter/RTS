package walnoot.rtsgame.rest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javazoom.jl.player.Player;


public class MP3 {
    private String fileName;
    private Player player; 
    private boolean isLooping = false;

    // constructor that takes the name of an MP3 file
    public MP3(String fileName) {
        this.fileName = fileName;
    }

    public void close() { if (player != null) player.close();}

    // play the MP3 file to the sound card
    public void play() {
        try {
            InputStream in = getClass().getResourceAsStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(in);
            player = new Player(bis);
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + fileName);
            System.out.println(e);
        }

        // run in new thread to play in background
        new Thread() {
            public void run() {
                try { 
                	player.play(); 
                }
                catch (Exception e) { System.out.println(e); }
            }
        }.start();
    }
    
    public void loop(){
        try {
            InputStream in = getClass().getResourceAsStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(in);
            player = new Player(bis);
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + fileName);
            System.out.println(e);
        }

        // run in new thread to play in background
        new Thread() {
            public void run() {
                try { 
                	player.play();
                	
                }catch (Exception e) { e.printStackTrace(); }

            	close();
            	if(isLooping)loop();
            }
        }.start();
    }
    
    public void stop(){
    	close();
    	isLooping = false;
    }
    
    public int getLenght(){
    	File file = new File(fileName);
	    AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
		} catch (Exception e) {e.printStackTrace();}
		
	    AudioFormat format = audioInputStream.getFormat();
	    long audioFileLength = file.length();
	    int frameSize = format.getFrameSize();
	    float frameRate = format.getFrameRate();
	    float durationInSeconds = (audioFileLength / (frameSize * frameRate));
	    return (int) durationInSeconds;
    }
    
    // test client
    public static void main(String[] args) {
        String filename = "/res/Sounds/Buildbaseofoperations.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.loop();

        // do whatever computation you like, while music plays
        int i;
       for(i = 0 ; i < Math.pow(10, 7); i++){}
       System.out.println(i);
       mp3.stop();
        // when the computation is done, stop playing it

        // play from the beginning

    }
}
