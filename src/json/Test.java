package json;
import core.Game;
import tools.com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by julian on 11/23/17.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        String filename = "../MatchTilesAtDagstuhl/examples/Bejeweled.mtg";
        System.out.println("Trying to read " +  filename);
        BufferedReader br = new BufferedReader (new FileReader(filename));
        String json = null;
        Gson gson = new Gson();
        Game game = gson.fromJson (json, Game.class);
        System.out.println(game);
    }

}
