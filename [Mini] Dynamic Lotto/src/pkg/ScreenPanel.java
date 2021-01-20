package pkg;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ScreenPanel extends JPanel {
	
	private Image dbImage;
	private Graphics dbGraphic;
	
	private Image imgBackground;
	
	private Image imgMinNumber;
	private Image imgMaxNumber;
	private Image imgNumOfLotto;
	
	private Image imgLogo;
	private Image imgMadeBy;
	
	public ScreenPanel(){
		try {
			imgBackground = ImageIO.read(getClass().getResource("/images/background.jpg"));
			
			imgMinNumber = ImageIO.read(getClass().getResource("/images/txtMinNumber.png"));
			imgMaxNumber = ImageIO.read(getClass().getResource("/images/txtMaxNumber.png"));
			imgNumOfLotto = ImageIO.read(getClass().getResource("/images/txtNumOfLotto.png"));
		
			imgLogo = ImageIO.read(getClass().getResource("/images/logo.png"));
			imgMadeBy = ImageIO.read(getClass().getResource("/images/madeBy.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paint(Graphics g){
		dbImage = createImage(MainFrame.WIDTH, MainFrame.HEIGHT);
		dbGraphic = dbImage.getGraphics();
		screenDraw(dbGraphic);
		g.drawImage(dbImage, 0, 0, null);
	}
	
	public int getCenterPosition(int pos, int length){
		return pos - length/2;
	}
	
	public void screenDraw(Graphics g){
		g.drawImage(imgBackground, 0, 0, null);
		g.drawImage(imgLogo, 50, 50, null);
		
		g.drawImage(imgMadeBy, MainFrame.WIDTH - 411 - 20, MainFrame.HEIGHT - 24 - 50, null);
		
		g.drawImage(imgMinNumber, 20, getCenterPosition(MainFrame.HEIGHT/2, 40) - 80, null);
		g.drawImage(imgMaxNumber, (20 + 105 + 10) + 100 + 50, getCenterPosition(MainFrame.HEIGHT/2, 40) - 80, null);
		g.drawImage(imgNumOfLotto, (((20 + 105 + 10) + 100 + 50) + 111 + 10) + 100 + 50, getCenterPosition(MainFrame.HEIGHT/2, 40) - 80, null);
		
		paintComponents(g);
		this.repaint();
	}
}