package com.example.wedeliver;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wedeliver.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.Viewholder>{
    ArrayList<ArrayList<String>> arrayList;
    Context context;
    public RequestAdapter(Context context, ArrayList<ArrayList<String>> arrayList)
    {
        this.context=context;
        this.arrayList=arrayList;

    }
    @NonNull
    @Override
    public RequestAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_requests_adapter, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.Viewholder holder, int position) {
        ArrayList<String> details=arrayList.get(position);
        if(details.get(0).equals("")) {
            holder.Date.setText(details.get(1));
            holder.problem.setText(details.get(2));
            holder.Time.setText(details.get(3));
            holder.solution.setText("");
        }
        else
        {
            holder.Date.setText(details.get(1));
            holder.problem.setText(details.get(2));
            holder.Time.setText(details.get(3));
            holder.solution.setText(details.get(0));

        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class Viewholder extends RecyclerView.ViewHolder {
        TextView Date,Time,solution,problem;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Date=itemView.findViewById(R.id.date);
            Time=itemView.findViewById(R.id.time);
            solution=itemView.findViewById(R.id.solution);
            problem=itemView.findViewById(R.id.problem);
        }
    }
}
