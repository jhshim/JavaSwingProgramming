package pkg;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Hint extends JFrame {
	
	JPanel hintContent = null;
	private JLabel img;
	private Image resizedImage;
	
	public Hint(String link){
		setTitle("Hint Picture");
		setBounds(1200, 100, 560, 410);
		
		initialize(link);
	}
	
	public Hint(File file){
		setTitle("Hint Picture");
		setBounds(1200, 100, 560, 410);
		
		initialize(file);
	}
	
	private void initialize(String link){
		hintContent = new JPanel();
		hintContent.setLayout(null);
		setContentPane(hintContent);
		
		img = new JLabel("");
		img.setBounds(17, 15, 504, 324);
		
		Image orginalImage = null;
		
		try {
			orginalImage = ImageIO.read(getClass().getResource(link));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resizedImage = orginalImage.getScaledInstance( 500, 313, Image.SCALE_SMOOTH );
		
		img.setIcon(new ImageIcon(resizedImage));
		hintContent.add(img);
	}
	
	private void initialize(File file){
		hintContent = new JPanel();
		hintContent.setLayout(null);
		setContentPane(hintContent);
		
		img = new JLabel("");
		img.setBounds(17, 15, 504, 324);
		
		Image orginalImage = null;
		
		try {
			orginalImage = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resizedImage = orginalImage.getScaledInstance( 500, 313, Image.SCALE_SMOOTH );
		
		img.setIcon(new ImageIcon(resizedImage));
		hintContent.add(img);
	}
}