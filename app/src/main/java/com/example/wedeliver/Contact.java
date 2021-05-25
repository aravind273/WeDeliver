package com.example.wedeliver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Contact extends AppCompatActivity {
    Toolbar toolbar;
    EditText contact;
    Button submit;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        toolbar=findViewById(R.id.toolbar_contact);
        toolbar.setTitle("Contact us");
        contact=findViewById(R.id.message);
        submit=findViewById(R.id.submit_contact);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth=FirebaseAuth.getInstance();
                String useremail=firebaseAuth.getCurrentUser().getEmail();
                String newUserEmail=useremail.replace(".","");
                String date= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()) ;
                String time=new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("support").child(newUserEmail).child(date+time);
                databaseReference.child("problem").setValue(contact.getText().toString());
                databaseReference.child("resolved").setValue("not");
                databaseReference.child("date").setValue(date);
                databaseReference.child("time").setValue(time);
                if(databaseReference!=null)
                {
                    Toast.makeText(getApplicationContext(),"Request successfully sent",Toast.LENGTH_SHORT).show();
                    contact.getText().clear();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Try again!",Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
}