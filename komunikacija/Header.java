package komunikacija;

public enum Header 
{
	NAME("NAME"),
	ACCEPTED("ACCEPTED"),
	DENIED("DENIED"),
	WAIT("WAIT"),
	GAME_STARTING("GAME_STARTING"),
	ERROR("ERROR"),
	FINISH("FINISH"),
	EXPRESSION("EXPRESSION"),
	RESULT_RESPONSE("RESULT_RESPONSE"),
	NO_RESPONSE("NO_RESPONSE"),
	SCORE("SCORE");
	
	private String value;
	
	private Header(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
