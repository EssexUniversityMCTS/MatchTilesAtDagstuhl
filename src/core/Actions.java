package core;

import player.MatchTilePlayerAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julian on 11/23/17.
 */
public class Actions {

    public List<Action> actions;

    public Actions()
    {
        actions = new ArrayList<>();
    }

    public boolean validate(ArrayList<MatchTilePlayerAction> tiles)
    {
        boolean valid = true;
        for(Action act : actions)
        {
            valid &= act.validate(tiles);
        }
        return valid;
    }

    public String toString () {
        StringBuffer sb = new StringBuffer ("Grid: ");
        sb.append ("\n");
        return sb.toString ();
    };

}
