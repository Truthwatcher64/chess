package dataaccess;
import model.*;

import java.util.HashSet;
import java.util.Set;

public class FakeData {
    private Set<UserData> users= new HashSet<>();
    private Set<AuthData> auths = new HashSet<>();
    private Set<GameData> games = new HashSet<>();

    public Set<UserData> getUsers(){
        return this.users;
    }
    public Set<GameData> getGames(){
        return this.games;
    }
    public Set<AuthData> getAuths(){
        return this.auths;
    }
}
