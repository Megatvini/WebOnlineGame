package Core.Bean;

import Interfaces.iAccount;
import com.google.gson.annotations.Expose;
import com.mysql.fabric.xmlrpc.base.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Nika on 19:55, 6/27/2015.
 */
public class Notification {
    @Expose
    private List<iAccount> friendRequestsFrom;
    @Expose
    private List<GameInvitation> inviteGamesFrom;
    @Expose
    private List<NotificationMessage> newMessages;
    @Expose
    private Date serverTime;

    public Notification(List<iAccount> friendRequestsFrom,
                        List<GameInvitation> inviteGamesFrom,
                        List<NotificationMessage> newMessages) {
        this.friendRequestsFrom = friendRequestsFrom;
        this.inviteGamesFrom = inviteGamesFrom;
        this.newMessages = newMessages;
        this.serverTime = new Date(System.currentTimeMillis());
    }


    public List<iAccount> getFriendRequestsFrom() {
        return friendRequestsFrom;
    }

    public void setFriendRequestsFrom(List<iAccount> friendRequestsFrom) {
        this.friendRequestsFrom = friendRequestsFrom;
    }

    public List<GameInvitation> getInviteGamesFrom() {
        return inviteGamesFrom;
    }

    public void setInviteGamesFrom(List<GameInvitation> inviteGamesFrom) {
        this.inviteGamesFrom = inviteGamesFrom;
    }

    public List<NotificationMessage> getNewMessages() {
        return newMessages;
    }

    public void setNewMessages(List<NotificationMessage> newMessages) {
        this.newMessages = newMessages;
    }
}
