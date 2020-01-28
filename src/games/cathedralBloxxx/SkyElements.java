package games.cathedralBloxxx;

import org.newdawn.slick.Image;

public class SkyElements {

	private int posX;
	private int posY;
	private int width;
	private int height;
	private Image image;

	public SkyElements(int posX, int posY, int width, int height, Image image) {
		this.posX = posX;
		this.posY = posY;
		this.width=width;
		this.height=height;
		this.image=image;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}
