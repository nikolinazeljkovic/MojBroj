package server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
	public static final int MAX_NUMBER = 30;
	public static final int NUMBER_COUNT = 4;
	public static final int OPERATION_COUNT = NUMBER_COUNT - 1;

	private Random rand = new Random();
	private ArrayList<Integer> numbers = new ArrayList<>(); // svi brojevi
	private ArrayList<String> op = new ArrayList<>(); // kao i operacije se salju klijentu preko mreze
	private int result; // rez se salje klijentu isto preko soketa
	private String expression = "";

	public Expression() {
		generate();
		this.result = calculate(expression);

	}

	
	private void generate() {

		for (int i = 0; i < NUMBER_COUNT; i++) {
			int num = rand.nextInt(MAX_NUMBER);
			numbers.add(num);
		}

		for (int i = 0; i < OPERATION_COUNT; i++) {
			Operation operation;

			do {
				operation = Operation.values()[rand.nextInt(Operation.values().length)];
			} while (operation == Operation.DIVISION && numbers.get(i + 1) == 0); // ponavljamo generisanje operacije
											      // ako je djelilac jednak 0

			op.add(operation.getValue());
		}

		for (int i = 0; i < OPERATION_COUNT; i++) {
			this.expression += numbers.get(i).toString() + op.get(i);
		}
		this.expression += numbers.get(NUMBER_COUNT - 1);
	}
	
	public int calculatePointsForAttempt(int attempt) {
		int difference = Math.abs(result - attempt);
		int calculatedPoints;
		
		if(difference == 0)
			calculatedPoints = 10;
		else if(difference == 1)
			calculatedPoints = 8;
		else if(difference <= 5)
			calculatedPoints = 5;
		else if(difference <= 10)
			calculatedPoints = 2;
		else
			calculatedPoints = 0;
		
		return calculatedPoints;
	}

	public List<Integer> getNumbers() {
		return this.numbers;
	}

	public List<String> getOperations() {
		return this.op;
	}

	public String getExpression() {
		return expression;
	}

	public int getResult() {
		return result;
	}
	@Override
	public String toString() {
		return expression;
	}

	private static final Pattern EXPR_RE = Pattern.compile("\\G\\s*([+-]?)\\s*([^+-]+)");
	private static final Pattern TERM_RE = Pattern.compile("\\G(^|(?<!^)\\*|(?<!^)/)\\s*(\\d*\\.?\\d+)\\s*");

	private static double evalTerm(String term) {
		Matcher m = TERM_RE.matcher(term);
		double product = Double.NaN;
		int matchEnd;
		for (matchEnd = -1; m.find(); matchEnd = m.end()) {
			switch (m.group(1)) {
			case "*":
				product *= Integer.parseInt(m.group(2));
				break;
			case "/":
				product /= Integer.parseInt(m.group(2));
				break;
			case "":
				product = Integer.parseInt(m.group(2));
				break;
			}
		}
		if (matchEnd != term.length()) {
			throw new IllegalArgumentException("Invalid term \"" + term + "\"");
		}
		return product;
	}
}
