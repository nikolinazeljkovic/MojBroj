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
}
