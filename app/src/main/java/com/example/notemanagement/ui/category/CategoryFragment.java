package com.example.notemanagement.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CategoryFragment extends Fragment {

    private CategoryViewModel categoryViewModel;
    RecyclerView rvCategory;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        categoryViewModel =
                new ViewModelProvider(this).get(CategoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_category, container, false);
//        final TextView textView = root.findViewById(R.id.text_category);
//        categoryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        rvCategory = root.findViewById(R.id.recyclerViewCategory);
        List categoryList = new ArrayList<>();
//        Category category = new Category();
//        categoryList.add(category);
        Date currentTime = Calendar.getInstance().getTime();
        categoryList.add(new Category("Work", currentTime));
        categoryList.add(new Category("Study",currentTime));
        categoryList.add(new Category("Play sport",currentTime));
        categoryList.add(new Category("Deadline",currentTime));
        categoryList.add(new Category("Hang out",currentTime));
        categoryList.add(new Category("Business",currentTime));
        categoryList.add(new Category("Business",currentTime));
        categoryList.add(new Category("Business",currentTime));
        categoryList.add(new Category("Business",currentTime));
        categoryList.add(new Category("Business",currentTime));
        categoryList.add(new Category("Business",currentTime));
        categoryList.add(new Category("Business",currentTime));
        categoryList.add(new Category("Business",currentTime));
        categoryList.add(new Category("Business",currentTime));


        CategoryAdapter adapter = new CategoryAdapter(categoryList);
        rvCategory.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        rvCategory.setLayoutManager(linearLayoutManager);

        return root;
    }
}