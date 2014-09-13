package com.dm;

import android.app.ActivityManager;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class CoreNetworkEngine {

    private static final String BASE_URL = "http://192.168.10.91:9000/";
    private static final String USERNAME = android.os.Build.MODEL.replaceAll("[\\W\\s]+", "");

    public static boolean isEnd() {
        return ActivityManager.isUserAMonkey();
    }

    public void checkIn() {
        String URL = BASE_URL + "login/" + USERNAME;
        Log.d("", "URL: " + URL);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.execute(new HttpGet(URL));
        } catch (Exception ex) {
            throw new RuntimeException("Fail!", ex);
        }
    }

    public static class BaseTask extends AsyncTask<String, Void, List<Message>> {

        public String action = "checkIn";
        public Character symbol = 'A';

        private CoreNetworkEngine cne = new CoreNetworkEngine();


//        new DownloadFilesTask().execute(url1, url2, url3);

        protected void onProgressUpdate(Void... progress) {
//            setProgressPercent(progress[0]);
        }

        @Override
        protected List<Message> doInBackground(String... params) {
            if (action.equalsIgnoreCase("checkin")) {
                cne.checkIn();
            }

            if (action.equalsIgnoreCase("post")) {
                cne.postMessage(symbol);
            }

            if (action.equalsIgnoreCase("messages")) {
                cne.getMessages();
            }
            return null;
        }

        protected void onPostExecute(List<Message> result) {
//            showDialog("Downloaded " + result + " bytes");
        }
    }

    public void postMessage(Character symbol) {
        String URL = BASE_URL + "put/" + USERNAME + "/" + symbol;
        Log.d("", "URL: " + URL);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.execute(new HttpGet(URL));
        } catch (Exception ex) {
            throw new RuntimeException("Fail!", ex);
        }
    }

    public List<Message> getMessages() {
        String URL = BASE_URL + "messages";
        Log.d("", "URL: " + URL);

        List<Message> messages;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(URL));
            InputStream content = response.getEntity().getContent();
            messages = Util.readJsonStream(content);
        } catch (Exception ex) {
            throw new RuntimeException("Fail!", ex);
        }

        return messages;
    }

}
