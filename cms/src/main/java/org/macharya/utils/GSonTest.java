package org.macharya.utils;

import com.google.gson.Gson;
import org.macharya.model.Message;

/**
 * Created by maheshacharya on 7/28/16.
 */
public class GSonTest {

    public static void main(String args []){
        Gson gson = new Gson();
        String json = "{\n" +
                "  \"properties\": [\n" +
                "    {\n" +
                "      \"a\": \"b\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"x\": \"y\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
       Message message  = gson.fromJson(json, Message.class);
        System.out.println(message.getProperties());

    }
}
