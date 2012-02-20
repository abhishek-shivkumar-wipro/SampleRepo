import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import org.json.JSONArray;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJaxbRestClient;
import com.google.code.facebookapi.FacebookJsonRestClient;
import com.google.code.facebookapi.FacebookXmlRestClient;
import com.google.code.facebookapi.IFacebookRestClient;
import com.google.code.facebookapi.ProfileField;
import com.google.code.facebookapi.schema.FriendsGetResponse;
import com.google.code.facebookapi.schema.User;
import com.google.code.facebookapi.schema.UsersGetInfoResponse;

import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;


public class FacebookClient {
	
public static void main(String args[]) {
		String API_KEY = "290732590990625";
		String SECRET_KEY = "0f1f3f41883409c3218c6907a462ad59";
		
//		String API_KEY = "254517191295957";
//		String SECRET_KEY = "93d4cb6907605c2e1971ff0ef2f395eb";
		
        FacebookJaxbRestClient client = new FacebookJaxbRestClient(API_KEY,
                SECRET_KEY);

        try {
            String token = client.auth_createToken();
            String url = "http://www.facebook.com/login.php?api_key=" + API_KEY
                    + "&v=1.0" + "&auth_token=" + token;
            System.out.println(url);
            Runtime.getRuntime().exec("explorer \"" + url + "\"");

            System.out.println("Use browser to login then press return");
            System.in.read();

            String session = client.auth_getSession(token, true);
//            System.out.println("Session key is " + session);
            
            String tempSecret = client.getCacheSessionSecret();
            
            client = new FacebookJaxbRestClient(API_KEY, tempSecret, session);

            FriendsGetResponse response = client.friends_get(); // (JSONArray)client.friends_get();
//            System.out.println(response.toString());
            
            //
            List<Long> friends = response.getUid();
            //List<Long> friends_aboutMe = response.getUid();
            
            // Go fetch the information for the user list of user ids
            client.users_getInfo(friends, EnumSet.of(ProfileField.NAME));
            
            UsersGetInfoResponse userResponse = (UsersGetInfoResponse) client.getResponsePOJO();
            
            List<User> users = userResponse.getUser();
            System.out.println("The Names of the user's friends are:");
            
            WritableWorkbook workbook = Workbook.createWorkbook(new File("output.xls")); 
            WritableSheet sheet = workbook.createSheet("First Sheet", 0); 
            try {
                int cnt = 0; 
                for (User user : users) {
                	String name = user.getName();
                	System.out.println(name);
                	Label label = new Label(0, cnt, name);
                	sheet.addCell(label);
                	cnt++;
                }
                workbook.write();
                workbook.close();
			} catch (RowsExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            

            //
//            Long userId = client.users_getLoggedInUser(); 
            
            
            		
        } catch (FacebookException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}