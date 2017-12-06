public class Dart extends Projectile {

	private int health = 1;
	private int damage = 2;
	private int speed = 15;
	private double dx = 1;
	private double dy = 0;
	private int maxRange = 300;

	public Dart( int dam ) {
		super(dam);
	}
}