package core;

import visualisation.MatchTilePlayerFrame;

/**
 * Created by dperez on 23/11/2017.
 */
public class Game
{
    public Grid grid;
    public Actions actions;
    public Rules rules;
    public Termination termination;

    private MatchTilePlayerFrame frame;




    public void setFrame(MatchTilePlayerFrame frame)
    {
        this.frame = frame;
    }

}
