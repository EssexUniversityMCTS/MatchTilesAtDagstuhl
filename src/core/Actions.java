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

    public boolean execute(Game game, ArrayList<MatchTilePlayerAction> tiles)
    {
        boolean valid = true;
        for(Action act : actions)
        {
            boolean thisValid = act.validate(tiles);
            if(thisValid)
                act.execute(game, tiles);
            valid &= thisValid;
        }
        return valid;
    }
}
