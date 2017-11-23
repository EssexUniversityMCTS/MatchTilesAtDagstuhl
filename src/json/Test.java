package json;
import core.Game;
import core.GameConfiguration;
import tools.com.google.gson.Gson;
import tools.com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by julian on 11/23/17.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        String filename = "../MatchTilesAtDagstuhl/examples/Bejeweled.mtg";
        System.out.println("Trying to read " +  filename);
        String json = new Scanner(new File(filename)).useDelimiter("\\Z").next();
        System.out.println(json);
        //BufferedReader br = new BufferedReader (new FileReader(filename));
        //JsonReader jr = new JsonReader();
        //String json = null;
        Gson gson = new Gson();
        GameConfiguration conf = gson.fromJson (json, GameConfiguration.class);
        System.out.println(conf);
    }

}
