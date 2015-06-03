package Core.Model;

import Interfaces.Controller.iAccount;
import Interfaces.View.iShorProfile;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by gukam on 5/29/2015.
 */
public class UserControl {
   private static HashMap<String ,iAccount> _accounts = new HashMap<String, iAccount>();

    public static void RegisterUser(iAccount account){
        _accounts.put(account.getNickname(), account);
    }
    public static iAccount getUser(String nickname) throws Exception {
        if(!_accounts.containsKey(nickname)) throw new Exception("notRegistered");
        return _accounts.get(nickname);
    }

    public static HashMap<String, iShorProfile> getOnlineUsers(){
        HashMap<String, iShorProfile> users = new HashMap<String, iShorProfile>();
        for(iAccount acc : _accounts.values()){
            users.put(acc.getNickname(), acc);
        }
        return  users;
    }
}
