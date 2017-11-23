package json;
import core.Game;
import tools.com.google.gson.Gson;

/**
 * Created by julian on 11/23/17.
 */
public class Test {

    public static void main(String[] args) {
        String json = null;
        Gson gson = new Gson();
        Game game = gson.fromJson (json, Game.class);

    }

}
