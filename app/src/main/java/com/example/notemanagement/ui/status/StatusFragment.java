package com.example.notemanagement.ui.status;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.Entity.Status;
import com.example.notemanagement.R;
import com.example.notemanagement.RoomDB;
import com.example.notemanagement.userstore.UserLocalStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import static com.example.notemanagement.Utils.CONSTANT.MY_PREFERENCE_NAME;
import static com.example.notemanagement.Utils.CONSTANT.SAVE;
import static com.example.notemanagement.Utils.CONSTANT.SUCCESS_MESSAGE;
import static com.example.notemanagement.Utils.CONSTANT.UPDATE_CODE;

public class StatusFragment extends Fragment {
    private int userId;

    private RecyclerView recyclerViewStatus;
    private StatusAdapter statusAdapter;
    private FloatingActionButton fptAddStatus;
    View root;
    UserLocalStore userLocalStore;
    Context context;
    Account currentUser;

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
        currentUser = new Account();

        userLocalStore = new UserLocalStore(requireContext());


            if (userLocalStore.getLoginUser()!=null)
            {
                currentUser= userLocalStore.getLoginUser();
                this.userId= currentUser.getID();
            }

        db = RoomDB.getDatabase(getActivity().getApplicationContext());
        //get elements in the layout
        recyclerViewStatus = (RecyclerView) view.findViewById(R.id.recyclStatusList);
        //get observable to list in adapter





        ////
        /////thêm userid /accountid
        /////
        /////
        db.statusDAO().getStatusByAccountId(userId).observe(getViewLifecycleOwner(), status -> {
            statusAdapter.setAdapter(status);
            //Constrain when display
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());


            recyclerViewStatus.setLayoutManager(layoutManager);
            recyclerViewStatus.setAdapter(statusAdapter);
            Log.d("lll", "onViewCreated: ");
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
                        EditText nameCategory = dialogView.findViewById(R.id.edtDialogName);
                        if (nameCategory.getText().toString().trim() != null) {
                            status.setName(nameCategory.getText().toString());
                            //New thread for work with database
                            db.databaseWriteExecutor.execute(() -> {
                                db.statusDAO().update(status);
                            });
                        }
                        Toast.makeText(getActivity().getApplicationContext(), SUCCESS_MESSAGE, Toast.LENGTH_LONG);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                break;

            case 1:
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
                tvStatusName.setError("Please enter the status' name!");
                return;
            }

            db.databaseWriteExecutor.execute(() -> {
                Status status = new Status(edtNewName.getText().toString(), Calendar.getInstance().getTime(), userId);
                db.statusDAO().insert(status);
            });
            alertDialog.dismiss();
        });
    }
}