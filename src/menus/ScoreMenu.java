package menus;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import api.APIListener;
import api.TGDApi;

import app.AppFont;
import app.AppLoader;
import app.ui.Button;
import app.ui.TGDComponent;
import app.ui.TGDComponent.OnClickListener;
import app.ui.TextField;
import app.ui.TextField.EnterActionListener;

import db.Person;

import games.cathedralBloxxx.World;

public class ScoreMenu extends BasicGameState implements EnterActionListener, OnClickListener, APIListener{

	private ArrayList<ArrayList<Person>> games=new ArrayList<ArrayList<Person>>();

	private int WIDTH_SEPARATOR;
	private int HEIGHT_ROW;
	private int NB_GAME;

	private int BEGIN_Y_TAB;
	private int END_Y_TAB;
	private int PADDING_LEFT_COLUMN;
	private int PADDING_RIGHT_COLUMN;
	private int PADDING_LEFT;
	private int PADDING_RIGHT;

	private int LARGEUR_TABLEAU;
	private int LARGEUR_COLUMN;
	private int HAUTEUR_TABLEAU;

	private String TITLE = "MEILLEURS SCORES";


	private TrueTypeFont fontHighScore;
	private TextField textField;
	private Button button;

	private StateBasedGame game;

	private int ID;

	public ScoreMenu(int ID) {
		this.ID = ID;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		WIDTH_SEPARATOR = 5;
		HEIGHT_ROW=45;
		NB_GAME=1;

		BEGIN_Y_TAB=100;
		END_Y_TAB=container.getHeight()-150;
		PADDING_LEFT_COLUMN = 30;
		PADDING_RIGHT_COLUMN = 30;
		PADDING_LEFT = 40;
		PADDING_RIGHT = 40;

		LARGEUR_TABLEAU=container.getWidth()-PADDING_LEFT- PADDING_RIGHT;
		LARGEUR_COLUMN=LARGEUR_TABLEAU/NB_GAME;
		HAUTEUR_TABLEAU=END_Y_TAB-BEGIN_Y_TAB;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game){
		this.game=game;
		TGDApi.setApiListener(this);
		for(int i=0;i<NB_GAME;i++)
		{
			games.add(new ArrayList<Person>());
			TGDApi.getScoreForGame(i+1,10);
		}
		fontHighScore=AppLoader.loadFont("/fonts/press-start-2p.ttf", AppFont.BOLD, 35);

		textField=new TextField(container,container.getWidth()/2,END_Y_TAB+50,TGDComponent.AUTOMATIC,TGDComponent.AUTOMATIC);
		textField.setPlaceHolder("Entrez un pseudo pour voir le score ...");
		textField.setEnterActionListener(this);
		textField.setMaxNumberOfLetter(80);
		textField.setX(container.getWidth()/2-textField.getWidth()/2);


		button=new Button("VISUALISER",container,container.getWidth()/2,END_Y_TAB+50,TGDComponent.AUTOMATIC,TGDComponent.AUTOMATIC);
		button.setHeight(textField.getHeight());
		button.setOnClickListener(this);
		button.setX(textField.getX()+textField.getWidth()+20);

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.white);
		g.setFont(fontHighScore);
		g.drawString(TITLE, (container.getWidth()-fontHighScore.getWidth(TITLE))/2, 30);
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
	public void update(GameContainer container, StateBasedGame game, int delta) {
		textField.update(container, game, delta);
		textField.setX(container.getWidth()/2-textField.getWidth()/2);

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
			game.enterState(1, new FadeOutTransition(),new FadeInTransition());
		}
	}
	private void showHighScorePlayer() {
		((HighScorePlayerMenu) game.getState(5)).setNamePlayer(textField.getText());
		game.enterState(5, new FadeOutTransition(),new FadeInTransition());
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
