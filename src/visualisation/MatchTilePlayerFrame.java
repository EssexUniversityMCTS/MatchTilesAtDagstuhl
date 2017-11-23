package visualisation;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MatchTilePlayerFrame {

	public MatchTilePlayerPanel gamePanel = new MatchTilePlayerPanel();
	
	public static void main(String[] args){
		MatchTilePlayerFrame mtp = new MatchTilePlayerFrame(5, 5);
	}
	
	public MatchTilePlayerFrame(int numRows, int numCols){
		JFrame frame = new JFrame();
		frame.setTitle("Match that tile!!");
		frame.setSize(100 * numRows, 100 * numCols);
		frame.setVisible(true);
		frame.add(gamePanel);
		gamePanel.setVisible(true);
		
	}
	
}
