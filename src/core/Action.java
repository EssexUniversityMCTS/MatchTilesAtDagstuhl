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

    public boolean validate(ArrayList<MatchTilePlayerAction> tiles) {

        if(tiles.size() > 0)
            System.out.println(tiles.size());

        if (tiles.size() != 2)
            return false;

        if(tiles.get(1).highlightedCells.size() != 2)
            return false;

        MatchTilePlayerAction mtpa = tiles.get(1);
        MatchTileCell first = mtpa.highlightedCells.get(0);
        MatchTileCell second = mtpa.highlightedCells.get(1);

        if (first.x == second.x) {
            if ((first.y == second.y + 1) ||
                    (first.y == second.y - 1))
                return true;
        } else if (first.y == second.y) {
            if ((first.x == second.x + 1) ||
                    (first.x == second.x - 1))
                return true;
        }

        return false;
    }

    public void execute(Game game, ArrayList<MatchTilePlayerAction> tiles) {

        MatchTilePlayerAction mtpa = tiles.get(1);
        MatchTileCell first = mtpa.highlightedCells.get(0);
        MatchTileCell second = mtpa.highlightedCells.get(1);

        MatchTileCell swap = game.conf.grid.grid[first.x][first.y];
        game.conf.grid.grid[first.x][first.y] = game.conf.grid.grid[second.x][second.y];
        game.conf.grid.grid[second.x][second.y] = swap;
    }
}
