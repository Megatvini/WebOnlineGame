package Core.Model;

import Core.Controller.Account;
import Interfaces.Controller.iAccount;
import Interfaces.View.iProfile;
import Interfaces.View.iShorProfile;

import java.sql.ResultSet;
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
        iAccount account = new Account();
        ResultSet result = DBWorker.getResult("SELECT * FROM accounts where Nickname = '" + nickname + "'");
        if (!result.next()) throw new Exception("notRegistered");
        account.setNickname(nickname);
        account.setFirstname(result.getString("FirstName"));
        account.setLastname(result.getString("LastName"));
        account.setPassword(result.getString("Password"));
        account.setGender(result.getString("Gender").equals("Male") ? Account.Gender.MALE : Account.Gender.FEMALE);
        // TODO: wamosagebia about, surati, rating, birthdate, mail

        return account;
    }

    public static HashMap<String, iShorProfile> getOnlineUsers(){
        HashMap<String, iShorProfile> users = new HashMap<String, iShorProfile>();
        for(iAccount acc : _accounts.values()){
            users.put(acc.getNickname(), acc);
        }
        return  users;
    }
}
