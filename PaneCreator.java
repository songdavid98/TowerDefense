import javafx.application.Platform;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton; 
import javafx.scene.control.ToggleGroup;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PaneCreator {

	public static Pane map() {
		Pane map = new Pane();
		map.setLayoutX(0);
		map.setLayoutY(0);

		Rectangle grass = new Rectangle();
		grass.getStyleClass().add("grass");
		grass.setWidth(750);
		grass.setHeight(750);

		Polyline road = new Polyline();
		road.getPoints().addAll( new Double[] {
			0.0,300.0,
			300.0,300.0,
			300.0,100.0,
			600.0,100.0,
			600.0,650.0,
			0.0,650.0
		});
		road.getStyleClass().add("path");

		map.getChildren().addAll( grass, road );
		map.setOnMouseReleased( e -> {
			Run.buyTower( (int)e.getX(), (int)e.getY() );
			Run.rangeCircle.setFill( Color.rgb(0,0,0,0) );
		});
		map.setOnMouseDragged( e -> {
			Run.rangeCircle.setFill( Color.rgb(0,0,0,0.5) );
			switch(Run.getSelection()) {
				case "dart":	Run.rangeCircle.setRadius( 175 );
								break;
				case "cannon": 	Run.rangeCircle.setRadius( 250 );
								break;
				default: 		Run.rangeCircle.setRadius( 0 );
								break;
			}
			Run.rangeCircle.setCenterX( e.getX() );
			Run.rangeCircle.setCenterY( e.getY() );
		});
		map.getChildren().add( Run.rangeCircle );
		return map;
	}

	public static VBox startMenu() {
		VBox v = new VBox();

		Text title = new Text("Tower Defense");
		title.getStyleClass().add("title");

		v.getChildren().add(title);
		Button startButton = new Button("START");
		Button helpButton = new Button("HELP");
		Button quitButton = new Button("QUIT");

		startButton.getStyleClass().add("menuButton");
		helpButton.getStyleClass().add("menuButton");
		quitButton.getStyleClass().add("menuButton");

		startButton.setOnAction( e -> Run.playScene() );

		helpButton.setOnAction( e -> Run.helpScene() );
		quitButton.setOnAction( e -> Platform.exit() );

		v.getChildren().addAll( startButton, helpButton, quitButton);
		return v;
	}

	public static VBox helpMenu() {
		Text info = new Text("a lot");
		info.setText("The goal of this game is to survive as long as possible. \n"+
			"You do this by defeating enemies with towers that you will have to place down.\n"+
			"You will use your mouse to interact with the menus given. \n"+
			"Dart monkeys are a good source of cheap damage. \n" +
			"Bomb towers do splash damage. " +
			"You can hold down the mouse button while a tower is selected to check its range.\n" +
			"Good luck and have fun!");

		info.getStyleClass().add("text");
		VBox v = new VBox();
		v.getChildren().add( info );
		Button backButton = new Button("BACK");
		backButton.getStyleClass().add("menuButton");
		backButton.setOnAction(e -> Run.startMenu() );
		v.getChildren().add( backButton );
		return v;
	}

	public static Pane controls() {
		Pane controls = new Pane();

		controls.setLayoutX(750);
		controls.setLayoutY(0);

		Rectangle controlBg = new Rectangle();
		controlBg.setWidth(250);
		controlBg.setHeight(750);
		controlBg.getStyleClass().add("controlBg");
		controls.getChildren().add( controlBg );

		VBox v = new VBox(8);
		v.getChildren().add( topMenu() );

		Text score = new Text();
		score.getStyleClass().add("large");
		score.textProperty().bind(
			new SimpleStringProperty("Score: ").concat(
				Run.getScore().asString() ) );
		Text moneyDisplay = new Text();
		moneyDisplay.getStyleClass().add("large");
		moneyDisplay.textProperty().bind(
			new SimpleStringProperty("Money: ").concat( 
				Run.getMoney().asString() ) );
		Text playerLivesDisplay = new Text();
		playerLivesDisplay.getStyleClass().add("large");
		playerLivesDisplay.textProperty().bind(
			new SimpleStringProperty("Lives: ").concat( 
				Run.getPlayerLives().asString() ) );
		v.getChildren().addAll( score, moneyDisplay, playerLivesDisplay);

		RadioButton rbDart = new RadioButton("Dart Monkey -Cost:200");
		RadioButton rbCannon = new RadioButton("Cannon -Cost:500");
		//RadioButton rbSuper = new RadioButton("Super Monkey -Cost:3000");
		ToggleGroup towerSelect = new ToggleGroup();
		rbDart.setToggleGroup( towerSelect );
		rbCannon.setToggleGroup( towerSelect );
		//rbSuper.setToggleGroup( towerSelect );
		rbDart.setOnAction(e -> Run.setSelection( "dart" ) );
		rbCannon.setOnAction(e -> Run.setSelection( "cannon" ) );
		//rbSuper.setOnAction(e -> Run.setSelection( "super" ) );

		v.getChildren().addAll( rbDart, rbCannon);

		controls.getChildren().add( v );
		return controls;
	}

	public static Pane topMenu() {
		Button playButton = new Button("Start Round");
		playButton.getStyleClass().add("menuButton");
		playButton.setOnAction( e -> Run.startRound() );

		Button backButton = new Button("EXIT");
		backButton.getStyleClass().add("menuButton");
		backButton.setOnAction(e -> {
			Run.getTimers().stream()
				.forEach( t -> t.cancel() );
			Run.getThreads().stream()
				.forEach( t -> t.interrupt());
			Run.getTimers().clear();
			Run.getThreads().clear();
			Run.towers.clear();
			Run.enemies.clear();
			Run.projectiles.clear();
			Run.startMenu();
		});

		HBox menu = new HBox();
		menu.getChildren().addAll( playButton, backButton );
		return menu;
	}

	public static Pane loseScene() {
		VBox v = new VBox();
		v.getStyleClass().add("controlBg");
		Text loseText = new Text();
		loseText.getStyleClass().add("large");
		loseText.textProperty().bind(
			new SimpleStringProperty("You let the enemy get to the end!\n"+
				"Your final score: " ).concat(
				Run.getScore().asString() ) );
		Button backButton = new Button("Exit to Menu");
		backButton.getStyleClass().add("menuButton");
		backButton.setOnAction(e -> {
			Run.startMenu();
		});
		v.getChildren().addAll( loseText, backButton );
		return v;
	}
}