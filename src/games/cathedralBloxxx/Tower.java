package games.cathedralBloxxx;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class Tower extends Rectangle {

	private World world;
	private ArrayList<Block> blocks;
	private boolean needDefile;
	private int mult;
	private boolean comb;
	private double alpha;
	private double amplitude;

	public Tower(World world, float x, float y, Block initialBlock) {
		super(x, y, 0, 0);
		this.world = world;
		blocks = new ArrayList<Block>();
		addBlock(initialBlock);
		this.comb = false;
		this.alpha = 1;
		this.amplitude = 0;
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) {
		// Rendering

		for(int i = 0; i < blocks.size(); i++){
			blocks.get(i).render(arg0, arg1, g);
		}
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) {
		// Updating

		for(int i = 0; i < blocks.size(); i++){
			blocks.get(i).update(arg0, arg1, arg2);
			blocks.get(i).setX(blocks.get(i).getX()+((float) (amplitude*Math.cos(alpha*world.getTimeInMillis()/1000))));
			if (needDefile==true){
				blocks.get(i).setY(blocks.get(i).getY()+arg2/5);
			}
		}
		if(needDefile)world.getDecor().setHeight(world.getDecor().getHeight()+arg2/5);

		if(blocks.size()>1)
		{
			if(blocks.get(blocks.size()-2).getY()<arg0.getHeight())needDefile=true;
			else needDefile=false;
		}
	}

	private void addBlock(Block initialBlock) {
		blocks.add(initialBlock);
	}

	public float getTopY() {
		return(blocks.get(blocks.size()-1).getY());
	}

	public float getTopX() {
		return(blocks.get(blocks.size()-1).getX());
	}

	public Block getTop() {
		return(blocks.get(blocks.size()-1));
	}

	private boolean combo(float topX, float x) {
		if(Math.abs(topX-x)<10){
			return true;
		}
		return false;
	}

	//O: il n'est pas pret de tomber sur le tower;
	//1: il tombe sur le tower;
	//2: vascille a gauche;
	//3: vascille a droite;

	public int isColliding(Shape shape) {
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
		if(world.getDifficulty()==0){
			world.setScore(world.getScore()+mult*100);
		}else{
			world.setScore(world.getScore()+mult*2*world.getDifficulty()*100);
		}
		world.getPendulum().notifyStackedBlock();

		System.out.println("top="+getTop().getX());
		System.out.println("block="+block.getX());

		amplitude+=Math.abs((getTop().getX()-block.getX())/3)/100;
		blocks.add(block);
	}

}
