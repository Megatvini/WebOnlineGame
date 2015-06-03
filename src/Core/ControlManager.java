package Core;

import Core.Controller.Account;
import Core.Model.UserControl;
import Interfaces.Controller.iAccount;
import  Interfaces.View.iShorProfile;

import java.util.HashMap;

/**
 * Created by gukam on 5/29/2015.
 */
public class ControlManager {
    public static iAccount getAccount(String nickname) throws Exception {
        return (new Account(nickname));
    }

    public static HashMap<String, iShorProfile> getOnlineUsers(){
        HashMap<String, iShorProfile> users = UserControl.getOnlineUsers();
        return users;
    }
}
