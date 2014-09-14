package com.facebook.linguahack.droidmotionchat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {

    public static int N = 32;
    public static int M = 32;

    private String userName;
    private String serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        N = getResources().getInteger(R.integer.N);
        M = getResources().getInteger(R.integer.M);

        Log.d("", "NNNN " + N);
        Log.d("", "MMMM " + M);

        chatLayout = (LinearLayout) findViewById(R.id.chatLayout);


        Bundle ext = getIntent().getExtras();
        userName = ext.getString(Consts.VAR_USER_NAME);
        serverUrl = ext.getString(Consts.VAR_SERVER_URL);

        getWindow().addContentView(
                new GestureView(userName, serverUrl, this),
                new GestureView.LayoutParams(GestureView.LayoutParams.FILL_PARENT, GestureView.LayoutParams.FILL_PARENT)
        );

        new CoreNetworkEngine.NetworkTask(userName, serverUrl, CoreNetworkEngine.ACTION_LOGIN).execute("");
        handler.postDelayed(runnable, 100);

    }

    private Handler handler = new Handler();
    private LinearLayout chatLayout;

    private Runnable runnable = new Runnable() {

        private List<View> messages = new LinkedList<View>();

        @Override
        public void run() {
            new CoreNetworkEngine.NetworkTask(userName, serverUrl, CoreNetworkEngine.ACTION_RECEIVE_MESSAGES) {
                @Override
                protected void onPostExecute(List<Message> result) {

                    for (View textView : messages) {
                        chatLayout.removeView(textView);
                    }
                    messages.clear();

                    for (int i = result.size() - 1; i >= 0; --i) {
                        Message m = result.get(i);
                        Log.d("", m.toString());

                        final TextView textView = new TextView(MainActivity.this);
                        textView.setTextSize(16f);
                        textView.setText(m.toString());
                        textView.setTextColor(m.getUser().hashCode() | 0xff000000);

                        chatLayout.addView(textView);
                        messages.add(textView);
                    }

                }
            }.execute("");
            handler.postDelayed(this, 1000);
        }
    };
}
