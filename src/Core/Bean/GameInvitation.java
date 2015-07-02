package Core.Bean;

import Interfaces.iAccount;
import com.google.gson.annotations.Expose;

/**
 * Created by Nika on 16:06, 6/28/2015.
 */
public class GameInvitation {
    @Expose
    private iAccount senderAccount;
    
    @Expose
    private int roomSize;

    public GameInvitation(iAccount senderAccount, int roomSize) {
        this.senderAccount = senderAccount;
        this.roomSize = roomSize;
    }

    public iAccount getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(iAccount senderAccount) {
        this.senderAccount = senderAccount;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }
}
