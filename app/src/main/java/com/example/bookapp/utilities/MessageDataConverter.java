package com.example.bookapp.utilities;

import androidx.annotation.NonNull;

import com.example.bookapp.models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageDataConverter {

    @NonNull
    public static ArrayList<Message> convertJsonArrayToMessages(String response) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray == null) {
            return new ArrayList<>();
        } else {
            return convertJsonArrayToMessages(jsonArray);
        }
    }

    @NonNull
    public static ArrayList<Message> convertJsonArrayToMessages(@NonNull JSONArray jsonArray) {
        ArrayList<Message> dataToReturn = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject messageJson = jsonArray.getJSONObject(i);
                dataToReturn.add(convertJsonObjectToMessage(messageJson));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataToReturn;
    }

    @NonNull
    public static Message convertJsonObjectToMessage(@NonNull JSONObject messageJson) throws JSONException {
        Message.Builder builder = new Message.Builder();
        builder.setMessageContent(messageJson.getString("messageContent"));
        builder.setMessageDate(messageJson.getLong("messageDate"));
        builder.setMessageID(messageJson.getString("messageId"));
        builder.setSenderID(messageJson.getString("senderId"));
        return builder.createMessage();
    }
}