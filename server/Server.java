import java.util.HashSet;
import java.util.Set;

import server.model.Player;

public class Server
{
  public static final int PORT = 9996;
	private static Player waitingPlayer = null;
	private static Set<Player> connectedPlayers = new HashSet<Player>();

public static synchronized boolean addPlayer(Player player) {
		return connectedPlayers.add(player);
	}


//TODO main

private static String getName(String message) throws MessageException {
		Message m = Message.parseMessage(message);
		
		if (Header.NAME.getValue().equals(m.getHeader()))
			return m.getValue();
		
		throw new RuntimeException("Name is not sent");
	}
	
}
