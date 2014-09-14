package com.facebook.linguahack.droidmotionchat;

import android.app.ActivityManager;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class CoreNetworkEngine {

    private static final String TAG = "CoreNetworkEngine";
    // FIXME configurable
//    private static final String BASE_URL = "http://192.168.10.91:9000/";
//    private static final String BASE_URL = "http://192.168.0.101:9000/";
    private static final String BASE_URL = "http://192.168.10.146:9000/";
    private static final String USERNAME = android.os.Build.MODEL.replaceAll("[\\W\\s]+", "");

    public static final int ACTION_LOGIN = 0;
    public static final int ACTION_NEW_MESSAGE = 1;
    public static final int ACTION_UPDATE_MESSAGE = 2;
    public static final int ACTION_RECEIVE_MESSAGES = 3;

    public static final char SYMBOL_NEW = '*';

    private static final List<Message> EMPTY_RESPONSE = new LinkedList<Message>();

    HttpClient client = new DefaultHttpClient();


    public static class NetworkTask extends AsyncTask<String, Void, List<Message>> {

        public NetworkTask(Integer action) {
            this(action, ' ');
        }

        public NetworkTask(Integer action, Character symbol) {
            this.action = action;
            this.symbol = symbol;
        }

        private final Integer action;
        private final Character symbol;

        private CoreNetworkEngine cne = new CoreNetworkEngine();


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

    public List<Message> login() throws Exception {
        String URL = BASE_URL + "login/" + USERNAME;
        Log.d("", "URL: " + URL);

        client.execute(new HttpGet(URL));
        return EMPTY_RESPONSE;
    }

    public List<Message> newMessage(Character symbol) throws Exception {
        String URL = BASE_URL + "put/" + USERNAME + "/" + symbol;
        Log.d("", "URL: " + URL);

        client.execute(new HttpGet(URL));
        return EMPTY_RESPONSE;
    }

    public List<Message> updateMessage(Character symbol) throws Exception {
        String URL = BASE_URL + "update/" + USERNAME + "/" + symbol;
        Log.d("", "URL: " + URL);

        client.execute(new HttpGet(URL));
        return EMPTY_RESPONSE;
    }

    public List<Message> receiveMessages() throws Exception {
        String URL = BASE_URL + "messages";
        Log.d("", "URL: " + URL);

        List<Message> messages;

        HttpResponse response = client.execute(new HttpGet(URL));
        InputStream content = response.getEntity().getContent();
        messages = Util.readJsonStream(content);

        return messages;
    }

}
