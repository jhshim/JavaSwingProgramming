package pkg;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author tails1101
 *
 */
public class ImageDatas {

	private boolean isCollected;
	
	private static final int PROWS = 8;
	private static final int PSIZE = PROWS*PROWS;
	
	private BufferedImage[] imageData = new BufferedImage[PSIZE];
	private BufferedImage tileImage = null;

	int scrHeight = 0;
	int scrWidth = 0;
    int bwidth = 0;
    int bheight = 0;

	/**
	 * Create the frame.
	 */
	public ImageDatas(String link) {
		initialize(link);
	}
	
	public ImageDatas(File file) {
		initialize(file);
	}
	
	private void initialize(String link) {
		//System.out.println("w="+scrWidth+" h="+scrHeight);
		try {
			tileImage = ImageIO
					.read(getClass().getResource(link));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int pwidth = tileImage.getWidth()/PROWS;
		int pheight =  tileImage.getHeight()/PROWS;

        for (int i=0; i<PROWS; ++i){
        	for (int j=0; j<PROWS; ++j){
        		imageData[PROWS*i+j] = 
        				tileImage.getSubimage(j*pwidth, i*pheight, pwidth, pheight);
        	}
        }
        
	}
	
	private void initialize(File file) {
		//System.out.println("w="+scrWidth+" h="+scrHeight);
		
		Image originalImage = null;
		Image resizedImage = null;
		
		try {
			originalImage = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resizedImage = originalImage.getScaledInstance( 1000, 625, Image.SCALE_SMOOTH);
		
		tileImage = toBufferedImage(resizedImage);
		
		int pwidth = tileImage.getWidth()/PROWS;
		int pheight =  tileImage.getHeight()/PROWS;

        for (int i=0; i<PROWS; ++i){
        	for (int j=0; j<PROWS; ++j){
        		imageData[PROWS*i+j] = 
        				tileImage.getSubimage(j*pwidth, i*pheight, pwidth, pheight);
        	}
        }
        
	}
	
	public BufferedImage toBufferedImage(Image img)
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

	public boolean isCollected() {
		return isCollected;
	}

	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}

	public BufferedImage getImageData(int i) {
		return imageData[i];
	}
	
}