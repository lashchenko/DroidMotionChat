package com.facebook.linguahack.droidmotionchat;


import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Util {

    public static BitSet convert(int[] array) {
        BitSet bs = new BitSet(array.length);
        for (int i = 0; i < array.length; ++i) {
            bs.set(i, array[i] == 1);
        }
        return bs;
    }

    public static List<Message> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        reader.setLenient(true);
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public static List<Message> readMessagesArray(JsonReader reader) throws IOException {
        List<Message> messages = new ArrayList<Message>();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("messages")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    messages.add(readMessage(reader));
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return messages;
    }

    public static Message readMessage(JsonReader reader) throws IOException {
        String text = null;
        String user = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("user")) {
                user = reader.nextString();
            } else if (name.equals("text")) {
                text = reader.nextString();
            } else {
                reader.skipValue(); // ignore messageId
            }
        }
        reader.endObject();
        return new Message(text, user);
    }
}
