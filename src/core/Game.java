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


    public boolean gameOver;
    public int gameTick;

    public Game()
    {
        gameOver=false;
        gameTick = 0;
    }

    public void playGame()
    {

        while(!gameOver)
        {
            this.gameCycle();
            frame.gamePanel.repaint();

        }

    }

    public void gameCycle()
    {
        gameTick++;

        //Ask action to the player.

        rules.execute(this);

    }






    public void setFrame(MatchTilePlayerFrame frame)
    {
        this.frame = frame;
    }



}
