import core.Game;
import visualisation.MatchTilePlayerFrame;

class Test
{
    public static void main (String args[]) {

        MatchTilePlayerFrame frame = new MatchTilePlayerFrame(5, 5);

        Game game = new Game();

        game.setFrame(frame);
    }
}