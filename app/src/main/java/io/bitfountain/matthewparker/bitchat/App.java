package io.bitfountain.matthewparker.bitchat;

import android.app.Application;

import com.firebase.client.Firebase;
import com.parse.Parse;

/**
 * Created by Kevin Wilde on 1/6/2016.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
    }
}
