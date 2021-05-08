package com.example.notemanagement.ui.category;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.R;
import com.example.notemanagement.RoomDB;
import com.example.notemanagement.userstore.UserLocalStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import static com.example.notemanagement.Utils.CONSTANT.DELETE_CODE;
import static com.example.notemanagement.Utils.CONSTANT.SUCCESS_MESSAGE;
import static com.example.notemanagement.Utils.CONSTANT.UPDATE_CODE;

public class CategoryFragment extends Fragment {
    //Init
    UserLocalStore userLocalStore;
    Account currentAcc;

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

        currentAcc = new Account();
        userLocalStore = new UserLocalStore(requireContext());

        if (userLocalStore.getLoginUser() != null) {
            currentAcc = userLocalStore.getLoginUser();
        }

        db = RoomDB.getDatabase(getActivity().getApplicationContext());
        //get elements in the layout
        recyclerViewCategory = (RecyclerView) view.findViewById(R.id.recyclerViewCategory);
        //get observable to list in adapter
        db.categoryDAO().getCategoryByUser(currentAcc.getId()).observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.setAdapter(categories);
            //Constrain when display
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

            recyclerViewCategory.setAdapter(categoryAdapter);
            recyclerViewCategory.setLayoutManager(layoutManager);
        });

        //Add event for floating action button
        fptAddCategory = (FloatingActionButton) view.findViewById(R.id.fptAddStatus);
        fptAddCategory.setOnClickListener((View v) -> {
            addNewCategory(v);
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);

        switch (item.getItemId()) {
            case UPDATE_CODE:
                //get element was update by index in adapter
                int position = item.getGroupId();

                Category category = categoryAdapter.getList().get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                ViewGroup viewGroup = (ViewGroup) root.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, viewGroup, false);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);

                //Display value of category which updated
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
                        String categoryUpdate = nameCategory.getText().toString().trim();

                        if (categoryUpdate != null) {
                            // EditText nameCategory = v.findViewById(R.id.edtDialogName);
                            category.setName(nameCategory.getText().toString());
                            //New thread for work with database
                            db.databaseWriteExecutor.execute(() -> {
                                Boolean isDuplicated = db.categoryDAO().getCategoryByName(categoryUpdate) != null ? false : true;
                                if (isDuplicated) {
                                    db.categoryDAO().updateNameCategory(category.getCategoryId(), categoryUpdate);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity().getApplicationContext(), "Success update Priority", Toast.LENGTH_LONG);
                                            alertDialog.dismiss();
                                        }
                                    });
                                } else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            nameCategory.setError("This Category is available!!!");
                                            nameCategory.setFocusable(true);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
                if (getActivity() != null && !getActivity().isFinishing()) {
                    alertDialog.show();
                }
                break;

            case DELETE_CODE:
                //get true id
                int positionDelete = item.getGroupId();
                int mappingPositionInList = categoryAdapter.getList().get(positionDelete).getCategoryId();

                //new thread for work with database
                db.databaseWriteExecutor.execute(() -> {
                    db.categoryDAO().deleteById(mappingPositionInList);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Notify that action done!!!
                            Snackbar.make(root, SUCCESS_MESSAGE, Snackbar.LENGTH_LONG)
                                    .setAction("Ok", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    })
                                    .show();
                        }
                    });
                });
        }
        return true;
    }

    /**
     * Create new Category
     *
     * @param v
     */
    public void addNewCategory(View v) {

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

        TextView tvCategoryName = alertDialog.findViewById(R.id.tvDialogTitle);
        tvCategoryName.setText("Category form");
        EditText edtNewName = alertDialog.findViewById(R.id.edtDialogName);

        Button btnAdd = (Button) dialogView.findViewById(R.id.btnEdit);
        btnAdd.setOnClickListener((View addView) -> {

            if (edtNewName.getText().toString().trim().isEmpty()) {
                edtNewName.setError("Please enter the category' name!");
                return;
            }
            String nameCategory = edtNewName.getText().toString().trim();
            Category category = new Category(nameCategory, Calendar.getInstance().getTime(), currentAcc.getId());

            db.databaseWriteExecutor.execute(() -> {
                //isDuplicate = true: available this priority in database
                Boolean isDuplicate = db.categoryDAO().getCategoryByName(nameCategory) != null ? false : true;
                if (!isDuplicate) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edtNewName.setError("This category is available");
                            edtNewName.setFocusable(true);
                        }
                    });
                } else {
                    db.categoryDAO().insert(category);
                    alertDialog.dismiss();
                }
            });
        });
    }
}