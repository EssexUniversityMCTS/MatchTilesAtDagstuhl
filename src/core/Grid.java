package core;

import game_states.MatchTileCell;

import java.awt.*;
import java.util.Random;

/**
 * Created by julian on 11/23/17.
 */
public class Grid {

    String type;
    int size;
    String cell;
    String blocks;
    String color;

    MatchTileCell[][] grid;

    public Grid() {
    }

    public void init() {

        Random rnd = new Random();

        grid = new MatchTileCell[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Color c = new Color(rnd.nextInt(2), rnd.nextInt(2), rnd.nextInt(2));
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
}
