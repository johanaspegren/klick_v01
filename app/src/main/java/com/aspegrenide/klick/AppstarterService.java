package com.aspegrenide.klick;

import android.app.IntentService;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AppstarterService extends IntentService {
    public static final String CARDUID = "carduid";

    public AppstarterService() {
        super("AppstarterService");
        Log.d("APPSTARTER", "Init");
    }

    private CardDetails lookUpCardDetails(String cardUid) {
        ArrayList<CardDetails> cardDetailsList = MainActivity.getCards();
        for (CardDetails cd: cardDetailsList) {
            if(cd.getCardId().equals(cardUid)) {
                Log.d("APPSTARTER", "Found match " + cd.getUri());
                return (cd);
            }
        }
        // not recognized, call main activity and register the card
        Intent registerCard = new Intent(this, MainActivity.class);
        registerCard.putExtra("REGISTER", cardUid);
        startActivity(registerCard);
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String cardUid = intent.getStringExtra("CARDUID");
        Log.d("APPSTARTER", "received carduid " + cardUid);
        //startBrowser(cardUid);
        CardDetails cd = lookUpCardDetails(cardUid);
        if (cd != null) {
            startApp(cd);
        }else {
            Log.d("APPSTARTER", "NO MATCH on" + cardUid);
        }
    }

    private void startApp(CardDetails cd) {
        String mUri = "";
        String mPkg = "";
        String mCls = "";
        String mAction = "";
        String mData = "";
        Log.d("APPSTARTER", "startApp: " + cd.getName());

        Intent myIntent;
        if(cd.getUri() != null) {
            myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cd.getUri()));
        } else {
            myIntent = new Intent(Intent.ACTION_VIEW);
        }

        if (cd.getPkg() != null) {
            //special
            if((cd.getAction() == null) && (cd.getUri() == null)) {
                startNewActivity(getApplicationContext(), cd.getPkg());
                return;
            }
            myIntent.setPackage(cd.getPkg());
        }

        if ((cd.getCls() != null) && (cd.getPkg() != null)) {
            myIntent.setClassName(cd.getPkg(), cd.getCls());
        }

        if (cd.getAction() != null) {
            myIntent.setAction(cd.getAction());
        }

        if (cd.getData() != null) {
            myIntent.setData(Uri.parse(cd.getData()));
        }
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("APPSTARTER", "startApp: uri    " + cd.getUri());
        Log.d("APPSTARTER", "startApp: action " + cd.getAction());
        Log.d("APPSTARTER", "startApp: data   " + cd.getData());
        Log.d("APPSTARTER", "startApp: cls    " + cd.getCls());
        Log.d("APPSTARTER", "startApp: pkg    " + cd.getPkg());

        startActivity(myIntent);

    }

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
