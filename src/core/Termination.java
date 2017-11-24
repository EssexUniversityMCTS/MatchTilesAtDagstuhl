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

    public String toString () {
        StringBuffer sb = new StringBuffer ("Termination: ");
        sb.append (type + " " + value);
        sb.append ("\n");
        return sb.toString ();
    };

}
