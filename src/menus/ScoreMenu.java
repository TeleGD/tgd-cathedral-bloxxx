package menus;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import api.APIListener;
import api.TGDApi;
import db.Person;
import games.cathedralBloxxx.World;
import general.Main;
import general.ui.Button;
import general.ui.TGDComponent;
import general.ui.TGDComponent.OnClickListener;
import general.ui.TextField;
import general.ui.TextField.EnterActionListener;
import general.utils.FontUtils;

public class ScoreMenu extends BasicGameState implements EnterActionListener, OnClickListener, APIListener{

	public static int ID=-1;

	private ArrayList<ArrayList<Person>> games=new ArrayList<ArrayList<Person>>();

	private static final int WIDTH_SEPARATOR = 5;
	private static final int HEIGHT_ROW=45;
	private static final int NB_GAME=1;

	private static final int BEGIN_Y_TAB=100;
	private static final int END_Y_TAB=Main.hauteur-150;
	private static final int PADDING_LEFT_COLUMN = 30;
	private static final int PADDING_RIGHT_COLUMN = 30;
	private static final int PADDING_LEFT = 40;
	private static final int PADDING_RIGHT = 40;

	private static final int LARGEUR_TABLEAU=Main.longueur-PADDING_LEFT- PADDING_RIGHT;
	private static final int LARGEUR_COLUMN=LARGEUR_TABLEAU/NB_GAME;
	private static final int HAUTEUR_TABLEAU=END_Y_TAB-BEGIN_Y_TAB;

	private static final String TITLE = "MEILLEURS SCORES";


	private TrueTypeFont fontHighScore;
	private TextField textField;
	private Button button;

	private StateBasedGame game;

	@Override
	public void enter(GameContainer container, StateBasedGame game){
		this.game=game;
		TGDApi.setApiListener(this);
		for(int i=0;i<NB_GAME;i++)
		{
			games.add(new ArrayList<Person>());
			TGDApi.getScoreForGame(i+1,10);
		}
		fontHighScore=FontUtils.loadCustomFont("press-start-2p.ttf", Font.BOLD, 35);

		textField=new TextField(container,Main.longueur/2,END_Y_TAB+50,TGDComponent.AUTOMATIC,TGDComponent.AUTOMATIC);
		textField.setPlaceHolder("Entrez un pseudo pour voir le score ...");
		textField.setEnterActionListener(this);
		textField.setMaxNumberOfLetter(80);
		textField.setX(Main.longueur/2-textField.getWidth()/2);


		button=new Button("VISUALISER",container,Main.longueur/2,END_Y_TAB+50,TGDComponent.AUTOMATIC,TGDComponent.AUTOMATIC);
		button.setHeight(textField.getHeight());
		button.setOnClickListener(this);
		button.setX(textField.getX()+textField.getWidth()+20);

	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.setFont(fontHighScore);
		g.drawString(TITLE, (Main.longueur-fontHighScore.getWidth(TITLE))/2, 30);
		g.resetFont();

		for(int i=0;i<NB_GAME;i++)
		{
			String name = null;
			if(i==0)name=World.GAME_NAME;
			g.setColor(new Color(255,0,0));
			g.drawString(name,PADDING_LEFT+ i*LARGEUR_COLUMN+PADDING_LEFT_COLUMN+LARGEUR_COLUMN/2-g.getFont().getWidth(name), BEGIN_Y_TAB);

			g.setColor(Color.white);
			if(i!=NB_GAME-1){
				g.fillRoundRect(PADDING_LEFT+(i+1)*LARGEUR_COLUMN,BEGIN_Y_TAB,WIDTH_SEPARATOR,HAUTEUR_TABLEAU,3);
			}
			if(games.size()>i){
				for(int j=0;j<games.get(i).size();j++)
				{
					g.drawString((j+1)+")",PADDING_LEFT+ i*LARGEUR_COLUMN+PADDING_LEFT_COLUMN, BEGIN_Y_TAB+(j+1)*HEIGHT_ROW);
					g.drawString(games.get(i).get(j).getName(),PADDING_LEFT+55+ i*LARGEUR_COLUMN+PADDING_LEFT_COLUMN, BEGIN_Y_TAB+(j+1)*HEIGHT_ROW);
					g.drawString(""+games.get(i).get(j).getScoreAtGame((i+1)), PADDING_LEFT+(i+1)*LARGEUR_COLUMN-PADDING_RIGHT_COLUMN-g.getFont().getWidth(games.get(i).get(j).getScoreAtGame(i+1)+""), BEGIN_Y_TAB+(j+1)*HEIGHT_ROW);

				}
			}


	   }

		textField.render(container, game, g);
		button.render(container, game, g);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		textField.update(container, game, delta);
		textField.setX(Main.longueur/2-textField.getWidth()/2);

		button.update(container, game, delta);
		button.setX(textField.getX()+textField.getWidth()+20);
	}

	@Override
	public void onEnterPressed() {
		showHighScorePlayer();
	}
	@Override
	public void onClick(TGDComponent componenent) {
		showHighScorePlayer();
	}

	@Override
	public void keyPressed(int key,char c){
		super.keyPressed(key, c);
		if(key==Input.KEY_ESCAPE){
			game.enterState(MainMenu.ID, new FadeOutTransition(),new FadeInTransition());
		}
	}
	private void showHighScorePlayer() {
		HighScorePlayerMenu.setNamePlayer(textField.getText());
		game.enterState(HighScorePlayerMenu.ID, new FadeOutTransition(),new FadeInTransition());
	}

	@Override
	public void onContentReceived(Object content) {
		System.out.println("Score:"+content.toString());

		if(content instanceof ArrayList<?>){
			ArrayList<Person> persons=((ArrayList<Person>)content);
			games.set(persons.get(0).getGamesPlayed().get(0)-1,persons);
		}
	}

	@Override
	public void onContentUpdated(String reponse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(String reason) {
		// TODO Auto-generated method stub

	}





}
