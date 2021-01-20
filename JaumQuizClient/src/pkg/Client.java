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
	
	private String IPAddress;  // IPAddress: IPÁÖ¼Ò
	private int portNumber;  // portNumber: Æ÷Æ®¹øÈ£
	
	private String username;  // username: ÇÃ·¹ÀÌ¾îÀÇ ¾ÆÀÌµğ
	private String type;  // type: ÇÃ·¹ÀÌ¾îÀÇ Å¸ÀÔ (ÇĞ»ı or ¼±»ı´Ô)
	private boolean isQuized = false;  // isQuized: ¹®Á¦°¡ ÃâÁ¦µÇ¾ú´ÂÁö¸¦ ÀÇ¹Ì
	
	public Client(GameUI ui, String IP, int port, String n, String t){
		this.IPAddress = IP;
		this.portNumber = port;
		this.gameui = ui;
		this.username = n;
		this.type = t;
	}
	
	public void execute(){
		// ¼­¹ö¿¡ Á¢¼ÓÇÏ´Â °úÁ¤
		try {
            Socket socket = new Socket(IPAddress, portNumber);  // ¼ÒÄÏÀ» »ı¼ºÇÏ°í ¼­¹ö¿Í ¿¬°áÇÔ
            JOptionPane.showMessageDialog(gameui, "¼­¹ö¿¡ Á¤»óÀûÀ¸·Î Á¢¼Ó µÇ¾ú½À´Ï´Ù!", "Notice", JOptionPane.INFORMATION_MESSAGE);

            if(type.equals("Student")){  // TypeÀÌ ÇĞ»ıÀÎÁö ¼±»ı´ÔÀÎÁö¿¡ µû¶ó Notice¸¦ ´Ù¸£°Ô Ãâ·ÂÇÔ
            	gameui.noticeLabel.setText("Notice: ¼±»ı´ÔÀÌ ¹®Á¦¸¦ ÀÔ·ÂÇÏ´Â ÁßÀÔ´Ï´Ù...");
            	gameui.findImageButton.setEnabled(false);  // ÇĞ»ıÀº ÈùÆ®¸¦ ºÎ¿©ÇÒ ¼ö ¾øÀ¸¹Ç·Î ÀÌ¹ÌÁö Àü¼Û ¹öÆ°ÀÌ ºñÈ°¼ºÈ­µÊ
            }
            else if(type.equals("Teacher")){
            	gameui.noticeLabel.setText("Notice: ´ç½ÅÀº ¼±»ı´ÔÀÔ´Ï´Ù. ¹®Á¦¸¦ ÀÔ·ÂÇÏ½Ê½Ã¿À.");
            }
            	
            sender = new Sender(socket);
    		receiver = new Receiver(socket);
    		
    		sender.start();
    		receiver.start();  // Thread Start
    		
        } catch(ConnectException ce) {
        	JOptionPane.showMessageDialog(gameui, "¿¬°á¿¡ ½ÇÆĞÇÏ¿´½À´Ï´Ù.", "Error...", JOptionPane.ERROR_MESSAGE);
            ce.printStackTrace();
            gameui.exit();
        } catch(Exception e) {
        	JOptionPane.showMessageDialog(gameui, "¿¬°á¿¡ ½ÇÆĞÇÏ¿´½À´Ï´Ù.", "Error...", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            gameui.exit();
        }
	}
	
	public byte[] ImageToBytes (Image img){  // ÀÌ¹ÌÁö¸¦ ¹ÙÀÌÆ®Çü ¹è¿­·Î ¹Ù²ãÁÜ
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
	
	public BufferedImage BufferedImage(Image img)  // ÀÏ¹İ ÀÌ¹ÌÁö¸¦ Buffered Image·Î ¹Ù²ãÁÜ
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
	
	public BufferedImage BytesToImage(byte[] bytes){  // ¹ÙÀÌÆ®Çü ¹è¿­À» ÀÌ¹ÌÁö·Î ¹Ù²ãÁÜ
		
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
                    out.flush();  // ¿ì¼± ÇÃ·¹ÀÌ¾îÀÇ ¾ÆÀÌµğ¿Í Å¸ÀÔ(ÇĞ»ı/¼±»ı´Ô)À» ¼­¹öÇÑÅ× Àü¼ÛÇÔ
                }
			} catch(IOException e1) {
                e1.printStackTrace();
            }

            gameui.submitButton.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {  // ´ä¾È, Á¤´ä, ÈùÆ®¸¦ Àü¼ÛÇÏ±â À§ÇÑ ¹öÆ°À» Å¬¸¯ÇßÀ» °æ¿ì
        			// È®ÀÎ ¹öÆ°À» ´­·¶À» ¶§ÀÇ ¾×¼Ç
        			try{
        				
        				if(!isKorean(gameui.inputTextField.getText())  // ¿µ¹®, Æ¯¼ö¹®ÀÚ, ¶ç¾î¾²±â, ±ÛÀÚ ¼ö ÃÊ°ú¸¦ ±İÁöÇÏ±â À§ÇØ »ç¿ë (´Ü, ÈùÆ® ÀÔ·ÂÀº Á¦¿Ü)
        						&& (type.equals("Student") || (type.equals("Teacher") && !isQuized))){
        					JOptionPane.showMessageDialog(gameui,
        							"¿µ¹®, Æ¯¼ö¹®ÀÚ, ¶ç¾î¾²±â´Â Çã¿ëµÇÁö ¾Ê½À´Ï´Ù.\n±×¸®°í 1ÀÚ ÀÌ»ó 20ÀÚ ÀÌ³»·Î ÀÔ·ÂÇÏ½Ê½Ã¿À.", "Error...", JOptionPane.ERROR_MESSAGE);
        				}
        				else{
        					
        					if(type.equals("Student")){
        						if(!isQuized){  // ¼±»ı´ÔÀÌ ¹®Á¦¸¦ ÃâÁ¦ÇÏÁö ¾ÊÀº »óÅÂ¿¡¼­ ´ä¾ÈÀ» ÀÔ·ÂÇßÀ» °æ¿ì
        							JOptionPane.showMessageDialog(gameui,
        									"¼±»ı´ÔÀÌ ¾ÆÁ÷ ¹®Á¦¸¦ ÃâÁ¦ÇÏÁö ¾Ê¾Ò½À´Ï´Ù.", "Error...", JOptionPane.ERROR_MESSAGE);
        						}
        						else{
        							String msg = "inputAnswer " + username + " " + gameui.inputTextField.getText();
        							out.write(msg.getBytes("UTF-8"));
        							out.flush();
        							gameui.inputTextField.setText("");  // ´ä¾ÈÀ» ÇÃ·¹ÀÌ¾îÀÇ ¾ÆÀÌµğ¿Í ÇÔ²² ¼­¹öÇÑÅ× Àü¼ÛÇÔ
        						}
        					}
        					else if(type.equals("Teacher")){
        						if(!isQuized){  // ¹®Á¦°¡ ¾ÆÁ÷ ÃâÁ¦µÇÁö ¾ÊÀº »óÅÂÀÌ¹Ç·Î Á¤´äÀ» ¼­¹öÇÑÅ× Àü¼Û
        							String msg = "setQuiz " + gameui.inputTextField.getText();
        							out.write(msg.getBytes("UTF-8"));
        							isQuized = true;
        							gameui.inputTextField.setText("");
        						}
        						else{  // ¹®Á¦°¡ ÃâÁ¦µÈ »óÅÂÀÌ¹Ç·Î ÈùÆ®¸¦ ¼­¹öÇÑÅ× Àü¼Û
        							int len = gameui.inputTextField.getText().length();
        							String strlen = String.valueOf(len);
        							
        							if(len < 10)  // ÈùÆ®ÀÇ ±æÀÌ°¡ 10 ¹Ì¸¸ÀÌ¸é ¾Õ¿¡ 0À» ºÙÀÓ (¿¹: 5 -> 05)
        								strlen = "0" + strlen;
        							
        							String msg1 = "setHint " + strlen + " " + gameui.inputTextField.getText();
        							out.write(msg1.getBytes("UTF-8"));
        							gameui.inputTextField.setText("");
        							
        							if(gameui.HasImage()){ // ÀÌ¹ÌÁö°¡ ÀÖÀ» °æ¿ì ºÒ·¯¿Â ÀÌ¹ÌÁö¸¦ ÇĞ»ıµéÇÑÅ× Àü¼Û
        								// ÀÌ¹ÌÁö¸¦ byte-array ÇüÅÂ·Î Àü¼ÛÇÏ´Â °úÁ¤
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
		
		public boolean isKorean(String word){  // ¿ÏÀüÇÑ ÇÑ±ÛÀÎÁö Ã¼Å©ÇÔ [ex: °¡(O), ¤¡(X)]
			if(word.length() <= 0 || word.length() > 20)  // 20ÀÚ¸¦ ÃÊ°úÇÒ °æ¿ì false
				return false;
			
			for(int c=0; c<word.length(); c++){
				if(!(word.charAt(c) >= '°¡' && word.charAt(c) <= 'ÆR'))  // ÇÑ±ÛÀÌ ¾Æ´Ï¸é false
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
        	String[] orderinfo;  // orderinfo[]: ¸í·É¹®À» ¶ç¾î¾²±â¸¦ ±âÁØÀ¸·Î ºĞ¸®ÇØ¼­ ÀúÀåÇÏ±â À§ÇÑ ¹è¿­
        	
        	try {
                while(in != null) {
                	byte[] bytes = new byte[1024];
                	
                	in.read(bytes, 0, 1024);  // ¼­¹ö·ÎºÎÅÍ ¸í·É¹®À» ¹ŞÀ½
                     
                	msg = new String(bytes, "UTF-8");
                    orderinfo = msg.split(" ");  // ¸í·É¹®À» ¶ç¾î¾²±â¸¦ ±âÁØÀ¸·Î ºĞ¸®ÇÑ ÈÄ ¹è¿­ ¼Ó¿¡ ÀúÀåÇÔ
                     
                    // Á¦°ø¹ŞÀº ¸í·É¿¡ µû¶ó ¾×¼ÇÀ» ¼öÇà
                     if(orderinfo[0].equals("setUnable")){  // ¹«È¿È­ ¸í·É¾î¸¦ ¹Ş¾ÒÀ» °æ¿ì ¼ÒÄÏÀ» closeÇÏ°í Á¾·áÇÔ
                    	 JOptionPane.showMessageDialog(gameui,
									"¾ÆÀÌµğ Áßº¹, ¼±»ı´ÔÀÇ Áßº¹, ¶Ç´Â ÀÎ¿ø ¼ö ÃÊ°ú·Î ÀÎÇØ ¸ğµç ±â´ÉÀÌ ºñÈ°¼ºÈ­ µÇ¾ú½À´Ï´Ù.", "Error...", JOptionPane.ERROR_MESSAGE);
                    	 gameui.exit();
                    	 
                    	 return;
                     }
                     
                     if(orderinfo[0].equals("notQuized")){  // ¼±»ı´ÔÀÌ °ÔÀÓÀ» ³ª°¬À» °æ¿ì ¹®Á¦ ÃâÁ¦ »óÅÂ¸¦ false·Î º¯°æÇÔ
                    	 JOptionPane.showMessageDialog(gameui,
									"¼±»ı´ÔÀÌ °ÔÀÓÀ» ³ª°¡¼­ " + orderinfo[1] + "´ÔÀ¸·Î ¹Ù²î¾ú½À´Ï´Ù.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    	 isQuized = false;
                     }
                     else if(orderinfo[0].equals("allClear")){  // È­¸é Å¬¸®¾î ¸í·É¾î¸¦ ¹Ş¾ÒÀ» °æ¿ì
                    	 gameui.quizLabel.setText("¹®Á¦: ");
                    	 gameui.hintLabel.setText("ÈùÆ®: ");
                    	 gameui.imageLabel.setIcon(new ImageIcon(GameUI.class.getResource("/pkg/default image.png")));
                    	 isQuized = true;  // È­¸éÀÌ clear µÇ¾ú´Ù´Â °ÍÀº »õ·Î¿î ¹®Á¦°¡ ÃâÁ¦µÇ¾ú´Ù´Â ¶æ
                     }
                     else{
                    	 
                    	 if(type.equals("Student")){
                    		 if(orderinfo[0].equals("setQuiz")){  // ¼±»ı´Ô(»ó´ë¹æ)ÀÌ ¹®Á¦¸¦ ÃâÁ¦ÇßÀ» °æ¿ì ±× ¹®Á¦¸¦ °ø°³ÇÔ
                    			 gameui.noticeLabel.setText("Notice: ¹®Á¦°¡ ÃâÁ¦µÇ¾ú½À´Ï´Ù. Á¤´äÀ» ¬Ãçº¸¼¼¿ä!");
                    			 gameui.quizLabel.setText("¹®Á¦: " + orderinfo[1]);
                    		 }
                    		 else if(orderinfo[0].equals("showAnswer")){
                    			 if(orderinfo[2].equals(username)){  // ÀÚ½ÅÀÌ Á¤´äÀ» ¬Ãá °æ¿ì
                    				 JOptionPane.showMessageDialog(gameui,
         									"Á¤´äÀ» ¬Ãß¼Ì³×¿ä!\nÁ¤´äÀº ÀÔ·ÂÇÏ½Å ´ë·Î " + orderinfo[1] + "ÀÔ´Ï´Ù.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    			 }
                    			 else{  // »ó´ë¹æÀÌ Á¤´äÀ» ¬Ãç¹ö¸° °æ¿ì
                    				 JOptionPane.showMessageDialog(gameui,
                    						 orderinfo[2] + "´ÔÀÌ Á¤´äÀ» ¬Ãè½À´Ï´Ù.\nÁ¤´äÀº " + orderinfo[1] + "¿´½À´Ï´Ù.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    				 gameui.noticeLabel.setText("Notice: ¼±»ı´ÔÀÌ ¹®Á¦¸¦ ÀÔ·ÂÇÏ´Â ÁßÀÔ´Ï´Ù...");
                    				 isQuized = false;  // ÀÚ½ÅÀÌ Á¤´äÀ» ¬ÃèÀ» °æ¿ì setTeacher ¸í·ÉÀ» ÅëÇØ isQuized°¡ 0ÀÌ µÇ¹Ç·Î if¹®ÀÌ ¾Æ´Ñ else¹® ³»¿¡¸¸ ½èÀ½ 
                    			 }
                    		 }
                    		 else if(orderinfo[0].equals("setTeacher")){  // Á¤´äÀ» ¬Ãß¸é clientTypeÀ» ¼±»ı´ÔÀ¸·Î º¯°æÇÔ
                    			 gameui.noticeLabel.setText("Notice: ´ç½ÅÀº ¼±»ı´ÔÀÔ´Ï´Ù. ¹®Á¦¸¦ ÀÔ·ÂÇÏ½Ê½Ã¿À.");
                    			 gameui.findImageButton.setEnabled(true);
                    			 type = "Teacher";
                    			 isQuized = false;  // setTeacher ¸í·ÉÀ» ¹Ş¾Ò´Ù´Â °ÍÀº ÀÌ »ç¶÷ÀÌ °ğ »õ·Î¿î ¹®Á¦¸¦ ÃâÁ¦ ÇÒ °ÍÀÌ¶ó´Â ¶æ
                    		 }
                    		 else if(orderinfo[0].equals("setHint")){  // ¼±»ı´ÔÀÌ ÈùÆ®¸¦ ÀÔ·ÂÇßÀ» °æ¿ì
                    			 gameui.hintLabel.setText("ÈùÆ®: " + msg.substring(11, 11+Integer.parseInt(orderinfo[1])));
                    		 }
                    		 else if(orderinfo[0].equals("submitImage")){  // ¼±»ı´ÔÀÌ ÈùÆ®¿ë ÀÌ¹ÌÁö¸¦ Àü¼ÛÇßÀ» °æ¿ì
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
                    			 
                    			 gameui.imageLabel.setIcon(new ImageIcon( BytesToImage(imgBytes) ));  // ¼±»ı´ÔÀÌ Àü¼ÛÇÑ ÈùÆ®¿ë ÀÌ¹ÌÁö¸¦ ¶ç¿ò
                    		 }
                    	 }
                    	 else if(type.equals("Teacher")){
                    		 if(orderinfo[0].equals("setQuiz")){  // ¼±»ı´ÔÀÌ ¹®Á¦¸¦ ÃâÁ¦ÇßÀ» °æ¿ì
                    			 gameui.noticeLabel.setText("Notice: ¹®Á¦°¡ ÃâÁ¦µÇ¾ú½À´Ï´Ù. ÈùÆ®¸¦ ÀÔ·ÂÇØ ÁÖ¼¼¿ä.");
                    			 gameui.quizLabel.setText("¹®Á¦: " + orderinfo[1]);
                    		 }
                    		 else if(orderinfo[0].equals("showAnswer")){  // ÇĞ»ıÀÌ Á¤´äÀ» ¬ÃèÀ» °æ¿ì ±× ÇĞ»ıÀÇ ¾ÆÀÌµğ¸¦ Ãâ·ÂÇÔ
                    			 JOptionPane.showMessageDialog(gameui,
                						 orderinfo[2] + "´ÔÀÌ Á¤´äÀ» ¬Ãè½À´Ï´Ù.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    		 }
                    		 else if(orderinfo[0].equals("unsetTeacher")){  // ÇĞ»ıÀÌ Á¤´äÀ» ¬Ãß¸é '¼±»ı´Ô'ÀÌ¶ó´Â ±ÇÇÑÀÌ ±× ÇĞ»ıÇÑÅ× ³Ñ¾î°¨
                    			 gameui.noticeLabel.setText("Notice: ¼±»ı´ÔÀÌ ¹®Á¦¸¦ ÀÔ·ÂÇÏ´Â ÁßÀÔ´Ï´Ù...");
                    			 gameui.findImageButton.setEnabled(false);
                    			 type = "Student";
                    			 isQuized = false;  // unsetTeacher ¸í·ÉÀ» ¹Ş¾Ò´Ù´Â °ÍÀº »õ·Î¿î ¹®Á¦°¡ °ğ ÃâÁ¦ µÉ °ÍÀÌ¶ó´Â ¶æ
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