package general;

import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import game3.world.MainMenu3;
import game3.world.World3;
import menus.HighScorePlayerMenu;
import menus.MainMenu;
import menus.ScoreMenu;
import menus.WelcomeMenu;

public class Main extends StateBasedGame{

	public static int longueur=1280;
	public static int hauteur=720;

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Main(),longueur, hauteur, false);
		app.setTargetFrameRate(60);
		app.setVSync(true);
		app.setShowFPS(true);
		app.start();
	}


	public Main() {
		super("MultiGame");
	}



	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new WelcomeMenu());
		addState(new MainMenu());
		addState(new World3());
		addState(new MainMenu3());
		addState(new ScoreMenu());
		addState(new HighScorePlayerMenu());


		this.enterState(WelcomeMenu.ID);
	}
}
