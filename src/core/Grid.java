package core;

import game_states.MatchTileCell;

import java.awt.*;
import java.util.Random;

/**
 * Created by julian on 11/23/17.
 */
public class Grid {

    String type;
    public int size;
    String cell;
    String blocks;
    String color;

    public MatchTileCell[][] grid;

    public Grid() {
    }

    public void init() {

        Random rnd = new Random();

        grid = new MatchTileCell[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Color c = new Color(rnd.nextInt(2)*255, rnd.nextInt(2)*255, rnd.nextInt(2)*255);
                grid[i][j] = new MatchTileCell(i, j, c, false);
            }
        }

    }

    public static void main(String args[]) {
        Grid g = new Grid();
        g.size = 5;
        g.init();
        int a = 0;
    }

    public String toString () {
        StringBuffer sb = new StringBuffer ("Grid: ");
        sb.append ("Type: " + type + ", size: " + size + ", cell" + cell + ", blocks" + blocks + ", color" + color);
        sb.append ("\n");
        return sb.toString ();
    };
}
