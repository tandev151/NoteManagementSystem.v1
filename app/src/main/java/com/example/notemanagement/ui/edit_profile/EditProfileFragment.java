package com.example.notemanagement.ui.edit_profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;


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


    private Button btnChange, btnSignIn;
    private EditText edtFirstname, edtLastname, edtEmail;
    private TextView errorinput, usercount;
    private UserLocalStore userLocalStore;
    private AccountDAO accountDAO;
    private int successUpdate =-1;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_edit_profile, container, false);
        userLocalStore= new UserLocalStore(getContext());
        //userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        btnChange= root.findViewById(R.id.btn_Change);
        edtEmail = root.findViewById(R.id.edtEmail);
        edtFirstname= root.findViewById(R.id.edtFirstName);
        edtLastname= root.findViewById(R.id.edtLastName);
        errorinput= root.findViewById(R.id.textinput_error);
        usercount= root.findViewById(R.id.user_count);
        btnSignIn= root.findViewById(R.id.btnHome);
        accountDAO= RoomDB.getDatabase(requireContext()).accountDAO();


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtEmail.getText().toString().isEmpty() ||
                        edtFirstname.getText().toString().isEmpty() ||
                        edtLastname.getText().toString().isEmpty())
                {
                    //Toast.makeText(getContext(),"Please fill all!",Toast.LENGTH_LONG);
                    errorinput.setText("Please fill all information!");
                }
                else
                {

                    RoomDB.databaseWriteExecutor.execute(()->
                    {
                        Account loginedUser= new Account();
                        loginedUser= userLocalStore.getLoginUser();

                        Account checkemail= new Account();
                        checkemail=accountDAO.getUserByMail(edtEmail.getText().toString());

                        if(checkemail!=null ) {
                            if (checkemail.getId() != loginedUser.getId())
                            {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                Account user= new Account(loginedUser.getId(),
                                        edtEmail.getText().toString().trim(),
                                        loginedUser.getPassword(),
                                        edtFirstname.getText().toString().trim(),
                                        edtLastname.getText().toString().trim());

                                accountDAO.update(user);
                                userLocalStore.storeUserData(user);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
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
    private boolean authentication()
    {
        if(userLocalStore.checkUserLogin())
            return true;
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(authentication()) {
            RoomDB.databaseWriteExecutor.execute(() ->
            {
                Account user = new Account();
                user = userLocalStore.getLoginUser();

                if (userLocalStore.checkUserLogin()) {

                    if (user.getLastName().equals(""))
                        edtLastname.setText("");
                    else edtLastname.setText(user.getLastName());

                    if (user.getFirstName().equals(""))
                        edtFirstname.setText("");
                    else
                        edtFirstname.setText(user.getFirstName());
                    edtEmail.setText(user.getUserName());
                }
            });
        }
    }

}