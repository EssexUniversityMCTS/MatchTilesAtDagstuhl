package player;

import game_states.MatchTileCell;

import java.util.ArrayList;

public class MatchTilePlayerAction {

	public static String CLICK_ACTION = "Click action";
	
	public static String DRAG_ACTION = "Drag action";
	
	public String actionType;
	
	public ArrayList<MatchTileCell> highlightedCells = new ArrayList();
	
	public MatchTileCell clickedCell;
	
}
