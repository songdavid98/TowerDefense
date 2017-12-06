import javafx.scene.image.Image;

public class DartMonkey extends Tower {


	public DartMonkey() {
		setImage(  new Image("images/dartMonkey.png") );
		projectile = new Dart(damage);
		projectile.setXY(getX() + 32, getY() + 32);
	}

	public DartMonkey(int x, int y) {
		super(x, y);
		setImage(  new Image("images/dartMonkey.png") );
		projectile = new Dart(damage);
		projectile.setXY(getX() + 32, getY() + 32);
	}


}