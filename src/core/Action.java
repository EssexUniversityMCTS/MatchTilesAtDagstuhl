package core;

import game_states.MatchTileCell;
import player.MatchTilePlayerAction;

import java.util.ArrayList;

/**
 * Created by julian on 11/23/17.
 */
public class Action {

    String type;
    int size;

    private MatchTileCell tile1;
    private MatchTileCell tile2;

    public boolean validate(ArrayList<MatchTilePlayerAction> tiles) {
        
        tile1 = null;
        tile2 = null;
        
        if (tiles.size() != 2)
            return false;

        MatchTilePlayerAction mtpa = null;
        if(tiles.get(1).highlightedCells.size() == 2)
        {
            mtpa = tiles.get(1);
        }else if(tiles.get(2).highlightedCells.size() == 2)
        {
            mtpa = tiles.get(2);
        }else{
            System.out.println("Not enough highlighted tiles! This should not be happening.");
            return false;
        }

        tile1 = mtpa.highlightedCells.get(0);
        tile2 = mtpa.highlightedCells.get(1);

        if (tile1.x == tile2.x) {
            if ((tile1.y == tile2.y + 1) ||
                    (tile1.y == tile2.y - 1))
                return true;
        } else if (tile1.y == tile2.y) {
            if ((tile1.x == tile2.x + 1) ||
                    (tile1.x == tile2.x - 1))
                return true;
        }

        return false;
    }

    public void execute(Game game, ArrayList<MatchTilePlayerAction> tiles) {

        MatchTileCell swap = game.conf.grid.grid[tile1.x][tile1.y];
        game.conf.grid.grid[tile1.x][tile1.y] = game.conf.grid.grid[tile2.x][tile2.y];
        game.conf.grid.grid[tile2.x][tile2.y] = swap;

        game.conf.grid.grid[tile1.x][tile1.y].x = tile1.x;
        game.conf.grid.grid[tile1.x][tile1.y].y = tile1.y;
        game.conf.grid.grid[tile2.x][tile2.y].x = tile2.x;
        game.conf.grid.grid[tile2.x][tile2.y].y = tile2.y;
    }
}
