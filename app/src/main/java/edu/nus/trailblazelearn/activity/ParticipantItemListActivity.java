package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.ParticipantItemAdapter;
import edu.nus.trailblazelearn.model.ParticipantItem;
import edu.nus.trailblazelearn.utility.dbUtil;

public class ParticipantItemListActivity extends AppCompatActivity {
private ParticipantItemAdapter participantItemAdapter;
private ArrayList<ParticipantItem> participantItemArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_item_list);

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        FloatingActionButton floatingActionButton = findViewById(R.id.add_item_ActionButton);
        Toolbar toolbar = findViewById(R.id.toolbar);

        RecyclerView recyclerView = findViewById(R.id.participant_list_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //ArrayList<ParticipantItem> participantItemArrayList = dbUtil.getParticipantItems("Ragu", progressBar, );
        participantItemAdapter = new ParticipantItemAdapter(getApplicationContext(), participantItemArrayList);
        recyclerView.setAdapter(participantItemAdapter);
        //dbUtil.getParticipantItems("Ragu", progressBar);

        FirebaseFirestore.getInstance().collection("participantActivities")
                .whereEqualTo("trailStationId",1234)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null) {
                    Log.d("ParticipantItemList", e.getMessage());
                }
                participantItemArrayList.clear();
                for(DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()) {
                    ParticipantItem participantItem = documentSnapshot.toObject(ParticipantItem.class);
                    participantItemArrayList.add(participantItem);
                }
                participantItemAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParticipantItemListActivity.this, ParticipantAddItemActivity.class));
            }
        });
    }

}