package pkg;

import javax.swing.JButton;

public class Tiles extends JButton {
	
	private boolean isCollected;
	private int pos;
	private int id;
	
	public Tiles(){
		isCollected = false;
		pos = 0;
		id = 0;
	}
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public boolean isCollected() {
		return isCollected;
	}

	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}
	
}