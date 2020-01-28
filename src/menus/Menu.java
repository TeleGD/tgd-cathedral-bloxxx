package menus;

import java.util.ArrayList;
import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import app.AppFont;
import app.AppLoader;

/**
 *
 * Pour faire un menu c'est simple,
 * il suffit de faire une classe qui herite de celle la et de
 * reseiner via les setters, les params TitrePrincipal, titreSecondaire
 * et les items. Et C'est tout
 * Vous recevrez l'index de l'item selectionné dans la méthode
 * onOptionItemSelected.
 *
		super.setTitrePrincipal("TELE-ARCADE DESIGN");
		super.setTitreSecondaire("Menu Principal");
		super.setItems("Jouer","Editeur", "Quitter");

		super.setEnableClignote(true);
		super.setCouleurClignote(Color.red);
		super.setTempsClignote(400);
 *
 *on peut add/remove item maintenant
 *ajouter autant d'item qu'on veut
 *
 *@author Jérôme
 */
public abstract class Menu extends BasicGameState {

	public int HEIGHT_ITEM;

	private int START_MENU_Y;
	private int END_MENU_Y;

	private int MAX_ITEMS_VISIBLE;

	private String titrePrincipal="";
	private String titreSecondaire="";
	protected ArrayList<String> items=new ArrayList<String>();

	private TrueTypeFont fontTitrePrincipal;
	private TrueTypeFont fontTitreSecondaire;
	protected TrueTypeFont fontItem;

	protected int selection;
	protected long tempsClignote=400;
	protected Color couleurClignote=Color.red;
	private boolean enableClignote=false;

	protected GameContainer container;
	protected StateBasedGame game;
	protected long time;

	protected TrueTypeFont fontConfirmText;

	protected int indexItemPlusGrand;
	private int decalage;
	private String bottomText="PRESS ENTER";

	private int ID;

	public Menu(int ID) {
		this.ID = ID;
		setFontTitrePrincipal("/fonts/press-start-2p.ttf",AppFont.BOLD,40);
		setFontTitreSecondaire("/fonts/vt323.ttf",AppFont.BOLD,24);
		setFontItem("/fonts/vt323.ttf",AppFont.BOLD,14);

		fontConfirmText=AppLoader.loadFont("/fonts/press-start-2p.ttf",AppFont.PLAIN,20);
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		HEIGHT_ITEM=30;

		START_MENU_Y=container.getHeight()/2-130;
		END_MENU_Y=container.getHeight()-200;

		MAX_ITEMS_VISIBLE = (END_MENU_Y-START_MENU_Y)/HEIGHT_ITEM-3;

		time=System.currentTimeMillis();
		this.container = container;
		this.game = game;
		container.setShowFPS(false);
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) {
		g.setBackground(Color.black);

		renderTitrePrincipal(arg0,arg1,g);
		renderTitreSecondaire(arg0,arg1,g);
		renderMenusItems(arg0,arg1,g);
		renderSelectionItem(arg0,arg1,g,selection);

		g.setColor(Color.white);
		g.drawRect(arg0.getWidth()/2-300, START_MENU_Y, 600,37);

		g.drawRect(arg0.getWidth()/2-300, END_MENU_Y, 600,37);

		g.setFont(fontConfirmText);
		g.drawString(bottomText, arg0.getWidth()/2-fontConfirmText.getWidth(bottomText)/2, 530);

	}

	public void renderSelectionItem(GameContainer arg0, StateBasedGame arg1, Graphics g,int position) {
		if(items==null)return;
		if(enableClignote){
			if((System.currentTimeMillis()-time)%(2*tempsClignote)<=tempsClignote)g.setColor(Color.white);
			else g.setColor(couleurClignote);
		}else{
			g.setColor(couleurClignote);
		}
		g.drawString(">>", arg0.getWidth()/2-fontItem.getWidth(items.get(indexItemPlusGrand))/2-35, getYMenu() + HEIGHT_ITEM * (position-decalage));
		g.drawString("<<", arg0.getWidth()/2-fontItem.getWidth(items.get(indexItemPlusGrand))/2+fontItem.getWidth(items.get((position)))+10, getYMenu() + HEIGHT_ITEM * (position-decalage));

	}

	public void renderTitrePrincipal(GameContainer arg0, StateBasedGame arg1, Graphics g) {
		g.setColor(Color.red);
		g.setFont(fontTitrePrincipal);
		g.drawString(titrePrincipal,(arg0.getWidth()-fontTitrePrincipal.getWidth(titrePrincipal))/2 , 120);
		g.setColor(Color.white);
		g.setFont(fontTitrePrincipal);
		g.drawString(titrePrincipal,(arg0.getWidth()-fontTitrePrincipal.getWidth(titrePrincipal))/2+4 , 122);

	}

	public void renderTitreSecondaire(GameContainer arg0, StateBasedGame arg1, Graphics g) {
		g.setFont(fontTitreSecondaire);
		g.drawString(titreSecondaire, arg0.getWidth()/2-fontTitreSecondaire.getWidth(titreSecondaire)/2, 232);
	}

	public void renderMenusItems(GameContainer arg0, StateBasedGame arg1, Graphics g) {
		if(items==null)return;

		g.setColor(Color.white);

		for (int i = decalage; i < Math.min(items.size(),decalage+MAX_ITEMS_VISIBLE); i++) {
			g.setFont(fontItem);
			g.drawString(this.items.get(i), arg0.getWidth()/2-fontItem.getWidth(items.get(indexItemPlusGrand))/2, getYMenu() + HEIGHT_ITEM * (i-decalage));
		}

	}

	private int getYMenu() {
		return START_MENU_Y+37+(END_MENU_Y-START_MENU_Y-37)/2-HEIGHT_ITEM*Math.min(MAX_ITEMS_VISIBLE,items.size())/2;
	}
	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) {


	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		//time=System.currentTimeMillis();
		switch (key) {
		//case Input.KEY_NUMPAD2:
		case Input.KEY_DOWN:
			if (selection < items.size() - 1)
				selection++;
			else
				selection = 0;
			if(selection>=MAX_ITEMS_VISIBLE){
				decalage=selection-MAX_ITEMS_VISIBLE+1;
			}else decalage=0;
			onOptionItemFocusedChanged(selection);
			break;
		//case Input.KEY_NUMPAD8:
		case Input.KEY_UP:
			if (selection > 0)
				selection--;
			else
				selection = items.size() - 1;
			if(selection>=MAX_ITEMS_VISIBLE){
				decalage=selection-MAX_ITEMS_VISIBLE+1;
			}else decalage=0;
			onOptionItemFocusedChanged(selection);
			break;
		case Input.KEY_ENTER:
			onOptionItemSelected(selection);
			break;

		case Input.KEY_ESCAPE:
			//exit();
			break;
		}
	}


	public abstract void onOptionItemFocusedChanged(int position);
	public abstract void onOptionItemSelected(int position);

	public String getTitrePrincipal() {
		return titrePrincipal;
	}

	public void setTitrePrincipal(String titrePrincipal) {
		this.titrePrincipal = titrePrincipal;
	}

	public String getTitreSecondaire() {
		return titreSecondaire;
	}

	public void setTitreSecondaire(String titreSecondaire) {
		this.titreSecondaire = titreSecondaire;
	}

	public ArrayList<String> getItems() {
		return items;
	}

	public void setItems(String... itemsLoc) {
		this.items = new ArrayList<String>(Arrays.asList(itemsLoc));
		calculerPlusGrandItem();
	}

	private void calculerPlusGrandItem() {
		indexItemPlusGrand=0;
		for(int i=0;i<items.size();i++){
			if(items.get(indexItemPlusGrand).length()<items.get(i).length()){
				indexItemPlusGrand=i;
			}
		}
	}
	public void setFontTitrePrincipal(String name, int type, int size) {
		fontTitrePrincipal=AppLoader.loadFont(name,type,size);
	}

	public void setFontTitreSecondaire(String name, int type, int size) {
		fontTitreSecondaire=AppLoader.loadFont(name,type,size);
	}
	public void setFontItem(String name, int type, int size) {
		fontItem=AppLoader.loadFont(name,type,size);

	}


	public void setEnableClignote(boolean b) {
		enableClignote=b;
	}

	public void setTempsClignote(long timeEnMillisecond){
		this.tempsClignote=timeEnMillisecond;
	}

	public void setCouleurClignote(Color coul){
		this.couleurClignote=coul;
	}
	public void addItem(String titre) {
		items.add(titre);
		calculerPlusGrandItem();
	}
	public void removeItemAtIndex(int index) {
		items.remove(index);
		calculerPlusGrandItem();

	}
	public void removeAllItems() {
		items.removeAll(items);
	}




	public void setBottomText(String text){
		this.bottomText=text;
	}

	public String getBottomText(){
		return bottomText;
	}


}
