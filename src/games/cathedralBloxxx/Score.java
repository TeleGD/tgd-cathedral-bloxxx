package games.cathedralBloxxx;

public class Score {

	private String player;
	private double count;

	public Score(String player, double count) {
		this.player = player;
		this.count = count;
	}

	public String getPlayer() {
		return this.player;
	}

	public double getCount() {
		return this.count;
	}

}
