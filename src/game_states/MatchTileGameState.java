package game_states;

import java.awt.Color;


public class MatchTileGameState {

	public MatchTileCell[][] cells;
	
	public int numRows;
	
	public int numCols;
	
	public MatchTileGameState(int numRows, int numCols){
		this.numRows = numRows;
		this.numCols = numCols;
		cells = new MatchTileCell[numRows][numCols];
		for (int x = 0; x < numCols; x++){
			for (int y = 0; y < numRows; y++){
				cells[x][y] = new MatchTileCell(x, y, null, true);
			}
		}
	}

	public void setColour(int x, int y, Color colour){
		cells[x][y].colour = colour;
		cells[x][y].isEmpty = false;
	}
	
}
