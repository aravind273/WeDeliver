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

public class user_account_details_demo extends AppCompatActivity {
    ImageView imageView;
    TextView name,email,phone,address;
    Button logout;
    TextView totalCartItems,totalOrders,requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_details_demo);
        imageView=findViewById(R.id.user_imageview);
        name=findViewById(R.id.user_name);
        email=findViewById(R.id.user_email);
        phone=findViewById(R.id.user_phone);
        address=findViewById(R.id.user_address);
        logout=findViewById(R.id.logout);
        requests=findViewById(R.id.user_requests);
        totalCartItems=findViewById(R.id.items_in_cart);
        totalOrders=findViewById(R.id.total_orders);
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
                                if(snapshot2.getKey().equals("name")) {
                                    String name1 = snapshot2.getValue().toString();
                                    if (name1.length() > 0) {
                                        if (Character.isLowerCase(name1.charAt(0))) {
                                            Character.toUpperCase(name1.charAt(0));
                                            name.setText(name1);
                                        } else {
                                            name.setText(name1);
                                        }
                                    }
                                }
                            }
                            else if(snapshot2.getKey().equals("email"))
                            {
                                email.setText(snapshot2.getValue().toString());
                            }
                            else if(snapshot2.getKey().equals("phonenumber"))
                            {
                                phone.setText("+91"+snapshot2.getValue().toString());
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
        DatabaseReference databaseReference_cart=FirebaseDatabase.getInstance().getReference().child("cart").child(newemail);
        if(databaseReference_cart==null)
        {
            totalCartItems.setText("0");
        }
        else
        {
            databaseReference_cart.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count=0;
                    for(DataSnapshot snapshot1:snapshot.getChildren())
                    {
                        for(DataSnapshot snapshot2:snapshot1.getChildren()) {
                            if (snapshot2.getKey().equals("count")) {
                                count += Integer.parseInt(snapshot2.getValue().toString());
                            }
                        }
                    }
                    totalCartItems.setText(""+count);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        DatabaseReference databaseReference_orders=FirebaseDatabase.getInstance().getReference().child("order").child(newemail);
        if(databaseReference_orders==null)
        {
            totalOrders.setText("0");
        }
        else
        {
            databaseReference_orders.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    totalOrders.setText(""+snapshot.getChildrenCount());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        totalCartItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),cart.class);
                startActivity(intent);
            }
        });
        totalOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),order.class);
                startActivity(intent);
            }
        });
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),View_Requests.class);
                startActivity(intent);

            }
        });

    }
}