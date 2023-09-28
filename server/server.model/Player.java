package server.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Objects;

import komunikacija.ExpressionMessage;
import komunikacija.Header;
import komunikacija.Message;
import komunikacija.MessageException;

public class Player extends Thread{
	private String name;// ime klijenta
	private int points = 0;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Round[] rounds;

	public Player(String name, Socket s) throws IOException {
		this.socket = s;
		this.name = name;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
	}

	@Override
	public void run() {
		if(rounds != null) {
			try {
				
				for (int i = 0; i < rounds.length; i++) {
					
					LocalDateTime startDateTime = LocalDateTime.now().plusSeconds(1L);
					LocalDateTime endDateTime = startDateTime.plusSeconds(Round.DURATION_SECONDS);
					
					Expression currentRoundExpression = rounds[i].getExpression();
					ExpressionMessage message = new ExpressionMessage(currentRoundExpression.getNumbers(), currentRoundExpression.getResult(), startDateTime, Round.DURATION_SECONDS);
					
					Message roundMessage = new Message(Header.EXPRESSION.getValue(), message.toString());
					sendMsg(roundMessage.toString());
					
					String expressionResponse = null;
					int pointsForThisRound = 0;
					
					//ponavljamo dok igrac ne posalje izraz i dok ne istekne vrijeme za rjesavanje runde
					try {
						while (!in.ready() && LocalDateTime.now().isBefore(endDateTime))
							Thread.sleep(1L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if(in.ready())
						expressionResponse = in.readLine();
					
					if(expressionResponse != null) {
						try {
							Message response = Message.parseMessage(expressionResponse);
							if(response.getHeader().equals(Header.RESULT_RESPONSE.getValue())) {
								int calculatedResult = Expression.calculate(response.getValue());
								pointsForThisRound = currentRoundExpression.calculatePointsForAttempt(calculatedResult);
							} else if(response.getHeader().equals(Header.NO_RESPONSE.getValue())) {
								System.out.println("No response timeout for " + name);
							}

						} catch (MessageException | IllegalArgumentException e) {
							e.printStackTrace();
						}
					}
					
					rounds[i].getPlayersPoints().put(this, pointsForThisRound);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public boolean isDisconnected() {
		return socket.isClosed();
	}
	
	public void closeConnection() throws IOException {
		socket.close();
	}

	public void sendMsg(String msg) {   // od strane servera se salje poruka
		out.println(msg);           // salje se poruka u vidu stringa igracu, koristi se kod slanja pitanja
	}

	//metoda getResponse da prima izraz od klijenta, dopuniti ili ne
	public String getResponse() throws IOException {
		String message = this.in.readLine(); // cita odgovor od klijenta
		return message;
	}

	public String getPlayerName() {
		return this.name;
	}
	
	public void setRounds(Round[] rounds) {
		this.rounds = rounds;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return Objects.equals(name, other.name);
	}

	public String toString() {
		return name + " : " + points;
	}

}


