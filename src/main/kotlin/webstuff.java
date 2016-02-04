import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.SubredditPaginator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by unwin on 03-Feb-16.
 */
public class webstuff {

    public webstuff() {

    }

    public String fetchJoke() {
        String joke = "joke-null";
        try {
            StringBuilder result = new StringBuilder();
            String temp;

            URL url = new URL("http://api.icndb.com/jokes/random");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(result.toString());

            JSONObject valueObj = (JSONObject) jsonObject.get("value");
            joke = (String) valueObj.get("joke");

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return joke;
    }

    public String getDaisyLink() throws Exception{
        String link = "";
        String line;
        String[] creds = {"", "", "", "", "", "", ""};

        UserAgent userAgent = UserAgent.of("javacord-bot", "com.unwin.javacord-bot", "v2.0", "fcumbadass");
        RedditClient redditClient = new RedditClient(userAgent);

        try{
            FileReader fRead = new FileReader("pass.txt");
            BufferedReader bRead = new BufferedReader(fRead);

            int i = 0;
            while( (line = bRead.readLine()) != null) {
                System.out.printf("line = %s\n", line);
                creds[i] = line;
                i++;
            }

        } catch(Exception ex){
            ex.printStackTrace();
        }

        Credentials credentials = Credentials.script(creds[2], creds[3], creds[4], creds[5]);
        OAuthData authData = redditClient.getOAuthHelper().easyAuth(credentials);
        redditClient.authenticate(authData);

        SubredditPaginator subredditPaginator = new SubredditPaginator(redditClient, "DaisyRidley");
        Listing<Submission> post = subredditPaginator.next();


        return link;
    }


}
