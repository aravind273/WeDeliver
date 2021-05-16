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
import com.google.firebase.auth.FirebaseUser;
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

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.Viewholder>
{

    public Context context;
    public ArrayList<cartItemDetails> courseModelArrayList;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    // Constructor
    public cartAdapter(Context context, ArrayList<cartItemDetails> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public cartAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_display_card, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
//        View view=LayoutInflater.from(context).inflate(R.layout.cart_display_card,null);
//        TextView name=view.findViewById(R.id.cart1_product_name);
//        TextView brand_name=view.findViewById(R.id.cart1_brand_name);
//        TextView discount1=view.findViewById(R.id.cart1_discount);
//        ImageView imageView=view.findViewById(R.id.cart1_product_image);
//        TextView sellingPrice=view.findViewById(R.id.cart1_selling_price);
//        TextView actualPrice=view.findViewById(R.id.cart1_product_price);
//        ImageView add=view.findViewById(R.id.cart1_head);
//        ImageView remove=view.findViewById(R.id.cart1_remove);
//        TextView quantity=view.findViewById(R.id.cart1_product_qty);
        cartItemDetails cartItemDetailsarray=courseModelArrayList.get(position);
        Glide.with(context).load(cartItemDetailsarray.getImage()).into(holder.product_image);
        holder.discount.setText(cartItemDetailsarray.getDicount()+"% OFF");
        holder.actual_price.setPaintFlags(holder.actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.product_name.setText(cartItemDetailsarray.getName());
        holder.product_brand.setText(cartItemDetailsarray.getBrand());
        firebaseAuth=FirebaseAuth.getInstance();


        String useremail=firebaseAuth.getCurrentUser().getEmail();
        String newUserEmail=useremail.replace(".","");

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("cart").child(""+newUserEmail).child(""+cartItemDetailsarray.getName());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot x:snapshot.getChildren())
                {
                    if(x.getKey().equals("count"))
                    {
                       long val = Long.parseLong(x.getValue(String.class));
                       // Toast.makeText(context,""+val1[0],Toast.LENGTH_SHORT).show();
                        holder.count.setText(""+val);
                    }
                    if(x.getKey().equals("selling_price"))
                    {
                        long val=Long.parseLong(x.getValue(String.class));
                        holder.selling_price.setText("₹ /-"+val);
                    }
                    if(x.getKey().equals("actual_price"))
                    {
                        long val=Long.parseLong(x.getValue(String.class));
                        holder.actual_price.setText(""+val);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       // Toast.makeText(context,""+asp+" "+aap+" "+cartItemDetailsarray.getSelling_price()+" "+cartItemDetailsarray.getActual_price()+" "+holder.count.getText().toString(),Toast.LENGTH_SHORT).show();
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long current=Long.parseLong(holder.count.getText().toString())+1;
                if(Long.parseLong(cartItemDetailsarray.getQuantity().toString())>=current)
                {
                    holder.count.setText("" +current);
                    holder.selling_price.setText("₹ /-"+((Long.parseLong((holder.selling_price.getText().toString().length()>3)?holder.selling_price.getText().toString().substring(4):holder.selling_price.getText().toString()))+Long.parseLong(cartItemDetailsarray.getBasic_selling_price())));
                    holder.actual_price.setText(""+((Long.parseLong(holder.actual_price.getText().toString()))+Long.parseLong(cartItemDetailsarray.getBasic_actual_price())));
                    DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("cart").child(""+newUserEmail).child(""+cartItemDetailsarray.getName());
                    databaseReference1.child("selling_price").setValue(holder.selling_price.getText().toString().substring(4));
                    databaseReference1.child("actual_price").setValue(holder.actual_price.getText().toString());
                    databaseReference1.child("count").setValue(""+current);

                }
                else
                {
                    Toast.makeText(context,"out of stock",Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long current=Long.parseLong(holder.count.getText().toString())-1;
                if(current>=1)
                {
                    holder.count.setText("" +current);
                    holder.selling_price.setText("₹ /-"+((Long.parseLong((holder.selling_price.getText().toString().length()>3)?holder.selling_price.getText().toString().substring(4):holder.selling_price.getText().toString()))-Long.parseLong(cartItemDetailsarray.getBasic_selling_price())));
                    holder.actual_price.setText(""+((Long.parseLong(holder.actual_price.getText().toString()))-Long.parseLong(cartItemDetailsarray.getBasic_actual_price())));
                    DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("cart").child(""+newUserEmail).child(""+cartItemDetailsarray.getName());
                    databaseReference1.child("selling_price").setValue(holder.selling_price.getText().toString().substring(4));
                    databaseReference1.child("actual_price").setValue(holder.actual_price.getText().toString());
                    databaseReference1.child("count").setValue(""+current);


                }
                else
                {
                    courseModelArrayList.remove(position);
                    notifyDataSetChanged();
                    DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("cart").child(""+newUserEmail).child(""+cartItemDetailsarray.getName());
                    databaseReference1.removeValue();
                }
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
        public TextView product_name,product_brand,selling_price,actual_price,discount,count;
        public  ImageView add,sub;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            product_image=itemView.findViewById(R.id.cart1_product_image);
            product_name=itemView.findViewById(R.id.cart1_product_name);
            product_brand=itemView.findViewById(R.id.cart1_brand_name);
            selling_price=itemView.findViewById(R.id.cart1_selling_price);
            actual_price=itemView.findViewById(R.id.cart1_product_price);
            count=itemView.findViewById(R.id.cart1_product_qty);
            add=itemView.findViewById(R.id.cart1_add);
            sub=itemView.findViewById(R.id.cart1_remove);
            discount=itemView.findViewById(R.id.cart1_discount);



        }
    }
}
