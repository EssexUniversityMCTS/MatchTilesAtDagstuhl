package core;

import game_states.MatchTileCell;

import java.awt.Color;

/**
 * Created by julian on 11/23/17.
 */
public class Match extends Rule {

    String effect;
    String type;
    int number;
    String shapes;
    String pattern;
    int reward;

    @Override
    public boolean execute(Game game) {
        Grid g = game.conf.grid;
        boolean madeChange = false;

        if (shapes.equals("OrthoLine") && pattern.equals("colour"))
        {
            // horizontal lines
            for (int i = 0; i < g.size; ++i) {
                Color lastColour = g.grid[i][0].colour;
                int matchLength = g.grid[i][0].isEmpty ? 0 : 1;
                for (int j = 1; j < g.size; ++j) {
                    if (!g.grid[i][j].isEmpty && g.grid[i][j].colour == lastColour) {
                        ++matchLength;
                    }
                    else {
                        // TODO: this only implements "type: minimum" correctly
                        if (matchLength >= number) {
                            for (int k = 0; k < matchLength; ++k) {
                                if (effect.equals("Clear"))
                                {
                                    g.grid[i][k].isEmpty = true;
                                    madeChange = true;
                                }
                                else
                                    throw new RuntimeException("Only effect 'Clear' is implemented.");
                            }
                        }
                        matchLength = 0;
                    }
                }
            }
            // vertical lines
            for (int i = 0; i < g.size; ++i) {
                Color lastColour = g.grid[0][i].colour;
                int matchLength = g.grid[0][i].isEmpty ? 0 : 1;
                for (int j = 1; j < g.size; ++j) {
                    if (!g.grid[j][i].isEmpty && g.grid[j][i].colour == lastColour) {
                        ++matchLength;
                    }
                    else {
                        // TODO: this only implements "type: minimum" correctly
                        if (matchLength >= number) {
                            for (int k = 0; k < matchLength; ++k) {
                                if (effect.equals("Clear"))
                                {
                                    g.grid[k][i].isEmpty = true;
                                    madeChange = true;
                                }
                                else
                                    throw new RuntimeException("Only effect 'Clear' is implemented.");
                            }
                        }
                        matchLength = 0;
                    }
                }
            }
        }

        return madeChange;
    }
}
