package core;

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

    Cell[][] grid;

    public Grid() {
        //init();
    }

    public void init() {
//        if(type.equalsIgnoreCase("Square"))
//        {
//
//        }

        Random rnd = new Random();

        grid = new Cell[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                grid[i][j] = new Cell(new int[]{rnd.nextInt(2), rnd.nextInt(2), rnd.nextInt(2)});
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
