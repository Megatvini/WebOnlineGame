package Core;

import Core.Controller.Account;
import Core.View.Profile;
import Interfaces.Controller.iAccount;

/**
 * Created by gukam on 5/29/2015.
 */
public class ControlManager { //user unda erqvas
    public static iAccount getAccount(String nickname){
        return (new Account(nickname));
    }
}
