package games.cathedralBloxxx;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

public class Block extends Rectangle {

	private World world;
	private float speedX;
	private float speedY;
	private float angle;
	private float accelY;

	private boolean isDroping;
	private boolean isRealeased;
	private Image image;
	private Audio soundPop;
	private float accelX;
	private boolean willDeath;
	private float speedAngle;

	public Block(World world, float x, float y, float width, float height,Image image) {
		super(x, y, width, height);
		this.image=image.getScaledCopy((int)width, (int)height);
		init(world);
	}

	private void init(World world) {
		this.world = world;
		this.isDroping = false;
		this.isRealeased = false;
		soundPop=AppLoader.loadAudio(World.DIRECTORY_SOUNDS+"pop.wav");
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) {
		// Rendering

		//g.rotate(arg0.getWidth()/2, -100, -(float) (angle*180/Math.PI));
		//g.rotate(getCenterY(), getCenterY(), (float) (angle*180/Math.PI));
		image.setRotation(angle);
		g.drawImage(image,x,y);
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int delta) {
		// Updating

		x += speedX*delta;
		y += speedY*delta;
		angle+=speedAngle*delta;

		speedX+=accelX;
		speedY+=accelY;

		if(isDroping){
			int isColliding=world.getTower().isColliding(this);
			if( isColliding==1){
				world.getTower().blockCollidedWithTower(this);
				soundPop.playAsSoundEffect(1,.3f,false);
			}else if(isColliding==2){// frole a gauche
				if(willDeath==false){
					this.speedX-=1.3f;
					AppLoader.loadAudio(World.DIRECTORY_SOUNDS+"game_over.ogg").playAsSoundEffect(1,.3f,false);
				}
				this.speedY=-0.5f;

				float angle=90*(world.getTower().getTopX()-getX()-width/2)/(width/2);
				setAngle(360-angle);
				willDeath=true;
			}else if(isColliding==3){
				if(willDeath==false){
					this.speedX+=1.3f;
					AppLoader.loadAudio(World.DIRECTORY_SOUNDS+"game_over.ogg").playAsSoundEffect(1,.3f,false);
				}
				this.speedY=-0.5f;
				float angle=90*(getX()-world.getTower().getTopX()-width/2)/(width/2);
				setAngle(angle);
				willDeath=true;
			}else if(willDeath){
				float angle=90*(getX()-world.getTower().getTopX()-width/2)/(width/2);
				setAngle(angle);
			}
		}
	}

	// Methods =================================================================================

	public void drop(float angleSpeed, float speedX, float speedY) {
		if(isDroping)return;
		this.speedY = speedY;
		this.speedX = speedX;
		this.speedAngle=angleSpeed;
		this.accelY= World.GRAVITY;
		isDroping = true;
		isRealeased = true;
	}

	// Getters and Setters =====================================================================

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}

	public void setAngle(float angle) {
		this.angle=angle;
	}

	public void setAccelY(float accelY) {
		this.accelY=accelY;
	}

	public boolean isDroping() {
		return isDroping;
	}

	public void setIsDroping(boolean b) {
		this.isDroping = b;
	}

	public boolean isRealeased() {
		return isRealeased;
	}

	public void setRealeased(boolean isRealeased) {
		this.isRealeased = isRealeased;
	}

	public void setAngleSpeed(int speedAngle) {
		this.speedAngle=speedAngle;
	}

	public float getAngle() {
		return angle;
	}

}
