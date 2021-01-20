package pkg;

import javax.swing.JButton;
import javax.swing.ImageIcon;

/**
 * @author tails1101
 *
 */
public class CardButtons extends JButton{
	
	private ImageIcon icon = null;
	private boolean isCollected;
	private int picID;
	private int ArrayID;
	private int RealPos;
	
	public CardButtons(){}
	
	public CardButtons(String link){
		isCollected = false;
		icon = new ImageIcon(getClass().getResource(link));
		this.setIcon(icon);
	}
	
	public void setIsCollected(boolean b){
		this.isCollected = b;
	}
	
	public boolean getIsCollected(){
		return this.isCollected;
	}
	
	public void setPicID(int i){
		this.picID = i;
	}
	
	public int getPicID(){
		return picID;
	}
	
	public void setRealPos(int pos){
		this.RealPos = pos;
	}
	
	public int getRealPos(){
		return RealPos;
	}
	
	public void setArrayID(int id){
		this.ArrayID = id;
	}
	
	public int getArrayID(){
		return this.ArrayID;
	}

}
