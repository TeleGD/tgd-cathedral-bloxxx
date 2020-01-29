package games.cathedralBloxxx;

import java.io.File;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import api.TGDApi;

import app.AppFont;
import app.AppLoader;
import app.ui.Button;
import app.ui.TGDComponent;
import app.ui.TGDComponent.OnClickListener;
import app.ui.TextField;
import app.ui.TextField.EnterActionListener;

public class World extends BasicGameState implements OnClickListener, EnterActionListener {
	public final static float GRAVITY= 0.3f;

	public final static String GAME_NAME="Cathedral Bloxxx";

	public final static String GAME_FOLDER_NAME="cathedralBloxxx";
	public final static String DIRECTORY_SOUNDS="/sounds"+File.separator+GAME_FOLDER_NAME+File.separator;
	public final static String DIRECTORY_MUSICS="/musics"+File.separator+GAME_FOLDER_NAME+File.separator;
	public final static String DIRECTORY_IMAGES="/images"+File.separator+GAME_FOLDER_NAME+File.separator;

	private int score;
	private Pendulum pendulum;
	private long time;
	private Tower tower;
	private Decor decor;
	private int difficulty;
	private String colorImage;

	private TrueTypeFont fontPerdu;
	private boolean perdu;
	private Audio soundMusicBackground;
	private float soundMusicBackgroundPos;
	private TextField textField;
	private Button bouton;
	private String textPerdu;

	private int ID;
	private int state;

	public World(int ID) {
		this.ID = ID;
		this.state = 0;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée une unique fois au chargement du programme */
		fontPerdu = AppLoader.loadFont("/fonts/press-start-2p.ttf", AppFont.BOLD, 20);
		soundMusicBackground=AppLoader.loadAudio(DIRECTORY_MUSICS+"what_is_love.ogg");
		soundMusicBackgroundPos = 0f;
		textField=new TextField(container,container.getWidth()/2-100,container.getHeight()*0.6f,150,TGDComponent.AUTOMATIC);
		textField.setMaxNumberOfLetter(13);
		textField.setUpperCaseLock(true);
		textField.setPlaceHolder("Entrez un pseudo");
		textField.setEnterActionListener(this);
		bouton=new Button("ENREGISTRER",container,container.getWidth()/2+60,container.getHeight()*0.6f,TGDComponent.AUTOMATIC,textField.getHeight());
		bouton.setOnClickListener(this);
		bouton.setPadding(10,15,10,15);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée à l'apparition de la page */
		if (this.state == 0) {
			this.play(container, game);
		} else if (this.state == 2) {
			this.resume(container, game);
		}
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée à la disparition de la page */
		if (this.state == 1) {
			this.pause(container, game);
		} else if (this.state == 3) {
			this.stop(container, game);
			this.state = 0; // TODO: remove
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		Input input = container.getInput();
		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			this.setState(1);
			game.enterState(2, new FadeOutTransition(), new FadeInTransition());
		}
		this.time += delta;
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			pendulum.releaseBlock();
			if(perdu){
				this.setState(3);
				game.enterState(3);
			}else{
				pendulum.releaseBlock();
			}
		}
		decor.update(container, game, delta);
		tower.update(container, game, delta);

		if(!perdu || pendulum.getBlock().isRealeased())pendulum.update(container, game,delta);

		if(!perdu && pendulum.getBlock().isRealeased() && pendulum.getBlock().getY()>container.getHeight()){
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
	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		/* Méthode exécutée environ 60 fois par seconde */

		//Affichage

		decor.render(container, game, context);
		tower.render(container, game, context);
		if(!perdu || pendulum.getBlock().isRealeased())pendulum.render(container, game,context);

		if(perdu){
			int t=Math.max(Math.max(fontPerdu.getWidth("Score :"+score),fontPerdu.getWidth(textPerdu)),400);

			context.setColor(new Color(80,80,20));
			context.fillRoundRect(container.getWidth()/2-t/2-40, container.getHeight()/2-150, t+80, 300,25,25);
			context.setFont(fontPerdu);
			context.setColor(Color.white);
			context.drawString(textPerdu, container.getWidth()/2-fontPerdu.getWidth(textPerdu)/2, container.getHeight()/2-30);
			//context.setFont(FontUtils.chargerFont("Kalinga", Font.PLAIN, 15, true));
			//context.drawString("PRESS ENTER TO RESTART",container.getWidth()/2-t/2-40,container.getHeight()/2+100);

			context.resetFont();
			context.drawString("PSEUDO", textField.getX()-context.getFont().getWidth("PSEUDO")-25, textField.getY()+textField.getHeight()/2-context.getFont().getHeight("PSEUDO")/2);

			textField.render(container, game, context);
			bouton.render(container, game, context);
		}

		context.setFont(fontPerdu);
		context.setColor(Color.cyan);
		context.drawString("Score :"+score, container.getWidth()/2-fontPerdu.getWidth("Score :"+score)/2, container.getHeight()/2-100);
	}

	public void play(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée une unique fois au début du jeu */
		//pendulum.enter(container, game);
		setDifficulty(container, difficulty);

		perdu=false;
		decor=new Decor(container);
		score=0;
		time=0; // on reinitialise le temps
		tower=new Tower(this,container.getWidth()/2,container.getHeight(),new Block(this,pendulum.getX() - 50, container.getHeight()-101,100,100,AppLoader.loadPicture(World.DIRECTORY_IMAGES+"Blocs/"+this.colorImage+" Porte.png")));

		soundMusicBackground.playAsMusic(1, 0.3f, true);
	}

	public void pause(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée lors de la mise en pause du jeu */
		soundMusicBackgroundPos = soundMusicBackground.getPosition();
		soundMusicBackground.stop();
	}

	public void resume(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée lors de la reprise du jeu */
		soundMusicBackground.playAsMusic(1, 0.3f, true);
		soundMusicBackground.setPosition(soundMusicBackgroundPos);
	}

	public void stop(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée une unique fois à la fin du jeu */
		soundMusicBackground.stop();
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public void setDifficulty(GameContainer container,int difficulty) {
		if(difficulty==0){
			colorImage="Rouge";
			pendulum=new Pendulum(container,this);
			pendulum.setSpeed(12000);
		}
		else if(difficulty==1){
			colorImage="Bleu";
			pendulum=new Pendulum(container,this);
			pendulum.setSpeed(16000);
			pendulum.setLength(1650);
		}
		else if(difficulty==2){
			colorImage="Vert";
			pendulum=new Pendulum(container,this);
			pendulum.setSpeed(22000);
			pendulum.setLength(1500);
		}
	}

	public long getTime() {
		return this.time / 1000;
	}

	public long getTimeInMillis() {
		// TODO Auto-generated method stub
		return this.time;
	}

	public Tower getTower() {
		// TODO Auto-generated method stub
		return tower;
	}

	public Pendulum getPendulum() {
		// TODO Auto-generated method stub
		return pendulum;
	}

	public Decor getDecor() {
		return decor;
	}

	public void notifyStackedBlock() {
		// TODO Auto-generated method stub
	}

	public int getDifficulty() {
		return this.difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public String getColorImage() {
		return this.colorImage;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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
