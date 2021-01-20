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
	
	private boolean hasImage = false;  // hasImage: �̹����� �ҷ����� ��� True, �ƴϸ� False
	
	private JPanel fwdScreen;  // fwdScreen: ù ȭ��
	private JPanel gameScreen;  // gameScreen: ���� ȭ��
	private JButton accessButton;  // accessButton: �������� �����ϴ� ��ư
	private JTextField IPTextField;  // IPTextField: IP�ּ� �Է� ��
	private JTextField portTextField;  // portTextField: ��Ʈ��ȣ �Է� ��
	private JTextField userTextField;  // userTextField: ���̵� �Է� ��
	private JRadioButton studentRadioButton;
	private JRadioButton teacherRadioButton;  // �л�, ������ �� �ϳ��� �����ϴ� RadioButton
	private JLabel lblIp;
	private JLabel label;
	private JLabel lblType;
	private JLabel label_2;
	private JLabel lblNewLabel_3;
	
	private JFileChooser ImageLoader = null;  // ImageLoader: PC���� ������ �ҷ��� �� �ְ� ���ִ� â
	private File loadedFile = null;  // loadedFile: �ҷ��� ���� ��ü�� ����
	
	private ButtonGroup btnGroup = new ButtonGroup();  // btnGroup: �л�, �������� Radio Button�� �׷����ϱ� ���� ��
	
	Image resizedImage;  // resizedImage: ���� �̹����κ��� ũ�Ⱑ �������� �̹���
	
	JLabel quizLabel;  // quizLabel: ������ ���� ���� Label
	JLabel hintLabel;  // hintLabel: ��Ʈ�� ���� ���� Label
	JLabel noticeLabel;  // noticeLabel: ������ ���� ���� Label
	JLabel imageLabel;  // imageLabel: �̹����� ���� ���� Label
	JButton submitButton;  // submitButton: ���, ����, ��Ʈ�� �����ϱ� ���� ��ư
	JButton findImageButton;  // findImageButton: ImageLoader�� ���� ���� ��ư
	JTextField inputTextField;  // inputTextField: ���, ����, �Ǵ� ��Ʈ �Է� ��
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
			public void actionPerformed(ActionEvent e) {  // �������� �����ϴ� ��ư�� Ŭ������ ���
				if(IPTextField.getText().equals("")){  // IP�ּҸ� �������� ���
					JOptionPane.showMessageDialog(gameui, "IP�ּҸ� �Է��� �ּ���.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else if(portTextField.getText().equals("")){  // ��Ʈ��ȣ�� �������� ���
					JOptionPane.showMessageDialog(gameui, "��Ʈ��ȣ�� �Է��� �ּ���.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else if(!studentRadioButton.isSelected() && !teacherRadioButton.isSelected()){  // �л�, ������ �� �� �������� �ʾ��� ���
					JOptionPane.showMessageDialog(gameui, "�л�, ������ �� �ϳ��� ������ �ּ���.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else if(userTextField.getText().equals("")){  // ���̵� �������� ���
					JOptionPane.showMessageDialog(gameui, "���̵� �Է��� �ּ���.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else if(!isInteger(portTextField.getText())){  // ��Ʈ��ȣ�� ��ȿ�� �ڿ����� �ƴ� ���
					JOptionPane.showMessageDialog(gameui, "��Ʈ��ȣ�� ��ȿ�� �ڿ����̿��� �մϴ�.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else if(!checkUsername(userTextField.getText())){  // ���̵� ����, ���� �̿��� ���ڰ� �� ���� ���
					JOptionPane.showMessageDialog(gameui, "���̵�� ���� �빮�� �� �ҹ���, ���ڸ� ���˴ϴ�.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else{  // ������ ���� �õ�
					String type = null;
					
					if(studentRadioButton.isSelected())
						type = "Student";
					else if(teacherRadioButton.isSelected())
						type = "Teacher";
					
					setContentPane(gameScreen);
					fwdScreen.setVisible(false);
					gameScreen.setVisible(true);  // ù ȭ�鿡�� ���� ȭ������ �̵���
					
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
		lblNewLabel.setFont(new Font("�޸�����ü", Font.BOLD, 80));
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setBounds(113, 134, 655, 104);
		fwdScreen.add(lblNewLabel);
		
		lblWelcomeToUs = new JLabel("Welcome to US~!!");
		lblWelcomeToUs.setFont(new Font("�޸տ���ü", Font.PLAIN, 40));
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
		noticeLabel.setFont(new Font("����", Font.BOLD, 18));
		noticeLabel.setForeground(Color.RED);
		noticeLabel.setBounds(17, 15, 844, 21);
		gameScreen.add(noticeLabel);
		
		inputTextField = new JTextField();
		inputTextField.setBounds(264, 391, 408, 27);
		gameScreen.add(inputTextField);
		inputTextField.setColumns(10);
		
		quizLabel = new JLabel("\uBB38\uC81C: ");
		quizLabel.setFont(new Font("����", Font.PLAIN, 18));
		quizLabel.setBounds(182, 319, 679, 21);
		gameScreen.add(quizLabel);
		
		hintLabel = new JLabel("\uD78C\uD2B8: ");
		hintLabel.setFont(new Font("����", Font.PLAIN, 18));
		hintLabel.setBounds(182, 355, 679, 21);
		gameScreen.add(hintLabel);
		
		lblNewLabel_3 = new JLabel("\uC785\uB825: ");
		lblNewLabel_3.setFont(new Font("����", Font.PLAIN, 18));
		lblNewLabel_3.setBounds(182, 394, 65, 21);
		gameScreen.add(lblNewLabel_3);
		
		ImageLoader = new JFileChooser();
		
		findImageButton = new JButton("\uCC3E\uAE30");
		findImageButton.setBounds(603, 223, 125, 29);
		findImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  // ImageLoader�� ���� ���� ��ư�� Ŭ������ ���
				if(!client.isQuized()){  // �������� ������ �������� ���� ���¿��� ��ư�� Ŭ������ ���
					JOptionPane.showMessageDialog(gameui, "������ ���� �����ϼž� �մϴ�.", "Error...", JOptionPane.ERROR_MESSAGE);
				}
				else if (ImageLoader.showOpenDialog(gameui) == JFileChooser.APPROVE_OPTION) {
					loadedFile = ImageLoader.getSelectedFile();
					
					Image orginalImage = null;
					
					try {
						orginalImage = ImageIO.read(loadedFile);
						
						resizedImage = orginalImage.getScaledInstance(295, 201, Image.SCALE_SMOOTH);
						imageLabel.setIcon(new ImageIcon(resizedImage));
						hasImage = true;  // �ҷ��� �̹����� ����� �������ؼ� ������ ���� ȭ�鿡 ���
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(gameui, "��ȿ�� �̹��� ������ �ƴմϴ�.", "Error...", JOptionPane.ERROR_MESSAGE);
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
	
	public boolean checkUsername(String word){  // �÷��̾��� ���̵� üũ�� (����, ���ڸ� ���)
		for(int c=0; c<word.length(); c++){
			if(!((word.charAt(c) >= 48 && word.charAt(c) <= 57)
					|| (word.charAt(c) >= 65 && word.charAt(c) <= 90)
					|| (word.charAt(c) >= 97 && word.charAt(c) <= 122)))
				return false;    
		}
		
		return true;
	}
	
	public boolean isInteger(String strnum){  // ��ȿ�� �ڿ������� üũ��
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