import java.util.Set;
import java.util.Iterator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Tower extends ImageView {

	private int range = 250;
	private int damage = 1;

	private Enemy target = null;

	private int x = 0;
	private int y = 0;

	private final int RATE;
	private int cooldown = 0;

	public Tower( int x, int y, int r, int d, int rate) {
		range = r;
		damage = d;
		this.x = x;
		this.y = y;
		this.RATE = rate;
	}

	public void findTarget( Set<Enemy> enemies) {
		if (enemies.isEmpty())
			return;
		target = enemies.iterator().next();
		if ( target.distanceFrom(x, y) < range )
			return;
		for (Enemy e : enemies) {
			if ( e.distanceFrom(x, y) < range ) {
				if ( target.getDistanceTraveled() > e.getDistanceTraveled() )
					target = e;
			}
		}
		//yet to do
	}

	public Projectile attack() {
		if ( cooldown == 0 ) {
			cooldown = RATE;
			Projectile p = new Projectile( damage );
			p.setXY( x, y);
			double height = target.getCenterX() - x;
			double base = target.getCenterY() - y;
			double dx = height/target.distanceFrom(x, y);
			double dy = base/target.distanceFrom(x, y);
			p.setdXY( dx, dy);

			return p;	
		}
		cooldown--;
		return null;
	}
}