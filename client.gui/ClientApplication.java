package client.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import client.Client;
import client.util.Util;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import komunikacija.ExpressionMessage;
import komunikacija.Header;
import komunikacija.Message;
import komunikacija.MessageException;

public class ClientApplication extends Application
{
	public static void main(String[] args) throws Exception {

		Scanner scan = new Scanner(System.in);

		System.out.print("Enter your name: ");
		String name = scan.nextLine();

		Client client = Client.getInstance();
		Message nameResponse = client.sendAndReceiveMessage(new Message(Header.NAME.getValue(), name));

		if (nameResponse.getHeader().equals(Header.ACCEPTED.getValue())) {

			Message gameStartOrWaitResponse = null;

			do {
				gameStartOrWaitResponse = client.receiveMessage();
			} while (gameStartOrWaitResponse.getHeader().equals(Header.WAIT.getValue()));

			if (gameStartOrWaitResponse.getHeader().equals(Header.GAME_STARTING.getValue()))
				launch(args);

		}

		scan.close();
		Platform.exit();
	}

	private ExpressionMessage firstRoundExpression;
	private int roundCounter = 1;

	@Override
	public void init() throws Exception {
		Client client = Client.getInstance();

		Message messageRound = client.receiveMessage();

		if (messageRound.getHeader().equals(Header.EXPRESSION.getValue())) {

			ExpressionMessage em = ExpressionMessage.parseMessage(messageRound.getValue());
			this.firstRoundExpression = em;
			LocalDateTime currentDateTime = LocalDateTime.now();
			LocalDateTime dateAndTimeWhenGamStarts = em.getStartDateTime();
			if (currentDateTime.isAfter(dateAndTimeWhenGamStarts)) {
				long startGameAfterMillis = ChronoUnit.MILLIS.between(LocalDateTime.now(), em.getStartDateTime());
				Thread.sleep(startGameAfterMillis);
			}
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO mozda on close zatvoriti soket a server da provjeri za waiting igraca
		// prije nego posalje da je game spreman
		VBox root = new VBox(50);
		root.setAlignment(Pos.CENTER);
		root.setBackground(new Background(new BackgroundFill(Color.BEIGE,null,null)));
		HBox hb1 = new HBox(10);
		HBox hb2 = new HBox(10);
		HBox hb3 = new HBox(10);
		HBox hb4 = new HBox(10);
		HBox hb5 = new HBox(10);
		HBox hb6 = new HBox(10);

		hb1.setAlignment(Pos.CENTER);
		hb1.setBackground(new Background(new BackgroundFill(Color.BEIGE,null,null)));
		hb2.setAlignment(Pos.CENTER);
		hb2.setBackground(new Background(new BackgroundFill(Color.BEIGE,null,null)));
		hb3.setAlignment(Pos.CENTER);
		hb3.setBackground(new Background(new BackgroundFill(Color.BEIGE,null,null)));
		hb4.setAlignment(Pos.CENTER);
		hb4.setBackground(new Background(new BackgroundFill(Color.BEIGE,null,null)));
		hb5.setAlignment(Pos.CENTER);
		hb5.setBackground(new Background(new BackgroundFill(Color.BEIGE,null,null)));
		HBox.setMargin(hb5, new Insets(20, 0, 0, 0));
		hb6.setAlignment(Pos.CENTER);
		hb5.setSpacing(20);

		Text genBroj = new Text();
		genBroj.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, 60));
		hb1.getChildren().add(genBroj);

		Button plus = new Button("+");
		Button minus = new Button("-");
		Button puta = new Button("*");
		Button podijeljeno = new Button("/");
		
		plus.setStyle("-fx-background-color: #f5deb3; -fx-font-size: 20px; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );"); 
		plus.setMinSize(40, 40);
		minus.setStyle("-fx-background-color: #f5deb3; -fx-font-size: 20px; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
		minus.setMinSize(40, 40);
		puta.setStyle("-fx-background-color: #f5deb3; -fx-font-size: 20px"; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
		puta.setMinSize(40, 40);
		podijeljeno.setStyle("-fx-background-color: #f5deb3; -fx-font-size: 20px; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );"");
		podijeljeno.setMinSize(40, 40);

		hb2.getChildren().addAll(plus, minus, puta, podijeljeno);

		// lista mora biti inicijalizovana brojevima koji su poslani od strane servera
		List<Integer> lista = firstRoundExpression.getNumbers();
		
		Label poruka = new Label("Unesite kombinaciju:");
		TextField unos = new TextField();
		hb4.getChildren().addAll(poruka, unos);

		Button obrisi = new Button("Obrisi unos");
		obrisi.setStyle("-fx-font-size: 2em; -fx-background-radius: 30; -fx-background-insets: 0,1,1; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
		Button potvrdi = new Button("Potvrdi unos");
		potvrdi.setStyle("-fx-font-size: 2em; -fx-background-color: Coral; -fx-text-fill: White; -fx-background-radius: 30; -fx-background-insets: 0,1,1; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
		hb5.getChildren().addAll(obrisi, potvrdi);

		root.getChildren().addAll(hb1, hb2, hb3, hb4, hb5, hb6);
		
		Label timerLabel = new Label();
		timerLabel.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, 40));
		
		hb6.getChildren().add(timerLabel);
		
		NoResponseTimerAnimation timer = new NoResponseTimerAnimation(timerLabel, firstRoundExpression.getRoundDurationInSeconds());

		Consumer<Message> nextExpressionChange = message -> {
			try {
				
				if (message.getHeader().equals(Header.EXPRESSION.getValue())) {
					
					ExpressionMessage em = ExpressionMessage.parseMessage(message.getValue());
					
					hb3.getChildren().setAll(integersToButtons(em.getNumbers(), unos));
					
					genBroj.setText(String.valueOf(em.getResult()));
					
					timer.reset();
					timer.start();
					
					primaryStage.setTitle("Moj broj (Runda " + roundCounter++ + ")");
					
				} else if (message.getHeader().equals(Header.SCORE.getValue())) {
					hb5.getChildren().clear();
					hb4.getChildren().clear();
					hb3.getChildren().clear();
					hb2.getChildren().clear();
					
					genBroj.setText("Bodovi: " + message.getValue());
					
					if(!timer.isStopped())
						timer.stop();
				} 
//				else if(message.getHeader().equals(Header.WAIT.getValue())) {
//					
//					genBroj.setText("Cekaj protivnika...");
//					potvrdi.setDisable(true);
//					timer.stop();
//				}
				
			} catch (MessageException e) {
				e.printStackTrace();
			}
		};
		
		timer.setGuiPostAction(nextExpressionChange);

		hb3.getChildren().addAll(integersToButtons(lista, unos));

		Scene scene = new Scene(root, 550, 650);
		primaryStage.setTitle("Moj broj (Runda " + roundCounter++ + ")");
		primaryStage.setScene(scene);
		primaryStage.show();

		plus.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String polje = unos.getText();
				unos.setText(polje + "+");

			}
		});
		minus.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String polje = unos.getText();
				unos.setText(polje + "-");

			}
		});
		puta.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String polje = unos.getText();
				unos.setText(polje + "*");

			}
		});
		podijeljeno.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String polje = unos.getText();
				unos.setText(polje + "/");

			}
		});
		obrisi.setOnAction(new EventHandler<ActionEvent>() {// kada se klikne na dugme obrisi u tekstualno polje unos se
															// brise
			public void handle(ActionEvent event) {
				unos.setText("");
			}
		});
		potvrdi.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				try {
					Util.calculate(unos.getText());
					Client client = Client.getInstance();

					Message expressionResponse = new Message(Header.RESULT_RESPONSE.getValue(), unos.getText());
					
					timer.stop();

					client.sendMessageAsync(expressionResponse, nextExpressionChange);

					unos.setText("");

				} catch (IllegalArgumentException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText(e.getMessage());
					alert.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		
		timer.start();

		int broj = firstRoundExpression.getResult();// prikazuje broj poslan od servera
		genBroj.setText("" + broj);

	}

	@Override
	public void stop() throws Exception {
		Client.getInstance().closeConnection();
	}

	private List<Button> integersToButtons(List<Integer> brojevi, TextField unos) {
		List<Button> buttons = new ArrayList<>();
		for (Integer broj : brojevi) {
			Button b = new Button(String.valueOf(broj));
			b.setStyle("-fx-background-color: SkyBlue; -fx-font-size: 23px");
			b.setMinSize(50, 50);
			buttons.add(b);

			b.setOnAction(new EventHandler<ActionEvent>() {// prikaze na dugmetu generisani broj
				public void handle(ActionEvent event) {
					String polje = unos.getText();
					unos.setText(polje + "" + broj); // da bi zalijepio sadrzaj dugmeta jedno do drugog
				}
			});
		}

		return buttons;
	}
}

