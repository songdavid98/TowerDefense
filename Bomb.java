import java.util.Set;
import java.util.Iterator;
import javafx.scene.image.Image;

public class Bomb extends Projectile {
	protected int speed = 12;
	private int explosionRange = 125;
	private int collisionDistance = 40;
	protected int health = 1;
	protected int damage = 1;

	public Bomb() {
		setImage( new Image("images/bomb.png") );
	}
	public Bomb(int dam) {
		setImage( new Image("images/bomb.png") );
		damage = dam;
	}

	public void attackEnemies( Set<Enemy> enemies) {
		Iterator<Enemy> i = enemies.iterator();
		while ( i.hasNext() ) {
			Enemy e = i.next();
			if ( e.distanceFrom( getX() +16, getY()+16) < collisionDistance ) {
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

	//for some reason, the health variable wasn't being inherited correctly
	public boolean isDead() {
		return (health < 1 || distanceTraveled > maxRange);
	}

	public void setXY( double x, double y ) {
		super.setXY(x, y);
	}

	public Bomb clone() {
		Bomb b = new Bomb();
		b.health = health;
		b.speed = speed;
		b.dx = dx;
		b.dy = dy;
		b.setX( getX() );
		b.setY( getY() );
		b.distanceTraveled = distanceTraveled;
		b.maxRange = maxRange;
		b.setImage( getImage() );
		return b;
	}
}