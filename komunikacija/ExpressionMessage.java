package komunikacija;

import java.util.Arrays;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExpressionMessage
{
  private List<Integer> numbers;
	private int result;
	private LocalDateTime startDateTime;// treba start date time samo za prvu rundu
	private long roundDurationInSeconds;
	public ExpressionMessage() {
		super();
	}

  public ExpressionMessage(List<Integer> numbers, int result, LocalDateTime startDateTime, long roundDurationInSeconds)
  {
        super();
	this.numbers = numbers;
	this.result = result;
	this.startDateTime = startDateTime;
	this.roundDurationInSeconds = roundDurationInSeconds;
  }
  public List<Integer> getNumbers() {
	  return numbers;
	}
  public void setNumbers(List<Integer> numbers) {
	this.numbers = numbers;
  }
  public int getResult() {
	return result;
	}
  public void setResult(int result) {
	this.result = result;
	}
  public LocalDateTime getStartDateTime() {
	return startDateTime;
	}
  public void setStartDateTime(LocalDateTime startDateTime) {
	this.startDateTime = startDateTime;
	}
  public long getRoundDurationInSeconds() {
	return roundDurationInSeconds;
	}
  public void setRoundDurationInSeconds(long roundDurationInSeconds) {
	this.roundDurationInSeconds = roundDurationInSeconds;
	}
  public static ExpressionMessage parseMessage(String message) throws MessageException {
	List<Integer> numbers = new ArrayList<Integer>();
	int result;
	LocalDateTime startDateTime;
	long roundDurationInSeconds;
	String[] split = message.split("#");

	if(split.length == 4) {
		//rekreiramo niz brojeva
		String[] split1 = split[0].split(",");
		for(String strNum : split1)
			numbers.add(Integer.valueOf(strNum));
			
		//trazeni broj
		result = Integer.valueOf(split[1]);
		
		//vrijeme pocetka prve runde
		startDateTime = LocalDateTime.parse(split[2]);
			
		//trajanje runde
		roundDurationInSeconds = Long.parseLong(split[3]);
			
		return new ExpressionMessage(numbers, result, startDateTime, roundDurationInSeconds);
		}
	  throw new MessageException("Invalid expression message!");
 }

 @Override
 public String toString() 
 {
	StringBuilder sb = new StringBuilder();
		
	Iterator<Integer> iterator = numbers.iterator();
		
	 while(iterator.hasNext()) {
	     Integer integer = iterator.next();
	     sb.append(integer);
	     if(iterator.hasNext())
             sb.append(",");
		}
	 sb.append("#");
	 sb.append(result);
    	 sb.append("#");
  	 sb.append(startDateTime);
	 sb.append("#");
	 sb.append(roundDurationInSeconds);
	 return sb.toString();
 }
}
