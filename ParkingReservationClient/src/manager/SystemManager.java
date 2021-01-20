package manager;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import database.DBOperation;
import model.Model;
import model.Reservation;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SystemManager extends JFrame {
	
	private SystemManager mainFrame = this;
	private JPanel mainPanel = new JPanel();
	private JPanel jpanel1 = new JPanel();
	
	private DBOperation dbService;
	private ExecutePython executePython;
	
	private DaemonThread myThread = null;
    private VideoCapture webSource = null;
    private Mat frame = new Mat();
    private MatOfByte mem = new MatOfByte();
    
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SystemManager window = new SystemManager();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SystemManager() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setContentPane(mainPanel);
		setBounds(30, 30, 800, 650);
		setTitle("주차 관리 시스템 (관리자용)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel.setVisible(true);
		mainPanel.setLayout(null);
		
		jpanel1.setBounds(12, 10, 662, 600);
		mainPanel.add(jpanel1);
		
		webSource =new VideoCapture(0);
		myThread = new DaemonThread();
		Thread t = new Thread(myThread);
		t.setDaemon(true);
		myThread.runnable = true;
		t.start();
		
		executePython = new ExecutePython();
		dbService = new DBOperation();
		
		JButton jButtonSave = new JButton("Save");
		jButtonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Highgui.imwrite("1.jpg", frame);
				
		    	String[] arguments = new String[1];
		    	arguments[0] = "C:/Projects/JAVA Projects/ParkingReservationClient/1.jpg";
		    	String plateNumber = executePython.execute("C:/Projects/JAVA Projects/ParkingReservationClient", "main2.py", arguments);
		    	
		    	Reservation reservation = (Reservation) dbService.select("reservation", plateNumber);
		    	
		    	if(reservation == null)
		    		JOptionPane.showMessageDialog(mainFrame, "예약하지 않은 사용자입니다. 현매로 진행하십시오.", "Notice", JOptionPane.INFORMATION_MESSAGE);
		    	else
		    		JOptionPane.showMessageDialog(mainFrame, "예약 확인되었습니다.\n" + reservation.getLocationID() + " 자리로 안내하십시오.", "Notice", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		jButtonSave.setBounds(686, 10, 71, 33);
		mainPanel.add(jButtonSave);
		
		JButton jButtonExit = new JButton("Exit");
		jButtonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		jButtonExit.setBounds(686, 53, 71, 33);
		mainPanel.add(jButtonExit);
		
	}
	
	public void exit() {
		myThread.runnable = false;
        webSource.release();
        System.exit(0);
	}
	
	class DaemonThread implements Runnable{
		
		protected volatile boolean runnable = false;
		
		@Override
		public  void run(){
		    synchronized(this){
		        while(runnable){
		            if(webSource.grab()){
				    	try{
				    		webSource.retrieve(frame);
						    Highgui.imencode(".bmp", frame, mem);
						    Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
				
						    BufferedImage buff = (BufferedImage) im;
						    Graphics g = jpanel1.getGraphics();
				
						    if (g.drawImage(buff, 0, 0, jpanel1.getWidth(), jpanel1.getHeight() -150 , 0, 0, buff.getWidth(), buff.getHeight(), null))
							    if(runnable == false){
							    	this.wait();
							    }
						 }
						 catch(Exception ex){
							 ex.printStackTrace();
					    }
				    }
		        }
		    }
		}
   }
}
