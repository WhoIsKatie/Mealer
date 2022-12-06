package com.uottawa.seg2105.group10.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawa.seg2105.group10.R;

import java.util.ArrayList;

public class Complaint_RecyclerViewAdapter extends RecyclerView.Adapter<Complaint_RecyclerViewAdapter.MyViewHolder>{

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<ComplaintModel> complaints;

    public Complaint_RecyclerViewAdapter(Context context, ArrayList<ComplaintModel> complaints, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.complaints = complaints;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public Complaint_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This method inflates the layout and gives a look to our rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.complaint_card_view_format, parent, false);
        return new Complaint_RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Complaint_RecyclerViewAdapter.MyViewHolder holder, int position) {
        // This method assigns values to our rows as they come back on the screen, given the position of the recycler view
        holder.nameOfUser.setText(complaints.get(position).getClientName());
        holder.timeOfComplaint.setText(complaints.get(position).getTimeOfComplaint());
    }

    @Override
    public int getItemCount() {
        // Returns the total number of items in the RecyclerView
        return complaints.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nameOfUser, raisedAComplaint, timeOfComplaint, view;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            nameOfUser = itemView.findViewById(R.id.compMealNameTextView);
            raisedAComplaint = itemView.findViewById(R.id.textView7);
            timeOfComplaint = itemView.findViewById(R.id.compClientNameTextView);
            view = itemView.findViewById(R.id.textView6);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }

            });
        }
    }
}

