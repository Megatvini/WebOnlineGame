package Core;

import Core.View.Profile;
import Interfaces.View.iProfile;

/**
 * Created by gukam on 5/29/2015.
 */
public class ViewManager {
    public static iProfile getProfile(int id){
        return (new Profile(1));
    }
}