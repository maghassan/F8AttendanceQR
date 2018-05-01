package org.enovatelab.f8attendance;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{

    public List<DataModel> AttendeesList;
    Context context;
    private Uri ProductImageUri = null;

    public DataAdapter(List<DataModel> AttendeesList){

        this.AttendeesList = AttendeesList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_view_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.attendeeCode.setText(AttendeesList.get(position).getAttendeeCode());

    }

    @Override
    public int getItemCount() {
        return AttendeesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TextView attendeeCode;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            attendeeCode = mView.findViewById(R.id.AttendeeCode);
        }
    }

}
