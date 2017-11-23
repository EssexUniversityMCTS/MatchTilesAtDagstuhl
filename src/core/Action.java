package core;

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


        MatchTilePlayerAction mtpa1 = tiles.get(0);
        MatchTilePlayerAction mtpa2 = tiles.get(1);

        if (mtpa1.clickedCell.x == mtpa2.clickedCell.x) {
            if ((mtpa1.clickedCell.y == mtpa2.clickedCell.y + 1) ||
                    (mtpa1.clickedCell.y == mtpa2.clickedCell.y - 1))
                return true;
        } else if (mtpa1.clickedCell.y == mtpa2.clickedCell.y) {
            if ((mtpa1.clickedCell.x == mtpa2.clickedCell.x + 1) ||
                    (mtpa1.clickedCell.x == mtpa2.clickedCell.x + 1))
                return true;
        }

        return false;
    }
}
