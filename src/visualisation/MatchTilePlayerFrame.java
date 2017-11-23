package visualisation;
import game_states.MatchTileGameState;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class MatchTilePlayerFrame extends JFrame implements ComponentListener{

	public MatchTilePlayerPanel gamePanel;
	
	public static void main(String[] args){
		MatchTilePlayerFrame mtpf = new MatchTilePlayerFrame(5, 5);
		mtpf.gamePanel.gameState.setColour(1, 1, Color.blue);
		mtpf.gamePanel.gameState.setColour(3, 3, Color.red);
		mtpf.updateBoard(mtpf.gamePanel.gameState);
	}
	
	public MatchTilePlayerFrame(int numRows, int numCols){
		setTitle("ANGELINA 17");
		setSize(100 * numRows, 100 * numCols);
		setVisible(true);
		gamePanel = new MatchTilePlayerPanel(numRows, numCols);
		add(gamePanel);
		gamePanel.setVisible(true);		
		setVisible(true);
		addWindowListener();
	}
	
	public void updateBoard(MatchTileGameState gameState){
		gamePanel.updateBoard(gameState);
		gamePanel.repaint();
	}
	
	private void addWindowListener() {

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

			public void windowActivated(WindowEvent e) { }

		});

		addComponentListener(this);
	}

	public void componentHidden(ComponentEvent arg0) {}
    
	public void componentMoved(ComponentEvent arg0) {}
    
	public void componentResized(ComponentEvent arg0) {
        gamePanel.updateBoard(gamePanel.gameState);
    }
    
	public void componentShown(ComponentEvent arg0) { }
	
	//public ArrayList<MatchTileUserAction> userActionsSinceLastTick(){
		
	//}
	//}

}
