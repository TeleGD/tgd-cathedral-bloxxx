package games.cathedralBloxxx;

import org.newdawn.slick.GameContainer;

public class Alien extends Mobile {

	public Alien(GameContainer container, Decor decor) {
		super(container,decor,World.DIRECTORY_IMAGES+"alien.png", World.DIRECTORY_IMAGES+"alien.png",World.DIRECTORY_SOUNDS+"alien.ogg");
		setWidth(60);
		setHeight(60);
	}

}
