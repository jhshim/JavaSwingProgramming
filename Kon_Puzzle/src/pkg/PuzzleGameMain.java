package pkg;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.Color;

/**
 * @author tails1101
 *
 */
public class PuzzleGameMain extends JFrame {
	
	private static final int PROWS = 8;
	private static final int PSIZE = PROWS*PROWS;
	
	private JFileChooser ImageLoader = null;
	private File loadedFile = null;
	private String defaultPath = "C://";
	
	private ImageDatas img = null;
	private PuzzleGameMain thisClass = this;
	private Hint hint = null;
	private Tiles[] tiles = new Tiles[PSIZE];
	
	private JPanel fwdContent;
	private JPanel sltContent2;
	private JPanel mainContent;
	
	private String imgLink;
	private boolean finished;
	private int[] TwoPos = new int[2];
	private int[] TwoID = new int[2];
	private int goswap = 0;
	private int imgNumber = 1;
	
	private JMenuBar menuBar;
	private JMenu mnMenu;
	private JMenuItem info;
	private JMenuItem mntmChangePicture;
	private JMenuItem mntmRegame;
	private JMenuItem mntmExit;
	private JMenu mnHint;
	private JMenuItem mntmHint;
	private JLabel imgview1;
	private JLabel subtext1;
	private JButton buttonPre;
	private JButton buttonNext;
	private JLabel imgN;
	private JButton btnGo;
	private JLabel subtext2;
	private JButton btnLoad;
	
	private JLabel sublogo;
	private JLabel sublogoShadow;
	private JButton btnNewButton;
	private JLabel lblProgrammedByTails;
	private JLabel mainimage;
	private JLabel mainsubimg1;
	private JLabel mainsubimg2;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PuzzleGameMain frame = new PuzzleGameMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PuzzleGameMain() {
		setTitle("K-ON! Puzzle~");
		setBounds(100, 100, 1020, 710);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadedFile = new File(defaultPath);
		ImageLoader = new JFileChooser();
		ImageLoader.setDialogTitle("Load Picture");
		
		initialize();
		sltinitialize();
	}
	
	private void initialize(){
		
		fwdContent = new JPanel();
		sltContent2 = new JPanel();
		mainContent = new JPanel();
		
		setContentPane(fwdContent);  // !
		
		fwdContent.setVisible(true);
		sltContent2.setVisible(false);
		mainContent.setVisible(false);
		
		fwdContent.setBackground(new Color(255, 240, 245));
		fwdContent.setLayout(null);
		
		
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		mntmChangePicture = new JMenuItem("Change Picture");
		mntmChangePicture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int q = JOptionPane.showConfirmDialog(thisClass, "정말로 사진을 변경하시겠습니까?", "Change Picture", JOptionPane.YES_NO_OPTION);
				if(q == JOptionPane.YES_OPTION){
					mainContent.setVisible(false);
					setContentPane(sltContent2);
					sltContent2.setVisible(true);
					menuBar.setVisible(false);
					mainContent.removeAll();
					finished = false;
				}
			}
		});
		mnMenu.add(mntmChangePicture);
		
		mntmRegame = new JMenuItem("ReGame");
		mntmRegame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int q = JOptionPane.showConfirmDialog(thisClass, "정말로 다시하시겠습니까?", "ReGame", JOptionPane.YES_NO_OPTION);
				if(q == JOptionPane.YES_OPTION){
					mainContent.removeAll();
					finished = false;
					mainContent.setVisible(false);
					gameInitialize();
					mainContent.setVisible(true);
				}
			}
		});
		mnMenu.add(mntmRegame);
		
		info = new JMenuItem("Info");
		info.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(thisClass,
						"K-ON 퍼즐게임 \nProgrammed by tails1101", 
						 "Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnMenu.add(info);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int q = JOptionPane.showConfirmDialog(thisClass, "정말로 나가시겠습니까?", "Exit", JOptionPane.YES_NO_OPTION);
				if(q == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
		mnMenu.add(mntmExit);
		
		mnHint = new JMenu("Hint");
		menuBar.add(mnHint);
		
		mntmHint = new JMenuItem("Hint");
		mntmHint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hint.setVisible(true);
			}
		});
		mnHint.add(mntmHint);
		
		menuBar.setVisible(false);
		
		// 게임 앞표지
		JLabel konLogo = new JLabel("");
		konLogo.setIcon(new ImageIcon(PuzzleGameMain.class.getResource("/images/konLogo.png")));
		konLogo.setBounds(251, 15, 575, 171);
		fwdContent.add(konLogo);
		
		sublogo = new JLabel("Puzzle~ Puzzle~");
		sublogo.setForeground(new Color(138, 43, 226));
		sublogo.setFont(new Font("휴먼편지체", Font.BOLD, 50));
		sublogo.setBounds(352, 159, 396, 45);
		fwdContent.add(sublogo);
		
		sublogoShadow = new JLabel("Puzzle~ Puzzle~");
		sublogoShadow.setForeground(new Color(255, 182, 193));
		sublogoShadow.setFont(new Font("휴먼편지체", Font.BOLD, 50));
		sublogoShadow.setBounds(356, 162, 380, 45);
		fwdContent.add(sublogoShadow);
		
		btnNewButton = new JButton("START");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setContentPane(sltContent2);
				fwdContent.setVisible(false);
				sltContent2.setVisible(true);
			}
		});
		btnNewButton.setFont(new Font("휴먼모음T", Font.PLAIN, 50));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(new Color(238, 130, 238));
		btnNewButton.setBounds(347, 544, 307, 80);
		fwdContent.add(btnNewButton);
		
		lblProgrammedByTails = new JLabel("programmed by tails1101");
		lblProgrammedByTails.setFont(new Font("휴먼엽서체", Font.PLAIN, 25));
		lblProgrammedByTails.setBounds(732, 589, 265, 35);
		fwdContent.add(lblProgrammedByTails);
		
		mainimage = new JLabel("");
		mainimage.setIcon(new ImageIcon(PuzzleGameMain.class.getResource("/images/mainimage.png")));
		mainimage.setBounds(276, 259, 509, 293);
		fwdContent.add(mainimage);
		
		mainsubimg1 = new JLabel("");
		mainsubimg1.setIcon(new ImageIcon(PuzzleGameMain.class.getResource("/images/mainsubimage1.png")));
		mainsubimg1.setBounds(468, 173, 365, 274);
		fwdContent.add(mainsubimg1);
		
		mainsubimg2 = new JLabel("");
		mainsubimg2.setIcon(new ImageIcon(PuzzleGameMain.class.getResource("/images/mainsubimage2.png")));
		mainsubimg2.setBounds(644, 330, 100, 75);
		fwdContent.add(mainsubimg2);
		
	}

	private void sltinitialize(){

		// 사진 선택 창
		imgNumber = 1;
		
		sltContent2.setBackground(new Color(255, 240, 245));
		sltContent2.setLayout(null);
		
		imgview1 = new JLabel("");
		imgview1.setIcon(new ImageIcon(PuzzleGameMain.class.getResource("/selectImages/selectImg1.jpg")));
		imgview1.setBounds(250, 148, 500, 313);
		sltContent2.add(imgview1);
		
		subtext1 = new JLabel("/8");
		subtext1.setFont(new Font("휴먼편지체", Font.PLAIN, 30));
		subtext1.setBounds(500, 95, 39, 43);
		sltContent2.add(subtext1);
		
		buttonNext = new JButton("\u25B6");
		buttonNext.setForeground(new Color(255, 255, 255));
		buttonNext.setBackground(new Color(238, 130, 238));
		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				arrowButton('n');
			}
		});
		buttonNext.setFont(new Font("굴림", Font.PLAIN, 50));
		buttonNext.setBounds(778, 268, 84, 72);
		sltContent2.add(buttonNext);
		
		
		buttonPre = new JButton("\u25C0");
		buttonPre.setForeground(new Color(255, 255, 255));
		buttonPre.setBackground(new Color(238, 130, 238));
		buttonPre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				arrowButton('p');
			}
		});
		buttonPre.setFont(new Font("굴림", Font.PLAIN, 50));
		buttonPre.setBounds(140, 268, 84, 72);
		sltContent2.add(buttonPre);
		
		imgN = new JLabel("1");
		imgN.setFont(new Font("휴먼편지체", Font.PLAIN, 30));
		imgN.setBounds(479, 95, 25, 43);
		sltContent2.add(imgN);
		
		btnGo = new JButton("Go!");
		btnGo.setBackground(new Color(138, 43, 226));
		btnGo.setForeground(new Color(255, 255, 255));
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gameInitialize();
				sltContent2.setVisible(false);
				setContentPane(mainContent);
				mainContent.setVisible(true);
			}
		});
		btnGo.setFont(new Font("휴먼편지체", Font.BOLD, 41));
		btnGo.setBounds(305, 487, 120, 58);
		sltContent2.add(btnGo);
		
		btnLoad = new JButton("Load Picture");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ImageLoader.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					loadedFile = ImageLoader.getSelectedFile();
					
					Image orginalImage = null;
					
					try {
						orginalImage = ImageIO.read(loadedFile);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					Image resizedImage = orginalImage.getScaledInstance(500, 313, Image.SCALE_SMOOTH);
					
					imgNumber = 9;
					imgN.setText("x");
					imgview1.setIcon(new ImageIcon(resizedImage));
				}
			}
		});
		btnLoad.setBackground(new Color(30, 144, 255));
		btnLoad.setForeground(new Color(255, 255, 255));
		btnLoad.setFont(new Font("휴먼편지체", Font.PLAIN, 41));
		btnLoad.setBounds(488, 487, 262, 58);
		sltContent2.add(btnLoad);
		
		subtext2 = new JLabel("Select Image");
		subtext2.setFont(new Font("휴먼엽서체", Font.PLAIN, 40));
		subtext2.setBounds(77, 15, 230, 58);
		sltContent2.add(subtext2);
	}
	
	private void gameInitialize() {
		
		menuBar.setVisible(true);
		mainContent.setLayout(new GridLayout(PROWS,PROWS,0,0));
		finished = false;
		
		switch(imgNumber){
			case 1:
				imgLink = "/images/konImage1.jpg";
				break;
			case 2:
				imgLink = "/images/konImage2.png";
				break;	
			case 3:
				imgLink = "/images/konImage3.jpg";  
				break;
			case 4:
				imgLink = "/images/konImage4.jpg";  
				break;
			case 5:
				imgLink = "/images/konImage5.jpg"; 
				break;
			case 6:
				imgLink = "/images/konImage6.jpg"; 
				break;
			case 7:
				imgLink = "/images/konImage7.jpg";
				break;
			case 8:
				imgLink = "/images/konImage8.jpg";  
				break;
			default:
				break;
		}
		
		if(imgNumber >= 1 && imgNumber <= 8){
			img = new ImageDatas(imgLink);
			hint = new Hint(imgLink);
		}
		else{
			img = new ImageDatas(loadedFile);
			hint = new Hint(loadedFile);
		}
       
		for (int i=0; i<PSIZE; ++i){
			tiles[i] = new Tiles();
			tiles[i].setIcon(new ImageIcon(img.getImageData(i)));
		}
		
		for (int i=0; i<PSIZE; ++i){
			int ran;
			
			while(true){
				ran = (int)(Math.random() * PSIZE);
				
				if(tiles[ran].isCollected() == false){
					tiles[ran].setCollected(true);
					break;
				}
			}
			
			tiles[ran].setId(ran);
			tiles[ran].setPos(i);
			
			tiles[ran].addActionListener(new ButtonListener(ran));
			mainContent.add(tiles[ran]);
		}
        
	}
	
	public void arrowButton(char c){
		if(imgNumber == 9)imgNumber = 1;
		else if(c == 'p'){
			if(imgNumber == 1) imgNumber = 8;
			else imgNumber--;
		}
		else if(c == 'n'){
			if(imgNumber == 8) imgNumber = 1;
			else imgNumber++;
		}
		
		String link = null;
		
		switch(imgNumber){
			case 1:
				link = "/selectImages/selectImg1.jpg";  
				break;
			case 2:
				link = "/selectImages/selectImg2.png";  
				break;	
			case 3:
				link = "/selectImages/selectImg3.jpg";  
				break;
			case 4:
				link = "/selectImages/selectImg4.jpg";  
				break;
			case 5:
				link = "/selectImages/selectImg5.jpg";  
				break;
			case 6:
				link = "/selectImages/selectImg6.jpg";  
				break;
			case 7:
				link = "/selectImages/selectImg7.jpg";  
				break;
			case 8:
				link = "/selectImages/selectImg8.jpg";  
				break;
			default:
				break;
		}
		
		imgN.setText(String.valueOf(imgNumber));
		imgview1.setIcon(new ImageIcon(getClass().getResource(link)));
	}
	
	public void ButtonAction(int i){
		
		goswap++;
		
		if(goswap == 1){
			TwoPos[0] = tiles[i].getPos();
			TwoID[0] = tiles[i].getId();
			tiles[i].setEnabled(false);
		}
		else if(goswap == 2){
			TwoPos[1] = tiles[i].getPos();
			TwoID[1] = tiles[i].getId();
			
			tiles[TwoID[0]].setVisible(false);
			mainContent.remove(tiles[TwoID[0]]);
			
			mainContent.add(tiles[TwoID[0]],TwoPos[1]);
			tiles[TwoID[0]].setVisible(true);
			tiles[TwoID[0]].setPos(TwoPos[1]);
			
			tiles[TwoID[1]].setVisible(false);
			mainContent.remove(tiles[TwoID[1]]);
			
			mainContent.add(tiles[TwoID[1]],TwoPos[0]);
			tiles[TwoID[1]].setVisible(true);
			tiles[TwoID[1]].setPos(TwoPos[0]);
			
			tiles[TwoID[0]].setEnabled(true);
			goswap = 0;
			
			boolean isFinish = true;
			
			for(int a=0; a<PSIZE; a++)
				if(tiles[a].getId() != tiles[a].getPos()){
					isFinish = false;
					break;
				}
			
			if(isFinish == true){
				JOptionPane.showMessageDialog(thisClass, "수고하셨습니다!", "Congratulation!", JOptionPane.INFORMATION_MESSAGE);
				finished = true;
			}
			
		}
		
	}
	
	public class ButtonListener implements ActionListener {
		
		private int id;
		
		public ButtonListener(int i){
			id = i;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			if(finished == false)
				ButtonAction(id);
		}
	}
}


