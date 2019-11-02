package games.cathedralBloxxx;

import java.awt.Font;
import java.io.File;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import api.TGDApi;
import general.Main;
import general.ui.Button;
import general.ui.TGDComponent;
import general.ui.TGDComponent.OnClickListener;
import general.ui.TextField;
import general.ui.TextField.EnterActionListener;
import general.utils.FontUtils;
import menus.MainMenu;

public class World extends BasicGameState implements OnClickListener, EnterActionListener{
	public final static float GRAVITY= 0.3f;
	public final static int ID=3;

	public final static String GAME_NAME="Cathedral Bloxxx";

	public final static String GAME_FOLDER_NAME="cathedralBloxxx";
	public final static String DIRECTORY_SOUNDS="sounds"+File.separator+GAME_FOLDER_NAME+File.separator;
	public final static String DIRECTORY_MUSICS="musics"+File.separator+GAME_FOLDER_NAME+File.separator;
	public final static String DIRECTORY_IMAGES="images"+File.separator+GAME_FOLDER_NAME+File.separator;

	private static int score = 0;
	public static Pendulum pendulum;
	public static long timeInitial;
	public static Tower tower;
	public static Decor decor;
	public static int difficulty;
	public static String colorImage;

	private TrueTypeFont fontPerdu;
	private static boolean perdu=false;
	private static Music soundMusicBackground;
	private StateBasedGame game;
	private TextField textField;
	private Button bouton;
	private String textPerdu;


	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game=game;

	}

	public static void setDifficulty(int difficulty){
		if(difficulty==0){
			colorImage="Rouge";
			pendulum=new Pendulum();
			pendulum.setSpeed(12000);

		}
		else if(difficulty==1){
			colorImage="Bleu";
			pendulum=new Pendulum();
			pendulum.setSpeed(16000);
			pendulum.setLength(1650);
		}
		else if(difficulty==2){
			colorImage="Vert";
			pendulum=new Pendulum();
			pendulum.setSpeed(22000);
			pendulum.setLength(1500);
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		//pendulum.enter(container, game);
		setDifficulty(difficulty);

		fontPerdu=FontUtils.loadFont("press-start-2p.ttf",Font.BOLD,20,false);

		decor=new Decor();
		timeInitial=System.currentTimeMillis(); // on reinitialise le temps
		tower=new Tower(Main.longueur/2,Main.hauteur,new Block(pendulum.getX() - 50, Main.hauteur-101,100,100,new Image(World.DIRECTORY_IMAGES+"Blocs/"+World.colorImage+" Porte.png")));

		soundMusicBackground=new Music(DIRECTORY_MUSICS+"what_is_love.ogg");
		soundMusicBackground.play(1, 0.3f);
		//Ici mettre tous les chargement d'image, creation de perso/decor et autre truc qui mettent du temps


		textField=new TextField(container,Main.longueur/2-100,Main.hauteur*0.6f,150,TGDComponent.AUTOMATIC);
		textField.setMaxNumberOfLetter(13);
		textField.setUpperCaseLock(true);
		textField.setPlaceHolder("Entrez un pseudo");
		textField.setEnterActionListener(this);
		bouton=new Button("ENREGISTRER",container,Main.longueur/2+60,Main.hauteur*0.6f,TGDComponent.AUTOMATIC,textField.getHeight());
		bouton.setOnClickListener(this);
		bouton.setPadding(10,15,10,15);
	}


	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

		//Affichage

        decor.render(container, game, g);
        tower.render(container, game, g);
        if(!perdu || pendulum.getBlock().isRealeased())pendulum.render(container, game,g);

        if(perdu){
        	int t=Math.max(Math.max(fontPerdu.getWidth("Score :"+score),fontPerdu.getWidth(textPerdu)),400);


        	g.setColor(new Color(80,80,20));
        	g.fillRoundRect(Main.longueur/2-t/2-40,  Main.hauteur/2-150, t+80, 300,25,25);
            g.setFont(fontPerdu);
            g.setColor(Color.white);
            g.drawString(textPerdu, Main.longueur/2-fontPerdu.getWidth(textPerdu)/2, Main.hauteur/2-30);
            //g.setFont(FontUtils.chargerFont("Kalinga", Font.PLAIN, 15, true));
            //g.drawString("PRESS ENTER TO RESTART",Main.longueur/2-t/2-40,Main.hauteur/2+100);

            g.resetFont();
    		g.drawString("PSEUDO", textField.getX()-g.getFont().getWidth("PSEUDO")-25, textField.getY()+textField.getHeight()/2-g.getFont().getHeight("PSEUDO")/2);

        	textField.render(container, game, g);
    		bouton.render(container, game, g);
        }

        g.setFont(fontPerdu);
        g.setColor(Color.cyan);
        g.drawString("Score :"+score, Main.longueur/2-fontPerdu.getWidth("Score :"+score)/2, Main.hauteur/2-100);


	}


	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		decor.update(container, game, delta);
		tower.update(container, game, delta);

        if(!perdu || pendulum.getBlock().isRealeased())pendulum.update(container, game,delta);

		if(!perdu && pendulum.getBlock().isRealeased() && pendulum.getBlock().getY()>Main.hauteur){
			pendulum.finishTower();
			perdu=true;
			textField.setText("");
			textPerdu="PERDU !";
			textField.setHasFocus(true);

		}

		if(perdu){
			textField.update(container, game, delta);
			bouton.update(container, game, delta);

		}



	}

	@Override
	public void keyPressed(int key, char  c){
  		switch(key){
  		case Input.KEY_SPACE:
  			pendulum.releaseBlock();
 			if(perdu){
 				reset();
 			}else{
 				pendulum.releaseBlock();
 			}
 			break;
  		case Input.KEY_ESCAPE:
  			if(perdu){
  				World.soundMusicBackground.stop();
				game.enterState(MainMenuCbl.ID);
			}
  			break;
  		case Input.KEY_F1:
  			World.soundMusicBackground.stop();
  			game.enterState(MainMenu.ID);
  			break;

  		}
  	}

	@Override
	public int getID() {
		return ID;
	}

	public static void reset() {
		 		perdu=false;
		 		try {
		 			decor=new Decor();
		 			score=0;
		 			setDifficulty(difficulty);
		 			timeInitial=System.currentTimeMillis(); // on reinitialise le temps
		 			tower=new Tower(Main.longueur/2,Main.hauteur,new Block(pendulum.getX() - 50, Main.hauteur-101,100,100,new Image(World.DIRECTORY_IMAGES+"Blocs/"+World.colorImage+" Porte.png")));

		 			soundMusicBackground=new Music(DIRECTORY_MUSICS+"what_is_love.ogg");
		 			soundMusicBackground.play(1, 0.3f);
		 		} catch (SlickException e) {
		 			// TODO Auto-generated catch block
		 			e.printStackTrace();
		 		}

		  	}

	public static long getTime(){
		return (System.currentTimeMillis()-timeInitial)/1000;
	}

	public static double getTimeInMillis() {
		// TODO Auto-generated method stub
		return (System.currentTimeMillis()-timeInitial);
	}

	public static Tower getTower() {
		// TODO Auto-generated method stub
		return tower;
	}


	public static Pendulum getPendulum() {
		// TODO Auto-generated method stub
		return pendulum;
	}

	public static Decor getDecor() {
		return decor;
	}

	public static void notifyStackedBlock() {
		// TODO Auto-generated method stub

	}

	public static int  getScore() {
		return score;
	}

	public static void setScore(int score) {
		World.score = score;
	}

	@Override
	public void onClick(TGDComponent component) {
		TGDApi.updateScoreForGame(textField.getText(), 3, score);
		textPerdu="Score Updated !";
	}

	@Override
	public void onEnterPressed() {
		TGDApi.updateScoreForGame(textField.getText(), 3, score);
		textPerdu="Score Updated !";
	}


}
