import java.util.Set;
import java.util.iterator;

public class Tower {

	public int range = 250;
	public int damage = 1;

	public Enemy target = null;

	public double x = 0;
	public double y = 0;

	public Tower( int x, int y, int r, int d) {
		range = r;
		damage = d;
		this.x = x;
		this.y = y;
	}

	public void findTarget( Set<Enemy> enemies) {
		target = enemies.iterator().next();
		if (enemies.isEmpty())
			return;

		for (Enemy e : enemies) {
			if ( e.distanceFrom(x, y) < range ) {
				if ( target.)
			}	
		}

		//yet to do
	}

	public Projectile attack() {
		Projectile p = new Projectile();
		return p;
	}
}