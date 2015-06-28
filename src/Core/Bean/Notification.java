package Core.Bean;

import Interfaces.iAccount;
import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Nika on 19:55, 6/27/2015.
 */
public class Notification {
    @Expose
    private Set<iAccount> friendRequestsFrom;
    @Expose
    private Map<iAccount, Integer> inviteGamesFrom;
    @Expose
    private Map<iAccount, List<Message>> newMessages;

    public Notification(Set<iAccount> friendRequestsFrom, Map<iAccount, Integer> inviteGamesFrom, Map<iAccount, List<Message>> newMessages) {
        this.friendRequestsFrom = friendRequestsFrom;
        this.inviteGamesFrom = inviteGamesFrom;
        this.newMessages = newMessages;
    }


    public Set<iAccount> getFriendRequestsFrom() {
        return friendRequestsFrom;
    }

    public void setFriendRequestsFrom(Set<iAccount> friendRequestsFrom) {
        this.friendRequestsFrom = friendRequestsFrom;
    }

    public Map<iAccount, Integer> getInviteGamesFrom() {
        return inviteGamesFrom;
    }

    public void setInviteGamesFrom(Map<iAccount, Integer> inviteGamesFrom) {
        this.inviteGamesFrom = inviteGamesFrom;
    }

    public Map<iAccount, List<Message>> getNewMessages() {
        return newMessages;
    }

    public void setNewMessages(Map<iAccount, List<Message>> newMessages) {
        this.newMessages = newMessages;
    }
}
