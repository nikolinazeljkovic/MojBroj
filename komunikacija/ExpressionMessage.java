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
	  
 }

 @Override
 public String toString() 
 {
 }
}
