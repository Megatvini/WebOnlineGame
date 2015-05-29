package Core.Model;

import Interfaces.Controller.iAccount;

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
    public static iAccount getUser(String nickname){
        return _accounts.get(nickname);
    }
}
