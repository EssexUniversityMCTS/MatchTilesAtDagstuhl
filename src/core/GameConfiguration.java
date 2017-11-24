package core;

/**
 * Created by julian on 11/23/17.
 */
public class GameConfiguration {

    public Grid grid;
    public Actions actions;
    public Rules rules;
    public Termination termination;

    public String toString () {
        StringBuffer sb = new StringBuffer ("Game Configuration;\n");
        sb.append (grid != null ? grid.toString () : "no grid\n");
        sb.append (actions != null ? actions.toString () : "no actions\n");
        sb.append (rules != null ? rules.toString () : "no rules\n");
        sb.append (termination != null ? termination.toString () : "no termination\n");
        return sb.toString ();
    }

}
