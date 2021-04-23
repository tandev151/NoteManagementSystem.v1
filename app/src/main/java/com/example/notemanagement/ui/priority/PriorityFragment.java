package com.example.notemanagement.ui.priority;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.notemanagement.R;

public class PriorityFragment extends Fragment {

    private PriorityViewModel priorityViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        priorityViewModel =
                new ViewModelProvider(this).get(PriorityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_priority, container, false);
        final TextView textView = root.findViewById(R.id.text_priority);
        priorityViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}