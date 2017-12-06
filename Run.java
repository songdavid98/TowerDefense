import javafx.application.Application; 
import javafx.application.Platform;
import javafx.stage.Stage; 
import javafx.scene.Scene; 
import javafx.scene.layout.Pane;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.property.SimpleIntegerProperty;

public class Run extends Application {

	public static Pane playArea = new Pane();
	public static Set<Enemy> enemies = Collections.synchronizedSet( 
		new HashSet<Enemy>() );
	public static HashSet<Projectile> projectiles = new HashSet<Projectile>();
	public static HashSet<Tower> towers = new HashSet<Tower>();
	private static HashSet<Timer> timers = new HashSet<Timer>();
	private static HashSet<Thread> threads = new HashSet<Thread>();
	
	private static Stage primaryStage;
	private static String selection = "";
	public static SimpleIntegerProperty score = new SimpleIntegerProperty(0);
	public static SimpleIntegerProperty money = new SimpleIntegerProperty(1000);

	@Override
	public void start(Stage stage) {
		primaryStage = stage;
		Pane pane = new Pane();
		Scene scene = new Scene(pane, 500, 500);
		scene.getStylesheets().add("main.css");
		primaryStage.setTitle("Run");
		primaryStage.setScene( scene );
		primaryStage.show();
		startMenu();
	}

	public static void startMenu() {
		Pane v = PaneCreator.startMenu();

		Scene scene = new Scene(v, 500, 500);
		scene.getStylesheets().add("main.css");
		
		primaryStage.setTitle("Run");
		primaryStage.setScene( scene );
		primaryStage.show();
	}

	public static void startRound() {
		if ( !threads.isEmpty() )
			return;
		money.set(1000);
		Thread spawn = new Thread( () -> spawnEnemies() );
		spawn.run();
		Thread frame = new Thread( () -> playFrame() );
		frame.run();
		threads.add( spawn );
		threads.add( frame );
	}

	public static void spawnEnemies() {
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
		timer.schedule( task , 0, 300);
	}

	public static void playFrame() {
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

	public static void runEnemies() {
		boolean isLoss = false;
		Iterator<Enemy> i = enemies.iterator();
		while ( i.hasNext() ) {
			Enemy e = i.next();
			if ( e.isDead() ) {
				playArea.getChildren().remove(e);
				i.remove();
				money.set( money.get() + 1);
				score.set( score.get() + 1);
				break;
			}
			if (e.getCenterY() == 650 && e.getCenterX() < 1 ) {
				isLoss = true;
				break;
			}
			e.move();
		}
		if (isLoss)
			loseScene();
	}

	public static void runProjectiles() {
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

	public static void runTowers() {
		for (Tower t : towers) {
			t.findTarget( enemies );
			Projectile p = t.attack();
			if (p != null) {
				projectiles.add(p);
				playArea.getChildren().add( p );
			}
		}
	}

	public static void playScene() {
		Pane p = new Pane();

		Pane controls = PaneCreator.controls();

		playArea = PaneCreator.map();
		p.getChildren().addAll( playArea , controls );

		Scene scene = new Scene( p , 1000, 750);

		scene.getStylesheets().add("main.css");
		primaryStage.setTitle("Play");
		primaryStage.setScene( scene );
		primaryStage.show();
	}

	public static void helpScene() {
		Pane v = PaneCreator.helpMenu();
		Scene scene = new Scene( v , 500, 500);
		scene.getStylesheets().add("main.css");
		primaryStage.setScene( scene );
		primaryStage.show();
	}

	public static void loseScene() {
		timers.stream()
			.forEach( t -> t.cancel() );
		threads.stream()
			.forEach( t -> t.interrupt());
		timers.clear();
		threads.clear();
		towers.clear();
		enemies.clear();
		projectiles.clear();
		Pane v = PaneCreator.loseScene();
		Scene scene = new Scene( v , 500, 500);
		scene.getStylesheets().add("main.css");
		primaryStage.setScene( scene );
		primaryStage.show();
	}

	public static void buyTower(int x, int y) {
		Tower t = new Tower();
			switch( selection ) {
				case "dart": 	t = new DartMonkey();
								break;
				case "cannon": 	t = new Cannon();
								break;
				default: 		return;
			}
			if ( t.getPrice() > money.getValue() )
				return;
			money.set(Run.money.get() - t.getPrice() );
			t.setXY( x-32, y-32 );
			towers.add(t);
			playArea.getChildren().add(t);
	}

	public static void setSelection(String s) {
		selection = s;
	}
	public static String getSelection() {
		return selection;
	}					
	public static HashSet<Timer> getTimers() {
		return timers;
	}
	public static HashSet<Thread> getThreads() {
		return threads;
	}
	public static SimpleIntegerProperty getMoney() {
		return money;
	}
	public static SimpleIntegerProperty getScore() {
		return score;
	}

	public static void main(String[] args) {
		launch(args);
	}
}