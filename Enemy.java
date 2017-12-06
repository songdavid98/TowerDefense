import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class Enemy extends Circle {

	private int health = 1;
	private int speed = 2;

	private double dx = 1;
	private double dy = 0;

	private double distanceTraveled = 0;

	public Enemy() {
		setCenterX(0);
		setCenterY(300);
		setRadius(25);
		setFill( Color.RED );
	}

	public Enemy(int h, int s) {
		health = h;
		speed = s;
		setCenterX(0);
		setCenterY(300);
		setRadius(25);
		if (health < 3)
			setFill( Color.RED );
		else if (health < 5)
			setFill( Color.BLUE );
		else if (health < 7)
			setFill( Color.GREEN );
		else if (health < 9)
			setFill( Color.YELLOW);
		else if (health < 11)
			setFill( Color.PINK );
	}

	public boolean isDead() {
		return health < 1;
	}

	public double distanceFrom(double x2, double y2) {
		double dx = getCenterX() - x2;
		double dy = getCenterY() - y2;
		return Math.sqrt( dx*dx + dy*dy );
	}

	public void takeDamage(int d) {
		health -= d;
		speed -= d;
		if (health < 3)
			setFill( Color.RED );
		else if (health < 5)
			setFill( Color.BLUE );
		else if (health < 7)
			setFill( Color.GREEN );
		else if (health < 9)
			setFill( Color.YELLOW);
		else if (health < 11)
			setFill( Color.PINK );
	}

	public void setDirection( int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void move() {
		if (distanceTraveled < 525) {
			if ( distanceTraveled < 310 && getCenterX() > 300 ) {
				setCenterX( 300 );
				dx = 0;
				dy = -1;
			}
			else if ( getCenterY() < 100 ) {
				setCenterY( 100 );
				dx = 1;
				dy = 0;
			}
		}
		else if ( distanceTraveled < 1375) {
			if ( getCenterY()  > 650 ) {
				setCenterY(650);
				dx = -1;
				dy = 0;
			}
			else if ( getCenterX() > 600 ) {
				setCenterX( 600 );
				dx = 0;
				dy = 1;
			}
		}
		
		//implement the turning
		setCenterX( getCenterX() + speed * dx);
		setCenterY( getCenterY() + speed * dy);
		distanceTraveled += speed;
	}

	public double getDistanceTraveled() {
		return distanceTraveled;
	}
	public int getHealth() {
		return health;
	}
	public String toString() {
		return Double.toString( getCenterX() ) + ", " + Double.toString( getCenterY() );
	}

}