package game3.world;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class Block extends Rectangle {

	private float speedX;
	private float speedY;
	
	private float successY;
	
	public Block(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		// Rendering
		arg2.setColor(Color.cyan);
		arg2.drawRect(x, y, width, height);
	}
	
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// Updating
		x += speedX;
		y += speedY;
	}

	// Methods =================================================================================

	public void drop(){
		successY = World3.getTower().gettopY();
		speedY = World3.GRAVITY;
		speedX = 5;
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
	
}
