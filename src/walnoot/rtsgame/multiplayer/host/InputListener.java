package walnoot.rtsgame.multiplayer.host;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;



public class InputListener extends Thread{
	
	private Player p;
	private BufferedReader r;
	private PrintStream out;
	
	public InputListener(Player p, BufferedReader r, PrintStream out){
		this.p = p;
		this.r = r;
		this.out = out;
	}
	
	public void run(){
		try{
			while(true){
				String received = r.readLine();
				if(r == null){
					p.quit();
					break;
				}
				p.InputReceived(received);
			}
		}catch(IOException e){
			System.out.println(e);
			System.out.println("connection lost!");
			p.quit();
		}
		
		try {
			r.close();
			out.close();
			p.quit();
		} catch (IOException e) {
			System.out.println(e);
			p.quit();
		}
	}
	
	public void send(String message){
		out.println(message);
	}
	
	public String read(){
		try {
			return r.readLine();
		} catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}
}
