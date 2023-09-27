package server.model;

import java.util.HashMap;
import java.util.Map;

public class Round {
	public static final long DURATION_SECONDS = 60L;
	private Expression expression;
	private Map<Player, Integer> playersPoints;//RACUNANJE BODOVA

	public Round() {
		this.expression = new Expression();
		this.playersPoints = new HashMap<Player, Integer>();
	}
public Round(Expression ex) {
		this.expression = ex;
	}

	public Expression getExpression() {
		return expression;
	}

	//TODO
	
}

