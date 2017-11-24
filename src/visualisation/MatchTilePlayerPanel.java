package visualisation;

import game_states.MatchTileCell;
import game_states.MatchTileGameState;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import player.MatchTilePlayerAction;

public class MatchTilePlayerPanel extends JPanel{
	
	public MatchTileGameState gameState;

	public BufferedImage gameImage = null;
	
	public int numCols = 0;
	
	public int numRows = 0;
	
	public ArrayList<MatchTilePlayerAction> userActions = new ArrayList();
	
	public MatchTilePlayerPanel(int numCols, int numRows){
		this.numCols = numCols;
		this.numRows = numRows;
		gameState = new MatchTileGameState(numCols, numRows);
		addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				handleClick(e);
			}

			public void mousePressed(MouseEvent e) {
				
			}

			public void mouseReleased(MouseEvent e) {
				handleMouseRelease();
			}

			public void mouseEntered(MouseEvent e) {
				
			}

			public void mouseExited(MouseEvent e) {
				
			}
			
		});
		
		addMouseMotionListener(new MouseMotionListener(){

			public void mouseDragged(MouseEvent e) {
				MatchTileCell cell = getCellAtPoint(e.getPoint());
				if (cell != null){
					cell.isHighlighted = true;
					updateBoard();
				}
			}

			public void mouseMoved(MouseEvent e) { }
			
		});
	}
	
	public void handleClick(MouseEvent e){
		MatchTileCell cell = getCellAtPoint(e.getPoint());
		if (cell != null){
			MatchTilePlayerAction userAction = new MatchTilePlayerAction();
			userAction.actionType = MatchTilePlayerAction.CLICK_ACTION;
			userAction.clickedCell = cell;
			userActions.add(userAction);
		}
	}
	
	public void handleMouseRelease(){
		boolean hasDrag = false;
		for (int x = 0; x < gameState.numCols; x++){
			for (int y = 0; y < gameState.numRows; y++){
				if (gameState.cells[x][y].isHighlighted){
					hasDrag = true;
				}
			}
		}
		if (hasDrag){
			MatchTilePlayerAction userAction = new MatchTilePlayerAction();
			userAction.actionType = MatchTilePlayerAction.DRAG_ACTION;
			for (int x = 0; x < gameState.numCols; x++){
				for (int y = 0; y < gameState.numRows; y++){
					if (gameState.cells[x][y].isHighlighted){
						userAction.highlightedCells.add(gameState.cells[x][y]);
						gameState.cells[x][y].isHighlighted = false;
					}
				}
			}
			userActions.add(userAction);
			updateBoard();
		}

		//userActions.clear();
	}
	
	public MatchTileCell getCellAtPoint(Point p){
		int w = getWidth();
		int h = getHeight();
		int cellWidth = Math.round((float)w/(float)numCols);
		int cellHeight = Math.round((float)h/(float)numRows);
		int x = (int)Math.floor((float)p.x/(float)cellWidth);
		int y = (int)Math.floor((float)p.y/(float)cellHeight);
		try{
			return gameState.cells[x][y];			
		}
		catch(Exception ex){
			return null;
		}
	}
	
	public void updateBoard(){
		updateBoard(gameState);
	}
	
	public void updateBoard(MatchTileGameState gameState){
		this.gameState = gameState;
		int w = getWidth();
		int h = getHeight();
		int cellWidth = Math.round((float)w/(float)numCols);
		int cellHeight = Math.round((float)h/(float)numRows);
		gameImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = gameImage.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		for (int x = 0; x < gameState.numCols; x++){
			for (int y = 0; y < gameState.numRows; y++){
				if (!gameState.cells[x][y].isEmpty){
					g.setColor(gameState.cells[x][y].colour);						
					g.fillRect(cellWidth * x, cellHeight * y, cellWidth, cellHeight);	
				}
			}
		}
		g.setColor(Color.black);
		for (int x = 0; x < numCols; x++){
			g.drawLine(x * cellWidth, 0, x * cellWidth, h);
		}
		for (int y = 0; y < numRows; y++){
			g.drawLine(0, y * cellHeight, w, y * cellHeight);
		}
		g.setColor(Color.yellow);
		for (int x = 0; x < gameState.numCols; x++){
			for (int y = 0; y < gameState.numRows; y++){
				if (gameState.cells[x][y].isHighlighted == true){
					g.drawRect(cellWidth * x, cellHeight * y, cellWidth, cellHeight);	
				}
			}
		}
		g.dispose();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		if (g != null) {
			Graphics2D g2 = (Graphics2D) g.create();
			if (gameImage != null){
				g2.drawImage(gameImage, 0, 0, null);				
			}
		}
	}
	
}
