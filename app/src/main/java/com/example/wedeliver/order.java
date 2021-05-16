package com.example.wedeliver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

public class order extends AppCompatActivity {
    ArrayList<cartItemDetails> arrayListtemp=cart.arrayList;
    RecyclerView recyclerView;
    ArrayList<orderDetails> arrayList;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,databaseReference_order,databaseReference_order1;
    boolean temp=false;
    ConstraintLayout empty;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        toolbar=findViewById(R.id.toolbar_order);
        empty=findViewById(R.id.ll_empty);
        recyclerView=findViewById(R.id.recyclerview_order);
        arrayList=new ArrayList<orderDetails>();
        toolbar.setTitle("Order History");
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            recyclerView.setVisibility(View.VISIBLE);
            firebaseAuth= FirebaseAuth.getInstance();
            String useremail=firebaseAuth.getCurrentUser().getEmail();
            String newemail=useremail.replace(".","");
        if(arrayListtemp!=null && arrayListtemp.size()>0)
        {
            String date= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()) ;
            String time=new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            databaseReference_order=FirebaseDatabase.getInstance().getReference().child("order").child(newemail).child(date+time);
            databaseReference_order.child("date").setValue(date);
            databaseReference_order.child("time").setValue(time);
            for(int i=0;i<arrayListtemp.size();i++)
            {
                cartItemDetails cartItemDetails=arrayListtemp.get(i);
                String name=cartItemDetails.getName();
                String brand=cartItemDetails.getBrand();
                String quantity=cartItemDetails.getQuantity();
                String selling_price=cartItemDetails.getSelling_price();
                String actual_price=cartItemDetails.getActual_price();
                String count=cartItemDetails.getCount();
                String image=cartItemDetails.getImage();
                String discount=cartItemDetails.getDicount();
                databaseReference_order.child(name).child("name").setValue(name);
                databaseReference_order.child(name).child("brand").setValue(brand);
                databaseReference_order.child(name).child("selling_price").setValue(selling_price);
                databaseReference_order.child(name).child("actual_price").setValue(actual_price);
                databaseReference_order.child(name).child("count").setValue(count);
                databaseReference_order.child(name).child("discount").setValue(discount);
                databaseReference_order.child(name).child("quantity").setValue(quantity);
            }
            databaseReference=FirebaseDatabase.getInstance().getReference().child("cart");
            databaseReference.removeValue();
            arrayListtemp.clear();

        }


        DatabaseReference databaseReference_check=FirebaseDatabase.getInstance().getReference();
        databaseReference_check.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.child("order").exists())
                {
                    databaseReference_order1=FirebaseDatabase.getInstance().getReference().child("order").child(newemail);
                    databaseReference_order1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot dataSnapshot:snapshot.getChildren())
                            {
                                orderDetails orderDetails = new orderDetails();
                                String details="";
                                long payableamount=0;
                                long actualamount=0;
                                long savingamount=0;

                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                                {
                                    if (dataSnapshot1.getKey().equals("date"))
                                    {
                                        orderDetails.setDate(dataSnapshot1.getValue(String.class));
                                    }
                                    else if (dataSnapshot1.getKey().equals("time"))
                                    {
                                        orderDetails.setTime(dataSnapshot1.getValue(String.class));
                                    }
                                    else
                                    {

                                        for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                                            if (snapshot1.getKey().equals("selling_price")) {
                                                payableamount += Long.parseLong(snapshot1.getValue(String.class));
                                            } else if (snapshot1.getKey().equals("actual_price")) {
                                                actualamount += Long.parseLong(snapshot1.getValue(String.class));
                                            } else if (snapshot1.getKey().equals("name")) {
                                                details += (snapshot1.getValue(String.class))+"\n";
                                            } else if (snapshot1.getKey().equals("count")) {
                                                details += (snapshot1.getValue(String.class)) + "-";
                                            }
                                        }
                                    }
                                }
                                orderDetails.setOrder_Details(details);
                                orderDetails.setSaved_amount(actualamount-payableamount);
                                orderDetails.setTotal_amount(payableamount);
                                orderDetails.setOrderId(ThreadLocalRandom.current().nextInt(1, 1000));
                                arrayList.add(orderDetails);
                            }
                            orderAdapter orderAdapter= new orderAdapter(getApplicationContext(),arrayList) ;
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(orderAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else
                {
                  empty.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}