package walnoot.rtsgame.multiplayer.client;

public class Message {
	private final String message;
	private int lifetimeLeft;
	private Chat chat;
	
	public Message(String message, int lifetimeInTicks, Chat chat) {
		this.message = message;
		lifetimeLeft = lifetimeInTicks;
		this.chat = chat;
	}
	
	public void update(){
		lifetimeLeft --;
		if(lifetimeLeft <= 0){
			chat.removeMessage(this);
		}
	}
	
	public String getMessage(){
		return message;
	}

}
