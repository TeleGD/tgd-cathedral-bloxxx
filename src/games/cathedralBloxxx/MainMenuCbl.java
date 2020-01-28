package games.cathedralBloxxx;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import app.AppLoader;

import games.cathedralBloxxx.World;

import menus.Menu;

public class MainMenuCbl extends Menu {

	private Image[] image;

	public MainMenuCbl(int ID) {
		super(ID);

		super.setTitrePrincipal("CATHEDRAL BLOXXX");
		super.setTitreSecondaire("Choisissez votre difficulté");
		super.setItems("Facile","Moyen","Difficile","Menu Principal");

		super.setEnableClignote(true);
		super.setCouleurClignote(Color.red);
		super.setTempsClignote(400);
		this.image = new Image[3];
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		image[0]=AppLoader.loadPicture(World.DIRECTORY_IMAGES+"Blocs/Rouge Normal.png").getScaledCopy(50, 50);
		image[1]=AppLoader.loadPicture(World.DIRECTORY_IMAGES+"Blocs/Bleu Normal.png").getScaledCopy(50, 50);
		image[2]=AppLoader.loadPicture(World.DIRECTORY_IMAGES+"Blocs/Vert Normal.png").getScaledCopy(50, 50);
	}

	@Override
	public void renderSelectionItem(GameContainer arg0, StateBasedGame arg1, Graphics g, int position) {
		super.renderSelectionItem(container, game, g, position);
		if (position<3){
			g.drawImage(image[position], arg0.getWidth()/2-fontItem.getWidth(items.get(indexItemPlusGrand))/2-125, 340 + 30 * selection);
			g.drawImage(image[position], arg0.getWidth()/2-fontItem.getWidth(items.get(indexItemPlusGrand))/2+120, 340 + 30 * selection);
		}
	}

	@Override
	public void onOptionItemFocusedChanged(int position) {
		time=System.currentTimeMillis();
	}

	@Override
	public void onOptionItemSelected(int position) {

		((World) game.getState(3)).setDifficulty(position);
		if(position<3){
			game.enterState(3, new FadeOutTransition(), new FadeInTransition());
		}else if (position==3){
			game.enterState(1, new FadeOutTransition(),new FadeInTransition());
		}
	}

}
