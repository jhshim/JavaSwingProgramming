package pkg;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GameUI extends JFrame {
	
	private GameUI gameui = this;
	
	private Client client;
	
	private boolean hasImage = false;  // hasImage: 이미지를 불러왔을 경우 True, 아니면 False
	
	private JPanel fwdScreen;  // fwdScreen: 첫 화면
	private JPanel gameScreen;  // gameScreen: 게임 화면
	private JButton accessButton;  // accessButton: 서버한테 접속하는 버튼
	private JTextField IPTextField;  // IPTextField: IP주소 입력 란
	private JTextField portTextField;  // portTextField: 포트번호 입력 란
	private JTextField userTextField;  // userTextField: 아이디 입력 란
	private JRadioButton studentRadioButton;
	private JRadioButton teacherRadioButton;  // 학생, 선생님 중 하나를 선택하는 RadioButton
	private JLabel lblIp;
	private JLabel label;
	private JLabel lblType;
	private JLabel label_2;
	private JLabel lblNewLabel_3;
	
	private JFileChooser ImageLoader = null;  // ImageLoader: PC에서 파일을 불러올 수 있게 해주는 창
	private File loadedFile = null;  // loadedFile: 불러온 파일 자체를 저장
	
	private ButtonGroup btnGroup = new ButtonGroup();  // btnGroup: 학생, 선생님의 Radio Button을 그룹핑하기 위한 것
	
	Image resizedImage;  // resizedImage: 원본 이미지로부터 크기가 재조절된 이미지
	
	JLabel quizLabel;  // quizLabel: 문제를 띄우기 위한 Label
	JLabel hintLabel;  // hintLabel: 힌트를 띄우기 위한 Label
	JLabel noticeLabel;  // noticeLabel: 공지를 띄우기 위한 Label
	JLabel imageLabel;  // imageLabel: 이미지를 띄우기 위한 Label
	JButton submitButton;  // submitButton: 답안, 정답, 힌트를 전송하기 위한 버튼
	JButton findImageButton;  // findImageButton: ImageLoader를 띄우기 위한 버튼
	JTextField inputTextField;  // inputTextField: 답안, 정답, 또는 힌트 입력 란
	private JLabel lblNewLabel;
	private JLabel lblWelcomeToUs;
	
	public GameUI(){
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});
		
		setTitle("Jaum Quiz~!!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		
		initialize1();
		initialize2();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameUI frame = new GameUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void initialize1(){
		fwdScreen = new JPanel();
		gameScreen = new JPanel();
		
		setContentPane(fwdScreen);
		
		fwdScreen.setVisible(true);
		gameScreen.setVisible(false);
		
		fwdScreen.setBackground(new Color(204, 255, 255));
		fwdScreen.setLayout(null);
		
		accessButton = new JButton("\uC811\uC18D");
		accessButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  // 서버한테 접속하는 버튼을 클릭했을 경우
				if(IPTextField.getText().equals("")){  // IP주소를 누락했을 경우
					JOptionPane.showMessageDialog(gameui, "IP주소를 입력해 주세요.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else if(portTextField.getText().equals("")){  // 포트번호를 누락했을 경우
					JOptionPane.showMessageDialog(gameui, "포트번호를 입력해 주세요.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else if(!studentRadioButton.isSelected() && !teacherRadioButton.isSelected()){  // 학생, 선생님 둘 다 선택하지 않았을 경우
					JOptionPane.showMessageDialog(gameui, "학생, 선생님 중 하나를 선택해 주세요.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else if(userTextField.getText().equals("")){  // 아이디를 누락했을 경우
					JOptionPane.showMessageDialog(gameui, "아이디를 입력해 주세요.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else if(!isInteger(portTextField.getText())){  // 포트번호가 유효한 자연수가 아닌 경우
					JOptionPane.showMessageDialog(gameui, "포트번호는 유효한 자연수이여야 합니다.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else if(!checkUsername(userTextField.getText())){  // 아이디에 영문, 숫자 이외의 문자가 들어가 있을 경우
					JOptionPane.showMessageDialog(gameui, "아이디는 영문 대문자 및 소문자, 숫자만 허용됩니다.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else{  // 서버에 접속 시도
					String type = null;
					
					if(studentRadioButton.isSelected())
						type = "Student";
					else if(teacherRadioButton.isSelected())
						type = "Teacher";
					
					setContentPane(gameScreen);
					fwdScreen.setVisible(false);
					gameScreen.setVisible(true);  // 첫 화면에서 게임 화면으로 이동함
					
					client = new Client(gameui, IPTextField.getText(),
							Integer.parseInt(portTextField.getText()), userTextField.getText(), type);
					client.execute();
				}
			}
		});
		accessButton.setBounds(367, 546, 125, 29);
		fwdScreen.add(accessButton);
		
		IPTextField = new JTextField();
		IPTextField.setBounds(342, 307, 249, 27);
		fwdScreen.add(IPTextField);
		IPTextField.setColumns(10);
		
		portTextField = new JTextField();
		portTextField.setColumns(10);
		portTextField.setBounds(342, 358, 249, 27);
		fwdScreen.add(portTextField);
		
		userTextField = new JTextField();
		userTextField.setColumns(10);
		userTextField.setBounds(342, 466, 249, 27);
		fwdScreen.add(userTextField);
		
		studentRadioButton = new JRadioButton("\uD559\uC0DD");
		studentRadioButton.setBounds(342, 412, 123, 29);
		fwdScreen.add(studentRadioButton);
		
		teacherRadioButton = new JRadioButton("\uC120\uC0DD\uB2D8");
		teacherRadioButton.setBounds(474, 412, 117, 29);
		fwdScreen.add(teacherRadioButton);
		
		lblIp = new JLabel("IP\uC8FC\uC18C:");
		lblIp.setBounds(230, 310, 89, 21);
		fwdScreen.add(lblIp);
		
		label = new JLabel("\uD3EC\uD2B8\uBC88\uD638:");
		label.setBounds(230, 361, 89, 21);
		fwdScreen.add(label);
		
		lblType = new JLabel("Type:");
		lblType.setBounds(230, 416, 89, 21);
		fwdScreen.add(lblType);
		
		label_2 = new JLabel("\uC544\uC774\uB514:");
		label_2.setBounds(230, 469, 89, 21);
		fwdScreen.add(label_2);
		
		btnGroup.add(studentRadioButton);
		btnGroup.add(teacherRadioButton);
		
		lblNewLabel = new JLabel("Online Jaum Quiz");
		lblNewLabel.setFont(new Font("휴먼편지체", Font.BOLD, 80));
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setBounds(113, 134, 655, 104);
		fwdScreen.add(lblNewLabel);
		
		lblWelcomeToUs = new JLabel("Welcome to US~!!");
		lblWelcomeToUs.setFont(new Font("휴먼엽서체", Font.PLAIN, 40));
		lblWelcomeToUs.setForeground(Color.RED);
		lblWelcomeToUs.setBounds(85, 83, 296, 61);
		fwdScreen.add(lblWelcomeToUs);
	}
	
	private void initialize2(){
		
		gameScreen.setBackground(new Color(204, 255, 255));
		gameScreen.setLayout(null);
		
		imageLabel = new JLabel("(image)");
		imageLabel.setIcon(new ImageIcon(GameUI.class.getResource("/pkg/default image.png")));
		imageLabel.setBackground(new Color(255, 255, 255));
		imageLabel.setBounds(291, 51, 295, 201);
		gameScreen.add(imageLabel);
		
		noticeLabel = new JLabel("Notice: ");
		noticeLabel.setFont(new Font("돋움", Font.BOLD, 18));
		noticeLabel.setForeground(Color.RED);
		noticeLabel.setBounds(17, 15, 844, 21);
		gameScreen.add(noticeLabel);
		
		inputTextField = new JTextField();
		inputTextField.setBounds(264, 391, 408, 27);
		gameScreen.add(inputTextField);
		inputTextField.setColumns(10);
		
		quizLabel = new JLabel("\uBB38\uC81C: ");
		quizLabel.setFont(new Font("돋움", Font.PLAIN, 18));
		quizLabel.setBounds(182, 319, 679, 21);
		gameScreen.add(quizLabel);
		
		hintLabel = new JLabel("\uD78C\uD2B8: ");
		hintLabel.setFont(new Font("돋움", Font.PLAIN, 18));
		hintLabel.setBounds(182, 355, 679, 21);
		gameScreen.add(hintLabel);
		
		lblNewLabel_3 = new JLabel("\uC785\uB825: ");
		lblNewLabel_3.setFont(new Font("돋움", Font.PLAIN, 18));
		lblNewLabel_3.setBounds(182, 394, 65, 21);
		gameScreen.add(lblNewLabel_3);
		
		ImageLoader = new JFileChooser();
		
		findImageButton = new JButton("\uCC3E\uAE30");
		findImageButton.setBounds(603, 223, 125, 29);
		findImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  // ImageLoader를 띄우기 위한 버튼을 클릭했을 경우
				if(!client.isQuized()){  // 선생님이 문제를 출제하지 않은 상태에서 버튼을 클릭했을 경우
					JOptionPane.showMessageDialog(gameui, "문제를 먼저 출제하셔야 합니다.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else if (ImageLoader.showOpenDialog(gameui) == JFileChooser.APPROVE_OPTION) {
					loadedFile = ImageLoader.getSelectedFile();
					
					Image orginalImage = null;
					
					try {
						orginalImage = ImageIO.read(loadedFile);
						
						resizedImage = orginalImage.getScaledInstance(295, 201, Image.SCALE_SMOOTH);
						imageLabel.setIcon(new ImageIcon(resizedImage));
						hasImage = true;  // 불러온 이미지를 사이즈를 재조절해서 저장한 다음 화면에 띄움
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(gameui, "유효한 이미지 파일이 아닙니다.", "Error...", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
			}
		});
		gameScreen.add(findImageButton);
		
		submitButton = new JButton("\uD655\uC778");
		submitButton.setBounds(364, 524, 125, 29);
		gameScreen.add(submitButton);
	}
	
	public boolean checkUsername(String word){  // 플레이어의 아이디를 체크함 (영문, 숫자만 허용)
		for(int c=0; c<word.length(); c++){
			if(!((word.charAt(c) >= 48 && word.charAt(c) <= 57)
					|| (word.charAt(c) >= 65 && word.charAt(c) <= 90)
					|| (word.charAt(c) >= 97 && word.charAt(c) <= 122)))
				return false;    
		}
		
		return true;
	}
	
	public boolean isInteger(String strnum){  // 유효한 자연수인지 체크함
		if(strnum.length() == 1 && strnum.charAt(0) == '0')
			return false;
		
		for(int c=0; c<strnum.length(); c++){
			if(!(strnum.charAt(c) >= 48 && strnum.charAt(c) <= 57))
				return false;    
		}
		
		return true;
	}
	
	public void exit(){
		System.exit(0);
	}

	public boolean HasImage() {
		return hasImage;
	}

	public void setHasImage(boolean hasImage) {
		this.hasImage = hasImage;
	}
}