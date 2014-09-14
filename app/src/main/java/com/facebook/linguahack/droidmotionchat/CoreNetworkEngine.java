package com.facebook.linguahack.droidmotionchat;

import android.app.ActivityManager;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class CoreNetworkEngine {

    private String userName;
    private String serverUrl;

    public CoreNetworkEngine(String userName, String serverUrl) {
        this.userName = userName;
        this.serverUrl = serverUrl;
    }

    private static final String TAG = "CoreNetworkEngine";

    public static final int ACTION_LOGIN = 0;
    public static final int ACTION_NEW_MESSAGE = 1;
    public static final int ACTION_UPDATE_MESSAGE = 2;
    public static final int ACTION_RECEIVE_MESSAGES = 3;

    public static final char SYMBOL_NEW = '~';

    static final List<Message> EMPTY_RESPONSE = new LinkedList<Message>();

    public static class NetworkTask extends AsyncTask<String, Void, List<Message>> {

        private final Integer action;
        private final Character symbol;
        private NetworkOps cne;


        public NetworkTask(String userName, String serverUrl, Integer action) {
            this(userName, serverUrl, action, ' ');
        }

        public NetworkTask(String userName, String serverUrl, Integer action, Character symbol) {
            this.action = action;
            this.symbol = symbol;
            cne = new NetworkOps(userName, serverUrl);
        }

        protected void onProgressUpdate(Void... progress) {
            /* not implemented */
        }

        protected List<Message> doInBackground(String... params) {
            List<Message> response = EMPTY_RESPONSE;
            try {
                switch (action) {
                    case CoreNetworkEngine.ACTION_LOGIN:
                        response = cne.login();
                        break;
                    case CoreNetworkEngine.ACTION_NEW_MESSAGE:
                        response = cne.newMessage(symbol);
                        break;
                    case CoreNetworkEngine.ACTION_UPDATE_MESSAGE:
                        response = cne.updateMessage(symbol);
                        break;
                    case CoreNetworkEngine.ACTION_RECEIVE_MESSAGES:
                        response = cne.receiveMessages();
                        break;
                    default:
                        /* not implemented */
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return response;
            }
        }

        protected void onPostExecute(List<Message> result) {
            /* for debug only */
//            for (Message m: result) {
//                Log.d(TAG, m.toString());
//            }
        }
    }
}

class NetworkOps {

    private String userName;
    private String serverUrl;
    private HttpClient client = new DefaultHttpClient();

    private HttpParams httpParameters = new BasicHttpParams();
    private int timeoutConnection = 1000;
    private int timeoutSocket = 1000;

    private DefaultHttpClient httpClient;

    public NetworkOps(String userName, String serverUrl) {
        this.userName = userName;
        this.serverUrl = serverUrl;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        httpClient = new DefaultHttpClient(httpParameters);
    }

    public List<Message> login() throws Exception {
        String URL = serverUrl + "login/" + userName;
        Log.d("", "URL: " + URL + " with timeout " + httpClient.getParams().getParameter(HttpConnectionParams.CONNECTION_TIMEOUT));
        client.execute(new HttpGet(URL));
        return CoreNetworkEngine.EMPTY_RESPONSE;
    }

    public List<Message> newMessage(Character symbol) throws Exception {
        String URL = serverUrl + "put/" + userName + "/" + map(symbol);
        Log.d("", "URL: " + URL);

        client.execute(new HttpGet(URL));
        return CoreNetworkEngine.EMPTY_RESPONSE;
    }

    public List<Message> updateMessage(Character symbol) throws Exception {
        String URL = serverUrl + "update/" + userName + "/" + map(symbol);
        Log.d("", "URL: " + URL);

        client.execute(new HttpGet(URL));
        return CoreNetworkEngine.EMPTY_RESPONSE;
    }

    private String map(Character symbol) {
        return (symbol == ' ') ? "%20" : "" + symbol;
    }

    public List<Message> receiveMessages() throws Exception {
        String URL = serverUrl + "messages";
        Log.d("", "URL: " + URL);

        List<Message> messages;

        HttpResponse response = client.execute(new HttpGet(URL));
        InputStream content = response.getEntity().getContent();
        messages = Util.readJsonStream(content);

        return messages;
    }
}