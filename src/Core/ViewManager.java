package Core;

import Core.Controller.Account;
import Interfaces.View.iProfile;

/**
 * Created by gukam on 5/29/2015.
 */
public class ViewManager {
    public static iProfile getProfile(String nickname) throws Exception {
        return (new Account(nickname));
    }
}
