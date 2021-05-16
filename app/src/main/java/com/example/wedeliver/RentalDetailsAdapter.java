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

public class RentalDetailsAdapter extends RecyclerView.Adapter<RentalDetailsAdapter.Viewholder>
{

    public Context context;
    public ArrayList<RentalDetails> rentalDetailsArrayList;


    // Constructor
    public RentalDetailsAdapter(Context context, ArrayList<RentalDetails> rentalDetailsArrayList) {
        this.context = context;
        this.rentalDetailsArrayList=rentalDetailsArrayList;
    }

    @NonNull
    @Override
    public RentalDetailsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rental_display_card, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position)
    {
        RentalDetails rentalDetails=rentalDetailsArrayList.get(position);
        Glide.with(context).load(rentalDetails.getImageview()).into(holder.imageView);
        holder.name.setText(rentalDetails.getName().toUpperCase());
        holder.price.setText("₹ /-"+rentalDetails.getPrice()+" Per day");
        holder.services.setText(rentalDetails.getServices().toUpperCase());
        holder.address.setText(rentalDetails.getAddress());
        holder.mobilenumber.setText(rentalDetails.getMobilenumber());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDailpad(rentalDetails.getMobilenumber());

            }
        });
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSupportChat(rentalDetails.getMobilenumber(),rentalDetails.getName());

            }
        });


    }



    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return rentalDetailsArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView name,price,address,mobilenumber,services;
        ImageView imageView,call,whatsapp;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.rental_person_name);
            price=itemView.findViewById(R.id.rental_price);
            services=itemView.findViewById(R.id.rental_service);
            address=itemView.findViewById(R.id.rental_address);
            mobilenumber=itemView.findViewById(R.id.rental_number);
            imageView=itemView.findViewById(R.id.rental_person_image);
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
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("tel:"+number));

            context.startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
