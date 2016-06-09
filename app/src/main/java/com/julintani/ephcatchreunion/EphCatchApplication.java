package com.julintani.ephcatchreunion;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.julintani.ephcatchreunion.models.MessagingManager;
import com.julintani.ephcatchreunion.models.User;
import com.parse.Parse;

/**
 * Created by ell on 4/24/16.
 */
public class EphCatchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        Parse.initialize(this, "Ii6zO3YikEpkOmOCHfpHS6g9eLAxlcQD1lf2fkrG", "5jum20Bi2JEBfeIw4IpxwmknxP9ftUc5crHEIxQB");
        MessagingManager.getInstance().refreshConversations(this);
    }
}
