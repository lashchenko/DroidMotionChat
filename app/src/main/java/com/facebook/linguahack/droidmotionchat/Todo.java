package com.facebook.linguahack.droidmotionchat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Mao on 14.09.2014.
 */
public class Todo {

    public static void main(String[] s) throws IOException {
        URLConnection connection = new URL("http://192.168.10.91:9000/login/Andrei").openConnection();
        InputStream response = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        System.out.println("Got response: " + stringBuilder.toString());


    }
}
