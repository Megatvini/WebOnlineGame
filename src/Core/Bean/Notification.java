package Core.Bean;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Nika on 19:55, 6/27/2015.
 */
public class Notification {
    @Expose
    private Set<String> friendRequestsFrom;
    @Expose
    private Map<String, Integer> inviteGamesFrom;
    @Expose
    private Map<String, List<Message>> newMessages;

    public Notification(Set<String> friendRequestsFrom, Map<String, Integer> inviteGamesFrom,
                        Map<String, List<Message>> newMessages) {
        this.friendRequestsFrom = friendRequestsFrom;
        this.inviteGamesFrom = inviteGamesFrom;
        this.newMessages = newMessages;
    }

    public Set<String> getFriendRequestsFrom() {
        return friendRequestsFrom;
    }

    public void setFriendRequestsFrom(Set<String> friendRequestsFrom) {
        this.friendRequestsFrom = friendRequestsFrom;
    }

    public Map<String, Integer> getInviteGamesFrom() {
        return inviteGamesFrom;
    }

    public void setInviteGamesFrom(Map<String, Integer> inviteGamesFrom) {
        this.inviteGamesFrom = inviteGamesFrom;
    }

    public Map<String, List<Message>> getNewMessages() {
        return newMessages;
    }

    public void setNewMessages(Map<String, List<Message>> newMessages) {
        this.newMessages = newMessages;
    }
}
