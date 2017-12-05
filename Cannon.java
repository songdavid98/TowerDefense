import javafx.scene.image.Image;

public class Cannon extends Tower {

	private int range = 275;
	private int damage = 1;

	private int RATE = 30;

	public Cannon() {

	}

	public Cannon( int x, int y) {
		super(x, y);
		setImage( new Image("images/cannon.png") );
		projectile = new Bomb(damage);
	}

	public Projectile attack() {
		if ( cooldown <= 0 && target != null) {
			cooldown = RATE;
			Projectile p = new Projectile( damage );
			double realX = getX() + 32;
			double realY = getY() + 32;
			p.setXY( realX, realY);
			double base = target.getCenterX() - realX;
			double height = target.getCenterY() - realY;
			double dx = base/target.distanceFrom(getX(), realX);
			double dy = height/target.distanceFrom(getX(), realY);
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