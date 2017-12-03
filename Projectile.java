import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.Iterator;
import java.util.Set;

public class Projectile extends ImageView{

	private int health = 1;
	private int damage = 1;
	private int speed = 15;
	private double dx = 1;
	private double dy = 0;

	private double distanceTraveled = 0;
	private int maxRange = 400;

	public Projectile() {

	}

	public Projectile( int dam ) {
		Image i = new Image("images/dart.png");
		setImage( i );
		damage = dam;
		setX(0);
		setY(0);
	}

	public void setXY( double x, double y ) {
		setX(x-16);
		setY(y-16);
	}

	public void setdXY( double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void move() {
		setX( getX() + speed * dx);
		setY( getY() + speed * dy);
		distanceTraveled += speed;
	}

	public boolean isDead() {
		return (health < 1 || distanceTraveled > maxRange);
	}

	public void attackEnemies( Set<Enemy> enemies) {
		Iterator<Enemy> i = enemies.iterator();
		while ( i.hasNext() ) {
			Enemy e = i.next();
			if ( e.distanceFrom( getX() +16, getY()+16) < 40 ) {
				e.takeDamage( damage );
				health--;
				break;
			}
		}
	}

	public Projectile clone() {
		Projectile p = new Projectile();
		p.health = health;
		p.speed = speed;
		p.dx = dx;
		p.dy = dy;
		p.setX( getX() );
		p.setY( getY() );
		p.distanceTraveled = distanceTraveled;
		p.maxRange = maxRange;
		p.setImage( getImage() );
		return p;
	}

}