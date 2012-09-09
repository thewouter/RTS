package walnoot.rtsgame.rest;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;



public class Sound {
       
        public static final Sound shot = new Sound("/res/Sounds/guns.wav"); // this can be played only once ! 
        public static final Sound RISING_SUN = new Sound("/res/Sounds/risingsun.wav");
        public static final Sound Victory = new Sound("/res/Sounds/Buildbaseofoperations.wav");
       
        private AudioClip ac;
        private String fileName;
       
        public Sound(String file){
                try{
                        ac = Applet.newAudioClip(Sound.class.getResource(file));
                }catch(Exception e){e.printStackTrace();}
                fileName = file;
               
        }
       
        public void play(){
        	try{
        		new Thread(){
        			public void run(){
        				ac.play();
        			}
        		}.start();
        	}catch(Exception e){e.printStackTrace();}
        }
        
        public void isPlaying(){
        	
        }
        
        public void loop(){
            try{
                new Thread(){
                        public void run(){
                                ac.loop();
                        }
                }.start();
            }catch(Exception e){e.printStackTrace();}
        }
        
        public void stop(){
        	ac.stop();
        }
        
        public int getLength(){
        	File file = new File("src" + fileName);
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
}