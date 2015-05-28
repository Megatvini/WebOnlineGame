package Interfaces.Model;

import Interfaces.Controller.iAccount;
import Interfaces.View.iProfile;

/**
 * Created by gukam on 5/28/2015.
 */
public interface iUserControl {
    public  void registerUser(iAccount acc);

    public iProfile getProfile(int id);

    public iProfile getProfile(String username);
}
