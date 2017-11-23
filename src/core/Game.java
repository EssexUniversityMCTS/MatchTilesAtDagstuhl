package core;

import player.MatchTilePlayerAction;
import visualisation.MatchTilePlayerFrame;

import java.awt.*;
import java.util.ArrayList;

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
        double delay = 60; //ms
        while(!gameOver)
        {
            // Determine the time to adjust framerate.
            long then = System.currentTimeMillis();


            this.gameCycle();

            // Get the remaining time to keep fps.
            long now = System.currentTimeMillis();
            int remaining = (int) Math.max(0, delay - (now - then));

            // Wait until de next cycle.
            waitStep(remaining);

            frame.gamePanel.repaint();

        }

    }

    public void gameCycle() {
        gameTick++;

        boolean valid = false;

        while(!valid) {
            //Ask action to the player.
            frame.gamePanel.repaint();
            ArrayList<MatchTilePlayerAction> tiles = frame.userActionsSinceLastTick();
            valid = conf.actions.validate(tiles);
        }

        conf.rules.execute(this);
        gameOver = conf.termination.check(this);

    }

    /**
     * Holds the game for the specified duration milliseconds
     *
     * @param duration
     *            time to wait.
     */
    void waitStep(int duration) {

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setFrame(MatchTilePlayerFrame frame)
    {
        this.frame = frame;
    }

    public static void main(String args[])
    {
        //test
        Game g = new Game();
        g.conf = new GameConfiguration();

        //Grid
        g.conf.grid = new Grid();
        g.conf.grid.size = 5;
        g.conf.grid.init();

        //Frame
        MatchTilePlayerFrame frame = new MatchTilePlayerFrame(g.conf.grid.size, g.conf.grid.size);
        frame.gamePanel.gameState.update(g);
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
