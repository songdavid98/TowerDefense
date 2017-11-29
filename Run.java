import javafx.application.Application; 
import javafx.application.Platform;
import javafx.stage.Stage; 
import javafx.scene.Scene; 
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import javafx.scene.shape.Shape;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Circle;

import javafx.animation.PathTransition;
import javafx.animation.Interpolator;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Iterator;

import java.util.Timer;
import java.util.TimerTask;

public class Run extends Application {

	private static Polyline path = new Polyline();
	private static Pane playArea = new Pane();
	private static Set<Enemy> enemies = Collections.synchronizedSet( 
		new LinkedHashSet<Enemy>() );
	public static HashSet<Projectile> projectiles = new HashSet<Projectile>();
	public static HashSet<Tower> towers = new HashSet<Tower>();
	private static HashSet<Timer> timers = new HashSet<Timer>();
	private static HashSet<Thread> threads = new HashSet<Thread>();
	

	@Override
	public void start(Stage primaryStage) {
		Pane pane = new Pane();
		Scene scene = new Scene(pane, 500, 500);
		scene.getStylesheets().add("main.css");
		primaryStage.setTitle("Run");
		primaryStage.setScene( scene );
		primaryStage.show();

		startMenu(primaryStage);
	}

	public void startMenu(Stage primaryStage) {

		VBox v = new VBox();

		Pane pane = new Pane();

		Text title = new Text("Tower Defense");

		title.getStyleClass().add("title");

		Button startButton = new Button("START");
		Button helpButton = new Button("HELP");
		Button quitButton = new Button("QUIT");

		startButton.getStyleClass().add("menuButton");
		helpButton.getStyleClass().add("menuButton");
		quitButton.getStyleClass().add("menuButton");

		startButton.setOnAction( e -> playScene( primaryStage ) );

		helpButton.setOnAction( e -> helpScene( primaryStage ) );
		quitButton.setOnAction( e -> Platform.exit() );

		v.getChildren().addAll( title, startButton, helpButton, quitButton);

		Scene scene = new Scene(v, 500, 500);
		scene.getStylesheets().add("main.css");
		
		primaryStage.setTitle("Run");
		primaryStage.setScene( scene );
		primaryStage.show();
	}

	public Pane map() {
		playArea = new Pane();
		// playArea.setHeight(750);
		// playArea.setWidth(750);
		playArea.setLayoutX(0);
		playArea.setLayoutY(0);

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
		path = road;

		playArea.getChildren().addAll( grass, road );

		return playArea;
	}

	public HBox topMenu(Stage primaryStage) {
		Button backButton = new Button("EXIT");
		backButton.getStyleClass().add("menuButton");
		backButton.setOnAction(e -> {
			timers.stream()
				.forEach( t -> t.cancel() );
			threads.stream()
				.forEach( t -> t.interrupt());
			startMenu(primaryStage); 
		});
		
		Button playButton = new Button("Start Round");
		playButton.getStyleClass().add("menuButton");
		playButton.setOnAction( e -> startRound() );

		HBox menu = new HBox();
		menu.getChildren().addAll( playButton, backButton );

		return menu;
	}

	public void startRound() {
		if ( !threads.isEmpty() )
			return;
		Thread spawn = new Thread( () -> spawnEnemies() );
		spawn.run();
		Thread frame = new Thread( () -> playFrame() );
		// to be done
		frame.run();
		towers.add( new Tower(325, 200, 500, 1, 20) );
		threads.add( spawn );
		threads.add( frame );
	}

	public void spawnEnemies() {
		Timer timer = new Timer();
		timers.add(timer);

		TimerTask task = new TimerTask() {
			public void run() {
				Platform.runLater( () -> {
					Enemy e = new Enemy();
					playArea.getChildren().add(e);
					enemies.add(e);
				});
			}
		};
		timer.schedule( task , 0, 1000);
	}

	public void playFrame() {
		Timer timer = new Timer();
		timers.add( timer );
		TimerTask task = new TimerTask() {
			public void run() {
				Platform.runLater( () -> {
					runEnemies();
					runProjectiles();
					runTowers();
				});
			}
		};
		timer.schedule( task , 0, 10);
	}

	public void runEnemies() {
		Iterator<Enemy> i = enemies.iterator();
		while ( i.hasNext() ) {
			Enemy e = i.next();
			if ( e.isDead() ) {
				playArea.getChildren().remove(e);
				i.remove();
				break;
			}
			e.move();
			//System.out.println(e);
		}
	}

	public void runProjectiles() {
		Iterator<Projectile> i = projectiles.iterator();
		while ( i.hasNext() ) {
			Projectile p = i.next();
			if ( p.isDead() ) {
				playArea.getChildren().remove(p);
				i.remove();
				break;
			}
			p.move();
		}
	}

	public void runTowers() {
		for (Tower t : towers) {
			t.findTarget( enemies );
			Projectile p = t.attack();
			if (p != null) {
				projectiles.add(p);
				playArea.getChildren().add( p );
			}
		}
	}

	public void playScene(Stage primaryStage) {
		Pane p = new Pane();

		Pane controls = new Pane();

		controls.setLayoutX(750);
		controls.setLayoutY(0);

		Rectangle controlBg = new Rectangle();
		controlBg.getStyleClass().add("controlBg");
		controlBg.setWidth(250);
		controlBg.setHeight(750);

		controls.getChildren().addAll( controlBg, topMenu(primaryStage) );

		p.getChildren().addAll( map() , controls );

		Scene scene = new Scene( p , 1000, 750);

		scene.getStylesheets().add("main.css");

		primaryStage.setTitle("Run");
		primaryStage.setScene( scene );
		primaryStage.show();
	}

	public void helpScene(Stage primaryStage) {
		Text info = new Text("a lot");
		info.setText("The goal of this game is to survive as long as possible. \n"+
			"You do this by defeating enemies with towers that you will have to place down.\n"+
			"You will use your mouse to interact with the menus given. \n"+
			"Good luck and have fun!");

		info.getStyleClass().add("text");

		Button backButton = new Button("BACK");
		backButton.getStyleClass().add("menuButton");
		backButton.setOnAction(e -> startMenu(primaryStage) );
		VBox  v = new VBox();
		v.getChildren().addAll( info, backButton );

		Scene scene = new Scene( v , 500, 500);
		scene.getStylesheets().add("main.css");
		primaryStage.setScene( scene );
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}