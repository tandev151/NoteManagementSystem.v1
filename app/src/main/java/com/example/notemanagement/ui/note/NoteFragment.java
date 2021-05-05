package com.example.notemanagement.ui.note;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.renderscript.RenderScript;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.notemanagement.Utils.CONSTANT.SUCCESS_MESSAGE;

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

        context = root.getContext();
        rvListNote = root.findViewById(R.id.rvListNote);
        btnCreateNote = root.findViewById(R.id.btnCreateNote);
        userLocalStore = new UserLocalStore(context);
        lStatus = new ArrayList<Status>();
        lCategory = new ArrayList<Category>();
        lPriority = new ArrayList<Priority>();
        btnCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userLocalStore.getLoginUser() != null) {
                    createNewNoteByBtn();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "You must to login !!", Toast.LENGTH_LONG);
                }
            }
        });


        if (userLocalStore.getLoginUser() != null) // check login user
        {
            createDatabaseNote();
            userIdCurrent = userLocalStore.getLoginUser().getId();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loadListSpinner(userIdCurrent); // list of new note
                }
            }).start();

            createRecyclerView();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "You must to login !!", Toast.LENGTH_LONG);
        }
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    public void createRecyclerView() {
        lNote = new ArrayList<>();
        noteDAO.getAllByUserId(userIdCurrent).observe(getViewLifecycleOwner(), lNote -> {
            noteManagerAdapter = new NoteManagerAdapter(lNote, lCategory, lStatus, lPriority, requireContext());
            rvListNote.setAdapter(noteManagerAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            rvListNote.setLayoutManager(linearLayoutManager);
        });

    }

    private void loadListSpinner(int userId) { // get all list for spinner
        lStatus = statusDAO.getAllByUserId(userId);
        lCategory = categoryDAO.getAllByUserId(userId);
        lPriority = priorityDAO.getAllByUserId(userId);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);

        switch (item.getItemId()) {
            case 2: //Update
                //get element was update by index in adapter
                int index = item.getGroupId();
                callDialogEditNote(index);
                break;

            case 1: //delete
                //get true id
                int indexDelete = item.getGroupId();
                Note note = noteManagerAdapter.getlNote().get(indexDelete);
                noteDB.databaseWriteExecutor.execute(() -> {
                    noteDAO.deleteById(note.getNoteId());
                });

        }
        return true;
    }

    private void callDialogEditNote(int index) {
        Note note = noteManagerAdapter.getlNote().get(index);
        lCategory = noteManagerAdapter.getlCategory();
        lPriority = noteManagerAdapter.getlPriority();
        lStatus = noteManagerAdapter.getlStatus();

        Date planDate = null;
        Context context = new ContextThemeWrapper(getContext(), R.style.AlertDialogTheme);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        vCreateNewNote = layoutInflater.inflate(R.layout.create_new_note, null, false);
        initializationComponentForViewCreateNewNote();
        builder.setView(vCreateNewNote);
        setSpinnerSelect(note);
        edtNameNote.setText(note.getName());
        DateFormat formater = new SimpleDateFormat("E, MMM dd yyyy");

        if (note.getPlanDate() != null)
            tvPlanDateNew.setText(formater.format(note.getPlanDate()));


        setBtnPlanDate();

        builder.setCancelable(false);

        builder.setPositiveButton("Update", (dialog, which) -> {
            try {
                note.setCategoryId(((Category) spCategory.getSelectedItem()).getCategoryId());
            } catch (Exception e) {
            }
            try {
                note.setStatusId(((Status) spStatus.getSelectedItem()).getStatusId());
            } catch (Exception e) {
            }
            try {
                note.setPriorityId(((Priority) spPriority.getSelectedItem()).getPriorityId());
            } catch (Exception e) {
            }

            note.setName(edtNameNote.getText().toString());
            note.setPlanDate(getPlanDate());
            noteDB.databaseWriteExecutor.execute(() -> {
                noteDAO.update(note);
            });

            dialog.dismiss();
        })
                .setNegativeButton("Close", (dialog, which) ->
                {
                    dialog.dismiss();
                });


        builder.show();
    }

    public void setSpinnerSelect(Note note) {

        int temp = 0;
        int tempS = 0;

        loadListForSpinnerViewCreateNewNote();

        for (int i = 0; i < lCategory.size(); i++)
            if (lCategory.get(i).getCategoryId() == note.getCategoryId()) {
                temp = i;
                break;
            }

        spCategory.setSelection(temp);

        for (int i = 0; i < lStatus.size(); i++)
            if (lStatus.get(i).getStatusId() == note.getStatusId()) {
                tempS = i;
                break;
            }

        spStatus.setSelection(tempS);

        int tempP = 0;
        for (int i = 0; i < lPriority.size(); i++)
            if (lPriority.get(i).getPriorityId() == note.getPriorityId()) {
                tempP = i;
                break;
            }

        spPriority.setSelection(tempP);
    }

    public void createNewNoteByBtn() {
        Context context = new ContextThemeWrapper(getContext(), R.style.AlertDialogTheme);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setOnDismissListener(null);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        vCreateNewNote = layoutInflater.inflate(R.layout.create_new_note, null, false);
        initializationComponentForViewCreateNewNote();
        builder.setView(vCreateNewNote);
        loadListForSpinnerViewCreateNewNote();

        Note note = new Note();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpFormat = new SimpleDateFormat("E, MMM dd yyyy");
        DateFormat formater = new SimpleDateFormat("E, MMM dd yyyy");
        tvPlanDateNew.setText(formater.format(cal.getTime()));
        setBtnPlanDate();

        builder.setCancelable(false);
        builder.setPositiveButton("Add", (dialog, which) -> {

            //Validate plan date

            try {
                note.setCategoryId(((Category) spCategory.getSelectedItem()).getCategoryId());
            } catch (Exception e) {
            }
            try {
                note.setStatusId(((Status) spStatus.getSelectedItem()).getStatusId());
            } catch (Exception e) {
            }
            try {
                note.setPriorityId(((Priority) spPriority.getSelectedItem()).getPriorityId());
            } catch (Exception e) {
            }

            note.setName(edtNameNote.getText().toString());
            note.setAccountId(userIdCurrent);
            note.setCreateDate(Calendar.getInstance().getTime());
            note.setPlanDate(getPlanDate());
            createNewNote(note);
            dialog.dismiss();


        })
                .setNegativeButton("Close", (dialog, which) ->
                {
                    dialog.dismiss();
                });


        builder.show();
    }

    private Date getPlanDate() {
        Calendar cal = Calendar.getInstance();
        DateFormat simpFormat = new SimpleDateFormat("E, MMM dd yyyy");
        try {
            cal.setTime(simpFormat.parse(tvPlanDateNew.getText().toString()));
        } catch (Exception e) {
        }
        return cal.getTime();
    }

    private void createNewNote(Note note) {
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

    private void setBtnPlanDate() {

        btnPlanDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(tvPlanDateNew);
            }
        });
    }

    public void setDate(TextView tvPlanDateNew) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Calendar cal = Calendar.getInstance();
        DateFormat formater = new SimpleDateFormat("E, MMM dd yyyy");
        //if the date is selected
        if (!tvPlanDateNew.getText().toString().isEmpty()) {
            SimpleDateFormat simpFormat = new SimpleDateFormat("E, MMM dd yyyy");

            try {
                cal.setTime(simpFormat.parse(tvPlanDateNew.getText().toString()));
                year = (cal.get(Calendar.YEAR));
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
            } catch (Exception e) {
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.DAY_OF_MONTH, day);
                cal.set(Calendar.MONTH, month);
                tvPlanDateNew.setText(formater.format(cal.getTime()));
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void setSpCategory() {
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<Category>(lCategory));
        if (!adapter.isEmpty()) {
            spCategory.setAdapter(adapter);
        }
    }

    //region Hello from comment
    private void setSpStatus() {
        ArrayAdapter<Status> adapterStatus = new ArrayAdapter<Status>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<Status>(lStatus));
        if (!adapterStatus.isEmpty()) spStatus.setAdapter(adapterStatus);
    }
    //endregion

    private void setSpPriority() {
        ArrayAdapter<Priority> adapterPriority = new ArrayAdapter<Priority>(requireContext(), android.R.layout.simple_spinner_item, lPriority);
        if (!adapterPriority.isEmpty()) spPriority.setAdapter(adapterPriority);
    }

    public void loadListForSpinnerViewCreateNewNote() {
        setSpCategory();
        setSpStatus();
        setSpPriority();
    }

    private void initializationComponentForViewCreateNewNote() {

        tvPlanDateNew = vCreateNewNote.findViewById(R.id.tvPlanDateNew);
        btnPlanDate = vCreateNewNote.findViewById(R.id.btnPlanDate);
        spPriority = vCreateNewNote.findViewById(R.id.spPriority);
        spCategory = vCreateNewNote.findViewById(R.id.spCategory);
        edtNameNote = vCreateNewNote.findViewById(R.id.edtNameNote);
        spStatus = vCreateNewNote.findViewById(R.id.spStatus);
    }

    public void createDatabaseNote() { // create value for database
        noteDB = RoomDB.getDatabase(requireContext());
        statusDAO = noteDB.statusDAO();
        categoryDAO = noteDB.categoryDAO();
        accountDAO = noteDB.accountDAO();
        priorityDAO = noteDB.priorityDAO();
        noteDAO = noteDB.noteDAO();
    }
}