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
    public int points;

    public Game()
    {
        gameOver=false;
        gameTick = 0;
        points=0;
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

        gameOver = termination.check(this);
    }


    public void setFrame(MatchTilePlayerFrame frame)
    {
        this.frame = frame;
    }

    public static void main(String args[])
    {
        //test
        Game g = new Game();

        //Grid
        g.grid = new Grid();
        g.grid.size = 5;
        g.grid.init();

        //Frame
        MatchTilePlayerFrame frame = new MatchTilePlayerFrame(g.grid.size, g.grid.size);
        g.setFrame(frame);

        //Actions
        g.actions = new Actions();
        g.actions.actions.add(new Action());

        //Rules
        g.rules = new Rules();
        Match m = new Match();
        m.number = 3;
        m.reward = 100;
        g.rules.rules.add(m);

        //Terminations
        g.termination = new Termination();
        g.termination.value = 100;

        g.playGame();

    }



}
