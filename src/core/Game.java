package core;

import visualisation.MatchTilePlayerFrame;

import java.awt.*;

/**
 * Created by dperez on 23/11/2017.
 */
public class Game
{
    public GameConfiguration conf;

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

        conf.rules.execute(this);

        gameOver = conf.termination.check(this);
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
        g.conf.grid = new Grid();
        g.conf.grid.size = 5;
        g.conf.grid.init();

        //Frame
        MatchTilePlayerFrame frame = new MatchTilePlayerFrame(g.conf.grid.size, g.conf.grid.size);
        frame.gamePanel.gameState.setColour(1,1, Color.red);
        frame.updateBoard(frame.gamePanel.gameState);
        g.setFrame(frame);

        //Actions
        g.conf.actions = new Actions();
        g.conf.actions.actions.add(new Action());

        //Rules
        g.conf.rules = new Rules();
        Match m = new Match();
        m.number = 3;
        m.reward = 100;
        g.conf.rules.rules.add(m);

        //Terminations
        g.conf.termination = new Termination();
        g.conf.termination.value = 100;

        g.playGame();

    }



}
