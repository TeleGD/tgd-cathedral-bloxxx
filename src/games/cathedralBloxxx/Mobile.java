package games.cathedralBloxxx;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

public class Mobile extends Rectangle {

	private Decor decor;
	private Image image0;
	private Image image1;

	private float goToX;
	private float goToY;
	private boolean isFlying;
	private int timer;
	private int nb;
	private float alpha;

	private Audio sound;

	public Mobile(GameContainer container, Decor decor, String urlImage, String urlImage2, String urlSound) {
		super(50, 50, 120, 60);
		this.decor = decor;
		if(sound==null){
			sound=AppLoader.loadAudio(urlSound);
		}
		leave(container);

		timer = (int) ((Math.random() + 1) * 600);

		this.x = goToX;
		this.y = goToY;

		image0=AppLoader.loadPicture(urlImage).getScaledCopy((int)width, (int)height);
		image1=AppLoader.loadPicture(urlImage2).getScaledCopy((int)width, (int)height);

		this.nb = 0;
		this.alpha = 0.02f;
	}

	public void start(GameContainer container) {
		majGoTo(container);
		isFlying = true;
		sound.playAsSoundEffect(1,.3f,true);
	}

	private void majGoTo(GameContainer container) {
		goToX = new Random().nextInt(container.getWidth());
		goToY = new Random().nextInt(container.getHeight());
	}

	public void stop(GameContainer container) {
		isFlying = false;
		leave(container);
	}

	private void leave(GameContainer container) {
		goToX = Math.random() < 0.5 ? -container.getWidth()-250 : container.getWidth()+250;
		goToY = (float) (container.getHeight() * Math.random());
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) {
		// Rendering
		nb ++;
		if(nb%10 <= 5){
			g.drawImage(image0,x,y);
		} else {
			g.drawImage(image1,x,y);
		}
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int delta) {
		// Updating
		x = lerp(x,goToX, alpha);
		y = lerp(y, goToY, alpha);
		if( (x - goToX) < 2 && (y - goToY) < 2 ){
			if(isFlying){
				majGoTo(arg0);
			}else{
				if(x>arg0.getWidth() || x<0){
					decor.getMobiles().remove(this);
					sound.stop();
				}
			}
		}
		if(nb > timer){
			stop(arg0);
		}
	}

	private float lerp(float point1, float point2, float alpha) {
		return point1 + alpha * (point2 - point1);
	}

}
