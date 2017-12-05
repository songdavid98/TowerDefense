import java.util.Set;
import java.util.Iterator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.property.DoubleProperty;


public class Tower extends ImageView {

	private int range = 200;
	protected int damage = 1;

	protected Enemy target = null;
	protected Projectile projectile;

	protected int RATE = 20;
	protected int cooldown = 0;
	private int price = 200;

	public Tower() {
		Image i = new Image("images/dartMonkey.png");
		setImage( i );
		projectile = new Projectile(damage);
		projectile.setXY(getX() + 32, getY() + 32);
	}

	public Tower( int x, int y) {
		Image i = new Image("images/dartMonkey.png");
		setImage( i );
		setX(x);
		setY(y);
		projectile = new Projectile(damage);
		projectile.setXY(getX() + 32, getY() + 32);
	}

	public void setXY(int x, int y) {
		setX(x);
		setY(y);
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

	public int getPrice() {
		return price;
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