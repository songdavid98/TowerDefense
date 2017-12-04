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

import javafx.util.Duration;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.control.RadioButton; 
import javafx.scene.control.ToggleGroup; 

public class Run extends Application {

	private static Pane playArea = new Pane();
	private static Set<Enemy> enemies = Collections.synchronizedSet( 
		new HashSet<Enemy>() );
	public static HashSet<Projectile> projectiles = new HashSet<Projectile>();
	public static HashSet<Tower> towers = new HashSet<Tower>();
	private static HashSet<Timer> timers = new HashSet<Timer>();
	private static HashSet<Thread> threads = new HashSet<Thread>();
	
	private static String selection = "";

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

		VBox v = PaneCreator.startMenu();

		Button startButton = new Button("START");
		Button helpButton = new Button("HELP");
		Button quitButton = new Button("QUIT");

		startButton.getStyleClass().add("menuButton");
		helpButton.getStyleClass().add("menuButton");
		quitButton.getStyleClass().add("menuButton");

		startButton.setOnAction( e -> playScene( primaryStage ) );

		helpButton.setOnAction( e -> helpScene( primaryStage ) );
		quitButton.setOnAction( e -> Platform.exit() );

		v.getChildren().addAll( startButton, helpButton, quitButton);

		Scene scene = new Scene(v, 500, 500);
		scene.getStylesheets().add("main.css");
		
		primaryStage.setTitle("Run");
		primaryStage.setScene( scene );
		primaryStage.show();
	}

	public HBox topMenu(Stage primaryStage) {
		Button playButton = new Button("Start Round");
		playButton.getStyleClass().add("menuButton");
		playButton.setOnAction( e -> startRound() );

		Button backButton = new Button("EXIT");
		backButton.getStyleClass().add("menuButton");
		backButton.setOnAction(e -> {
			timers.stream()
				.forEach( t -> t.cancel() );
			threads.stream()
				.forEach( t -> t.interrupt());
			startMenu(primaryStage); 
		});

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
		frame.run();
		Tower t = new Tower(325, 200, 500, 1, 20);
		towers.add( t );
		playArea.getChildren().add( t );
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
			p.attackEnemies(enemies);
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

		Pane controls = PaneCreator.controls();

		RadioButton rbDart = new RadioButton("Dart Monkey");
		RadioButton rbCannon = new RadioButton("Cannon");
		RadioButton rbSuper = new RadioButton("Super Monkey");
		ToggleGroup towerSelect = new ToggleGroup();
		rbDart.setToggleGroup( towerSelect );
		rbCannon.setToggleGroup( towerSelect );
		rbSuper.setToggleGroup( towerSelect );
		rbDart.setOnAction(e -> selection = "dart");
		rbCannon.setOnAction(e -> selection = "cannon");
		rbSuper.setOnAction(e -> selection = "super");

		VBox v = new VBox(8);
		v.getChildren().addAll( topMenu(primaryStage), rbDart, rbCannon, rbSuper );

		controls.getChildren().add( v );

		playArea = PaneCreator.map();
		p.getChildren().addAll( playArea , controls );

		Scene scene = new Scene( p , 1000, 750);

		scene.getStylesheets().add("main.css");
		primaryStage.setTitle("Play");
		primaryStage.setScene( scene );
		primaryStage.show();
	}

	public void helpScene(Stage primaryStage) {
		Button backButton = new Button("BACK");
		backButton.getStyleClass().add("menuButton");
		backButton.setOnAction(e -> startMenu(primaryStage) );
		VBox  v = PaneCreator.helpMenu();
		v.getChildren().add( backButton );

		Scene scene = new Scene( v , 500, 500);
		scene.getStylesheets().add("main.css");
		primaryStage.setScene( scene );
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}