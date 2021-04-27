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
import android.widget.Toast;

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
    private List<Note> lNote;
    private NoteManagerAdapter noteManagerAdapter;
    private Button btnPlanDate;
    private Spinner spCategory, spStatus, spPriority;
    private TextView tvPlanDateNew;
    private List<Status> lStatus;
    private List<Category> lCategory;
    private List<Priority> lPriority;
    private EditText edtNameNote;
    private Context context;
    private UserLocalStore userLocalStore;
    public static int userIdCurrent;
    private RoomDB noteDB;
    private CategoryDAO categoryDAO;
    private StatusDAO statusDAO;
    private PriorityDAO priorityDAO;
    private AccountDAO accountDAO;
    private NoteDAO noteDAO;

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);

        context=root.getContext();
        rvListNote = root.findViewById(R.id.rvListNote);
        btnCreateNote = root.findViewById(R.id.btnCreateNote);
        userLocalStore= new UserLocalStore(context);

        btnCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userLocalStore.getLoginUser()!=null)
                    createNewNoteByBtn();
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "You must to login !!", Toast.LENGTH_LONG);
                }
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(userLocalStore.getLoginUser()!=null) // Kiểm tra nếu có sự đăng nhập của user thì mới thực hiện load dữ liệu
        {
            createDatabaseNote();
            userIdCurrent = userLocalStore.getLoginUser().getID();
            loadListSpinner(userIdCurrent); // danh sách của các thuộc tính khi muốn tạo một note mới
            createRecyclerView();
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "You must to login !!", Toast.LENGTH_LONG);
        }
        // TODO: Use the ViewModel
    }

    public void createRecyclerView(){

        lNote = new ArrayList<Note>();
        if (userLocalStore.getLoginUser()!=null)
        {
            loadListNoteForRecyclerView(userIdCurrent);
        }
    }

    public void loadListNoteForRecyclerView(int userId) {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvListNote.setLayoutManager(linearLayoutManager);
        noteDAO.getAllByUserId(userId).observe(getViewLifecycleOwner(), lNote -> {

            //noteManagerAdapter = new NoteManagerAdapter(lNote,lCategory,lStatus,requireContext());
            noteManagerAdapter = new NoteManagerAdapter(lNote,lCategory,lStatus,lPriority,requireContext());
            //noteManagerAdapter.setlNote(lNote);
            rvListNote.setAdapter(noteManagerAdapter);
        });



//        noteDB.databaseWriteExecutor.execute(()->{
//                    lNote = new ArrayList<Note>(noteDAO.getAll(userId));
//                    lStatus = statusDAO.getAllList();
//                    lCategory = categoryDAO.getAll();
//                    lPriority = priorityDAO.getAllList();
//                    //noteManagerAdapter = new NoteManagerAdapter(lNote,lCategory,lStatus,requireContext());
//            noteManagerAdapter = new NoteManagerAdapter(lNote,lCategory,lStatus,lPriority,requireContext());
//                    //noteManagerAdapter.setlNote(lNote);
//                    rvListNote.setAdapter(noteManagerAdapter);
//                }
//
//        );
    }

    private void loadListSpinner(int userId){ // Lấy các danh sách
        lStatus = statusDAO.getAllByUserId(userId);
        lCategory = categoryDAO.getAllByUserId(userId);
        lPriority = priorityDAO.getAllByUserId(userId);
    }

    public void createNewNoteByBtn(){
        Date planDate = null;
        Context context = new ContextThemeWrapper(getContext(), R.style.AppTheme);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        vCreateNewNote = layoutInflater.inflate(R.layout.create_new_note,null,false);
        initializationComponentForViewCreateNewNote();
        builder.setView(vCreateNewNote);
        loadListForSpinnerViewCreateNewNote();
        setBtnPlanDate(planDate);
        builder.setCancelable(false);

        //End Button
        builder.setPositiveButton("Add", (dialog, which) ->{

            Note note = new Note();
            try{
                note.setCategoryId(((Category)spCategory.getSelectedItem()).getCategoryId());}catch (Exception e){}
            try{
                note.setStatusId(((Status)spStatus.getSelectedItem()).getStatusId());}catch (Exception e){}
            try{
                note.setPriorityId(((Priority)spPriority.getSelectedItem()).getPriorityId());}catch (Exception e){}

            note.setName(edtNameNote.getText().toString());
            note.setCreateDate(Calendar.getInstance().getTime());
            note.setPlanDate(planDate);
            note.setUserId(userIdCurrent);
            createNewNote(note);
            dialog.dismiss();

        })
                .setNegativeButton("Close",(dialog, which) ->
                {
                    dialog.dismiss();
                });


        builder.show();
    }

    private void createNewNote(Note note){
        try {
            createDatabaseNote();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    noteDAO.createNewNote(note);
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void  setBtnPlanDate(Date planDate){

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
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<Category>(lCategory));
        if(!adapter.isEmpty()) {
            spCategory.setAdapter(adapter);
        }
    }

    private void setSpStatus( ){
        spStatus = vCreateNewNote.findViewById(R.id.spStatus);
        ArrayAdapter<Status> adapterStatus = new ArrayAdapter<Status>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<Status>(lStatus));
        if(!adapterStatus.isEmpty())spStatus.setAdapter(adapterStatus);
    }

    private void setSpPriority(){
        ArrayAdapter<Priority> adapterPriority = new ArrayAdapter<Priority>(requireContext(), android.R.layout.simple_spinner_item, lPriority);
        if(!adapterPriority.isEmpty())spPriority.setAdapter(adapterPriority);
    }

    public void loadListForSpinnerViewCreateNewNote(){
            setSpCategory();
            setSpStatus();
            setSpPriority();
    }

    private void initializationComponentForViewCreateNewNote(){

        tvPlanDateNew = vCreateNewNote.findViewById(R.id.tvPlanDateNew);
        btnPlanDate = vCreateNewNote.findViewById(R.id.btnPlanDate);
        spPriority = vCreateNewNote.findViewById(R.id.spPriority);
        spCategory = vCreateNewNote.findViewById(R.id.spCategory);
        edtNameNote = vCreateNewNote.findViewById(R.id.edtNameNote);
    }

    public void createDatabaseNote() { // Khởi tạo các giá trị cho database
         noteDB = RoomDB.getDatabase(requireContext());
         statusDAO = noteDB.statusDAO();
         categoryDAO = noteDB.categoryDAO();
         accountDAO = noteDB.accountDAO();
         priorityDAO = noteDB.priorityDAO();
         noteDAO = noteDB.noteDAO();
    }
}