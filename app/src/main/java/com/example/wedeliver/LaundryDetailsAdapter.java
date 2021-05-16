package com.example.wedeliver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LaundryDetailsAdapter extends RecyclerView.Adapter<LaundryDetailsAdapter.Viewholder>
{

    public Context context;
    public ArrayList<LaundryDetails> laundryDetailsArrayList;


    // Constructor
    public LaundryDetailsAdapter(Context context, ArrayList<LaundryDetails> laundryDetailsArrayList) {
        this.context = context;
        this.laundryDetailsArrayList = laundryDetailsArrayList;
    }

    @NonNull
    @Override
    public LaundryDetailsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.laundry_display_card, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position)
    {
        LaundryDetails laundryDetails=laundryDetailsArrayList.get(position);
        Glide.with(context).load(laundryDetails.getImageview()).into(holder.imageView);
        holder.name.setText(laundryDetails.getName().toUpperCase());
        holder.price.setText("₹ /-"+laundryDetails.getPrice()+" Per month");
        holder.services.setText(laundryDetails.getServices().toUpperCase());
        holder.address.setText(laundryDetails.getAddress());
        holder.mobilenumber.setText(laundryDetails.getMobilenumber());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDailpad(laundryDetails.getMobilenumber());

            }
        });
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSupportChat(laundryDetails.getMobilenumber(),laundryDetails.getName());

            }
        });


    }



    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return laundryDetailsArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView name,price,address,mobilenumber,services;
        ImageView imageView,call,whatsapp;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.laundry_person_name);
            price=itemView.findViewById(R.id.laundry_price);
            services=itemView.findViewById(R.id.laundry_service);
            address=itemView.findViewById(R.id.laundry_address);
            mobilenumber=itemView.findViewById(R.id.laundry_number);
            imageView=itemView.findViewById(R.id.laundry_person_image);
            call=itemView.findViewById(R.id.call);
            whatsapp=itemView.findViewById(R.id.whatsapp);


        }
    }
    private void startSupportChat(String number,String name) {

        try {
            String headerReceiver = "";// Replace with your message.
            String bodyMessageFormal = "";// Replace with your message.
            String whatsappContain = headerReceiver + bodyMessageFormal;
            String trimToNumner = "+91"+number; //10 digit number
            Intent intent = new Intent ( Intent.ACTION_VIEW );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData ( Uri.parse ( "https://wa.me/" + trimToNumner + "/?text=" + "Hi "+name+" I want to book your service" ) );
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace ();
        }

    }
    private void openDailpad(String number)
    {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+number));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
