package com.aspegrenide.klick;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class DataManager {

    DatabaseReference mDatabase;

    public void writeCardDetailsListToFirebase(ArrayList<CardDetails> cardDetailsList, String dataBaseSet) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        for (CardDetails c : cardDetailsList) {
            mDatabase.child(dataBaseSet).child(c.getCardId()).setValue(c);

        }
    }
}
