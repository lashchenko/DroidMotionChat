package com.dm;

import android.app.*;
import android.os.*;
import android.widget.*;

public class MainActivity extends Activity {
    
	@Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(new GestureView(this));
        CoreNetworkEngine.BaseTask task = new CoreNetworkEngine.BaseTask();
        task.execute("");
        //        new DownloadFilesTask().execute(url1, url2, url3);
    }
	
}
