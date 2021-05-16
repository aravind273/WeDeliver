package com.example.wedeliver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class cart extends AppCompatActivity {
    public static ArrayList<cartItemDetails> arrayList;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Toolbar toolbar;
    LinearLayout emptycart;
    LinearLayout bottomlayout;
    LinearLayout products;
    TextView startShopping;
    Button placeOrder;
    DatabaseReference databaseReference_order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=findViewById(R.id.recyclerview_cart);
        emptycart=findViewById(R.id.empty_cart);
        bottomlayout=findViewById(R.id.ll_item);
        products=findViewById(R.id.ll_item_products);
        products.setVisibility(View.VISIBLE);
         bottomlayout.setVisibility(View.VISIBLE);
         startShopping=findViewById(R.id.startshopping);
         placeOrder=findViewById(R.id.proceed);

        toolbar=findViewById(R.id.toolbar2);
        toolbar.setTitle("Cart");
        firebaseAuth=FirebaseAuth.getInstance();
        String useremail=firebaseAuth.getCurrentUser().getEmail();
        arrayList=new ArrayList<cartItemDetails>();
        String newUserEmail=useremail.replace(".","");
        databaseReference=FirebaseDatabase.getInstance().getReference().child("cart").child(""+newUserEmail);
        ValueEventListener listener1=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean temp = false;
                    for (cartItemDetails cartItemDetails1 : arrayList) {
                        if (cartItemDetails1.getName()!=null && snapshot.child(cartItemDetails1.getName()).exists()) {
                            temp = true;
                            break;
                        }
                    }
                if (!temp) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        cartItemDetails cartItemDetails = new cartItemDetails();
                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {

                            if (snapshot2.getKey().equals("count")) {
                                cartItemDetails.setCount(snapshot2.getValue(String.class));
                            }
                            if (snapshot2.getKey().equals("name")) {
                                cartItemDetails.setName(snapshot2.getValue(String.class));
                            }
                            if (snapshot2.getKey().equals("brand")) {
                                cartItemDetails.setBrand(snapshot2.getValue(String.class));
                            }
                            if (snapshot2.getKey().equals("discount")) {
                                cartItemDetails.setDicount(snapshot2.getValue(String.class));
                            }
                            if (snapshot2.getKey().equals("image")) {
                                cartItemDetails.setImage(snapshot2.getValue(String.class));
                            }
                            if (snapshot2.getKey().equals("actual_price")) {
                                cartItemDetails.setActual_price(snapshot2.getValue(String.class));
                            }
                            if (snapshot2.getKey().equals("selling_price")) {
                                cartItemDetails.setSelling_price(snapshot2.getValue(String.class));
                            }
                            if (snapshot2.getKey().equals("quantity")) {
                                cartItemDetails.setQuantity(snapshot2.getValue(String.class));
                            }
                            if (snapshot2.getKey().equals("basic_selling_price")) {
                                cartItemDetails.setBasic_selling_price(snapshot2.getValue(String.class));
                            }
                            if (snapshot2.getKey().equals("basic_actual_price")) {
                                cartItemDetails.setBasic_actual_price(snapshot2.getValue(String.class));
                            }
                        }
                        arrayList.add(cartItemDetails);
//
                            
                    }
                    cartAdapter cartAdapter = new cartAdapter(getApplicationContext(), arrayList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(cartAdapter);
                }
                if(arrayList.size()==0)
                {
                    emptycart.setVisibility(View.VISIBLE);
                    products.setVisibility(View.INVISIBLE);
                    bottomlayout.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        startShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }
        });
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(getApplicationContext(),order.class);
//                startActivity(intent);
                String useremail=firebaseAuth.getCurrentUser().getEmail();
                String newemail=useremail.replace(".","");
                if(arrayList!=null && arrayList.size()>0)
                {
                    String date= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()) ;
                    String time=new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                    databaseReference_order=FirebaseDatabase.getInstance().getReference().child("order").child(newemail).child(date+time);
                    databaseReference_order.child("date").setValue(date);
                    databaseReference_order.child("time").setValue(time);
                    for(int i=0;i<arrayList.size();i++)
                    {
                        cartItemDetails cartItemDetails=arrayList.get(i);
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
                    arrayList.clear();

                }
                Intent intent=new Intent(getApplicationContext(),orderSuccess.class);
            startActivity(intent);


            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

}