package com.example.notemanagement.ui.status;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.Entity.Status;
import com.example.notemanagement.R;
import com.example.notemanagement.RoomDB;
import com.example.notemanagement.userstore.UserLocalStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import static com.example.notemanagement.Utils.CONSTANT.DELETE_CODE;
import static com.example.notemanagement.Utils.CONSTANT.SAVE;
import static com.example.notemanagement.Utils.CONSTANT.SUCCESS_MESSAGE;
import static com.example.notemanagement.Utils.CONSTANT.UPDATE_CODE;

public class StatusFragment extends Fragment {

    private RecyclerView recyclerViewStatus;
    private StatusAdapter statusAdapter;
    private FloatingActionButton fptAddStatus;
    View root;
    UserLocalStore userLocalStore;
    Context context;
    Account currentAcc;
    //get database
    RoomDB db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_status, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statusAdapter = new StatusAdapter(requireContext());

        //SharedPreferences.Editor editor = this.getActivity().getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        currentAcc = new Account();
        userLocalStore = new UserLocalStore(requireContext());


        if (userLocalStore.getLoginUser() != null) {
            currentAcc = userLocalStore.getLoginUser();
        }

        db = RoomDB.getDatabase(getActivity().getApplicationContext());
        //get elements in the layout
        recyclerViewStatus = (RecyclerView) view.findViewById(R.id.recyclStatusList);
        //get observable to list in adapter


        ////
        /////thêm userid /accountid
        /////
        /////
        db.statusDAO().getStatusByAccountId(currentAcc.getId()).observe(getViewLifecycleOwner(), status -> {
            statusAdapter.setAdapter(status);
            //Constrain when display
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());


            recyclerViewStatus.setLayoutManager(layoutManager);
            recyclerViewStatus.setAdapter(statusAdapter);

        });

        //Get share preference for check

        //Add event for floating action button
        fptAddStatus = (FloatingActionButton) view.findViewById(R.id.fptAddStatus);
        fptAddStatus.setOnClickListener((View v) -> {
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
                Status status = statusAdapter.getList().get(position);

                //Show dialog for change
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                ViewGroup viewGroup = (ViewGroup) root.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, viewGroup, false);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                //Display value of status which updated
                EditText nameCategory = dialogView.findViewById(R.id.edtDialogName);
                nameCategory.setText(status.getName());
                //Title for this dialog
                TextView title = dialogView.findViewById(R.id.tvDialogTitle);
                title.setText("Status form");
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
                saveDialog.setText(SAVE);
                saveDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String statusUpdate = nameCategory.getText().toString().trim();

                        if (statusUpdate != null) {


                            // EditText nameCategory = v.findViewById(R.id.edtDialogName);
                            status.setName(nameCategory.getText().toString());
                            //New thread for work with database
                            db.databaseWriteExecutor.execute(() -> {
                                Boolean isDuplicated = db.statusDAO().getStatusByNameDuplicate(statusUpdate) != null ? false : true;
                                if (isDuplicated) {
                                    db.statusDAO().updateNameStatus(status.getStatusId(), statusUpdate);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity().getApplicationContext(), "Success update Status", Toast.LENGTH_LONG);
                                            alertDialog.dismiss();
                                        }
                                    });

                                } else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            nameCategory.setError("This status is available!!!");
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
                int mappingPositionInList = statusAdapter.getList().get(positionDelete).getStatusId();

                //new thread for work with database
                db.databaseWriteExecutor.execute(() -> {
                    db.statusDAO().deleteStatusById(mappingPositionInList);
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
     *
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

        TextView tvStatusName = alertDialog.findViewById(R.id.tvDialogTitle);
        tvStatusName.setText("Status form ");
        EditText edtNewName = alertDialog.findViewById(R.id.edtDialogName);

        Button btnAdd = (Button) dialogView.findViewById(R.id.btnEdit);
        btnAdd.setOnClickListener((View addView) -> {

            if (edtNewName.getText().toString().trim().isEmpty()) {
                edtNewName.setError("Please enter the status' name!");
                return;
            }

            String nameStatus = edtNewName.getText().toString().trim();
            Status status = new Status(nameStatus, Calendar.getInstance().getTime(), currentAcc.getId());

            db.databaseWriteExecutor.execute(() -> {

                //isDuplicate = true: available this priority in database
                Boolean isDuplicate = db.statusDAO().getStatusByNameDuplicate(nameStatus) != null ? false : true;

                if (!isDuplicate) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edtNewName.setError("This status is available");
                            edtNewName.setFocusable(true);
                        }
                    });

                } else {
                    db.statusDAO().insert(status);
                    alertDialog.dismiss();
                }
            });
        });
    }
}