package pkg;

import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

/**
 * @author tails1101
 *
 */
public class FindSamePictureMain extends JFrame {
	
	private int recard = 0;
	private int count = 0;
	private int findNum = 0;
	private int loading = 1;
	
	private JPanel mainPane = new JPanel();
	private JPanel contentPane = new JPanel();
	
	private static final int ROWMAX = 4;
	private static final int COLMAX = 4;
	private static final int TOTALMAX = ROWMAX*COLMAX;
	
	CardButtons[] cards = new CardButtons[16];
	CardButtons[] lineCard = new CardButtons[16];
	CardButtons[] compBtns = new CardButtons[2];
	CardButtons tembtn = null;

	private FindSamePictureMain thisClass = this;

	JMenuBar panMenuBar;	
	JMenu fileMenu;
	JMenuItem jmiReGame;
	JMenuItem jmiExit;
	
	private ButtonGroup btnGroup = new ButtonGroup();
	private JPanel gamepanel;
	private JMenuItem info;
	private JButton btnNewButton;
	private JLabel copyright;
	private JLabel sublogo;
	private JLabel sublogoShadow;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FindSamePictureMain frame = new FindSamePictureMain();
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
	public FindSamePictureMain() {
		initialize();
		initialize2();
	}
	
	private void initialize() {
		panMenuBar = new JMenuBar();
		
		panMenuBar.setVisible(false);
		mainPane.setVisible(true);
		contentPane.setVisible(false);
		
		mainPane.setBackground(new Color(255, 240, 245));
		mainPane.setLayout(null);
		
		setContentPane(mainPane);
		
		btnNewButton = new JButton("START");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setContentPane(contentPane);
				mainPane.setVisible(false);
				panMenuBar.setVisible(true);
				contentPane.setVisible(true);
			}
		});
		btnNewButton.setFont(new Font("휴먼모음T", Font.PLAIN, 50));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(new Color(238, 130, 238));
		btnNewButton.setBounds(90, 413, 307, 80);
		mainPane.add(btnNewButton);
		
		copyright = new JLabel("Programmed by tails1101");
		copyright.setFont(new Font("휴먼엽서체", Font.PLAIN, 25));
		copyright.setBounds(114, 611, 267, 38);
		mainPane.add(copyright);
		
		JLabel konLogo = new JLabel("");
		konLogo.setIcon(new ImageIcon(FindSamePictureMain.class.getResource("/images/konLogo.PNG")));
		konLogo.setBounds(35, 111, 575, 171);
		mainPane.add(konLogo);
		
		sublogo = new JLabel("Find Same Picture~");
		sublogo.setForeground(new Color(138, 43, 226));
		sublogo.setFont(new Font("휴먼편지체", Font.BOLD, 40));
		sublogo.setBounds(101, 257, 396, 45);
		mainPane.add(sublogo);
		
		sublogoShadow = new JLabel("Find Same Picture~");
		sublogoShadow.setForeground(new Color(255, 182, 193));
		sublogoShadow.setFont(new Font("휴먼편지체", Font.BOLD, 40));
		sublogoShadow.setBounds(105, 260, 380, 45);
		mainPane.add(sublogoShadow);
	}
	
	private void initialize2() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				System.exit(0);
			}
		});
		setTitle("케이온 같은그림찾기");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 720);
		setJMenuBar(panMenuBar);
		
		fileMenu = new JMenu("Menu");
		panMenuBar.add(fileMenu);
		
		jmiReGame = new JMenuItem("ReGame");
		jmiReGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
					findNum = 0;
					count = 0;
					recard = 0;
					
					contentPane.removeAll();
					contentPane.setVisible(false);

					cardInitialize();
					contentPane.setVisible(true);
			}
		});
		
		info = new JMenuItem("Info");
		info.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(thisClass,
						"K-ON 같은그림찾기 게임 \nProgrammed by tails1101", 
						 "Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		fileMenu.add(info);
		fileMenu.add(jmiReGame);
		
		
		jmiExit = new JMenuItem("Exit");
		jmiExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		
		fileMenu.add(jmiExit);
		contentPane.setLayout(new GridLayout(COLMAX, ROWMAX, 0, 0));
		
		cardInitialize();
	}
	
	void cardInitialize(){
		
		lineCard[0] = new CardButtons("/images/yuiCard.PNG");
		lineCard[1] = new CardButtons("/images/yuiCard.PNG");
		lineCard[2] = new CardButtons("/images/mioCard.PNG");
		lineCard[3] = new CardButtons("/images/mioCard.PNG");
		lineCard[4] = new CardButtons("/images/litsuCard.PNG");
		lineCard[5] = new CardButtons("/images/litsuCard.PNG");
		lineCard[6] = new CardButtons("/images/mugiCard.PNG");
		lineCard[7] = new CardButtons("/images/mugiCard.PNG");
		lineCard[8] = new CardButtons("/images/azusaCard.PNG");
		lineCard[9] = new CardButtons("/images/azusaCard.PNG");
		lineCard[10] = new CardButtons("/images/nodokaCard.PNG");
		lineCard[11] = new CardButtons("/images/nodokaCard.PNG");
		lineCard[12] = new CardButtons("/images/sawakoCard.PNG");
		lineCard[13] = new CardButtons("/images/sawakoCard.PNG");
		lineCard[14] = new CardButtons("/images/uiCard.PNG");
		lineCard[15] = new CardButtons("/images/uiCard.PNG");
		
		for(int i=0; i<TOTALMAX; i++){
			cards[i] = new CardButtons("/images/Null.PNG");
			cards[i].setArrayID(i);
			cards[i].setBackground(new Color(255, 240, 245));
			lineCard[i].setBackground(new Color(255, 240, 245));
		}
		
		int n = 1;
		int n_add = 0;
		
		int k = 0;
		for(int i=0; i<TOTALMAX; i++){  // 그림 식별자 지정
			cards[i].setPicID(n);
			lineCard[i].setPicID(n);
			
			n_add++;
			if(n_add == 2){
				n++;
				n_add = 0;
			}
		}
		cards[0].setPicID(1);
		lineCard[0].setPicID(1);
		cards[1].setPicID(1);
		lineCard[1].setPicID(1);
		
		for(int i=0; i<TOTALMAX; i++){  // 랜덤으로 집어넣기
			
			int ran;
			do{
				ran = (int)(Math.random()*16);
			}while(cards[ran].getIsCollected() == true);
			
			contentPane.add(cards[ran]);
			cards[ran].setIsCollected(true);
			cards[ran].setRealPos(i);
			cards[ran].addActionListener(new ButtonListener1(ran,i));
		}
		
	}
	
	public class ButtonListener1 implements ActionListener{
		private int ID;
		private int POS;
		
		public ButtonListener1(int id, int pos){
			ID = id;
			POS = pos;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			if(loading == 1){
				cards[ID].setVisible(false);
				contentPane.remove(cards[ID]);
				contentPane.add(lineCard[ID], POS);
				lineCard[ID].setVisible(true);
			
				tembtn = cards[ID];
				new TickThread().start();
			}
		}
	}
	
	class TickThread extends Thread {
		
		@Override
		public void run() {
			
			recard++;
			
			if(recard == 1){
				compBtns[0] = tembtn;
			}
			else if(recard == 2){
				compBtns[1] = tembtn;
				
				try {
					loading = 0;
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
					loading = 1;
				}
				
				loading = 1;
				
				if(compBtns[0].getPicID() != compBtns[1].getPicID()){  // 클릭한 두 그림이 서로 다름
					
					int id = compBtns[0].getArrayID();
					int pos = compBtns[0].getRealPos();
					
					lineCard[id].setVisible(false);
					contentPane.remove(lineCard[id]);
					contentPane.add(cards[id], pos);
					cards[id].setVisible(true);
					
					id = compBtns[1].getArrayID();
					pos = compBtns[1].getRealPos();
					
					lineCard[id].setVisible(false);
					contentPane.remove(lineCard[id]);
					contentPane.add(cards[id], pos);
					cards[id].setVisible(true);
				}
				else{
					findNum++;
				}
				
				recard = 0;
				count++;
				
				if(findNum == 8){
					JOptionPane.showMessageDialog(thisClass,
						"수고하셨습니다!\n" + "총 횟수: " + count + " 회", 
							"Congratulation!", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}
}
