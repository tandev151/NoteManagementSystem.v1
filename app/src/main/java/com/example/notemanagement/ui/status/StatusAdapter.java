package com.example.notemanagement.ui.status;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.Entity.Status;
import com.example.notemanagement.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {
    private List<Status> statusList;
    private Context context;

    public StatusAdapter(List<Status> statusList, Context context) {
        this.statusList = statusList;
        this.context = context;
        notifyDataSetChanged();
    }

    public void setAdapter(List<Status> listStatus) {
        this.statusList = listStatus;
    }

    public StatusAdapter(Context context) {
        this.context = context;
    }

    public List<Status> getList() {
        return statusList;
    }


    /*
   class holds structure of view
    */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView tvName;
        public TextView tvCraeteDate;
        public Context context;
        public List<Status> statusList;

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 1, 1, "Delete");
            menu.add(getAdapterPosition(), 2, 1, "Edit");
        }


        public ViewHolder(View view, List<Status> statusList, Context context) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvName);
            tvCraeteDate = (TextView) view.findViewById(R.id.tvCreateDate);
            this.context = context;
            this.statusList = statusList;

            view.setOnCreateContextMenuListener(this);
        }
    }

    /*
    Class is used to set layout for view holder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Nạp layout cho View biểu diễn phần tử sinh viên
        View statusView =
                inflater.inflate(R.layout.status_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(statusView, statusList, context);
        return viewHolder;
    }

    /*
    class is used to assign data for view holder
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Status status = statusList.get(position);

        holder.tvName.setText(status.getName());

        //get date and format it
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.tvCraeteDate.setText(dateFormat.format(status.getCreateDate()));
    }

    @Override
    public int getItemCount() {
        if (statusList != null)
            return statusList.size();
        return 0;
    }
}
