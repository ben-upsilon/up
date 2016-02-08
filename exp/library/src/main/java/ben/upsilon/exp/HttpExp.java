package ben.upsilon.exp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http 简单封装
 * Created by ben on 14/11/14.
 */
public final class HttpExp {
    private static HttpURLConnection urlConnection;

    public void call(Request request) {
        try {
            URL url = new URL("http://www.android.com/");

            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

    }

    private void readStream(InputStream in) {
    }

    public static class Builder {

        public Request build() {
            return null;
        }
    }

    public class Request {
        URL url;
    }

}
