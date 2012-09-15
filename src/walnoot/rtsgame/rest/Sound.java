package walnoot.rtsgame.rest;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class Sound {
       private MP3 mp3;
       
        public Sound(String file){
                mp3 = new MP3(file);
        }
       
        public void play(){
        	mp3.play();
        	
        }
        
        public void isPlaying(){
        	
        }
        
        public void loop(){
            mp3.loop();
        }
        
        public void stop(){
        	mp3.stop();
        }
}