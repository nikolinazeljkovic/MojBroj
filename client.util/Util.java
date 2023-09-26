package client.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util 
{
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

	public static int calculate(String expr) {   // racuna izzracuna izraz iz stringa , koristi donju funkciju
		Matcher m = EXPR_RE.matcher(expr);
		int sum = 0;
		int matchEnd;
		for (matchEnd = -1; m.find(); matchEnd = m.end()) {
			sum += (("-".equals(m.group(1))) ? -1 : +1) * evalTerm(m.group(2));
		}
		if (matchEnd != expr.length()) {
			throw new IllegalArgumentException("Invalid expression \"" + expr + "\"");
		}
		return sum;
	}
}

