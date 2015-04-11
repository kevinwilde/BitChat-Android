package io.bitfountain.matthewparker.bitchat;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewparker on 4/11/15.
 */
public class MessageDataSource {
    public static void sendMessage(String sender, String recipient, String text){
        ParseObject message = new ParseObject("Message");
        message.put("sender",sender);
        message.put("recipient",recipient);
        message.put("text",text);
        message.saveInBackground();
    }

    public static void fetchMessages(String sender, String recipient, final Listener listener){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        query.whereEqualTo("sender", sender);
        query.whereEqualTo("recipient",recipient);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ArrayList<Message> messages = new ArrayList<Message>();
                for (ParseObject parseObject: parseObjects){
                    Message message = new Message((String)parseObject.get("text"),(String)parseObject.get("sender"));
                    messages.add(message);
                }
                listener.onFetchedMessages(messages);
            }
        });
    }

    public interface Listener{
        public void onFetchedMessages(ArrayList<Message> messages);
    }
}
