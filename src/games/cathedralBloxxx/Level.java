package games.cathedralBloxxx;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import app.AppMenu;
import app.elements.MenuItem;

import games.cathedralBloxxx.World;

public class Level extends AppMenu {

	public Level(int ID) {
		super(ID);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		super.initSize(container, game, 600, 400);
		super.init(container, game);
		this.setTitle("Niveau");
		this.setSubtitle("Sans sous-titre");
		this.setMenu(Arrays.asList(new MenuItem[] {
			new MenuItem("Facile") {
				public void itemSelected() {
					((World) game.getState(3)).setDifficulty(0);
					game.enterState(3, new FadeOutTransition(), new FadeInTransition());
				}
			},
			new MenuItem("Moyen") {
				public void itemSelected() {
					((World) game.getState(3)).setDifficulty(1);
					game.enterState(3, new FadeOutTransition(), new FadeInTransition());
				}
			},
			new MenuItem("Difficile") {
				public void itemSelected() {
					((World) game.getState(3)).setDifficulty(2);
					game.enterState(3, new FadeOutTransition(), new FadeInTransition());
				}
			},
			new MenuItem("Retour") {
				public void itemSelected() {
					game.enterState(1, new FadeOutTransition(), new FadeInTransition());
				}
			}
		}));
		this.setHint("SELECT A DIFFICULTY LEVEL");
	}

}
