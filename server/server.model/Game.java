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
