package games.cathedralBloxxx;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

import general.Main;

public class Tower extends Rectangle{

	ArrayList<Block> blocks;
	private boolean needDefile;
	private int mult;
	private boolean comb=false;
	private double alpha=1;
	private double amplitude=0;
	public static int difficulty;

	public Tower(float x, float y, Block initialBlock) {
		super(x, y, 0, 0);
		blocks = new ArrayList<Block>();
		addBlock(initialBlock);
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		// Rendering

		for(int i = 0; i < blocks.size(); i++){
			blocks.get(i).render(arg0, arg1, g);

		}
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// Updating


		for(int i = 0; i < blocks.size(); i++){
			blocks.get(i).update(arg0, arg1, arg2);
			blocks.get(i).setX(blocks.get(i).getX()+((float) (amplitude*Math.cos(alpha*World.getTimeInMillis()/1000))));
			if (needDefile==true){
				blocks.get(i).setY(blocks.get(i).getY()+arg2/5);
			}
		}
		if(needDefile)World.getDecor().setHeight(World.getDecor().getHeight()+arg2/5);

		if(blocks.size()>1)
		{
			if(blocks.get(blocks.size()-2).getY()<Main.hauteur)needDefile=true;
			else needDefile=false;
		}

	}

	public void addBlock(Block initialBlock) {
		blocks.add(initialBlock);
	}

	public float getTopY(){
		return(blocks.get(blocks.size()-1).getY());
	}
	public float getTopX(){
		return(blocks.get(blocks.size()-1).getX());
	}
	public Block getTop(){
		return(blocks.get(blocks.size()-1));
	}


	private boolean combo(float topX, float x) {
		if(Math.abs(topX-x)<10){
			return true;
		}
		return false;
	}

	//O: il n'est pas pret de tomber sur le tower;
	//1: il  tombe sur le tower;
	//2: vascille a gauche;
	//3: vascille a droite;

	public int isColliding(Shape shape){
		if(shape.getY()+shape.getHeight()<getTop().getY())return 0;
		if(shape.getY()>getTop().getY()+getTop().getHeight()/8)return 0;

		if(shape.getX()-getTop().getX()>getTop().getWidth()/2){
			if(shape.getX()-getTop().getX()<getTop().getWidth())return 3;
			else return 0;
		}
		if(shape.getX()-getTop().getX()<-getTop().getWidth()/2){
			if(shape.getX()-getTop().getX()>-getTop().getWidth())return 2;
			else return 0;
		}


		return 1;
	}

	public void blockCollidedWithTower(Block block) {
		block.setSpeedX(0);
		block.setSpeedY(0);
		block.setAccelY(0);
		block.setAngleSpeed(0);

		block.setAngle((int)(block.getAngle()));
		comb=combo(getTopX(), block.getX());
		block.setY(getTopY()-getTop().getHeight());
		block.setIsDroping(false);

		if(comb){
			mult+=1;
		}else{
			mult=1;
		}
		if(World.difficulty==0){

			World.setScore(World.getScore()+mult*100);
		}else{
			World.setScore(World.getScore()+mult*2*World.difficulty*100);
		}
		World.getPendulum().notifyStackedBlock();

		System.out.println("top="+getTop().getX());
		System.out.println("block="+block.getX());

		amplitude+=Math.abs((getTop().getX()-block.getX())/3)/100;
		blocks.add(block);

	}


}
