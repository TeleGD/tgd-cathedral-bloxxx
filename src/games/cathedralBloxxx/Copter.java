package games.cathedralBloxxx;

import org.newdawn.slick.GameContainer;

public class Copter extends Mobile{

	public Copter(GameContainer container, Decor decor) {
		super(container,decor,World.DIRECTORY_IMAGES+"Copter/copter1.png", World.DIRECTORY_IMAGES+"Copter/copter2.png",World.DIRECTORY_SOUNDS+"copter.ogg");
	}

}
