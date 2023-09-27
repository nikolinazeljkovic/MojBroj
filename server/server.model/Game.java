package server.model;

import java.io.IOException;

import komunikacija.Header;
import komunikacija.Message;
import server.Server;

public class Game extends Thread{
	public static final int BROJ_RUNDI = 3;
	private final Player igrac1;
	private final Player igrac2;
	private final Round[] runde;
	public Game(Player igrac1, Player igrac2) {
		super();
		this.igrac1 = igrac1;
		this.igrac2 = igrac2;
		this.runde = new Round[BROJ_RUNDI];
		for(int i=0; i<BROJ_RUNDI; i++)
			runde[i] = new Round();
	}
	public Player getIgrac1() {
		return igrac1;
	}
	public Player getIgrac2() {
		return igrac2;
	}
	public Round[] getRunde() {
		return runde;
	}

	
	@Override
	public void run() {
		igrac1.setRounds(runde);
		igrac2.setRounds(runde);
		
		igrac1.start();
		igrac2.start();
		
		try {
			igrac1.join();
			igrac2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//sabiramo poene iz svih rundi i vracamo to igracima
		for(Round r : runde) {
			
			int player1pointsForThisRound = r.getPlayersPoints().get(igrac1);
			int player2pointsForThisRound = r.getPlayersPoints().get(igrac2);
			
			igrac1.setPoints(igrac1.getPoints() + player1pointsForThisRound);
			igrac2.setPoints(igrac2.getPoints() + player2pointsForThisRound);
		}
		Message score1, score2;
		if(igrac1.getPoints() > igrac2.getPoints()) {
			score1 = new Message(Header.SCORE.getValue(), String.valueOf(igrac1.getPoints() + " - Pobjeda"));
			score2 = new Message(Header.SCORE.getValue(), String.valueOf(igrac2.getPoints()));
		} else if(igrac1.getPoints() == igrac2.getPoints()) {
			score1 = new Message(Header.SCORE.getValue(), String.valueOf(igrac1.getPoints() + " - Izjednaceno"));
			score2 = new Message(Header.SCORE.getValue(), String.valueOf(igrac2.getPoints() + " - Izjednaceno"));
		}else {
			score1 = new Message(Header.SCORE.getValue(), String.valueOf(igrac1.getPoints()));
			score2 = new Message(Header.SCORE.getValue(), String.valueOf(igrac2.getPoints() + " - Pobjeda"));
		}
		igrac1.sendMsg(score1.toString());
		igrac2.sendMsg(score2.toString());
		
		try {
			igrac1.closeConnection();
			System.out.println("Player " + igrac1 + " disconnected");
			igrac2.closeConnection();
			System.out.println("Player " + igrac2 + " disconnected");
			Server.removePlayer(igrac1);
			Server.removePlayer(igrac2);
			System.out.println(this + " ended");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
	
