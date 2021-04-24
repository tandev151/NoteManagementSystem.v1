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

public class NoteManagerAdapter extends RecyclerView.Adapter<NoteManagerAdapter.NoteViewHolder>  {



    //private static ClickListener clickListener;

    private ArrayList<Note> lNote;
    List<Category> lCategory;
    List<Status> lStatus;

    public ArrayList<Note> getlNote() {
        return lNote;
    }
    public Context context;
    public void setlNote(ArrayList<Note> lnote){
        this.lNote = lnote;
    }

    public NoteManagerAdapter(ArrayList<Note> list, Context context){

        setlNote(list);
        this.context = context;
    }

    public NoteManagerAdapter(ArrayList<Note> listNote, List<Category> lcategory, List<Status> lstatus, Context context){

        this.lNote = listNote;
        this.lStatus = lstatus;
        this.lCategory = lcategory;
        this.context = context;
    }

    private List<Priority> lPriority;

    public NoteManagerAdapter(ArrayList<Note> listNote, List<Category> lcategory, List<Status> lstatus, List<Priority> lpriority, Context context){

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_note,parent,false);
        return new NoteViewHolder(view, lNote, lCategory, lStatus,lPriority, context);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        RoomDB noteDB = RoomDB.getDatabase(context);
        NoteDAO noteDAO = noteDB.noteDAO();

        noteDB.databaseWriteExecutor.execute(() ->{

            try{ String categoryName = noteDAO.getCategory(getlNote().get(position).getCategoryId()).getName();
                holder.getTvCategory().setText(categoryName);
            }catch (Exception e){}

            try{  String statusName = noteDAO.getNameStatusById(getlNote().get(position).getStatusId());
                holder.getTvStatus().setText(statusName);
            }catch (Exception e){}

            try{  String plandate = lNote.get(position).getPlanDate().toString();
                holder.getTvPlanDate().setText(plandate);
            }catch (Exception e){}
        });


        String priorityName;

        holder.getTvNameNote().setText(getlNote().get(position).getName());


    }


    @Override
    public int getItemCount() {

        return lNote.size();
    }



    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener {


        private TextView tvNameNote, tvPriority, tvStatus, tvCategory, tvPlanDate, tvCreateDate;

        public NoteViewHolder(View itemView, ArrayList<Note> lNote, List<Category> lCategory, List<Status> lStatus, Context context) {
            super(itemView);
            this.tvNameNote = (TextView) itemView.findViewById(R.id.tvNameNote);
            this.tvPriority = (TextView) itemView.findViewById(R.id.tvPriority);
            this.tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            this.tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            this.tvPlanDate = (TextView) itemView.findViewById(R.id.tvPlanDate);
            this.tvCreateDate = (TextView) itemView.findViewById(R.id.tvCreateDate);

            this.lNoteEdit = lNote;
            this.lCategory = lCategory;
            this.lStatus = lStatus;
            this.context = context;
            itemView.setOnClickListener(this);
        }


        public TextView getTvNameNote() {
            return tvNameNote;
        }
        private List<Status> lStatus;
        private List<Category> lCategory;
        private ArrayList<Note> lNoteEdit;
        private Context context;
        EditText edtNameNoteEdit;
        Spinner spCategoryEdit, spStatusEdit, spPriorityEdit;
        Button btnPlanDate;
        TextView tvPlanDateNew;

        public NoteViewHolder(@NonNull View itemView, ArrayList<Note> lNoteEdit, List<Category> lCategory, List<Status> lStatus, List<Priority> lPriority, Context context) {
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
            itemView.setOnClickListener(this);
        }

        List<Priority> lPriority;


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

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        private void showPopupMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.popup_menu_note);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_popup_edit_note: {
                    callDialogEditNote(getAdapterPosition());

                    return true;}
                case R.id.action_popup_delect_note:
                    getDeleteNote();
                    return true;

                default:
                    return false;
            }

        }

        private void getDeleteNote(){

            Note note = lNoteEdit.get(getAdapterPosition());
            RoomDB noteDatabase = RoomDB.getDatabase(context);
            NoteDAO noteDAO = noteDatabase.noteDAO();
            noteDatabase.databaseWriteExecutor.execute(() ->{
                noteDAO.deleteNote(note);
            });
        }

        private void callDialogEditNote(int index)
        {
            View view;
            Note note = lNoteEdit.get(index);

            Date date = null;
            // NoteFragment  note_activity = (NoteFragment)context;
            Context contextNew = new ContextThemeWrapper(context, R.style.AppTheme);
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(contextNew);
            view = LayoutInflater.from(this.context).inflate(R.layout.create_new_note, null,false);
            builder.setView(view);
            tvPlanDateNew = view.findViewById(R.id.tvPlanDateNew);
            // ánh xạ
            edtNameNoteEdit = (EditText) view.findViewById(R.id.edtNameNote);
            spCategoryEdit = (Spinner) view.findViewById(R.id.spCategory);
            spPriorityEdit = (Spinner)view.findViewById(R.id.spPriority);
            spStatusEdit = (Spinner)view.findViewById(R.id.spStatus);
            loadSpinner(index);

            if(note.getPlanDate() != null){
                tvPlanDateNew.setText(note.getPlanDate().toString());
            }

            edtNameNoteEdit.setText(note.getName());
            btnPlanDate= view.findViewById(R.id.btnPlanDate);

            try {
                tvPlanDateNew.setText(note.getPlanDate().toString());
            }catch (Exception e){}

            btnPlanDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDate(tvPlanDateNew, date);
                }
            });
            // thực hiện edit

            builder.setPositiveButton("Update", (dialog, which) ->{
                try {
                    note.setName(edtNameNoteEdit.getText().toString());
                    note.setCategoryId(((Category) spCategoryEdit.getSelectedItem()).getCategoryId());
                    note.setStatusId(((Status) spStatusEdit.getSelectedItem()).getStatusId());
                    note.setPlanDate(date);

                    RoomDB noteDB = RoomDB.getDatabase(context);
                    NoteDAO noteDAO = noteDB.noteDAO();

                    noteDB.databaseWriteExecutor.execute(() ->{
                        noteDAO.update(note);
                    });

                    dialog.dismiss();
                }catch (Exception e){}

            })
                    .setNegativeButton("Close",(dialog, which) ->
                    {
                        // displayMessage("Operation cancel !");
                        dialog.dismiss();
                    })
                    .show();


        }

        public void setDate(TextView tvPlandate, Date planDate)
        {

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            Calendar cal = Calendar.getInstance();

            //if the date is selected
            try {
                if (!tvPlandate.getText().toString().isEmpty()) {
                    SimpleDateFormat simpFormat = new SimpleDateFormat("E, MMM dd yyyy");
                    //Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(simpFormat.parse(tvPlandate.getText().toString()));
                        year = (cal.get(Calendar.YEAR));
                        month = cal.get(Calendar.MONTH);
                        day = cal.get(Calendar.DAY_OF_MONTH);
                    } catch (Exception e) {
                    }
                }
            }catch (Exception e){}

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day)
                {

                    //Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR,year);
                    cal.set(Calendar.DAY_OF_MONTH,day);
                    cal.set(Calendar.MONTH,month);
                    DateFormat formater= new SimpleDateFormat("E, MMM dd yyyy");

                    tvPlandate.setText(formater.format(cal.getTime()));
                }
            }, year, month, day);
            planDate = cal.getTime();

            datePickerDialog.show();
        }


        public  void loadSpinner(int index){
            RoomDB noteDB = RoomDB.getDatabase(context);
            StatusDAO statusDAO = noteDB.statusDAO();
            CategoryDAO categoryDAO = noteDB.categoryDAO();
            int temp = 0;
            int tempS = 0;

            ArrayAdapter<Status> adapterStatus = new ArrayAdapter<Status>(context, android.R.layout.simple_spinner_item, new ArrayList<Status>(lStatus));
            spStatusEdit.setAdapter(adapterStatus);
            ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(context, android.R.layout.simple_spinner_item, new ArrayList<Category>(lCategory));
            spCategoryEdit.setAdapter(adapter);
            ArrayAdapter<Priority> adapterP = new ArrayAdapter<Priority>(context, android.R.layout.simple_spinner_item, new ArrayList<Priority>(lPriority));
            spPriorityEdit.setAdapter(adapterP);
            for(int i = 0; i < lCategory.size(); i++)
                if(lCategory.get(i).getCategoryId() == lNoteEdit.get(index).getCategoryId())
                {
                    temp = i;
                    break;
                }

            spCategoryEdit.setSelection(temp);

            for(int i = 0; i < lStatus.size(); i++)
                if(lStatus.get(i).getStatusId() == lNoteEdit.get(index).getStatusId())
                {
                    tempS = i;
                    break;
                }

            spStatusEdit.setSelection(tempS);

                int tempP = 0;
            for(int i = 0; i < lPriority.size(); i++)
            if(lPriority.get(i).getPriorityId() == lNoteEdit.get(index).getPriorityId())
            {
                tempP = i;
                break;
            }

            spPriorityEdit.setSelection(tempP);
            spStatusEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Status status = (Status) parent.getSelectedItem();

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            spCategoryEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Category user = (Category) parent.getSelectedItem();

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });




        }
    }

}