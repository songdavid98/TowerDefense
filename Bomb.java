import java.util.Set;
import java.util.Iterator;
import javafx.scene.image.Image;

public class Bomb extends Projectile {
	private int speed = 12;
	private int explosionRange = 75;
	private int collisionDistance = 40;
	private int health = 1;
	private int damage = 1;

	public Bomb() {

	}
	public Bomb(int dam) {
		Image i = new Image("images/dart.png");
		setImage( i );
		damage = dam;
	}

	public void attackEnemies( Set<Enemy> enemies) {
		Iterator<Enemy> i = enemies.iterator();
		while ( i.hasNext() ) {
			Enemy e = i.next();
			if ( e.distanceFrom( getX() +16, getY()+16) < collisionDistance ) {
				e.takeDamage( damage );
				health--;
				break;
			}
		}
		if (health < 1) {
			i = enemies.iterator();
			while ( i.hasNext() ) {
				Enemy e = i.next();
				if ( e.distanceFrom( getX() +16, getY()+16) < explosionRange ) {
					e.takeDamage( damage );
				}
			}
		}
	}
}