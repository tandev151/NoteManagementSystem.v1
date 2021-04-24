package com.example.notemanagement.ui.note;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.RenderScript;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.notemanagement.DAO.AccountDAO;
import com.example.notemanagement.DAO.CategoryDAO;
import com.example.notemanagement.DAO.NoteDAO;
import com.example.notemanagement.DAO.PriorityDAO;
import com.example.notemanagement.DAO.StatusDAO;
import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.Entity.Note;
import com.example.notemanagement.Entity.Priority;
import com.example.notemanagement.Entity.Status;
import com.example.notemanagement.NoteManagementActivity;
import com.example.notemanagement.R;
import com.example.notemanagement.RoomDB;
import com.example.notemanagement.SignInActivity;
import com.example.notemanagement.userstore.UserLocalStore;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NoteFragment extends Fragment {

    private FloatingActionButton btnCreateNote;
    private View vCreateNewNote;
    private RecyclerView rvListNote;
    private ArrayList<Note> lNote;
    private NoteManagerAdapter noteManagerAdapter;
    private Button btnPlanDate;
    private Spinner spCategory, spStatus, spPriority;
    private TextView tvPlanDateNew;
    private List<Status> lStatus;
    private List<Category> lCategory;
    private List<Priority> lPriority;
    private Button btnAddNote, btnCloseCreate;
    private EditText edtNameNote;
    private Context context;
    private UserLocalStore userLocalStore;
    private Account currentAcc;

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);
        rvListNote = root.findViewById(R.id.rvListNote);
        btnCreateNote = root.findViewById(R.id.btnCreateNote);
        context=root.getContext();
        userLocalStore= new UserLocalStore(context);

        btnCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewNote();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createRecyclerView();
        // TODO: Use the ViewModel
    }

    public void createRecyclerView(){

        lNote = new ArrayList<Note>();
        currentAcc = new Account();

        if (userLocalStore.getLoginUser()!=null)
        {
            currentAcc= userLocalStore.getLoginUser();
            loadListNote(currentAcc.getID());
        }

    }

    public void loadListNote(int idUser) {
//        Note note = new Note();
//        note.setCategoryId(4);
//        note.setName("new note");
//        note.setCreateDate(null);
//        note.setPlanDate(null);
//        note.setPriorityId(1);
//        note.setStatusId(1);
//        note.setUserId(3);


        RoomDB noteDB = RoomDB.getDatabase(this.requireContext());
        NoteDAO noteDAO = noteDB.noteDAO();
        StatusDAO statusDAO = noteDB.statusDAO();
        CategoryDAO categoryDAO = noteDB.categoryDAO();
        PriorityDAO priorityDAO = noteDB.priorityDAO();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvListNote.setLayoutManager(linearLayoutManager);

        noteDB.databaseWriteExecutor.execute(()->{
                   // noteDAO.createNewNote(note);
                    lNote = new ArrayList<Note>(noteDAO.getAll(idUser));
                    lStatus = statusDAO.getAllList();
                    lCategory = categoryDAO.getAll();
                    lPriority = priorityDAO.getAllList();
                    //noteManagerAdapter = new NoteManagerAdapter(lNote,lCategory,lStatus,requireContext());
            noteManagerAdapter = new NoteManagerAdapter(lNote,lCategory,lStatus,lPriority,requireContext());
                    //noteManagerAdapter.setlNote(lNote);
                    rvListNote.setAdapter(noteManagerAdapter);
                }

        );


    }


    public void createNewNote(){

        int categoryId = 0, statusId = 0, priorityId;
        Date planDate = null;
        Context context = new ContextThemeWrapper(getContext(), R.style.AppTheme);
        MaterialAlertDialogBuilder buider = new MaterialAlertDialogBuilder(context);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        vCreateNewNote = layoutInflater.inflate(R.layout.create_new_note,null,false);
        tvPlanDateNew = vCreateNewNote.findViewById(R.id.tvPlanDateNew);
        buider.setView(vCreateNewNote);
        loadListStatus();

        // Đổ dữ liệu Category
        // setSpCategory();
        try{
            categoryId =((Category)spCategory.getSelectedItem()).getCategoryId();}catch (Exception e){}
        // Kết thúc đổ dữ liệu vào Category

        // Đổ dữ liệu Status
        //setSpStatus();
        try{
            statusId =((Status)spStatus.getSelectedItem()).getStatusId();}catch (Exception e){}

        setBtnPlanDate(planDate);
        edtNameNote = vCreateNewNote.findViewById(R.id.edtNameNote);
        buider.setCancelable(false);

        //End Button
        buider.setPositiveButton("Add", (dialog, which) ->{

            Note note = new Note();
            try{
                note.setCategoryId(((Category)spCategory.getSelectedItem()).getCategoryId());}catch (Exception e){}
            // Kết thúc đổ dữ liệu vào Category

            // Đổ dữ liệu Status
            //setSpStatus();
            try{
                note.setStatusId(((Status)spStatus.getSelectedItem()).getStatusId());}catch (Exception e){}

            try{
                note.setPriorityId(((Priority)spPriority.getSelectedItem()).getPriorityId());}catch (Exception e){}

            note.setName(edtNameNote.getText().toString());
            note.setCreateDate(Calendar.getInstance().getTime());
            note.setPlanDate(planDate);
            note.setUserId(currentAcc.getID());//B Biến tạm
            try{ RoomDB noteDB = RoomDB.getDatabase(requireContext());
                NoteDAO noteDAO = noteDB.noteDAO();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        noteDAO.createNewNote(note);
                        dialog.dismiss();
                    }
                }).start();

                dialog.dismiss();}catch (Exception e){}

        })
                .setNegativeButton("Close",(dialog, which) ->
                {
                    // displayMessage("Operation cancel !");
                    dialog.dismiss();
                });


        buider.show();
    }


    private void  setBtnPlanDate(Date planDate){
        btnPlanDate = vCreateNewNote.findViewById(R.id.btnPlanDate);
        btnPlanDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(tvPlanDateNew, planDate);
            }
        });
    }

    public void setDate(TextView tvPlandate, Date planDate)
    {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Calendar cal = Calendar.getInstance();

        //if the date is selected
        if (!tvPlandate.getText().toString().isEmpty())
        {
            SimpleDateFormat simpFormat = new SimpleDateFormat("E, MMM dd yyyy");
            //Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(simpFormat.parse(tvPlandate.getText().toString()));
                year = (cal.get(Calendar.YEAR));
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
            }
            catch(Exception e)
            {}
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener(){
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



    private void setSpCategory(){
        spCategory = vCreateNewNote.findViewById(R.id.spCategory);

        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<Category>(lCategory));
        spCategory.setAdapter(adapter);
        spCategory.setSelection(0);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category category = (Category) parent.getSelectedItem();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setSpStatus( ){
        spStatus = vCreateNewNote.findViewById(R.id.spStatus);
        // loadListSpinnerCreateNote();
        ArrayAdapter<Status> adapterStatus = new ArrayAdapter<Status>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<Status>(lStatus));
        spStatus.setAdapter(adapterStatus);
        Integer temp = 1;
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Status status = (Status) parent.getSelectedItem();


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void setSpPriority(){
        spPriority = vCreateNewNote.findViewById(R.id.spPriority);

        ArrayAdapter<Priority> adapterPriority = new ArrayAdapter<Priority>(requireContext(), android.R.layout.simple_spinner_item, lPriority);
        spPriority.setAdapter(adapterPriority);

        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Priority priority = (Priority) parent.getSelectedItem();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void getSelectedCategory(View v) {
        Category c = (Category) spCategory.getSelectedItem();

    }

    public void loadListStatus(){
        RoomDB noteDB = RoomDB.getDatabase(requireContext());
        StatusDAO statusDAO = noteDB.statusDAO();
        CategoryDAO categoryDAO = noteDB.categoryDAO();
        AccountDAO userDAO = noteDB.accountDAO();
        PriorityDAO priorityDAO = noteDB.priorityDAO();
        noteDB.databaseWriteExecutor.execute(() ->{

            lStatus = statusDAO.getAllList();
            lCategory = categoryDAO.getAll();
            lPriority = priorityDAO.getAllList();
            setSpCategory();
            setSpStatus();
            setSpPriority();

        });



    }

    public void loadListPriority() {

    }
}