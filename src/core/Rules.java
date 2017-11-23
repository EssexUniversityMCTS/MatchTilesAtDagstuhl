package core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julian on 11/23/17.
 */
public class Rules {

    List<Rule> rules;

    public Rules()
    {
        rules = new ArrayList<>();
    }


    public void execute(Game game)
    {
        boolean noChange = false;

        while(!noChange)
        {
            noChange = false;
            for(Rule r : rules)
            {
                boolean triggered = r.execute(game);
                noChange |= triggered;
            }
        }
    }

}
