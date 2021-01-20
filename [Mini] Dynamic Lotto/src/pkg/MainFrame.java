package pkg;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
	
	private MainFrame thisClass = this;
	
	public static final int WIDTH = 960;
	public static final int HEIGHT = 624;
	
	private ScreenPanel screenPanel;
	
	private JButton btnGetLottoNumber;
	private JButton btnExit;
	
	private JTextField txtfMinNumber;
	private JTextField txtfMaxNumber;
	private JTextField txtfNumOfLotto;
	
	private ImageIcon imgGetLottoNumber;
	private ImageIcon imgGetLottoNumberEntered;
	private ImageIcon imgGetLottoNumberPressed;
	private ImageIcon imgExit;
	private ImageIcon imgExitEntered;
	private ImageIcon imgExitPressed;
	
	public static void main(String[] args){
		new MainFrame();
	}
	
	public MainFrame(){
		setTitle("Dynamic Lotto!!");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(null);
		
		screenPanel = new ScreenPanel();
		screenPanel.setBounds(0, 0, WIDTH, HEIGHT);
		screenPanel.setLayout(null);
		setContentPane(screenPanel);
		
		imgGetLottoNumber = new ImageIcon(getClass().getResource("/images/btnGetLottoNumber.png"));
		imgGetLottoNumberEntered = new ImageIcon(getClass().getResource("/images/btnGetLottoNumberEntered.png"));
		imgGetLottoNumberPressed = new ImageIcon(getClass().getResource("/images/btnGetLottoNumberPressed.png"));
		imgExit = new ImageIcon(getClass().getResource("/images/btnExit.png"));
		imgExitEntered = new ImageIcon(getClass().getResource("/images/btnExitEntered.png"));
		imgExitPressed = new ImageIcon(getClass().getResource("/images/btnExitPressed.png"));
		
		txtfMinNumber = new JTextField();
		txtfMinNumber.setDocument(new JTextFieldLimit(5));
		txtfMinNumber.setBounds(20 + 105 + 10, getCenterPosition(HEIGHT/2, 40) - 80, 100, 40);
		txtfMinNumber.setFont(new Font("돋움", Font.PLAIN, 30));
		screenPanel.add(txtfMinNumber);
		
		txtfMaxNumber = new JTextField();
		txtfMaxNumber.setDocument(new JTextFieldLimit(5));
		txtfMaxNumber.setBounds(((20 + 105 + 10) + 100 + 50) + 111 + 10, getCenterPosition(HEIGHT/2, 40) - 80, 100, 40);
		txtfMaxNumber.setFont(new Font("돋움", Font.PLAIN, 30));
		screenPanel.add(txtfMaxNumber);
		
		txtfNumOfLotto = new JTextField();
		txtfNumOfLotto.setDocument(new JTextFieldLimit(5));
		txtfNumOfLotto.setBounds(((((20 + 105 + 10) + 100 + 50) + 111 + 10) + 100 + 50) + 147 + 10, getCenterPosition(HEIGHT/2, 40) - 80, 100, 40);
		txtfNumOfLotto.setFont(new Font("돋움", Font.PLAIN, 30));
		screenPanel.add(txtfNumOfLotto);
		
		btnGetLottoNumber = new JButton();
		btnGetLottoNumber.setBounds(getCenterPosition(WIDTH/2, 324) - 324/2 - 30, getCenterPosition(HEIGHT/2, 40) - 30 + 120, 324, 112);
		btnGetLottoNumber.setIcon(imgGetLottoNumber);
		btnGetLottoNumber.setBorderPainted(false);
		btnGetLottoNumber.setContentAreaFilled(false);
		btnGetLottoNumber.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnGetLottoNumber.setIcon(imgGetLottoNumberEntered);
				btnGetLottoNumber.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				btnGetLottoNumber.setIcon(imgGetLottoNumber);
				btnGetLottoNumber.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				
				getLottoNumber();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnGetLottoNumber.setIcon(imgGetLottoNumber);
				btnGetLottoNumber.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				btnGetLottoNumber.setIcon(imgGetLottoNumberPressed);
			}
		});
		screenPanel.add(btnGetLottoNumber);
		
		btnExit = new JButton();
		btnExit.setBounds(getCenterPosition(WIDTH/2, 324) + 324/2 + 30, getCenterPosition(HEIGHT/2, 40) - 30 + 120, 324, 112);
		btnExit.setIcon(imgExit);
		btnExit.setBorderPainted(false);
		btnExit.setContentAreaFilled(false);
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnExit.setIcon(imgExitEntered);
				btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				btnExit.setIcon(imgExit);
				btnExit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				
				JLabel label = new JLabel("정말로 나가시겠습니까?");
				label.setFont(new Font("돋움", Font.BOLD, 15));
				
				int q = JOptionPane.showConfirmDialog(thisClass, label, "Exit", JOptionPane.YES_NO_OPTION);
				
				if(q == JOptionPane.YES_OPTION)
					System.exit(0);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnExit.setIcon(imgExit);
				btnExit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				btnExit.setIcon(imgExitPressed);
			}
		});
		screenPanel.add(btnExit);
		
		repaint();
	}
	
	public int getCenterPosition(int pos, int length){
		return pos - length/2;
	}
	
	public boolean isNumber(String str){
		for(int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			
			if( !((c >= '0' && c <= '9') || (c == '-' && i == 0)) )
				return false;
		}
		
		return true;
	}
	
	public int getRandomNumber(int min, int max){
        return (int)(Math.random() * (max-min+1)) + min;
    }
	
	public void getLottoNumber(){
		String strMinNumber = txtfMinNumber.getText();
		String strMaxNumber = txtfMaxNumber.getText();
		String strNumOfLotto = txtfNumOfLotto.getText();
		JLabel label = new JLabel();
		
		if(strMinNumber.equals("")){
			label.setText("최소 값을 입력해 주세요.");
			label.setFont(new Font("돋움", Font.BOLD, 15));
			
			JOptionPane.showMessageDialog(this, label, "Error", JOptionPane.ERROR_MESSAGE);
		}
		else if(strMaxNumber.equals("")){
			label.setText("최대 값을 입력해 주세요.");
			label.setFont(new Font("돋움", Font.BOLD, 15));
			
			JOptionPane.showMessageDialog(this, label, "Error", JOptionPane.ERROR_MESSAGE);
		}
		else if(strNumOfLotto.equals("")){
			label.setText("추첨 갯수를 입력해 주세요.");
			label.setFont(new Font("돋움", Font.BOLD, 15));
			
			JOptionPane.showMessageDialog(this, label, "Error", JOptionPane.ERROR_MESSAGE);
		}
		else if( !isNumber(strMinNumber) ){
			label.setText("최소 값에는 정수가 들어가야 됩니다.");
			label.setFont(new Font("돋움", Font.BOLD, 15));
			
			JOptionPane.showMessageDialog(this, label, "Error", JOptionPane.ERROR_MESSAGE);
		}
		else if( !isNumber(strMaxNumber) ){
			label.setText("최대 값에는 정수가 들어가야 됩니다.");
			label.setFont(new Font("돋움", Font.BOLD, 15));
			
			JOptionPane.showMessageDialog(this, label, "Error", JOptionPane.ERROR_MESSAGE);
		}
		else if( !isNumber(strNumOfLotto) ){
			label.setText("추첨 갯수에는 정수가 들어가야 됩니다.");
			label.setFont(new Font("돋움", Font.BOLD, 15));
			
			JOptionPane.showMessageDialog(this, label, "Error", JOptionPane.ERROR_MESSAGE);
		}
		else{
			int minNumber = Integer.parseInt(strMinNumber);
			int maxNumber = Integer.parseInt(strMaxNumber);
			int numOfLotto = Integer.parseInt(strNumOfLotto);
			
			if(minNumber >= maxNumber){
				label.setText("최소값은 최대값보다 더 작아야 됩니다.");
				label.setFont(new Font("돋움", Font.BOLD, 15));
				
				JOptionPane.showMessageDialog(this, label, "Error", JOptionPane.ERROR_MESSAGE);
			}
			else if(maxNumber - minNumber + 1 <= numOfLotto){
				label.setText("추첨 갯수는 범위보다 더 작아야 됩니다.");
				label.setFont(new Font("돋움", Font.BOLD, 15));
				
				JOptionPane.showMessageDialog(this, label, "Error", JOptionPane.ERROR_MESSAGE);
			}
			else{
				int[] allNumber = new int[maxNumber - minNumber + 1];
				boolean[] selected = new boolean[maxNumber - minNumber + 1];
				int n = minNumber;
				
				for(int i=0; i<maxNumber-minNumber+1; i++){
					allNumber[i] = n;
					selected[i] = false;
					n++;
				}
				
				int[] selectedNumbers = new int[numOfLotto];
				int selectedIndex;
				int a = 0;
				
				while(a < numOfLotto){
					while(true){
						selectedIndex = getRandomNumber(0, maxNumber-minNumber+1-1);
						
						if(!selected[selectedIndex]){
							selected[selectedIndex] = true;
							break;
						}
					}
					
					selectedNumbers[a] = allNumber[selectedIndex];
					a++;
				}
				
				Arrays.sort(selectedNumbers);
				
				String selectedNums = "<br><br><font size=40 color='red'>";
				
				for(int i=0; i<numOfLotto; i++)
					selectedNums += selectedNumbers[i] + "<br>";
				
				label.setText("<html>당첨 번호는 아래와 같습니다!" + selectedNums + "</font><br>당첨되신 분들 축하드려요!! ^0^</html>");
				label.setFont(new Font("돋움", Font.BOLD, 15));
				
				JOptionPane.showMessageDialog(this, label, "Congratulation!", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}