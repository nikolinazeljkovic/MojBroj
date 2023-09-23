import java.util.HashSet;
import java.util.Set;

import server.model.Player;

public class Server
{
  public static final int PORT = 9996;
	private static Player waitingPlayer = null;
	private static Set<Player> connectedPlayers = new HashSet<Player>();
    
}
