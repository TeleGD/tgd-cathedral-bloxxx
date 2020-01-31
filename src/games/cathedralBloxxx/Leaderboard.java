package games.cathedralBloxxx;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import app.AppMenu;
import app.elements.MenuItem;

public class Leaderboard extends AppMenu {

	private int difficulty;

	public Leaderboard(int ID) {
		super(ID);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		super.initSize(container, game, 600, 400);
		super.init(container, game);
		this.setTitle("Classement");
		this.setSubtitle("Sans sous-titre");
		this.setHint("LOOK FOR YOUR NAME");
		this.difficulty = 0;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		List<Score> scores = Loader.restoreScores(this.difficulty);
		List<MenuItem> menu = new ArrayList<MenuItem>();
		for (Score score: scores) {
			String player = score.getPlayer();
			if (player.length() > 10) {
				player = player.substring(0, 10);
			} else {
				player = String.format("%-10s", player);
			}
			double count = score.getCount();
			menu.add(new MenuItem(player + " : " + count) {
				public void itemSelected() {}
			});
		}
		menu.add(new MenuItem("Retour") {
			public void itemSelected() {
				game.enterState(1, new FadeOutTransition(), new FadeInTransition());
			}
		});
		this.setMenu(menu);
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

}
