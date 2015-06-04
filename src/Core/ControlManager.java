package Core;

import Core.Controller.Account;
import Core.Model.UserControl;
import Interfaces.Controller.iAccount;
import  Interfaces.View.iShorProfile;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by gukam on 5/29/2015.
 */
public class ControlManager {
    public static iAccount getAccount(String nickname) throws Exception {
        return (new Account(nickname));
    }

    public static HashMap<String, iShorProfile> getOnlineUsers(String except){
        HashMap<String, iShorProfile> users = UserControl.getOnlineUsers();
        users.remove(except);
        return users;
    }

    public static HashMap<String, iShorProfile> getOnlineUsersLike(String searchText){
        HashMap<String, iShorProfile> users = UserControl.getOnlineUsers();
        HashSet<String> nicknames = new HashSet<>();
        for (String nickname : users.keySet()){
            if(!nickname.contains(searchText))
                nicknames.add(nickname);
        }
        for (String nickname : nicknames){
                users.remove(nickname);
        }
        return users;
    }
}
