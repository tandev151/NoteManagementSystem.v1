package com.example.notemanagement.ui.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.Entity.Status;
import com.example.notemanagement.R;
import com.example.notemanagement.RoomDB;
import com.example.notemanagement.ui.status.StatusAdapter;
import com.example.notemanagement.ui.status.StatusFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.notemanagement.Utils.CONSTANT.MY_PREFERENCE_NAME;
import static com.example.notemanagement.Utils.CONSTANT.SUCCESS_MESSAGE;

public class CategoryFragment extends Fragment {

    private int userId;

    private RecyclerView recyclerViewCategory;
    private CategoryAdapter categoryAdapter;
    private FloatingActionButton fptAddCategory;
    View root;

    //get database
    RoomDB db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_category, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryAdapter = new CategoryAdapter(requireContext());

        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("user",String.valueOf(1)).commit();
        //Get user login now
        SharedPreferences pref= this.getActivity().getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String hashUser = pref.getString("user", null);
        userId = Integer.valueOf(hashUser);

        db = RoomDB.getDatabase(getActivity().getApplicationContext());
        //get elements in the layout
        recyclerViewCategory = (RecyclerView) view.findViewById(R.id.recyclerViewCategory);
        //get observable to list in adapter
        db.categoryDAO().getCategoryByUser(this.userId).observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.setAdapter(categories);
            //Constrain when display
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

            recyclerViewCategory.setAdapter(categoryAdapter);
            recyclerViewCategory.setLayoutManager(layoutManager);
        });

        //Get share preference for check


        //Add event for floating action button
        fptAddCategory = (FloatingActionButton) view.findViewById(R.id.fptAddStatus);
        fptAddCategory.setOnClickListener((View v) -> {
            addNewStatus(v);
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);

        switch (item.getItemId()) {
            case 2: //Update
                //get element was update by index in adapter
                int position = item.getGroupId();

                Category category = categoryAdapter.getList().get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                ViewGroup viewGroup = (ViewGroup) root.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, viewGroup, false);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);


                //Display value of status which updated
                EditText nameCategory = dialogView.findViewById(R.id.edtDialogName);
                nameCategory.setText(category.getName());
                //Title for this dialog
                TextView title = dialogView.findViewById(R.id.tvDialogTitle);
                title.setText("Save your modify ?");
                //Button cancel
                Button cancelDialog = dialogView.findViewById(R.id.btnClose);
                cancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                //Button save
                Button saveDialog = dialogView.findViewById(R.id.btnEdit);
                saveDialog.setText("Save");
                saveDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText nameCategory = alertDialog.findViewById(R.id.edtDialogName);
                        if (nameCategory.getText().toString().trim() != null) {
                            category.setName(nameCategory.getText().toString());
                            //New thread for work with database
                            db.databaseWriteExecutor.execute(() -> {
                                db.categoryDAO().update(category);
                            });
                        }
                        Toast.makeText(getActivity().getApplicationContext(), "Success create category", Toast.LENGTH_LONG);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;

            case 1:
                //get true id
                int positionDelete = item.getGroupId();
                int mappingPositionInList = categoryAdapter.getList().get(positionDelete).getCategoryId();

                //new thread for work with database
                db.databaseWriteExecutor.execute(() -> {
                    db.categoryDAO().deleteById(mappingPositionInList);
                });
                //Notify that action done!!!
                Snackbar.make(root, SUCCESS_MESSAGE, Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .show();
        }
        return true;
    }

    /**
     * Create new status
     * @param v
     */
    public void addNewStatus(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        ViewGroup viewGroup = (ViewGroup) root.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog, viewGroup, false);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        //click close button
        Button btnClose = (Button) dialogView.findViewById(R.id.btnClose);
        btnClose.setOnClickListener((View view) -> {
            alertDialog.dismiss();
        });

        TextView tvCategryName = alertDialog.findViewById(R.id.tvDialogTitle);
        EditText edtNewName = alertDialog.findViewById(R.id.edtDialogName);

        Button btnAdd = (Button) dialogView.findViewById(R.id.btnEdit);
        btnAdd.setOnClickListener((View addView) -> {

            if (edtNewName.getText().toString().trim().isEmpty()) {
                tvCategryName.setError("Please enter the category' name!");
                return;
            }

            db.databaseWriteExecutor.execute(() -> {
                Category category = new Category(edtNewName.getText().toString(), Calendar.getInstance().getTime(), userId);

                db.categoryDAO().insert(category);
            });
            alertDialog.dismiss();
        });
    }
}