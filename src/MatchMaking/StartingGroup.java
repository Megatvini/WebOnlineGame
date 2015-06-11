package MatchMaking;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Nika on 05:02, 6/11/2015.
 */
public class StartingGroup {
    private String creator;
    private Collection<String> group;
    public StartingGroup(String creator) {
        this.creator = creator;
        group = new HashSet<>();
        group.add(creator);
    }

    public String getCreator() {
        return creator;
    }

    public Collection<String> getGroup() {
        return group;
    }

    public void addUser(String userName) {
        group.add(userName);
    }
}
