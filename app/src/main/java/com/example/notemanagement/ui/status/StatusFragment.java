package com.example.notemanagement.ui.status;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notemanagement.Entity.Category;
import com.example.notemanagement.Entity.Status;
import com.example.notemanagement.R;
import com.example.notemanagement.RoomDB;
import com.example.notemanagement.ui.category.CategoryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

public class StatusFragment extends Fragment {

    private StatusViewModel mViewModel;

    public static StatusFragment newInstance() {
        return new StatusFragment();
    }

    private RecyclerView recyclStatusList;
    private StatusAdapter statusAdapter;
    private FloatingActionButton fptAddStatus;
    View root;
    LiveData<List<Status>> statusList;

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
        mViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        // TODO: Use the ViewModel


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statusAdapter= new StatusAdapter(requireContext());
        db = RoomDB.getDatabase(getActivity().getApplicationContext());
        //get elements in the layout
        recyclStatusList = (RecyclerView) view.findViewById(R.id.recyclStatusList);
        db.statusDAO().getAll().observe(this, status->{
            statusAdapter.setAdapter(status);

            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

            recyclStatusList.setAdapter(statusAdapter);
            recyclStatusList.setLayoutManager(layoutManager);
        });
        fptAddStatus = (FloatingActionButton) view.findViewById(R.id.fptAddStatus);
        fptAddStatus.setOnClickListener((View v) -> {
            addNewStatus(v);

        });

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
         super.onContextItemSelected(item);
        Log.e("Select", String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case 2: //Update
                Log.e("Select", String.valueOf(item.getItemId()));
                int position= item.getGroupId();
                Status status = statusAdapter.getList().get(position);

                //Show dialog for change
                Dialog dialog = new Dialog(StatusFragment.this.getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_dialog);

                EditText nameCategory = dialog.findViewById(R.id.edtDialogName);
                nameCategory.setText(status.getName());

                TextView title= dialog.findViewById(R.id.tvDialogTitle);
                title.setText("Save your modify ?");

                Button cancelDialog = dialog.findViewById(R.id.btnClose);
                cancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button saveDialog = dialog.findViewById(R.id.btnEdit);
                saveDialog.setText("Save");
                saveDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText nameCategory = dialog.findViewById(R.id.edtDialogName);
                        if (nameCategory.getText().toString().trim() != null) {
                            status.setName(nameCategory.getText().toString());
                            db.databaseWriteExecutor.execute(()->{
                                db.statusDAO().update(status);
                            });

                        }
                        Toast.makeText(getActivity().getApplicationContext(), "Susscess create category", Toast.LENGTH_LONG);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
            case 1:
                Log.e("Select", String.valueOf(item.getItemId()));
                int positionDelete= item.getGroupId();
               // categoryViewModel.deleteById(positionDelete);

                //get true id
                int mappingPositionInList = statusAdapter.getList().get(positionDelete).getStatusid();
                db.databaseWriteExecutor.execute(()->{
                    db.statusDAO().deleteStatusById(mappingPositionInList);
                });

                Snackbar.make(root,"Xóa thành công",Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
        }

         return true;
    }

    /*
        processes for adding new status
         */
    public void addNewStatus(View v)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        ViewGroup viewGroup = (ViewGroup)root.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        //click close button
        Button btnClose =(Button)dialogView.findViewById(R.id.btnClose);
        btnClose.setOnClickListener((View view)->{
            alertDialog.dismiss();
        });

        TextView tvStatusName =  alertDialog.findViewById(R.id.tvDialogTitle);
        EditText edtNewName= alertDialog.findViewById(R.id.edtDialogName);

        Button btnAdd = (Button)dialogView.findViewById(R.id.btnEdit);
        btnAdd.setOnClickListener((View addView)->{

            if(edtNewName.getText().toString().trim().isEmpty()){
                tvStatusName.setError("Please enter the status' name!");
                return;
            }

            db.databaseWriteExecutor.execute(()->{

                Status status = new Status(edtNewName.getText().toString(), Calendar.getInstance().getTime());
                db.statusDAO().insert(status);

            });
            alertDialog.dismiss();


        });
    }
}