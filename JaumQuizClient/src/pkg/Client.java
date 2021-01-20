package pkg;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Client {
	
	private GameUI gameui;
	
	private Sender sender;  // sender: Client for Sending
	private Receiver receiver;  // receiver: Client for Receiving
	
	private String IPAddress;  // IPAddress: IP�ּ�
	private int portNumber;  // portNumber: ��Ʈ��ȣ
	
	private String username;  // username: �÷��̾��� ���̵�
	private String type;  // type: �÷��̾��� Ÿ�� (�л� or ������)
	private boolean isQuized = false;  // isQuized: ������ �����Ǿ������� �ǹ�
	
	public Client(GameUI ui, String IP, int port, String n, String t){
		this.IPAddress = IP;
		this.portNumber = port;
		this.gameui = ui;
		this.username = n;
		this.type = t;
	}
	
	public void execute(){
		// ������ �����ϴ� ����
		try {
            Socket socket = new Socket(IPAddress, portNumber);  // ������ �����ϰ� ������ ������
            JOptionPane.showMessageDialog(gameui, "������ ���������� ���� �Ǿ����ϴ�!", "Notice", JOptionPane.INFORMATION_MESSAGE);

            if(type.equals("Student")){  // Type�� �л����� ������������ ���� Notice�� �ٸ��� �����
            	gameui.noticeLabel.setText("Notice: �������� ������ �Է��ϴ� ���Դϴ�...");
            	gameui.findImageButton.setEnabled(false);  // �л��� ��Ʈ�� �ο��� �� �����Ƿ� �̹��� ���� ��ư�� ��Ȱ��ȭ��
            }
            else if(type.equals("Teacher")){
            	gameui.noticeLabel.setText("Notice: ����� �������Դϴ�. ������ �Է��Ͻʽÿ�.");
            }
            	
            sender = new Sender(socket);
    		receiver = new Receiver(socket);
    		
    		sender.start();
    		receiver.start();  // Thread Start
    		
        } catch(ConnectException ce) {
        	JOptionPane.showMessageDialog(gameui, "���ῡ �����Ͽ����ϴ�.", "Error...", JOptionPane.ERROR_MESSAGE);
            ce.printStackTrace();
            gameui.exit();
        } catch(Exception e) {
        	JOptionPane.showMessageDialog(gameui, "���ῡ �����Ͽ����ϴ�.", "Error...", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            gameui.exit();
        }
	}
	
	public byte[] ImageToBytes (Image img){  // �̹����� ����Ʈ�� �迭�� �ٲ���
		 // open image
		 BufferedImage bufferedImage = BufferedImage(img);
		 
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 byte[] imageInByte = null;
			try {
				ImageIO.write( bufferedImage, "jpg", baos );
				baos.flush();
				imageInByte = baos.toByteArray();
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		 return imageInByte;
	}
	
	public BufferedImage BufferedImage(Image img)  // �Ϲ� �̹����� Buffered Image�� �ٲ���
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	public BufferedImage BytesToImage(byte[] bytes){  // ����Ʈ�� �迭�� �̹����� �ٲ���
		
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return img;
	}
	
	class Sender extends Thread{
		
		Socket socket;
        DataOutputStream out;
		
		public Sender(Socket s){
			this.socket = s;
			
			try {
                out = new DataOutputStream(socket.getOutputStream());
            } catch(Exception e) {
                e.printStackTrace();
            }
		}
		
		@Override
		public void run(){
			try {
                if (out != null) {
                	String str = username + " " + type;
                    out.write(str.getBytes("UTF-8"));
                    out.flush();  // �켱 �÷��̾��� ���̵�� Ÿ��(�л�/������)�� �������� ������
                }
			} catch(IOException e1) {
                e1.printStackTrace();
            }

            gameui.submitButton.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {  // ���, ����, ��Ʈ�� �����ϱ� ���� ��ư�� Ŭ������ ���
        			// Ȯ�� ��ư�� ������ ���� �׼�
        			try{
        				
        				if(!isKorean(gameui.inputTextField.getText())  // ����, Ư������, ����, ���� �� �ʰ��� �����ϱ� ���� ��� (��, ��Ʈ �Է��� ����)
        						&& (type.equals("Student") || (type.equals("Teacher") && !isQuized))){
        					JOptionPane.showMessageDialog(gameui,
        							"����, Ư������, ����� ������ �ʽ��ϴ�.\n�׸��� 1�� �̻� 20�� �̳��� �Է��Ͻʽÿ�.", "Error...", JOptionPane.ERROR_MESSAGE);
        				}
        				else{
        					
        					if(type.equals("Student")){
        						if(!isQuized){  // �������� ������ �������� ���� ���¿��� ����� �Է����� ���
        							JOptionPane.showMessageDialog(gameui,
        									"�������� ���� ������ �������� �ʾҽ��ϴ�.", "Error...", JOptionPane.ERROR_MESSAGE);
        						}
        						else{
        							String msg = "inputAnswer " + username + " " + gameui.inputTextField.getText();
        							out.write(msg.getBytes("UTF-8"));
        							out.flush();
        							gameui.inputTextField.setText("");  // ����� �÷��̾��� ���̵�� �Բ� �������� ������
        						}
        					}
        					else if(type.equals("Teacher")){
        						if(!isQuized){  // ������ ���� �������� ���� �����̹Ƿ� ������ �������� ����
        							String msg = "setQuiz " + gameui.inputTextField.getText();
        							out.write(msg.getBytes("UTF-8"));
        							isQuized = true;
        							gameui.inputTextField.setText("");
        						}
        						else{  // ������ ������ �����̹Ƿ� ��Ʈ�� �������� ����
        							int len = gameui.inputTextField.getText().length();
        							String strlen = String.valueOf(len);
        							
        							if(len < 10)  // ��Ʈ�� ���̰� 10 �̸��̸� �տ� 0�� ���� (��: 5 -> 05)
        								strlen = "0" + strlen;
        							
        							String msg1 = "setHint " + strlen + " " + gameui.inputTextField.getText();
        							out.write(msg1.getBytes("UTF-8"));
        							gameui.inputTextField.setText("");
        							
        							if(gameui.HasImage()){ // �̹����� ���� ��� �ҷ��� �̹����� �л������� ����
        								// �̹����� byte-array ���·� �����ϴ� ����
        								byte[] bytes = ImageToBytes(gameui.resizedImage);
        								
        								String msg2 = "submitImage " + bytes.length;
            							out.write(msg2.getBytes("UTF-8"));
            							
            							int x = 0;
            							for(int i=0; i<bytes.length; i++){
            								byte[] temp = new byte[1020];
            								
            								for(int j=0; j<1020; j++){
            									if(x >= bytes.length){
            										break;
            									}
            									else{
            										temp[j] = bytes[x];
            										x++;
            									}
            								}
            								
            								out.write(temp);
            							}
        								
        								gameui.setHasImage(false);
        								gameui.imageLabel.setIcon(new ImageIcon(GameUI.class.getResource("/pkg/default image.png")));
        							}
        						}
        						
        						out.flush();
        					}
        				}
        			} catch(Exception e2){
        				e2.printStackTrace();
        			}
        		}
            });           
		}
		
		public boolean isKorean(String word){  // ������ �ѱ����� üũ�� [ex: ��(O), ��(X)]
			if(word.length() <= 0 || word.length() > 20)  // 20�ڸ� �ʰ��� ��� false
				return false;
			
			for(int c=0; c<word.length(); c++){
				if(!(word.charAt(c) >= '��' && word.charAt(c) <= '�R'))  // �ѱ��� �ƴϸ� false
					return false;
			}
			
			return true;
		}
	}

	class Receiver extends Thread{
		
		Socket socket;
        DataInputStream in;
        
        public Receiver(Socket s){
			this.socket = s;
			
			try {
                in = new DataInputStream(socket.getInputStream());
            } catch(IOException e) {
                e.printStackTrace();
            }
		}
        
        @Override
		public void run(){
        	String msg;
        	String[] orderinfo;  // orderinfo[]: ��ɹ��� ���⸦ �������� �и��ؼ� �����ϱ� ���� �迭
        	
        	try {
                while(in != null) {
                	byte[] bytes = new byte[1024];
                	
                	in.read(bytes, 0, 1024);  // �����κ��� ��ɹ��� ����
                     
                	msg = new String(bytes, "UTF-8");
                    orderinfo = msg.split(" ");  // ��ɹ��� ���⸦ �������� �и��� �� �迭 �ӿ� ������
                     
                    // �������� ��ɿ� ���� �׼��� ����
                     if(orderinfo[0].equals("setUnable")){  // ��ȿȭ ��ɾ �޾��� ��� ������ close�ϰ� ������
                    	 JOptionPane.showMessageDialog(gameui,
									"���̵� �ߺ�, �������� �ߺ�, �Ǵ� �ο� �� �ʰ��� ���� ��� ����� ��Ȱ��ȭ �Ǿ����ϴ�.", "Error...", JOptionPane.ERROR_MESSAGE);
                    	 gameui.exit();
                    	 
                    	 return;
                     }
                     
                     if(orderinfo[0].equals("notQuized")){  // �������� ������ ������ ��� ���� ���� ���¸� false�� ������
                    	 JOptionPane.showMessageDialog(gameui,
									"�������� ������ ������ " + orderinfo[1] + "������ �ٲ�����ϴ�.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    	 isQuized = false;
                     }
                     else if(orderinfo[0].equals("allClear")){  // ȭ�� Ŭ���� ��ɾ �޾��� ���
                    	 gameui.quizLabel.setText("����: ");
                    	 gameui.hintLabel.setText("��Ʈ: ");
                    	 gameui.imageLabel.setIcon(new ImageIcon(GameUI.class.getResource("/pkg/default image.png")));
                    	 isQuized = true;  // ȭ���� clear �Ǿ��ٴ� ���� ���ο� ������ �����Ǿ��ٴ� ��
                     }
                     else{
                    	 
                    	 if(type.equals("Student")){
                    		 if(orderinfo[0].equals("setQuiz")){  // ������(����)�� ������ �������� ��� �� ������ ������
                    			 gameui.noticeLabel.setText("Notice: ������ �����Ǿ����ϴ�. ������ ���纸����!");
                    			 gameui.quizLabel.setText("����: " + orderinfo[1]);
                    		 }
                    		 else if(orderinfo[0].equals("showAnswer")){
                    			 if(orderinfo[2].equals(username)){  // �ڽ��� ������ ���� ���
                    				 JOptionPane.showMessageDialog(gameui,
         									"������ ���߼̳׿�!\n������ �Է��Ͻ� ��� " + orderinfo[1] + "�Դϴ�.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    			 }
                    			 else{  // ������ ������ ������� ���
                    				 JOptionPane.showMessageDialog(gameui,
                    						 orderinfo[2] + "���� ������ ������ϴ�.\n������ " + orderinfo[1] + "�����ϴ�.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    				 gameui.noticeLabel.setText("Notice: �������� ������ �Է��ϴ� ���Դϴ�...");
                    				 isQuized = false;  // �ڽ��� ������ ������ ��� setTeacher ����� ���� isQuized�� 0�� �ǹǷ� if���� �ƴ� else�� ������ ���� 
                    			 }
                    		 }
                    		 else if(orderinfo[0].equals("setTeacher")){  // ������ ���߸� clientType�� ���������� ������
                    			 gameui.noticeLabel.setText("Notice: ����� �������Դϴ�. ������ �Է��Ͻʽÿ�.");
                    			 gameui.findImageButton.setEnabled(true);
                    			 type = "Teacher";
                    			 isQuized = false;  // setTeacher ����� �޾Ҵٴ� ���� �� ����� �� ���ο� ������ ���� �� ���̶�� ��
                    		 }
                    		 else if(orderinfo[0].equals("setHint")){  // �������� ��Ʈ�� �Է����� ���
                    			 gameui.hintLabel.setText("��Ʈ: " + msg.substring(11, 11+Integer.parseInt(orderinfo[1])));
                    		 }
                    		 else if(orderinfo[0].equals("submitImage")){  // �������� ��Ʈ�� �̹����� �������� ���
                    			 int length = Integer.parseInt(orderinfo[1]);
                    			 byte[] imgBytes = new byte[length]; 
                    			 
                    			 int x = 0;
                    			 for(int i=0; i<length; i=i+1020){
                    				 byte[] temp = new byte[1020];
                    				 
                    				 in.read(temp, 0, 1020);
                    				 
                    				 for(int j=0; j<1020; j++){
                    					 if(x >= length){
                    						 break;
     									 }
                    					 else{
                    						 imgBytes[x] = temp[j];
                    						 x++;
                    					 }
                    				 }
                    			 }
                    			 
                    			 gameui.imageLabel.setIcon(new ImageIcon( BytesToImage(imgBytes) ));  // �������� ������ ��Ʈ�� �̹����� ���
                    		 }
                    	 }
                    	 else if(type.equals("Teacher")){
                    		 if(orderinfo[0].equals("setQuiz")){  // �������� ������ �������� ���
                    			 gameui.noticeLabel.setText("Notice: ������ �����Ǿ����ϴ�. ��Ʈ�� �Է��� �ּ���.");
                    			 gameui.quizLabel.setText("����: " + orderinfo[1]);
                    		 }
                    		 else if(orderinfo[0].equals("showAnswer")){  // �л��� ������ ������ ��� �� �л��� ���̵� �����
                    			 JOptionPane.showMessageDialog(gameui,
                						 orderinfo[2] + "���� ������ ������ϴ�.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    		 }
                    		 else if(orderinfo[0].equals("unsetTeacher")){  // �л��� ������ ���߸� '������'�̶�� ������ �� �л����� �Ѿ
                    			 gameui.noticeLabel.setText("Notice: �������� ������ �Է��ϴ� ���Դϴ�...");
                    			 gameui.findImageButton.setEnabled(false);
                    			 type = "Student";
                    			 isQuized = false;  // unsetTeacher ����� �޾Ҵٴ� ���� ���ο� ������ �� ���� �� ���̶�� ��
                    		 }
                    	 }
                     }
                 } 
             } catch(IOException e) {
            	 e.printStackTrace();
             }
		}
		
	}

	public boolean isQuized() {
		return isQuized;
	}
}