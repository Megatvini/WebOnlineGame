package Core;

import Core.Model.UserControl;

/**
 * Created by gukam on 5/30/2015.
 */
public class AuthenticationManager {
public static  boolean login(String nickname, String password){
    return UserControl.getUser(nickname).getPassword().equals(password) ? true : false;
    }
}
