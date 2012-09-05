package walnoot.rtsgame.multiplayer.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import walnoot.rtsgame.screen.MPGameScreen;

public class InputListener extends Thread{
	BufferedReader r;
	PrintStream p;
	MPGameScreen owner;
	
	
	public InputListener(MPGameScreen screen, BufferedReader r, PrintStream p){
		this.r = r ;
		this.p = p;
		owner = screen;
	}
	
	public void run(){
		try{
			while(true){
				String received = r.readLine();
				if(r == null){
					System.out.println("null");
					break;
					
				}
				owner.messageReceived(received);
				System.out.println("received ");
			}
		}catch(IOException e){
			System.out.println(e);
		}
		
		try {
			r.close();
			p.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public String read(){
		try {
			return r.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public void send(String message){
		p.println(message);
	}
}
