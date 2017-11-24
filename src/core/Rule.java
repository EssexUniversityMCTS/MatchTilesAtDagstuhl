package core;

/**
 * Created by julian on 11/23/17.
 */
public abstract class Rule {

    public abstract boolean execute(Game game);

    public String toString () {
        StringBuffer sb = new StringBuffer ("Rule: ");
        sb.append ("\n");
        return sb.toString ();
    };

}
