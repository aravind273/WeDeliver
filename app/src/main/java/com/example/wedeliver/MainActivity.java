    package com.example.wedeliver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;
    public class MainActivity extends AppCompatActivity {
    String url1 = "https://i.ibb.co/nk3kSBf/grocery1.jpg";
    String url2 = "https://i.ibb.co/5v5yP3r/grocery.jpg";
        String url3 = "https://i.ibb.co/j5Sxqjm/apple.jpg";
      Toolbar toolbar;
        SliderLayout sliderShow;
        RecyclerView recyclerView_grocery_items,recyclerView_laundry,recyclerView_rental;
        StorageReference storageReference;
        DatabaseReference databaseReference;
        FirebaseAuth firebaseAuth;
        DrawerLayout drawerLayout;
        ChipNavigationBar chipNavigationBar;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            toolbar=findViewById(R.id.toolbar1);
            toolbar.setTitle("WeDeliver");
            toolbar.setTitleTextColor(getColor(R.color.color_white));
             setSupportActionBar(toolbar);
            recyclerView_grocery_items=findViewById(R.id.recyclerview_grocery);
            recyclerView_laundry=findViewById(R.id.recyclerview_laundry);
            recyclerView_rental=findViewById(R.id.recyclerview_rental);
//            drawerLayout=findViewById(R.id.drawer_layout);
//          ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//            drawerLayout.addDrawerListener(toggle);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            toggle.syncState();

            firebaseAuth=FirebaseAuth.getInstance();
            ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
            SliderView sliderView = findViewById(R.id.slider);
            sliderDataArrayList.add(new SliderData(url1));
            sliderDataArrayList.add(new SliderData(url2));
            sliderDataArrayList.add(new SliderData(url3));
            SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);
            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            sliderView.setSliderAdapter(adapter);
            sliderView.setScrollTimeInSec(3);
            sliderView.setAutoCycle(true);
            sliderView.startAutoCycle();
            displayItemDetails();
            displayLaundryItems();
            displayRentalItems();
            chipNavigationBar=findViewById(R.id.chipnav);
            chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int id) {
                    switch(id)
                    {
                        case R.id.mycart:
                                Intent intent = new Intent(MainActivity.this,cart.class);
                                startActivity(intent);
                            break;
                        case R.id.myorders:
                            Intent intent1=new Intent(MainActivity.this,order.class);
                            startActivity(intent1);
                            break;
                        case R.id.myaccount:
                            Intent intent2=new Intent(MainActivity.this,user_account_details.class);
                            startActivity(intent2);
                            break;


                    }
                }
            });

        }


        public void displayItemDetails() {
            storageReference= FirebaseStorage.getInstance().getReference("Images");
            databaseReference=FirebaseDatabase.getInstance().getReference("Items");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<itemDetails> arrayList=new ArrayList<>();

                    for(DataSnapshot snapshot1:snapshot.getChildren())
                    {
                        itemDetails itemDetails=new itemDetails();

                        for(DataSnapshot snapshot2:snapshot1.getChildren()) {
                            String link="";
                            if (snapshot2.getKey().equals("link"))
                            {
                                itemDetails.setImageView(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("name"))
                            {
                                itemDetails.setProduct_name(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("brand_name"))
                            {
                                itemDetails.setProduct_brand(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("selling_price"))
                            {
                                itemDetails.setSelling_price(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("price"))
                            {
                                itemDetails.setActual_price(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("discount"))
                            {
                                itemDetails.setDiscount(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("no_of_items"))
                            {
                                itemDetails.setCapacity(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("booked_items_count"))
                            {
                                itemDetails.setBooked_items_count(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("no_of_items"))
                            {
                                itemDetails.setNo_of_items(snapshot2.getValue().toString());
                            }


                        }
                        arrayList.add(itemDetails);
                    }
                    itemDetailsAdapter adapter=new itemDetailsAdapter(getApplicationContext(),arrayList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView_grocery_items.setLayoutManager(linearLayoutManager);
                    recyclerView_grocery_items.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
        public void displayRentalItems() {
            storageReference= FirebaseStorage.getInstance().getReference("Rental");
            databaseReference=FirebaseDatabase.getInstance().getReference("Rental");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<RentalDetails> arrayList=new ArrayList<>();

                    for(DataSnapshot snapshot1:snapshot.getChildren())
                    {

                       RentalDetails rentalDetails=new RentalDetails();
                        for(DataSnapshot snapshot2:snapshot1.getChildren()) {
                            if(snapshot2.getKey().equals("name"))
                            {
                                rentalDetails.setName(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("price"))
                            {
                                rentalDetails.setPrice(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("services"))
                            {
                                rentalDetails.setServices(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("mobilenumber"))
                            {
                                rentalDetails.setMobilenumber(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("address"))
                            {
                                rentalDetails.setAddress(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("link"))
                            {
                                rentalDetails.setImageview(snapshot2.getValue().toString());
                            }

                        }
                        arrayList.add(rentalDetails);
                    }
                    RentalDetailsAdapter adapter=new RentalDetailsAdapter(getApplicationContext(),arrayList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView_rental.setLayoutManager(linearLayoutManager);
                    recyclerView_rental.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
        public void displayLaundryItems() {
            storageReference= FirebaseStorage.getInstance().getReference("Laundry");
            databaseReference=FirebaseDatabase.getInstance().getReference("Laundry");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<LaundryDetails> arrayList=new ArrayList<>();

                    for(DataSnapshot snapshot1:snapshot.getChildren())
                    {

                        LaundryDetails laundryDetails=new LaundryDetails();
                        for(DataSnapshot snapshot2:snapshot1.getChildren()) {
                            if(snapshot2.getKey().equals("name"))
                            {
                                laundryDetails.setName(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("price"))
                            {
                                laundryDetails.setPrice(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("services"))
                            {
                                laundryDetails.setServices(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("mobilenumber"))
                            {
                                laundryDetails.setMobilenumber(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("address"))
                            {
                                laundryDetails.setAddress(snapshot2.getValue().toString());
                            }
                            if(snapshot2.getKey().equals("link"))
                            {
                                laundryDetails.setImageview(snapshot2.getValue().toString());
                            }

                        }
                        arrayList.add(laundryDetails);
                    }
                    LaundryDetailsAdapter adapter=new LaundryDetailsAdapter(getApplicationContext(),arrayList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView_laundry.setLayoutManager(linearLayoutManager);
                    recyclerView_laundry.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }

        @Override
        public void onBackPressed() {
//            finish();

                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                  //super.onBackPressed();

        }

        public Context getcontext()
        {
            return getApplicationContext();
        }
    }