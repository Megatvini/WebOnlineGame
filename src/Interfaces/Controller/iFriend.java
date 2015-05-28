package Interfaces.Controller;

import Interfaces.View.iProfile;
import Interfaces.View.iMessageView;

/**
 * Created by gukam on 5/28/2015.
 */
public interface iFriend extends iProfile {
    public void Delete();

    public void MessageSend(iMessageView message);
}
