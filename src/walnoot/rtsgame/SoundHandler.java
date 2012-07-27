package walnoot.rtsgame;

import java.applet.AudioClip;
import java.util.LinkedList;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class SoundHandler {
	LinkedList<AudioClip> looping = new LinkedList<AudioClip>();
	AudioPlayer player;
	
	public SoundHandler(){
		player = AudioPlayer.player;
		}
	
	public void update(){
	}
	
	public void playSong(AudioStream MD){
		player.start(MD);
	}
	
	public void loopSong(AudioClip clip){
		
	}
	
	public void stopSong(AudioClip clip){
		
	}
	
}
