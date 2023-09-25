package client.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util 
{
  private static final Pattern EXPR_RE = Pattern.compile("\\G\\s*([+-]?)\\s*([^+-]+)");
	private static final Pattern TERM_RE = Pattern.compile("\\G(^|(?<!^)\\*|(?<!^)/)\\s*(\\d*\\.?\\d+)\\s*");
}
