package com.example.wedeliver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.storage.OnObbStateChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class itemDetailsAdapter extends RecyclerView.Adapter<itemDetailsAdapter.Viewholder>
{

    public Context context;
    public ArrayList<itemDetails> courseModelArrayList;
    FirebaseAuth firebaseAuth;


    // Constructor
    public itemDetailsAdapter(Context context, ArrayList<itemDetails> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public itemDetailsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_card, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemDetailsAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        itemDetails itemDetails = courseModelArrayList.get(position);
        //Context context = holder.product_image.getContext();
        Glide.with(context).load(itemDetails.getImageView()).into(holder.product_image);
        holder.discount.setText(itemDetails.getDiscount()+"% OFF");
        holder.actual_price.setText("Rs:"+itemDetails.getActual_price());
        holder.actual_price.setPaintFlags(holder.actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.selling_price.setText("₹ /-"+itemDetails.getSelling_price());
        holder.product_name.setText(itemDetails.getProduct_name());
        holder.product_brand.setText(itemDetails.getProduct_brand());
        final long[] maxitems = {Long.parseLong(itemDetails.getCapacity().toString())};
        final long[] booked_items_count = {Long.parseLong(itemDetails.getBooked_items_count().toString())};
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    displayAlertDialog(itemDetails,view);

            }
        });
    }

    public void displayAlertDialog(itemDetails itemDetails,View view1) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_cart,null);
        TextView name=view.findViewById(R.id.cart_product_name);
        TextView brand_name=view.findViewById(R.id.cart_brand_name);
        TextView discount1=view.findViewById(R.id.cart_discount);
        ImageView imageView=view.findViewById(R.id.cart_product_image);
        TextView sellingPrice=view.findViewById(R.id.cart_selling_price);
        TextView actualPrice=view.findViewById(R.id.cart_product_price);
        ImageView add=view.findViewById(R.id.cart_head);
        ImageView remove=view.findViewById(R.id.cart_remove);
        TextView quantity=view.findViewById(R.id.cart_product_qty);
        TextView continuebutton=view.findViewById(R.id.cart_add_to_cart);
        AlertDialog.Builder builder=new AlertDialog.Builder(view1.getContext());
        builder.setView(view);
        firebaseAuth=FirebaseAuth.getInstance();

        actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        name.setText(itemDetails.getProduct_name());
        brand_name.setText(itemDetails.getProduct_brand());
       Glide.with(view1.getContext()).load(itemDetails.getImageView()).into(imageView);

        String useremail=firebaseAuth.getCurrentUser().getEmail();
        String newUserEmail=useremail.replace(".","");

        long actual_price1=Long.parseLong(itemDetails.getActual_price());
        long discount2=Long.parseLong(itemDetails.getDiscount());
        long selling_price1=actual_price1-((actual_price1*discount2)/100);
        sellingPrice.setText("₹ /-"+selling_price1);
        actualPrice.setText(""+actual_price1);
        discount1.setText(itemDetails.getDiscount()+"%OFF");


        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("cart").child(""+newUserEmail).child(""+itemDetails.getProduct_name());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot x:snapshot.getChildren())
                {
                    if(x.getKey().equals("count"))
                    {
                        long val=Long.parseLong(x.getValue(String.class));
                        quantity.setText(""+val);
                    }
                    if(x.getKey().equals("selling_price"))
                    {
                        long val=Long.parseLong(x.getValue(String.class));
                        sellingPrice.setText("₹ /-"+val);
                    }
                    if(x.getKey().equals("actual_price"))
                    {
                        long val=Long.parseLong(x.getValue(String.class));
                        actualPrice.setText(""+val);
                    }

                }

               // Toast.makeText(context,""+current1[0],Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long current=Long.parseLong(quantity.getText().toString())+1;

                if(Long.parseLong(itemDetails.getNo_of_items().toString())>=current)
                {
                    quantity.setText("" +current);
                    sellingPrice.setText("₹ /-"+selling_price1*current);
                    actualPrice.setText(""+actual_price1*current);
                }
                else
                {
                    Toast.makeText(context,"out of stock",Toast.LENGTH_SHORT).show();
                }

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long current=Long.parseLong(quantity.getText().toString())-1;
                if(current>=1)
                {
                    quantity.setText("" +current);
                    sellingPrice.setText("₹ /-"+selling_price1*current);
                    actualPrice.setText(""+actual_price1*current);
                }

            }
        });
        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("")
//                Intent intent=new Intent(context,cart.class);
//                context.startActivity(intent);
                DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference();
                databaseReference1.child("cart").child(""+newUserEmail).child(""+itemDetails.getProduct_name()).child("count").setValue(quantity.getText().toString());
                databaseReference1.child("cart").child(""+newUserEmail).child(""+itemDetails.getProduct_name()).child("name").setValue(itemDetails.getProduct_name());
                databaseReference1.child("cart").child(""+newUserEmail).child(""+itemDetails.getProduct_name()).child("image").setValue(itemDetails.getImageView());
                databaseReference1.child("cart").child(""+newUserEmail).child(""+itemDetails.getProduct_name()).child("selling_price").setValue(sellingPrice.getText().toString().substring(4));
                databaseReference1.child("cart").child(""+newUserEmail).child(""+itemDetails.getProduct_name()).child("brand").setValue(itemDetails.getProduct_brand());
                databaseReference1.child("cart").child(""+newUserEmail).child(""+itemDetails.getProduct_name()).child("discount").setValue(itemDetails.getDiscount());
                databaseReference1.child("cart").child(""+newUserEmail).child(""+itemDetails.getProduct_name()).child("actual_price").setValue(actualPrice.getText().toString());
                databaseReference1.child("cart").child(""+newUserEmail).child(""+itemDetails.getProduct_name()).child("quantity").setValue(itemDetails.getNo_of_items());
                databaseReference1.child("cart").child(""+newUserEmail).child(""+itemDetails.getProduct_name()).child("basic_selling_price").setValue(itemDetails.getSelling_price());
                databaseReference1.child("cart").child(""+newUserEmail).child(""+itemDetails.getProduct_name()).child("basic_actual_price").setValue(itemDetails.getActual_price());
                Toast.makeText(context,"Item added into cart",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });



    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public ImageView product_image;
        public TextView product_name,product_brand,selling_price,actual_price,discount;
        public TextView add,counter;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            product_image=itemView.findViewById(R.id.product_img);
            product_name=itemView.findViewById(R.id.product_name);
            product_brand=itemView.findViewById(R.id.brand_name);
            selling_price=itemView.findViewById(R.id.selling_price);
            actual_price=itemView.findViewById(R.id.product_price);
            add=itemView.findViewById(R.id.product_add);
            discount=itemView.findViewById(R.id.discount);


        }
    }
}
