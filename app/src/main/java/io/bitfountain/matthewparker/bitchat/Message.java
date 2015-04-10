package io.bitfountain.matthewparker.bitchat;

/**
 * Created by matthewparker on 4/10/15.
 */
public class Message {
    private String mText;
    private String mSender;

    Message(String text, String sender){
        mText = text;
        mSender = sender;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
