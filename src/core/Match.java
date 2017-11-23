package core;

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

        //TODO: Execute changes in the board according to these rules


        //Return TRUE if the Rule made a change in the engine.
        return false;
    }
}
