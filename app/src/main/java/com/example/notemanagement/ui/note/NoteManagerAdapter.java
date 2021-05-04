package com.example.notemanagement.ui.note;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.DAO.CategoryDAO;
import com.example.notemanagement.DAO.NoteDAO;
import com.example.notemanagement.DAO.StatusDAO;
import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.Entity.Note;
import com.example.notemanagement.Entity.Priority;
import com.example.notemanagement.Entity.Status;
import com.example.notemanagement.R;
import com.example.notemanagement.RoomDB;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NoteManagerAdapter extends RecyclerView.Adapter<NoteManagerAdapter.NoteViewHolder> {

    private List<Note> lNote;
    List<Category> lCategory;
    List<Status> lStatus;

    public List<Category> getlCategory() {
        return lCategory;
    }

    public List<Status> getlStatus() {
        return lStatus;
    }

    public List<Priority> getlPriority() {
        return lPriority;
    }

    public List<Note> getlNote() {
        return lNote;
    }

    public Context context;

    public void setlNote(List<Note> lnote) {
        this.lNote = lnote;
    }

    public NoteManagerAdapter(List<Note> list, Context context) {

        setlNote(list);
        this.context = context;
    }

    public NoteManagerAdapter(ArrayList<Note> listNote, List<Category> lcategory, List<Status> lstatus, Context context) {

        this.lNote = listNote;
        this.lStatus = lstatus;
        this.lCategory = lcategory;
        this.context = context;
    }

    private List<Priority> lPriority;

    public NoteManagerAdapter(List<Note> listNote, List<Category> lcategory, List<Status> lstatus, List<Priority> lpriority, Context context) {

        this.lNote = listNote;
        this.lStatus = lstatus;
        this.lCategory = lcategory;
        this.context = context;
        this.lPriority = lpriority;
    }

    @NonNull
    @Override
    // Tạo viewholder cho class mới tạo
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_note, parent, false);
        return new NoteViewHolder(view, lNote, lCategory, lStatus, lPriority, context);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Note note = getlNote().get(position);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        for (int i = 0; i < lCategory.size(); i++) { // tên Category
            if (lCategory.get(i).getCategoryId() == note.getCategoryId()) {
                holder.tvCategory.setText(lCategory.get(i).getName());
                break;
            }
        }
        for (int i = 0; i < lStatus.size(); i++) { //tên Status
            if (lStatus.get(i).getStatusId() == note.getStatusId()) {
                holder.tvStatus.setText(lStatus.get(i).getName());
                break;
            }
        }

        for (int i = 0; i < lPriority.size(); i++) { //Tên Priority
            if (lPriority.get(i).getPriorityId() == note.getPriorityId()) {
                holder.tvPriority.setText(lPriority.get(i).getName());
                break;
            }
        }

        if (note.getCreateDate() != null) {
            holder.tvCreateDate.setText(dateFormat.format(note.getCreateDate()));
        }

        if (note.getPlanDate() != null) {
            holder.tvPlanDate.setText(dateFormat.format(note.getPlanDate()));
        }

        holder.tvNameNote.setText(note.getName());

    }

    @Override
    public int getItemCount() {

        return lNote.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


        private TextView tvNameNote, tvPriority, tvStatus, tvCategory, tvPlanDate, tvCreateDate;

        public TextView getTvNameNote() {
            return tvNameNote;
        }

        private List<Status> lStatus;
        private List<Category> lCategory;
        private List<Note> lNoteEdit;
        private Context context;
        private List<Priority> lPriority;

        public NoteViewHolder(@NonNull View itemView, List<Note> lNoteEdit, List<Category> lCategory, List<Status> lStatus, List<Priority> lPriority, Context context) {
            super(itemView);
            this.tvNameNote = (TextView) itemView.findViewById(R.id.tvNameNote);
            this.tvPriority = (TextView) itemView.findViewById(R.id.tvPriority);
            this.tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            this.tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            this.tvPlanDate = (TextView) itemView.findViewById(R.id.tvPlanDate);
            this.tvCreateDate = (TextView) itemView.findViewById(R.id.tvCreateDate);

            this.lNoteEdit = lNoteEdit;
            this.lPriority = lPriority;
            this.lCategory = lCategory;
            this.lStatus = lStatus;
            this.context = context;
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 1, 1, "Delete");
            menu.add(getAdapterPosition(), 2, 1, "Edit");
        }

        public TextView getTvPriority() {
            return tvPriority;
        }

        public TextView getTvStatus() {
            return tvStatus;
        }

        public TextView getTvCategory() {
            return tvCategory;
        }

        public TextView getTvPlanDate() {
            return tvPlanDate;
        }

        public TextView getTvCreateDate() {
            return tvCreateDate;
        }


    }

}