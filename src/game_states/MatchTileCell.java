package game_states;

import java.awt.Color;

public class MatchTileCell {

	public int x = 0;
	
	public int y = 0;
	
	public Color colour = Color.WHITE;
	
	public boolean isEmpty = true;
	
	public boolean isHighlighted = false;
	
	public MatchTileCell(int x, int y, Color colour, boolean isEmpty){
		this.x = x;
		this.y = y;
		this.colour = colour;
		this.isEmpty = isEmpty;
	}
	
	public String toString(){
		if (isEmpty){
			return "(" + x + "," + y + ") is empty";	
		}
		return "(" + x + "," + y + ") colour = " + colour.toString();
	}
}
