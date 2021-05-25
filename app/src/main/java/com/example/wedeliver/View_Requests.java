package com.example.wedeliver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class View_Requests extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__requests);
        recyclerView=findViewById(R.id.recyvlerview_view_requests);
        String useremail= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String newUserEmail=useremail.replace(".","");
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("support").child(newUserEmail);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ArrayList<String>> arrayList=new ArrayList<>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    ArrayList<String> arrayList_data = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (dataSnapshot1.getKey().equals("answer")) {
                            arrayList_data.add(dataSnapshot1.getValue().toString());
                        } else if (dataSnapshot1.getKey().equals("date")) {
                            arrayList_data.add(dataSnapshot1.getValue().toString());
                        } else if (dataSnapshot1.getKey().equals("problem")) {
                            arrayList_data.add(dataSnapshot1.getValue().toString());
                        } else if (dataSnapshot1.getKey().equals("time")) {
                            arrayList_data.add(dataSnapshot1.getValue().toString());
                        }

                    }
                    if (arrayList_data.size() == 3) {
                        arrayList_data.add(0, "");
                    }

                        arrayList.add(arrayList_data);
                }
                RequestAdapter requestAdapter=new RequestAdapter(getApplicationContext(),arrayList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(requestAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}