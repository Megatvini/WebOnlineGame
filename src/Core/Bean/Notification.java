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
    private Map<String, GameInvitation> inviteGamesFrom;
    @Expose
    private Map<String, NotificationMessage> newMessages;

    public Notification(Set<iAccount> friendRequestsFrom, Map<String, GameInvitation> inviteGamesFrom,
                        Map<String, NotificationMessage> newMessages) {
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

    public Map<String, GameInvitation> getInviteGamesFrom() {
        return inviteGamesFrom;
    }

    public void setInviteGamesFrom(Map<String, GameInvitation> inviteGamesFrom) {
        this.inviteGamesFrom = inviteGamesFrom;
    }

    public Map<String, NotificationMessage> getNewMessages() {
        return newMessages;
    }

    public void setNewMessages(Map<String, NotificationMessage> newMessages) {
        this.newMessages = newMessages;
    }
}
