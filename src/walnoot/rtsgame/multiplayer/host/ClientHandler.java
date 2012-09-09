package walnoot.rtsgame.multiplayer.host;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ClientHandler extends Thread{
	ServerSocket serverSocket;
	boolean running = true;
	private MPHost host;
	
	public ClientHandler(MPHost host){
		this.host = host;
	}
	
	public void run(){
		try {
			serverSocket = new ServerSocket(host.port);
			while(running){
				Socket s = serverSocket.accept();
				host.addPlayer(new Player(null, null,host, new BufferedReader(new InputStreamReader(s.getInputStream())), new PrintStream(s.getOutputStream()),host.map.getEntities() ));
				}
			
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
}
