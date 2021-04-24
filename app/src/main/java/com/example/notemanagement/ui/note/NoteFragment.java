package com.example.notemanagement.ui.note;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.RenderScript;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.Entity.Note;
import com.example.notemanagement.Entity.Priority;
import com.example.notemanagement.Entity.Status;
import com.example.notemanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {

    private NoteViewModel mViewModel;
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


    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        // TODO: Use the ViewModel
    }

}