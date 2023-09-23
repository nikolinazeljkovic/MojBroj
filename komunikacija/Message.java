
public class Message {
	
  public static final String SEPARATOR = "::";
  private String header;
  private String value;
  
  public Message() {}

  public Message(String header, String value) {
        super();
	this.header = header;
	this.value = value;
	}

  @Override
  public String toString() {
       return header + SEPARATOR + value;
	}
}
