package com.example.wedeliver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_account_details extends AppCompatActivity {
    ImageView imageView;
    TextView name,email,phone,address;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_details);
        imageView=findViewById(R.id.user_imageview);
        name=findViewById(R.id.user_name);
        email=findViewById(R.id.user_email);
        phone=findViewById(R.id.user_phone);
        address=findViewById(R.id.user_address);
        logout=findViewById(R.id.logout);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        String emaill= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String newemail=emaill.replace(".","");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    if(snapshot1.getKey().equals(newemail))
                    {
                        for(DataSnapshot snapshot2:snapshot1.getChildren())
                        {
                            if(snapshot2.getKey().equals("name"))
                            {
                                name.setText(snapshot2.getValue().toString());
                            }
                            else if(snapshot2.getKey().equals("email"))
                            {
                                email.setText(snapshot2.getValue().toString());
                            }
                            else if(snapshot2.getKey().equals("phonenumber"))
                            {
                                phone.setText(snapshot2.getValue().toString());
                            }
                            else if(snapshot2.getKey().equals("address"))
                            {
                                address.setText(snapshot2.getValue().toString());
                            }
                            else if(snapshot2.getKey().equals("ImageLink"))
                            {
                                Glide.with(getApplicationContext()).load(Uri.parse(snapshot2.getValue().toString())).into(imageView);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getApplicationContext(),login.class);
                startActivity(intent);
            }
        });

    }
}