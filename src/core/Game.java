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
            valid = actions.validate(tiles);
        }

        rules.execute(this);
        gameOver = termination.check(this);
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

        //Grid
        g.grid = new Grid();
        g.grid.size = 5;
        g.grid.init();

        //Frame
        MatchTilePlayerFrame frame = new MatchTilePlayerFrame(g.grid.size, g.grid.size);
        frame.gamePanel.gameState.update(g);
        frame.updateBoard(frame.gamePanel.gameState);
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
