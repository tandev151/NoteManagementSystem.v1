package com.example.notemanagement.ui.edit_profile;

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

public class EditProfileFragment extends Fragment {
    //init
    private Button btnChange, btnSignIn;
    private EditText edtFirstName, edtLastName, edtEmail;
    private TextView errorInput, userCount;
    private UserLocalStore userLocalStore;
    private AccountDAO accountDAO;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        userLocalStore = new UserLocalStore(getContext());
        //=====================Hook========================
        btnChange = root.findViewById(R.id.btn_Change);
        edtEmail = root.findViewById(R.id.edtEmail);
        edtFirstName = root.findViewById(R.id.edtFirstName);
        edtLastName = root.findViewById(R.id.edtLastName);
        errorInput = root.findViewById(R.id.textinput_error);
        userCount = root.findViewById(R.id.user_count);
        btnSignIn = root.findViewById(R.id.btnHome);
        accountDAO = RoomDB.getDatabase(requireContext()).accountDAO();

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail.getText().toString().isEmpty() ||
                        edtFirstName.getText().toString().isEmpty() ||
                        edtLastName.getText().toString().isEmpty()) {
                    errorInput.setText("Please fill all information!");
                } else {
                    RoomDB.databaseWriteExecutor.execute(() ->
                    {
                        Account loginedUser = new Account();
                        loginedUser = userLocalStore.getLoginUser();

                        Account checkemail = new Account();
                        checkemail = accountDAO.getUserByMail(edtEmail.getText().toString());

                        if (checkemail != null) {
                            if (checkemail.getId() != loginedUser.getId()) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Email was exist!!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Account user = new Account(loginedUser.getId(),
                                        edtEmail.getText().toString().trim(),
                                        loginedUser.getPassword(),
                                        edtFirstName.getText().toString().trim(),
                                        edtLastName.getText().toString().trim());

                                accountDAO.update(user);
                                userLocalStore.storeUserData(user);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Update successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private boolean authentication() {
        if (userLocalStore.checkUserLogin())
            return true;
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (authentication()) {
            RoomDB.databaseWriteExecutor.execute(() ->
            {
                Account user = new Account();
                user = userLocalStore.getLoginUser();

                if (userLocalStore.checkUserLogin()) {

                    if (user.getLastName().equals(""))
                        edtLastName.setText("");
                    else edtLastName.setText(user.getLastName());

                    if (user.getFirstName().equals(""))
                        edtFirstName.setText("");
                    else
                        edtFirstName.setText(user.getFirstName());
                    edtEmail.setText(user.getUserName());
                }
            });
        }
    }
}