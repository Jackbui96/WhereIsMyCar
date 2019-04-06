package com.example.richa.buttonclickapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.richa.buttonclickapp.Object.LicensePlateInfo;
import com.example.richa.buttonclickapp.Object.SearchHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class searchHistoryActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    public static String searchLicensePlate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        linearLayout = findViewById(R.id.linearLayout);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8),(int) (height * 0.6));

        TextView searchHistoryTitle = new TextView(this);
        searchHistoryTitle.setText("Search History");
        searchHistoryTitle.setTextSize(24);
        searchHistoryTitle.setTypeface(null, Typeface.BOLD);
        searchHistoryTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(searchHistoryTitle);

        databaseReference.child("searches")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userId;

                        // go through each child under "cars" parents
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final SearchHistory searchHistory = snapshot.getValue(SearchHistory.class);
                            userId = searchHistory.userId;
                            String LoggedInUserId = firebaseAuth.getUid();

                            System.out.println(userId  + "=???????" + LoggedInUserId);

                            if(userId.equals(LoggedInUserId)) {
                                System.out.println("------- The logged in user has this search result -------" + searchHistory);
                                final TextView searchHistoryTextView = new TextView(getApplicationContext());
                                searchHistoryTextView.setTextSize(24);
                                searchHistoryTextView.setPadding(10,10,10,0);
                                searchHistoryTextView.setText(searchHistory.searchedLicensePlate);

                                searchHistoryTextView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(v == searchHistoryTextView){
                                            System.out.println("You click this search history!!!!!!!!!!!");
                                            finish();
                                            Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                                            searchLicensePlate = searchHistory.searchedLicensePlate;
                                            intent.putExtra("SEARCH_HISTORY_INPUT", searchLicensePlate);
                                            startActivity(intent);
                                        }
                                    }
                                });
                                linearLayout.addView(searchHistoryTextView);
                            }
                            else
                            {
                                continue;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }
}
