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

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder>  {
    private List<Status> statusList;
    private Context context;

    public StatusAdapter(List<Status> statusList, Context context) {
        this.statusList = statusList;
        this.context = context;
        notifyDataSetChanged();
    }
    public  void setAdapter(List<Status> listStatus){
        this.statusList=listStatus;
    }
    public StatusAdapter(Context context){
        this.context=context;
    }
    public List<Status> getList(){ return  statusList;}


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
            menu.add(getAdapterPosition(),1,1,"Delete");
            menu.add(getAdapterPosition(),2,1,"Edit");
        }


        public ViewHolder(View view, List<Status>statusList, Context context) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvName);
            tvCraeteDate = (TextView) view.findViewById(R.id.tvCreateDate);
            this.context = context;
            this.statusList = statusList;

            view.setOnCreateContextMenuListener(this);



//            view.setOnCreateContextMenuListener( this);
            //when clicking an status item
//            view.setOnClickListener((View v) -> {
//                clickStatusItem(v);
//            });
            //process other actions

        }

        /*
        func is to process clicking an status item
         */
//        public void clickStatusItem(View v) {
//            PopupMenu menu = new PopupMenu(v.getContext(), v);
//            menu.inflate(R.menu.status_menu);
//
//            //when click menu
//            menu.setOnMenuItemClickListener(this);
//            menu.show();
//        }

//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.popup_status_edit:
//                    clickStatusEdit(getAdapterPosition());
//                    return true;
//                case R.id.popup_status_delete:
//                    clickStatusDelete(getAdapterPosition());
//                    return true;
//                default:
//                    return false;
//            }
//        }

        /*
        func is to handle clicking event of edit item
         */
//        void clickStatusEdit(int itemPostion)
//        {
////            StatusActivity statusActivity = (StatusActivity)context;
////            AlertDialog.Builder builder = new AlertDialog.Builder(statusActivity);
////            ViewGroup viewGroup = (ViewGroup)itemView.findViewById(android.R.id.content);
////            View dialogView = LayoutInflater.from(this.context).inflate(R.layout.edit_status_custom_dialog, viewGroup, false);
////            builder.setView(dialogView);
////            AlertDialog alertDialog = builder.create();
////            alertDialog.setCancelable(false);
////            alertDialog.show();
////
////            //click close button
////            Button btnClose =(Button)dialogView.findViewById(R.id.btnClose);
////            btnClose.setOnClickListener((View view)->{
////                alertDialog.dismiss();
////            });
////
////            //show old name in text view
////            TextView tvOldStatusName = (TextView)alertDialog.findViewById(R.id.tvOldStatusName);
////            tvOldStatusName.setText(this.statusList.get(itemPostion).getName());
////
////            TextView tvNewStatusName = (TextView)alertDialog.findViewById(R.id.tvNewStatusName);
////
////            Button btnAdd = (Button)dialogView.findViewById(R.id.btnEdit);
////            btnAdd.setOnClickListener((View addView)->{
////
////                if(tvNewStatusName.getText().toString().trim().isEmpty()){
////                    tvNewStatusName.setError("Please enter the status' name!");
////                    return;
////                }
////
////                NoteRoomDatabase db = NoteRoomDatabase.getDatabase(context);
////                db.databaseWriteExecutor.execute(()->{
////                    Status status = this.statusList.get(itemPostion);
////                    status.setName(tvNewStatusName.getText().toString().trim());
////                    db.statusDAO().update(status);
////
////                    //load data again
////                    ((StatusActivity)context).loadstatus();
////                });
////
////                alertDialog.dismiss();
////            });
//        }
//
//        /*
//        func is to handle clicking event of delete item
//         */
//        void clickStatusDelete(int itemPosition)
//        {
//
//        }
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        holder.tvCraeteDate.setText(dateFormat.format(status.getCreateDate()));
    }

    @Override
    public int getItemCount() {
        if(statusList!=null)
            return statusList.size();
        return 0;
    }
}
