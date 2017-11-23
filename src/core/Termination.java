package core;

/**
 * Created by julian on 11/23/17.
 */
public class Termination {

    String type;
    int value;


    public boolean check(Game game)
    {
        if(game.gameTick >= value)
            return true;
        return false;
    }

}
