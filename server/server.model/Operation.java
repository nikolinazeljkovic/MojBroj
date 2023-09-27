package server.model;

public enum Operation {
	ADDITION("+"),
	SUBTRACTION("-"),
	MULTIPLICATION("*"),
	DIVISION("/");
	
	
	private String value;
	
	private Operation(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	
}
