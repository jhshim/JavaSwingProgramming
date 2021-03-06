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
	
	private String IPAddress;  // IPAddress: IP주소
	private int portNumber;  // portNumber: 포트번호
	
	private String username;  // username: 플레이어의 아이디
	private String type;  // type: 플레이어의 타입 (학생 or 선생님)
	private boolean isQuized = false;  // isQuized: 문제가 출제되었는지를 의미
	
	public Client(GameUI ui, String IP, int port, String n, String t){
		this.IPAddress = IP;
		this.portNumber = port;
		this.gameui = ui;
		this.username = n;
		this.type = t;
	}
	
	public void execute(){
		// 서버에 접속하는 과정
		try {
            Socket socket = new Socket(IPAddress, portNumber);  // 소켓을 생성하고 서버와 연결함
            JOptionPane.showMessageDialog(gameui, "서버에 정상적으로 접속 되었습니다!", "Notice", JOptionPane.INFORMATION_MESSAGE);

            if(type.equals("Student")){  // Type이 학생인지 선생님인지에 따라 Notice를 다르게 출력함
            	gameui.noticeLabel.setText("Notice: 선생님이 문제를 입력하는 중입니다...");
            	gameui.findImageButton.setEnabled(false);  // 학생은 힌트를 부여할 수 없으므로 이미지 전송 버튼이 비활성화됨
            }
            else if(type.equals("Teacher")){
            	gameui.noticeLabel.setText("Notice: 당신은 선생님입니다. 문제를 입력하십시오.");
            }
            	
            sender = new Sender(socket);
    		receiver = new Receiver(socket);
    		
    		sender.start();
    		receiver.start();  // Thread Start
    		
        } catch(ConnectException ce) {
        	JOptionPane.showMessageDialog(gameui, "연결에 실패하였습니다.", "Error...", JOptionPane.ERROR_MESSAGE);
            ce.printStackTrace();
            gameui.exit();
        } catch(Exception e) {
        	JOptionPane.showMessageDialog(gameui, "연결에 실패하였습니다.", "Error...", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            gameui.exit();
        }
	}
	
	public byte[] ImageToBytes (Image img){  // 이미지를 바이트형 배열로 바꿔줌
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
	
	public BufferedImage BufferedImage(Image img)  // 일반 이미지를 Buffered Image로 바꿔줌
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
	
	public BufferedImage BytesToImage(byte[] bytes){  // 바이트형 배열을 이미지로 바꿔줌
		
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
                    out.flush();  // 우선 플레이어의 아이디와 타입(학생/선생님)을 서버한테 전송함
                }
			} catch(IOException e1) {
                e1.printStackTrace();
            }

            gameui.submitButton.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {  // 답안, 정답, 힌트를 전송하기 위한 버튼을 클릭했을 경우
        			// 확인 버튼을 눌렀을 때의 액션
        			try{
        				
        				if(!isKorean(gameui.inputTextField.getText())  // 영문, 특수문자, 띄어쓰기, 글자 수 초과를 금지하기 위해 사용 (단, 힌트 입력은 제외)
        						&& (type.equals("Student") || (type.equals("Teacher") && !isQuized))){
        					JOptionPane.showMessageDialog(gameui,
        							"영문, 특수문자, 띄어쓰기는 허용되지 않습니다.\n그리고 1자 이상 20자 이내로 입력하십시오.", "Error...", JOptionPane.ERROR_MESSAGE);
        				}
        				else{
        					
        					if(type.equals("Student")){
        						if(!isQuized){  // 선생님이 문제를 출제하지 않은 상태에서 답안을 입력했을 경우
        							JOptionPane.showMessageDialog(gameui,
        									"선생님이 아직 문제를 출제하지 않았습니다.", "Error...", JOptionPane.ERROR_MESSAGE);
        						}
        						else{
        							String msg = "inputAnswer " + username + " " + gameui.inputTextField.getText();
        							out.write(msg.getBytes("UTF-8"));
        							out.flush();
        							gameui.inputTextField.setText("");  // 답안을 플레이어의 아이디와 함께 서버한테 전송함
        						}
        					}
        					else if(type.equals("Teacher")){
        						if(!isQuized){  // 문제가 아직 출제되지 않은 상태이므로 정답을 서버한테 전송
        							String msg = "setQuiz " + gameui.inputTextField.getText();
        							out.write(msg.getBytes("UTF-8"));
        							isQuized = true;
        							gameui.inputTextField.setText("");
        						}
        						else{  // 문제가 출제된 상태이므로 힌트를 서버한테 전송
        							int len = gameui.inputTextField.getText().length();
        							String strlen = String.valueOf(len);
        							
        							if(len < 10)  // 힌트의 길이가 10 미만이면 앞에 0을 붙임 (예: 5 -> 05)
        								strlen = "0" + strlen;
        							
        							String msg1 = "setHint " + strlen + " " + gameui.inputTextField.getText();
        							out.write(msg1.getBytes("UTF-8"));
        							gameui.inputTextField.setText("");
        							
        							if(gameui.HasImage()){ // 이미지가 있을 경우 불러온 이미지를 학생들한테 전송
        								// 이미지를 byte-array 형태로 전송하는 과정
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
		
		public boolean isKorean(String word){  // 완전한 한글인지 체크함 [ex: 가(O), ㄱ(X)]
			if(word.length() <= 0 || word.length() > 20)  // 20자를 초과할 경우 false
				return false;
			
			for(int c=0; c<word.length(); c++){
				if(!(word.charAt(c) >= '가' && word.charAt(c) <= '힣'))  // 한글이 아니면 false
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
        	String[] orderinfo;  // orderinfo[]: 명령문을 띄어쓰기를 기준으로 분리해서 저장하기 위한 배열
        	
        	try {
                while(in != null) {
                	byte[] bytes = new byte[1024];
                	
                	in.read(bytes, 0, 1024);  // 서버로부터 명령문을 받음
                     
                	msg = new String(bytes, "UTF-8");
                    orderinfo = msg.split(" ");  // 명령문을 띄어쓰기를 기준으로 분리한 후 배열 속에 저장함
                     
                    // 제공받은 명령에 따라 액션을 수행
                     if(orderinfo[0].equals("setUnable")){  // 무효화 명령어를 받았을 경우 소켓을 close하고 종료함
                    	 JOptionPane.showMessageDialog(gameui,
									"아이디 중복, 선생님의 중복, 또는 인원 수 초과로 인해 모든 기능이 비활성화 되었습니다.", "Error...", JOptionPane.ERROR_MESSAGE);
                    	 gameui.exit();
                    	 
                    	 return;
                     }
                     
                     if(orderinfo[0].equals("notQuized")){  // 선생님이 게임을 나갔을 경우 문제 출제 상태를 false로 변경함
                    	 JOptionPane.showMessageDialog(gameui,
									"선생님이 게임을 나가서 " + orderinfo[1] + "님으로 바뀌었습니다.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    	 isQuized = false;
                     }
                     else if(orderinfo[0].equals("allClear")){  // 화면 클리어 명령어를 받았을 경우
                    	 gameui.quizLabel.setText("문제: ");
                    	 gameui.hintLabel.setText("힌트: ");
                    	 gameui.imageLabel.setIcon(new ImageIcon(GameUI.class.getResource("/pkg/default image.png")));
                    	 isQuized = true;  // 화면이 clear 되었다는 것은 새로운 문제가 출제되었다는 뜻
                     }
                     else{
                    	 
                    	 if(type.equals("Student")){
                    		 if(orderinfo[0].equals("setQuiz")){  // 선생님(상대방)이 문제를 출제했을 경우 그 문제를 공개함
                    			 gameui.noticeLabel.setText("Notice: 문제가 출제되었습니다. 정답을 맟춰보세요!");
                    			 gameui.quizLabel.setText("문제: " + orderinfo[1]);
                    		 }
                    		 else if(orderinfo[0].equals("showAnswer")){
                    			 if(orderinfo[2].equals(username)){  // 자신이 정답을 맟춘 경우
                    				 JOptionPane.showMessageDialog(gameui,
         									"정답을 맟추셨네요!\n정답은 입력하신 대로 " + orderinfo[1] + "입니다.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    			 }
                    			 else{  // 상대방이 정답을 맟춰버린 경우
                    				 JOptionPane.showMessageDialog(gameui,
                    						 orderinfo[2] + "님이 정답을 맟췄습니다.\n정답은 " + orderinfo[1] + "였습니다.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    				 gameui.noticeLabel.setText("Notice: 선생님이 문제를 입력하는 중입니다...");
                    				 isQuized = false;  // 자신이 정답을 맟췄을 경우 setTeacher 명령을 통해 isQuized가 0이 되므로 if문이 아닌 else문 내에만 썼음 
                    			 }
                    		 }
                    		 else if(orderinfo[0].equals("setTeacher")){  // 정답을 맟추면 clientType을 선생님으로 변경함
                    			 gameui.noticeLabel.setText("Notice: 당신은 선생님입니다. 문제를 입력하십시오.");
                    			 gameui.findImageButton.setEnabled(true);
                    			 type = "Teacher";
                    			 isQuized = false;  // setTeacher 명령을 받았다는 것은 이 사람이 곧 새로운 문제를 출제 할 것이라는 뜻
                    		 }
                    		 else if(orderinfo[0].equals("setHint")){  // 선생님이 힌트를 입력했을 경우
                    			 gameui.hintLabel.setText("힌트: " + msg.substring(11, 11+Integer.parseInt(orderinfo[1])));
                    		 }
                    		 else if(orderinfo[0].equals("submitImage")){  // 선생님이 힌트용 이미지를 전송했을 경우
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
                    			 
                    			 gameui.imageLabel.setIcon(new ImageIcon( BytesToImage(imgBytes) ));  // 선생님이 전송한 힌트용 이미지를 띄움
                    		 }
                    	 }
                    	 else if(type.equals("Teacher")){
                    		 if(orderinfo[0].equals("setQuiz")){  // 선생님이 문제를 출제했을 경우
                    			 gameui.noticeLabel.setText("Notice: 문제가 출제되었습니다. 힌트를 입력해 주세요.");
                    			 gameui.quizLabel.setText("문제: " + orderinfo[1]);
                    		 }
                    		 else if(orderinfo[0].equals("showAnswer")){  // 학생이 정답을 맟췄을 경우 그 학생의 아이디를 출력함
                    			 JOptionPane.showMessageDialog(gameui,
                						 orderinfo[2] + "님이 정답을 맟췄습니다.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    		 }
                    		 else if(orderinfo[0].equals("unsetTeacher")){  // 학생이 정답을 맟추면 '선생님'이라는 권한이 그 학생한테 넘어감
                    			 gameui.noticeLabel.setText("Notice: 선생님이 문제를 입력하는 중입니다...");
                    			 gameui.findImageButton.setEnabled(false);
                    			 type = "Student";
                    			 isQuized = false;  // unsetTeacher 명령을 받았다는 것은 새로운 문제가 곧 출제 될 것이라는 뜻
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