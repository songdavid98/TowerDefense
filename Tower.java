import java.util.Set;
import java.util.Iterator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.property.DoubleProperty;


public class Tower extends ImageView {

	private int range = 200;
	private int damage = 1;

	private Enemy target = null;
	private Projectile projectile;

	private int RATE = 20;
	private int cooldown = 0;

	public Tower() {
		setFitHeight(64);
		setFitWidth(64);
		projectile = new Projectile(damage);
	}

	public Tower( int x, int y, int r, int d, int rate) {
		Image i = new Image("images/dartMonkey.png");
		setImage( i );
		setFitHeight(64);
		setFitWidth(64);
		setX(x);
		setY(y);
		range = r;
		damage = d;
		this.RATE = rate;
		projectile = new Projectile(damage);
		projectile.setXY(getX() + 32, getY() + 32);
	}

	public void findTarget( Set<Enemy> enemies) {
		if (enemies.isEmpty()) {
			target = null;
			return;
		}
		target = enemies.iterator().next();
		if ( target.distanceFrom(getX(), getY()) < range )
			return;
		for (Enemy e : enemies) {
			if ( e.distanceFrom( getX(), getY()) < range ) {
				if ( target.getDistanceTraveled() > e.getDistanceTraveled() )
					target = e;
			}
		}
		//yet to do
	}

	public Projectile attack() {
		if ( cooldown <= 0 && target != null) {
			cooldown = RATE;
			Projectile p = projectile.clone();
			p.setImage(projectile.getImage());
			double base = target.getCenterX() - p.getX();
			double height = target.getCenterY() - p.getY();
			double dx = base/target.distanceFrom(getX(), p.getX());
			double dy = height/target.distanceFrom(getX(), p.getY());
			p.setdXY( dx, dy);
			if (height == 0) {
				if (base > 0)
					p.setRotate(90);
				else
					p.setRotate(-90);
				return p;
			}
			double degrees = Math.toDegrees( Math.atan( -1*base / height) );
			if ( height > 0)
				degrees += 180;
			p.setRotate( degrees );
			setRotate(degrees);
			return p;	
		}
		cooldown--;
		return null;
	}
}