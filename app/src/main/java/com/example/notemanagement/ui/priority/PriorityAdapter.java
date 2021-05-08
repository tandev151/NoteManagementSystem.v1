package com.example.notemanagement.ui.priority;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.Entity.Priority;
import com.example.notemanagement.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.example.notemanagement.Utils.CONSTANT.DELETE_CODE;
import static com.example.notemanagement.Utils.CONSTANT.UPDATE_CODE;

public class PriorityAdapter extends RecyclerView.Adapter<PriorityAdapter.ViewHolder> {
    private List<Priority> priorityList;
    private Context context;

    public PriorityAdapter(List<Priority> priorityList, Context context) {
        this.priorityList = priorityList;
        this.context = context;
        notifyDataSetChanged();
    }

    public void setAdapter(List<Priority> listPriority) {
        this.priorityList = listPriority;
    }

    public PriorityAdapter(Context context) {
        this.context = context;
    }

    public List<Priority> getList() {
        return priorityList;
    }

    /*
   class holds structure of view
    */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView tvName;
        public TextView tvCreateDate;
        public Context context;
        public List<Priority> priorityList;

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), UPDATE_CODE, 1, "Edit");
            menu.add(getAdapterPosition(), DELETE_CODE, 1, "Delete");

        }

        public ViewHolder(View view, List<Priority> listPriority, Context context) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvName);
            tvCreateDate = (TextView) view.findViewById(R.id.tvCreateDate);
            this.context = context;
            this.priorityList = listPriority;

            view.setOnCreateContextMenuListener(this);
        }
    }
    /*
    Class is used to set layout for view holder
     */
    @NonNull
    @Override
    public PriorityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View statusView =
                inflater.inflate(R.layout.status_item, parent, false);

        PriorityAdapter.ViewHolder viewHolder = new PriorityAdapter.ViewHolder(statusView, priorityList, context);
        return viewHolder;
    }

    /*
    class is used to assign data for view holder
     */
    @Override
    public void onBindViewHolder(PriorityAdapter.ViewHolder holder, int position) {
        Priority category = priorityList.get(position);

        holder.tvName.setText(category.getName());

        //get date and format it
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.tvCreateDate.setText(dateFormat.format(category.getCreateDate()));
    }

    @Override
    public int getItemCount() {
        if (priorityList != null)
            return priorityList.size();
        return 0;
    }
}
