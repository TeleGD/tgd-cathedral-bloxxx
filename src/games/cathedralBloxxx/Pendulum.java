package games.cathedralBloxxx;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import general.Main;

public class Pendulum extends BasicGameState{
	private int x;
	private int y;
	private float speed;
	private float theta;
	private float length;


	private double omega;
	private Block block;
	private Image corde;

	public Pendulum(){
		x=Main.longueur/2;
		y=-1400;
		speed=8000;
		length=1800;
		initialAngle=(float) (-Math.PI/12);
		omega=World.GRAVITY/length;
		loadImage();
		addBlock();
	}

	public void loadImage(){
		try {
			corde=new Image(World.DIRECTORY_IMAGES+"corde.png").getScaledCopy(10, (int) length);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void addBlock() {
		try {
			block=new Block(0,0,100,100,new Image(World.DIRECTORY_IMAGES+"Blocs/"+World.colorImage+" Normal.png"));
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		theta=0;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.white);

		g.drawImage(corde,x,y);
		corde.setRotation((float) (-theta*180.0f/Math.PI));
		corde.setCenterOfRotation(0, 0);

		block.render(container, game, g);


	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int arg2) throws SlickException {
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
		return (float) (initialAngle*Math.cos(speed*(omega*World.getTimeInMillis()/1000.0)));
	}
	private float calculateThetaDot() {
		return (float) (initialAngle*-1*omega*speed*Math.sin(speed*(omega*World.getTimeInMillis()/1000.0)));
	}


	public void releaseBlock() {
		if(block.isRealeased())return;
		float thetaDot=calculateThetaDot();
		block.drop((float) (-thetaDot/3000*180.0f/Math.PI),(float)(length*thetaDot*1+Math.pow(Math.tan(theta),2))/1800,0);
	}




	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getX() {
		return this.x;
	}
	public float getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y=y;
	}

	public void setX(int x) {
		this.x=x;
	}


	public void notifyStackedBlock() {
		addBlock();
	}


	public float getLength() {
		return length;
	}



	public void setLength(float length) {
		this.length = length;
		loadImage();
	}




	private float initialAngle;
	public float getInitialAngle() {
		return initialAngle;
	}



	public void setInitialAngle(float initialAngle) {
		this.initialAngle = initialAngle;
	}

	public float getSpeed() {
		return speed;
	}



	public void setSpeed(float f) {
		this.speed = f;
	}

	public Block getBlock() {
		return block;
	}
	public void finishTower() throws SlickException {
		block=new Block(World.getTower().getTop().getX(),0,100,100, new Image(World.DIRECTORY_IMAGES+"Blocs/"+World.colorImage+" Toit.png"));
		block.setSpeedY(2f);
		block.setSpeedX(0f);
		block.setRealeased(true);
		block.setIsDroping(true);
	}


}
