package menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import game3.world.MainMenu3;
import game3.world.World3;

public class MainMenu extends Menu{

	public static int ID = -3;

	public MainMenu(){
		super.setTitrePrincipal("MULTIGAME DESIGN");
		super.setTitreSecondaire("Menu Principal");
		super.setItems(World3.GAME_NAME,"Scores", "Quitter");

		super.setEnableClignote(false);
		super.setCouleurClignote(Color.red);
		super.setTempsClignote(400);
	}

	@Override
	public void onOptionItemFocusedChanged(int position) {
		time=System.currentTimeMillis();
	}

	@Override
	public void onOptionItemSelected(int position) {
		switch (position) {
		case 0:
			game.enterState(MainMenu3.ID, new FadeOutTransition(),
					new FadeInTransition());
			break;
		case 1:
			System.out.println("gone there");
			game.enterState(ScoreMenu.ID, new FadeOutTransition(),
					new FadeInTransition());
			break;
		case 2:
			System.out.println("exit");
			System.exit(0);
			break;
		}
	}

	@Override
	public int getID() {
		return ID;
	}

}
