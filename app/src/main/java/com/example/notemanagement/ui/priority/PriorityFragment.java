package com.example.notemanagement.ui.priority;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.Entity.Priority;
import com.example.notemanagement.Entity.User;
import com.example.notemanagement.R;
import com.example.notemanagement.RoomDB;
import com.example.notemanagement.ui.category.CategoryAdapter;
import com.example.notemanagement.userstore.UserLocalStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import static com.example.notemanagement.Utils.CONSTANT.DELETE_CODE;
import static com.example.notemanagement.Utils.CONSTANT.MY_PREFERENCE_NAME;
import static com.example.notemanagement.Utils.CONSTANT.SUCCESS_MESSAGE;
import static com.example.notemanagement.Utils.CONSTANT.UPDATE_CODE;

public class PriorityFragment extends Fragment {

    private RecyclerView recyclerViewPriority;
    private PriorityAdapter priorityAdapter;
    private FloatingActionButton fptAddPriority;
    View root;
    UserLocalStore userLocalStore;
    Context context;
    Account currentAcc;

    //get database
    RoomDB db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_priority, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        priorityAdapter = new PriorityAdapter(requireContext());

        currentAcc = new Account();
        userLocalStore= new UserLocalStore(requireContext());
        if (userLocalStore.getLoginUser()!=null)
        {
            currentAcc= userLocalStore.getLoginUser();

        }

        db = RoomDB.getDatabase(getActivity().getApplicationContext());
        //get elements in the layout
        recyclerViewPriority = (RecyclerView) view.findViewById(R.id.recyclerPriorityList);
        //get observable to list in adapter
        db.priorityDAO().getPriorityById(currentAcc.getID()).observe(getViewLifecycleOwner(), priorities -> {
            priorityAdapter.setAdapter(priorities);
            //Constrain when display
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

            recyclerViewPriority.setAdapter(priorityAdapter);
            recyclerViewPriority.setLayoutManager(layoutManager);
        });

        //Get share preference for check


        //Add event for floating action button
        fptAddPriority = (FloatingActionButton) view.findViewById(R.id.fptAddPriority);
        fptAddPriority.setOnClickListener((View v) -> {
            addNewStatus(v);
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);

        switch (item.getItemId()) {
            case UPDATE_CODE: //Update
                //get element was update by index in adapter
                int position = item.getGroupId();

                Priority priority = priorityAdapter.getList().get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                ViewGroup viewGroup = (ViewGroup) root.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, viewGroup, false);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);


                //Display value of status which updated
                EditText nameCategory = dialogView.findViewById(R.id.edtDialogName);
                nameCategory.setText(priority.getName());
                //Title for this dialog
                TextView title = dialogView.findViewById(R.id.tvDialogTitle);
                title.setText("Priority form ?");
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
                            priority.setName(nameCategory.getText().toString());
                            //New thread for work with database
                            db.databaseWriteExecutor.execute(() -> {
                                db.priorityDAO().update(priority);
                            });
                        }
                        Toast.makeText(getActivity().getApplicationContext(), "Success create category", Toast.LENGTH_LONG);
                        alertDialog.dismiss();
                    }
                });


                alertDialog.show();
                break;

            case DELETE_CODE:
                //get true id
                int positionDelete = item.getGroupId();
                int mappingPositionInList = priorityAdapter.getList().get(positionDelete).getPriorityId();

                //new thread for work with database
                db.databaseWriteExecutor.execute(() -> {
                    db.priorityDAO().deletePriorityById(mappingPositionInList);
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

        TextView tvPriorityName = alertDialog.findViewById(R.id.tvDialogTitle);
        tvPriorityName.setText("Priority form ");
        EditText edtNewName = alertDialog.findViewById(R.id.edtDialogName);

        Button btnAdd = (Button) dialogView.findViewById(R.id.btnEdit);
        btnAdd.setOnClickListener((View addView) -> {

            if (edtNewName.getText().toString().trim().isEmpty()) {
                tvPriorityName.setError("Please enter the priority' name!");
                return;
            }

            db.databaseWriteExecutor.execute(() -> {
                Priority priority = new Priority(edtNewName.getText().toString(), Calendar.getInstance().getTime(), currentAcc.getID());

                db.priorityDAO().insert(priority);
            });
            alertDialog.dismiss();
        });
    }

}