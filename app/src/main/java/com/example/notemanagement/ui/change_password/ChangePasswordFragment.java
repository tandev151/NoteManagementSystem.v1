package com.example.notemanagement.ui.change_password;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notemanagement.DAO.AccountDAO;
import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.R;
import com.example.notemanagement.RoomDB;
import com.example.notemanagement.userstore.UserLocalStore;

public class ChangePasswordFragment extends Fragment {


    private EditText curpass, newpass, confirmpass;
    private Button btnChange;
    private AccountDAO accountDAO;
    private UserLocalStore userLocalStore;
    private TextView tvErr;
    private int error = -2;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);
        curpass = root.findViewById(R.id.edtCurrentpass);
        newpass = root.findViewById(R.id.edtNewpass);
        confirmpass = root.findViewById(R.id.edtConfirm);
        btnChange = root.findViewById(R.id.btnChange);
        tvErr = root.findViewById(R.id.tverr);

        accountDAO = RoomDB.getDatabase(requireContext()).accountDAO();

        userLocalStore = new UserLocalStore(getContext());

        btnChange.setOnClickListener(v -> {
            if (checkInput()) {
                RoomDB.databaseWriteExecutor.execute(() ->
                {
                    String currentPassword= curpass.getText().toString();
                    String newPassword= newpass.getText().toString();
                    String confirmPassword= confirmpass.getText().toString();
                    if (userLocalStore.checkUserLogin()) {
                        Account user = new Account();
                        user = userLocalStore.getLoginUser();
                        if (user != null) {
                            if (user.getPassword().equals(currentPassword)) {

                                if (newpass.getText().toString().equals(confirmPassword)) {
                                    if (user.getPassword().equals(newPassword)) {
                                        error = 3;
                                    } else {
                                        user.setPassword(newPassword);
                                        accountDAO.update(user);
                                        userLocalStore.storeUserData(user);
                                        error = 0;
                                    }
                                } else {
                                    error = 1;
                                }
                            } else {
                                error = 2;
                            }
                        }

                    }
                });
                if (error == 3) {
                    Toast.makeText(getActivity(), "The password is the same as the current one!!!", Toast.LENGTH_SHORT).show();
                    error = -1;
                    return;
                }
                if (error == 0) {
                    Toast.makeText(getActivity(), "Password Successful changed!!", Toast.LENGTH_SHORT).show();
                    error = -1;
                    return;
                }
                if (error == 1) {
                    Toast.makeText(getActivity(), "Password is not match", Toast.LENGTH_SHORT).show();
                    error = -1;
                    return;
                }
                if (error == 2) {
                    Toast.makeText(getActivity(), "Current password is incorrect", Toast.LENGTH_SHORT).show();
                    error = -1;
                    return;
                }
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private boolean checkInput() {
        if (curpass.getText().toString().isEmpty() || newpass.getText().toString().isEmpty() || confirmpass.getText().toString().isEmpty()) {
            tvErr.setText("All info need filled in!!!");
            return false;
        }
        return true;
    }

}