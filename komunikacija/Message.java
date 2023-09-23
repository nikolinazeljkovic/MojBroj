package komunikacija;

import java.util.Arrays;
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

  public static Message parseMessage(String message) throws MessageException {
	String[] split = message.split(SEPARATOR);
	if(split.length != 2 || Arrays.stream(Header.values()).noneMatch(header -> header.getValue().equals(split[0])))
	    throw new MessageException("Invalid message");
	return new Message(split[0], split[1]);
	} 
	
  public String getHeader() {
	return header;
	}

  public void setHeader(String header) {
	this.header = header;
	}
	
  public String getValue() {
	return value;
       }
	
  public void setValue(String value) {
	this.value = value;
	}
  @Override
  public String toString() {
       return header + SEPARATOR + value;
	}
}
