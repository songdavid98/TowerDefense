import javafx.application.Platform;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polyline;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton; 
import javafx.scene.control.ToggleGroup; 
import javafx.scene.control.CheckBox;

import javafx.util.converter.NumberStringConverter;
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
			Tower t = new Tower();
			switch(Run.getSelection()) {
				case "dart": 	t = new DartMonkey();
								break;
				case "cannon": 	t = new Cannon();
								break;
				default: break;
			}
			Run.money.set(Run.money.get() - t.getPrice() );
			t.setXY( (int)e.getX(), (int)e.getY() );
			Run.towers.add(t);
			Run.playArea.getChildren().add(t);
			System.out.println( t.getX() );
		});

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

		Text moneyDisplay = new Text();
		moneyDisplay.getStyleClass().add("large");
		moneyDisplay.textProperty().bind(
			new SimpleStringProperty("Money: ").concat( 
				Run.getMoney().asString() ) );
		CheckBox isBuying = new CheckBox("BuyingMode");
		v.getChildren().add(moneyDisplay);

		RadioButton rbDart = new RadioButton("Dart Monkey");
		RadioButton rbCannon = new RadioButton("Cannon");
		RadioButton rbSuper = new RadioButton("Super Monkey");
		ToggleGroup towerSelect = new ToggleGroup();
		rbDart.setToggleGroup( towerSelect );
		rbCannon.setToggleGroup( towerSelect );
		rbSuper.setToggleGroup( towerSelect );
		rbDart.setOnAction(e -> Run.setSelection( "dart" ) );
		rbCannon.setOnAction(e -> Run.setSelection( "cannon" ) );
		rbSuper.setOnAction(e -> Run.setSelection( "super" ) );

		v.getChildren().addAll( rbDart, rbCannon, rbSuper );

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
			Run.startMenu(); 
		});

		HBox menu = new HBox();
		menu.getChildren().addAll( playButton, backButton );
		return menu;
	}

}