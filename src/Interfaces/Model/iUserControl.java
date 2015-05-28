package Interfaces.Model;

import Interfaces.Controller.iAccount;
import Interfaces.View.iProfile;
import Interfaces.View.iShorProfile;

import java.util.HashSet;

/**
 * Created by gukam on 5/28/2015.
 */
public interface iUserControl {
    public  void registerUser(iAccount acc);

    public  void changeUser(iAccount acc);

    public iProfile getProfile(int id);

    public iProfile getProfile(String username);

    public void AddFriend(iShorProfile aaccFrom, iShorProfile accTo);

    public void RemoveFriend(iShorProfile aaccFrom, iShorProfile accTo);

    public HashSet<iShorProfile> searchUsers(String str);
}
