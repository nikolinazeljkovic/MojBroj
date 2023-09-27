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


	//TODO STEFAN


}
