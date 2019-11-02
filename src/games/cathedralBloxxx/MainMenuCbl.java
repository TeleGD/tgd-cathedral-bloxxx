package games.cathedralBloxxx;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import games.cathedralBloxxx.World;
import general.Main;
import menus.MainMenu;
import menus.Menu;

public class MainMenuCbl extends Menu{

	public static int ID = -10;

	private Image[] image= new Image[3];
	public MainMenuCbl(){

		super.setTitrePrincipal("CATHEDRAL BLOXXX");
		super.setTitreSecondaire("Choisissez votre difficulté");
		super.setItems("Facile","Moyen","Difficile","Menu Principal");

		super.setEnableClignote(true);
		super.setCouleurClignote(Color.red);
		super.setTempsClignote(400);

	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		image[0]=new Image(World.DIRECTORY_IMAGES+"Blocs/Rouge Normal.png").getScaledCopy(50, 50);
		image[1]=new Image(World.DIRECTORY_IMAGES+"Blocs/Bleu Normal.png").getScaledCopy(50, 50);
		image[2]=new Image(World.DIRECTORY_IMAGES+"Blocs/Vert Normal.png").getScaledCopy(50, 50);
	}


	@Override
	public void renderSelectionItem(GameContainer arg0, StateBasedGame arg1, Graphics g,int position) {
		super.renderSelectionItem(container, game, g, position);
		if (position<3){
			g.drawImage(image[position], Main.longueur/2-fontItem.getWidth(items.get(indexItemPlusGrand))/2-125, 340 + 30 * selection);
			g.drawImage(image[position], Main.longueur/2-fontItem.getWidth(items.get(indexItemPlusGrand))/2+120, 340 + 30 * selection);
		}

	}
	@Override
	public void onOptionItemFocusedChanged(int position) {
		time=System.currentTimeMillis();
	}

	@Override
	public void onOptionItemSelected(int position) {

		World.difficulty=position;
		if(position<3){
			game.enterState(World.ID, new FadeOutTransition(),
					new FadeInTransition());
		}else if (position==3){
			game.enterState(MainMenu.ID);
		}

	}

	@Override
	public int getID() {
		return ID;
	}

}
