package games.cathedralBloxxx;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

public class Pendulum {

	private World world;
	private int x;
	private int y;
	private float speed;
	private float theta;
	private float length;

	private double omega;
	private Block block;
	private Image corde;
	private float initialAngle;

	public Pendulum(GameContainer container, World world) {
		this.world = world;
		x=container.getWidth()/2;
		y=-1400;
		speed=8000;
		theta=0;
		length=1800;
		initialAngle=(float) (-Math.PI/12);
		omega=World.GRAVITY/length;
		loadImage();
		addBlock();
	}

	private void loadImage() {
		corde=AppLoader.loadPicture(World.DIRECTORY_IMAGES+"corde.png").getScaledCopy(10, (int) length);
	}

	private void addBlock() {
		block=new Block(this.world,0,0,100,100,AppLoader.loadPicture(World.DIRECTORY_IMAGES+"Blocs/"+world.getColorImage()+" Normal.png"));
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.white);

		g.drawImage(corde,x,y);
		corde.setRotation((float) (-theta*180.0f/Math.PI));
		corde.setCenterOfRotation(0, 0);

		block.render(container, game, g);
	}

	public void update(GameContainer container, StateBasedGame game, int arg2) {
		theta=calculateTheta(); //theta=theta0cos(wt)
		block.update(container, game, arg2);
		if(!block.isRealeased()){
			block.setX(x+ (float)(length*Math.sin(theta))-block.getWidth()/2);
			block.setY(y+ (float)(length*Math.cos(theta))-block.getHeight()/2);
			block.setAngle((float) (-theta*180/Math.PI));
		}

		speed+=0.01;
		speed=Math.min(speed,18000);
	}

	private float calculateTheta() {
		return (float) (initialAngle*Math.cos(speed*(omega*world.getTimeInMillis()/1000.0)));
	}

	private float calculateThetaDot() {
		return (float) (initialAngle*-1*omega*speed*Math.sin(speed*(omega*world.getTimeInMillis()/1000.0)));
	}

	public void releaseBlock() {
		if(block.isRealeased())return;
		float thetaDot=calculateThetaDot();
		block.drop((float) (-thetaDot/3000*180.0f/Math.PI),(float)(length*thetaDot*1+Math.pow(Math.tan(theta),2))/1800,0);
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public void notifyStackedBlock() {
		addBlock();
	}

	public void setLength(float length) {
		this.length = length;
		loadImage();
	}

	public void setSpeed(float f) {
		this.speed = f;
	}

	public Block getBlock() {
		return block;
	}

	public void finishTower() {
		block=new Block(this.world,world.getTower().getTop().getX(),0,100,100, AppLoader.loadPicture(World.DIRECTORY_IMAGES+"Blocs/"+world.getColorImage()+" Toit.png"));
		block.setSpeedY(2f);
		block.setSpeedX(0f);
		block.setRealeased(true);
		block.setIsDroping(true);
	}

}
