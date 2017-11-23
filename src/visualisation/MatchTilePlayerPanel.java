package visualisation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MatchTilePlayerPanel extends JPanel{

	public BufferedImage gameImage = null;
	
	public int numCols = 0;
	
	public int numRows = 0;
	
	public void paintComponent(Graphics g) {
		if (g != null) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setTransform(AffineTransform.getScaleInstance(0.5d, 0.5d));
			if (gameImage != null)
				g2.drawImage(gameImage, 0, 0, null);
		}
	}

}
