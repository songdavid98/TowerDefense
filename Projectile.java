import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.HashSet;

public class Projectile extends ImageView {

	private int damage = 1;
	private int speed = 10;
	private double dx = 1;
	private double dy = 0;

	private double distanceTraveled = 0;
	private int maxRange = 500;

	public Projectile( int dam ) {
		Image i = new Image("images/dart.png");
		setImage( i );
		damage = dam;
		setX(0);
		setY(0);
	}

	public void setXY( int x, int y ) {
		setX(x);
		setY(y);
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
		return distanceTraveled > maxRange;
	}

	public void damage(Enemy e) {
		e.takeDamage( damage );
	}

	public Enemy enemyInRange( HashSet<Enemy> enemies) {
		return new Enemy();
	}



}