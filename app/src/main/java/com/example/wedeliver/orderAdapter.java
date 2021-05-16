package com.example.wedeliver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.Viewholder>
{

    public Context context;
    public ArrayList<orderDetails> orderDetailsArrayList;
    FirebaseAuth firebaseAuth;


    // Constructor
    public orderAdapter(Context context, ArrayList<orderDetails> courseModelArrayList) {
        this.context = context;
        this.orderDetailsArrayList= courseModelArrayList;
    }

    @NonNull
    @Override
    public orderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        orderDetails orderDetails=orderDetailsArrayList.get(position);
        holder.orderId.setText(""+orderDetails.getOrderId());
        holder.date.setText(""+orderDetails.getDate());
        holder.time.setText(""+orderDetails.getTime());
        holder.payable.setText("₹"+orderDetails.getTotal_amount());
        holder.savings.setText("₹"+orderDetails.getSaved_amount());
        holder.status.setText("cash on delivery");
        holder.orderDetails.setText(""+orderDetails.getOrder_Details());
        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }



    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return orderDetailsArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView date,time,orderId,status,payable,savings,pay,orderDetails;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.order_date);
            time=itemView.findViewById(R.id.order_time);
            orderId=itemView.findViewById(R.id.orderId);
            status=itemView.findViewById(R.id.status);
            payable=itemView.findViewById(R.id.payableAmount);
            savings=itemView.findViewById(R.id.total_savings);
            orderDetails=itemView.findViewById(R.id.orderDetails);
            pay=itemView.findViewById(R.id.pay);

        }
    }
}
