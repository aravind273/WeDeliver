package com.example.wedeliver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class orderSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        LinearLayout linearLayout=findViewById(R.id.linearLayout1);
        Snackbar.make(linearLayout,"Order Placed", Snackbar.LENGTH_SHORT).show();//snackbar added
       DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("cart");
       if(databaseReference!=null) {
           databaseReference.removeValue();
       }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}