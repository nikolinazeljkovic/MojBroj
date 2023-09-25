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



	public static void main(String[] args) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("Server is running on port: " + PORT);

			while (true) {

				Socket client = serverSocket.accept();
				System.out.println("Client connected");

				BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

				try {
					// provjeravamo ime
					String name = getName(br.readLine());

					Player newPlayer = new Player(name, client); 				// kreiramo igraca, pokusavamo ga dodati (nece uspjeti ako vec postoji igrac sa tim imenom)

					if (addPlayer(newPlayer)) {

						// dodavanje uspjesno, znaci nije bio konektovan ranije
						Message accepted = new Message(Header.ACCEPTED.getValue(), newPlayer.getPlayerName());
						pw.println(accepted.toString());						// obavjestavamo izgraca da je prihvacen

						System.out.println("Player accepted: " + name);
						
						if (waitingPlayer != null) { // provjeravamo da li postoji igrac koji ceka na igru
							
							Message response1 = new Message(Header.GAME_STARTING.getValue(), waitingPlayer.getPlayerName() + "...");
							Message response2 = new Message(Header.GAME_STARTING.getValue(), newPlayer.getPlayerName() + "...");
							pw.println(response1.toString());
							waitingPlayer.sendMsg(response2.toString());			// obavjestavamo igrace da je suparnik pronadjen i da se pripreme za citanje brojeva i broja

							Game newGame = new Game(newPlayer, waitingPlayer); 	// ako ima kreiramo igru

							waitingPlayer = null; 								// nema vise igraca koji cekaju

							newGame.start(); 									// startujemo igru
							
							System.out.println("New game started: " + newGame);

						} else {

							waitingPlayer = newPlayer; 							// ako nema stavimo ga kao igraca koji ceka
							
							Message response = new Message(Header.WAIT.getValue(), "Wait for opponent...");
							pw.println(response.toString());					// i obavijestimo igraca o tome
						
							System.out.println("Player waiting: " + name);
						}
					} else {

						// konektovan je igrac sa datim imenom, odbijamo ga

						Message response = new Message(Header.DENIED.getValue(), "Already connected");
						
						pw.println(response.toString());
						
						throw new Exception("Client denied");
					}

				} catch (Exception e) {
					System.err.println(e.getMessage());
					client.close();
				}
			}
		}
	}
	
private static String getName(String message) throws MessageException {
		Message m = Message.parseMessage(message);
		
		if (Header.NAME.getValue().equals(m.getHeader()))
			return m.getValue();
		
		throw new RuntimeException("Name is not sent");
	}
	
}
