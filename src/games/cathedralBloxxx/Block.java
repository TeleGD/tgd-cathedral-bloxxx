package games.cathedralBloxxx;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class Block extends Rectangle {

	private float speedX;
	private float speedY;
	private float angle;
	private float successY;
	private float accelY;

	private boolean isDroping = false;
	private boolean isRealeased = false;
	private Image image;
	private Sound soundPop;
	private float accelX;
	private boolean willDeath;
	private float speedAngle;


	public Block(float x, float y, float width, float height,Image image) {
		super(x, y, width, height);
		this.image=image.getScaledCopy((int)width, (int)height);
		init();
	}



	public Block(float x, float y, float width, float height) {
		super(x, y, width, height);
		init();
	}
	private void init() {

		try {
			soundPop=new Sound(World.DIRECTORY_SOUNDS+"pop.wav");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		// Rendering

		//g.rotate(Main.longueur/2, -100, -(float) (angle*180/Math.PI));
		//g.rotate(getCenterY(), getCenterY(), (float) (angle*180/Math.PI));
		image.setRotation(angle);
		g.drawImage(image,x,y);
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int delta) throws SlickException {
		// Updating

		x += speedX*delta;
		y += speedY*delta;
		angle+=speedAngle*delta;

		speedX+=accelX;
		speedY+=accelY;

		if(isDroping){
			int isColliding=World.getTower().isColliding(this);
			if( isColliding==1){
				World.getTower().blockCollidedWithTower(this);
				soundPop.play();
			}else if(isColliding==2){// frole a gauche
				if(willDeath==false){
					this.speedX-=1.3f;
					new Sound(World.DIRECTORY_SOUNDS+"game_over.ogg").play();
				}
				this.speedY=-0.5f;

				float angle=90*(World.getTower().getTopX()-getX()-width/2)/(width/2);
				setAngle(360-angle);
				willDeath=true;
			}else if(isColliding==3){
				if(willDeath==false){
					this.speedX+=1.3f;
					new Sound(World.DIRECTORY_SOUNDS+"game_over.ogg").play();

				}
				this.speedY=-0.5f;
				float angle=90*(getX()-World.getTower().getTopX()-width/2)/(width/2);
				setAngle(angle);
				willDeath=true;

			}else if(willDeath){

				float angle=90*(getX()-World.getTower().getTopX()-width/2)/(width/2);
				setAngle(angle);
			}
		}

	}
	// Methods =================================================================================

	public void drop(float angleSpeed,float speedX,float speedY){
		if(isDroping)return;

		successY = World.getTower().getTopY();
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
		// TODO Auto-generated method stub
		return isDroping;
	}

	public void setIsDroping(boolean b) {
		// TODO Auto-generated method stub
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
