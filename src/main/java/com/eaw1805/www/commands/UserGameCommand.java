package empire.webapp.commands;

/**
 * Created by IntelliJ IDEA.
 * User: karavias
 * Date: 11/27/11
 * Time: 1:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserGameCommand {
    private String game;
    private String nation;
    private String user;

    public UserGameCommand() {
        game = null;
        nation = null;
        user = null;
    }

    public String getGame() {
        return game;
    }

    public void setGame(final String game) {
        this.game = game;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(final String nation) {
        this.nation = nation;
    }

    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }
}
