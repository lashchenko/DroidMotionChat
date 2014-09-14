package com.facebook.linguahack.droidmotionchat;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {

    public static int N = 64;
    public static int M = 64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        N = getResources().getInteger(R.integer.N);
        M = getResources().getInteger(R.integer.M);

        Log.d("", "NNNN " + N);
        Log.d("", "MMMM " + M);

        chatLayout = (LinearLayout)findViewById(R.id.chatLayout);

        getWindow().addContentView(
                new GestureView(this),
                new GestureView.LayoutParams(GestureView.LayoutParams.FILL_PARENT, GestureView.LayoutParams.FILL_PARENT)
        );

        new CoreNetworkEngine.NetworkTask(CoreNetworkEngine.ACTION_LOGIN).execute("");
        handler.postDelayed(runnable, 100);

    }

    private Handler handler = new Handler();
    private LinearLayout chatLayout;

    private Runnable runnable = new Runnable() {

        private List<View> messages = new LinkedList<View>();

        @Override
        public void run() {
            new CoreNetworkEngine.NetworkTask(CoreNetworkEngine.ACTION_RECEIVE_MESSAGES) {
                @Override
                protected void onPostExecute(List<Message> result) {

                    for (View textView: messages) {
                        chatLayout.removeView(textView);
                    }
                    messages.clear();

                    for (int i=result.size()-1; i>=0; --i) {
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
