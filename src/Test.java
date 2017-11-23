import core.Game;
import json.JSONParser;
import visualisation.MatchTilePlayerFrame;

import javax.swing.*;

class Test
{
    public static void main (String args[]) {

        MatchTilePlayerFrame frame = new MatchTilePlayerFrame(5, 5);

        Game game = new Game();

        game.setFrame(frame);
    }
}