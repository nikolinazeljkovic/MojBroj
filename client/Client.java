package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

import javafx.application.Platform;
import komunikacija.Message;
import komunikacija.MessageException;

//Klasa enkapsulira atribute i metode koje obezbjedjuju komunikaciju sa serverom
//Klasa Client je singleton klasa
public class Client {
	public static final String SERVER_HOST = "localhost";
	public static final int SERVER_PORT = 9996;
	private static boolean logServerResponses = true;
	private static Client instance;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	public static Client getInstance() throws UnknownHostException, IOException {
		if (instance == null) {
			instance = new Client();
		}
		return instance;
	}

	public static void printServerMessage(Message message) {
		if (logServerResponses)
			System.out.println(SERVER_HOST + ": " + message.getHeader() + " - " + message.getValue());
	}

	private Client() throws UnknownHostException, IOException {
		this.socket = new Socket(SERVER_HOST, SERVER_PORT);
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		System.out.println("Connected to " + socket.getRemoteSocketAddress());
	}

	public void sendMessage(Message message) {
		out.println(message.toString());
	}

	public Message receiveMessage() throws MessageException, IOException {
		Message receivedMessage = Message.parseMessage(in.readLine());
		printServerMessage(receivedMessage);
		return receivedMessage;
	}

	public Message sendAndReceiveMessage(Message message) throws MessageException, IOException, InterruptedException {
		out.println(message.toString());
		String input = null;
		while((input = in.readLine()) == null)
			Thread.sleep(1L);
		Message receivedMessage = Message.parseMessage(input);
		printServerMessage(receivedMessage);
		return receivedMessage;
	}

	public void sendMessageAsync(Message message, Consumer<Message> guiPostAction) {
		new Thread(() -> {
			sendMessage(message);
			try {
				String input = null;
				while((input = in.readLine()) == null)
					Thread.sleep(1L);
				Message response = Message.parseMessage(input);
				printServerMessage(response);
				if (guiPostAction != null)
					Platform.runLater(() -> guiPostAction.accept(response));
			} catch (MessageException | IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	public void closeConnection() throws IOException {
		socket.close();
	}
}
