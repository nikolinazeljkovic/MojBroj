package client.gui;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import client.Client;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;
import komunikacija.Header;
import komunikacija.Message;

public class NoResponseTimerAnimation {
	private Label timerLabel;
	private Timeline timeline;
	private Consumer<Message> guiPostAction;
	private final long roundDurationInSeconds;

	public NoResponseTimerAnimation(Label timerLabel, long roundDurationInSeconds, Consumer<Message> guiPostAction) {
		super();
		this.timerLabel = timerLabel;
		this.roundDurationInSeconds = roundDurationInSeconds;
		this.guiPostAction = guiPostAction;
		reset();
	}
	
	public NoResponseTimerAnimation(Label timerLabel, long roundDurationInSeconds) {
		this(timerLabel, roundDurationInSeconds, null);
	}

	public Consumer<Message> getGuiPostAction() {
		return guiPostAction;
	}

	public void setGuiPostAction(Consumer<Message> guiPostAction) {
		this.guiPostAction = guiPostAction;
	}

	public void reset() {
		if (timeline != null && !timeline.getStatus().equals(Status.STOPPED)) {
			timeline.stop();
		}

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		long mm = (roundDurationInSeconds % 3600) / 60;
		long ss = roundDurationInSeconds % 60;
		
		LocalTime startTime = LocalTime.of(0, (int)mm, (int)ss);
		
		timerLabel.setText(dtf.format(startTime));
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				LocalTime currentTime = LocalTime.parse(timerLabel.getText(), dtf).minusSeconds(1);
				// update timerLabel
				timerLabel.setText(dtf.format(currentTime));
				if (!LocalTime.of(0, 0, 0).isBefore(currentTime)) {
					timeline.stop();
					
					Message m = new Message(Header.NO_RESPONSE.getValue(), "No response");
					
					try {
						Client.getInstance().sendMessageAsync(m, guiPostAction);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}));
	}

	public void start() {
		timeline.playFromStart();
	}

	public void stop() {
		timeline.stop();
	}
	
	public boolean isStopped() {
		return timeline.getStatus().equals(Status.STOPPED);
	}
}

