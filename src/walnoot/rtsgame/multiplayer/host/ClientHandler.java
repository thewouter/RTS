package walnoot.rtsgame.multiplayer.host;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import walnoot.rtsgame.map.entities.Entity;


public class ClientHandler extends Thread{
	ServerSocket serverSocket;
	boolean running = true;
	private MPHost host;
	private List<Entity> entities;
	
	public ClientHandler(MPHost host){
		this.host = host;
	}
	
	public void run(){
		try {
			serverSocket = new ServerSocket(host.port);
			while(running){
				Socket s = serverSocket.accept();
				Player p;
				p = new Player(null, null,host, new BufferedReader(new InputStreamReader(s.getInputStream())), new PrintStream(s.getOutputStream()),host.map.getEntities() );
				host.addPlayer(p);
			}
			
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
}
