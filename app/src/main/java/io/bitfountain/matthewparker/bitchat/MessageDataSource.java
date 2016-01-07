package io.bitfountain.matthewparker.bitchat;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by matthewparker on 4/11/15.
 */
public class MessageDataSource {

    private static final String TAG = "MessageDataSource";

    private static final Firebase sRef = new Firebase("https://flickering-torch-3874.firebaseio.com/messages");
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_SENDER = "sender";

    public static void saveMessage(Message message, String convoId){
        Date date = message.getDate();
        SimpleDateFormat df = new SimpleDateFormat(sDateFormat.toPattern());
        String key = df.format(date);
        HashMap<String, String> msg = new HashMap<>();
        msg.put(COLUMN_TEXT, message.getText());
        msg.put(COLUMN_SENDER,message.getSender());
        sRef.child(convoId).child(key).setValue(msg);
    }

    public static MessagesListener addMessagesListener(String convoId, final MessagesCallbacks callbacks){
        MessagesListener listener = new MessagesListener(callbacks);
        sRef.child(convoId).addChildEventListener(listener);
        return listener;
    }

    public static void stop(MessagesListener listener) {
        if (listener != null) {
            sRef.removeEventListener(listener);
        }
    }

    public static class MessagesListener implements ChildEventListener {

        private MessagesCallbacks callbacks;

        MessagesListener(MessagesCallbacks callbacks) {
            this.callbacks = callbacks;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap<String,String> msg = (HashMap)dataSnapshot.getValue();
            Message message = new Message();
            message.setText(msg.get(COLUMN_TEXT));
            message.setSender(msg.get(COLUMN_SENDER));
            try {
                message.setDate(sDateFormat.parse(dataSnapshot.getKey()));
            }catch (Exception e){
                Log.d(TAG, "Couldn't parse date" + e);
            }
            if(callbacks != null){
                callbacks.onMessageAdded(message);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }

    public interface MessagesCallbacks {
        public void onMessageAdded(Message message);
    }


//// PARSE VERSION
//    public static void sendMessage(String sender, String recipient, String text){
//        ParseObject message = new ParseObject("Message");
//        message.put(COLUMN_SENDER,sender);
//        message.put("recipient",recipient);
//        message.put(COLUMN_TEXT,text);
//        message.saveInBackground();
//    }
//
//    public static void fetchMessagesAfter(String sender, String recipient, Date after, final Listener listener){
//        ParseQuery<ParseObject> mainQuery = messagesQuery(sender, recipient);
//        mainQuery.whereGreaterThan("createdAt", after);
//        mainQuery.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> parseObjects, ParseException e) {
//                if (e == null) {
//                    ArrayList<Message> messages = new ArrayList<Message>();
//                    for (ParseObject parseObject : parseObjects) {
//                        Message message = new Message(parseObject.getString(COLUMN_TEXT), parseObject.getString(COLUMN_SENDER));
//                        message.setDate(parseObject.getCreatedAt());
//                        messages.add(message);
//                    }
//                    listener.onAddMessages(messages);
//                }
//            }
//        });
//    }
//
//    public static void fetchMessages(String sender, String recipient, final Listener listener){
//
//        ParseQuery<ParseObject> mainQuery = messagesQuery(sender, recipient);
//        mainQuery.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> parseObjects, ParseException e) {
//                if (e == null) {
//                    ArrayList<Message> messages = new ArrayList<Message>();
//                    for (ParseObject parseObject : parseObjects) {
//                        Message message = new Message(parseObject.getString(COLUMN_TEXT), parseObject.getString(COLUMN_SENDER));
//                        message.setDate(parseObject.getCreatedAt());
//                        messages.add(message);
//                    }
//                    listener.onFetchedMessages(messages);
//                }
//            }
//        });
//    }
//
//    private static ParseQuery<ParseObject> messagesQuery(String sender, String recipient){
//        ParseQuery<ParseObject> querySent = ParseQuery.getQuery("Message");
//        querySent.whereEqualTo(COLUMN_SENDER, sender);
//        querySent.whereEqualTo("recipient",recipient);
//
//        ParseQuery<ParseObject> queryReceived = ParseQuery.getQuery("Message");
//        queryReceived.whereEqualTo("recipient", sender);
//        queryReceived.whereEqualTo(COLUMN_SENDER,recipient);
//
//        List<ParseQuery<ParseObject>> queries = new ArrayList<>();
//        queries.add(querySent);
//        queries.add(queryReceived);
//        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
//        mainQuery.orderByAscending("createdAt");
//        return mainQuery;
//    }
//
//    public interface Listener{
//        public void onFetchedMessages(ArrayList<Message> messages);
//        public void onAddMessages(ArrayList<Message> messages);
//    }
}
