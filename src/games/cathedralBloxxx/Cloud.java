package games.cathedralBloxxx;

import org.newdawn.slick.Image;

public class Cloud extends SkyElements{

	private int numberImageCloud;

	public Cloud(int posX, int posY, int width, int height, int numberImageCloud, Image image) {
		super(posX,posY,width,height,image);
		this.numberImageCloud=numberImageCloud;
	}

	public int getNumberImageCloud() {
		return numberImageCloud;
	}

	public void setNumberImageCloud(int numberImageCloud) {
		this.numberImageCloud = numberImageCloud;
	}

}
