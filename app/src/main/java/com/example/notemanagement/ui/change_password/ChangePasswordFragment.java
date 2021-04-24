package com.example.notemanagement.ui.change_password;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.RoomDatabase;

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
    private int error=-1;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_change_password, container, false);
        curpass= root.findViewById(R.id.edtCurrentpass);
        newpass= root.findViewById(R.id.edtNewpass);
        confirmpass= root.findViewById(R.id.edtConfirm);
        btnChange= root.findViewById(R.id.btnChange);
        tvErr= root.findViewById(R.id.tverr);

        accountDAO= RoomDB.getDatabase(requireContext()).accountDAO();

        userLocalStore= new UserLocalStore(getContext());

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkInput()){
                    RoomDB.databaseWriteExecutor.execute(()->
                    {
                        if (userLocalStore.checkUserLogin()) {
                            Account user = new Account();
                            user = userLocalStore.getLoginUser();
                            if(user!=null )
                            {
                                if(user.getPassWord().equals(curpass.getText().toString()))
                                {
                                    if(newpass.getText().toString().equals(confirmpass.getText().toString())){
                                        user.setPassWord(newpass.getText().toString());
                                        accountDAO.update(user);
                                        error=0;
                                    }
                                    else {
                                        error=1;
                                    }
                                }
                                else
                                {
                                    error=2;
                                }
                            }

                        }
                    });

                    if(error==0){
                        Toast.makeText(getActivity(),"Thay đổi thành công !", Toast.LENGTH_SHORT).show();
                        error=-1;
                        return;
                    }
                    if(error==1){
                        Toast.makeText(getActivity(),"Mật khẩu xác nhận không khớp !", Toast.LENGTH_SHORT).show();
                        error=-1;
                        return;
                    }

                    if(error==2)
                    {
                        Toast.makeText(getActivity(),"Mật khẩu hiện tại sai !", Toast.LENGTH_SHORT).show();
                        error=-1;
                        return;
                    }
                }
            }

        });

        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private boolean  checkInput()
    {
        if(curpass.getText().toString().isEmpty() || newpass.getText().toString().isEmpty() || confirmpass.getText().toString().isEmpty()){
            tvErr.setText("Bạn phải điền đủ thông tin !");
            return false;
        }
        return true;
    }

}