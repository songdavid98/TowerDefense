import java.util.Set;
import java.util.Iterator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.property.DoubleProperty;


public class Tower extends ImageView {

	private int range = 175;
	protected int damage = 1;

	protected Enemy target = null;
	protected Projectile projectile;

	protected int RATE = 60;
	protected int cooldown = 0;
	public int price = 200;

	public Tower() {

	}

	public Tower( int x, int y) {
		setX(x);
		setY(y);
	}

	public void setXY(int x, int y) {
		setX(x);
		setY(y);
		projectile.setXY(getX() + 32, getY() + 32);
	}

	public int getPrice() {
		return price;
	}

	public void findTarget( Set<Enemy> enemies) {
		if (enemies.isEmpty()) {
			target = null;
			return;
		}
		target = enemies.iterator().next();
		if ( target.distanceFrom(getX(), getY()) < range )
			return;
		boolean isFound = false;
		for (Enemy e : enemies) {
			if ( e.distanceFrom( getX(), getY()) < range ) {
				isFound = true;
				if ( target.getDistanceTraveled() < e.getDistanceTraveled() )
					target = e;
			}
		}
		if (!isFound)
			target = null;
		//yet to do
	}

	public Projectile attack() {
		if ( cooldown <= 0 && target != null) {
			cooldown = RATE;
			Projectile p = projectile.clone();
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