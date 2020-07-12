package ducthuan.com.lamdep.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ducthuan.com.lamdep.Activity.DangNhapActivity;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_DangKy extends Fragment {


    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;



    TextInputLayout txtTenDangKy, txtEmailDangKy, txtMatKhauDangky;
    Button btnDangKy;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dang_ky, container, false);

        AnhXa();
        addEvents();

        return view;
    }

    private void addEvents() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!kiemTraEmail() | !kiemTraPassword() | !kiemTraUsername()){
                    return;
                }

                final String name = txtTenDangKy.getEditText().getText().toString();
                final String email = txtEmailDangKy.getEditText().getText().toString();
                String matkhau = txtMatKhauDangky.getEditText().getText().toString();

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(email,matkhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
                            Map<String,Object>map = new HashMap<>();
                            map.put("id",firebaseAuth.getUid());
                            map.put("name",name);
                            map.put("email",email);
                            map.put("status","offline");
                            databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("kiemtra",task.toString());
                                    }

                                }
                            });
                        }

                    }
                });


                DataService dataService = APIService.getService();
                Call<String>callback = dataService.kiemTraDangKy(name,email,matkhau);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String check = response.body();
                        if(check.equals("Success")){
                            Toast.makeText(getActivity(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), DangNhapActivity.class));
                        }else if(check.equals("Fail")){
                            txtEmailDangKy.setError("Email đã tồn tại");
                        }else {
                            Toast.makeText(getActivity(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }



    private void AnhXa() {
        txtTenDangKy = view.findViewById(R.id.txtTenDangKy);
        txtEmailDangKy = view.findViewById(R.id.txtEmailDangKy);
        txtMatKhauDangky = view.findViewById(R.id.txtMatKhauDangky);
        btnDangKy = view.findViewById(R.id.btnDangKy);
    }

    private boolean kiemTraEmail(){
        String emailInput = txtEmailDangKy.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()){
            txtEmailDangKy.setError("Email không được bỏ trống");
            return false;
            //Kiểm tra định dạng email
        }else if(emailInput.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){//\
            txtEmailDangKy.setError(null);
            txtEmailDangKy.setErrorEnabled(false);
            return true;
        } else {
            txtEmailDangKy.setError("Email sai định dạng");
            return false;
        }
    }

    private boolean kiemTraUsername(){
        String username = txtTenDangKy.getEditText().getText().toString().trim();
        if(username.isEmpty()){
            txtTenDangKy.setError("Username không được bỏ trống");
            return false;
        }else if(username.length()>15){
            txtTenDangKy.setError("Tối đa 15 ký tự");
            return false;
        }else {
            txtTenDangKy.setError(null);
            txtTenDangKy.setErrorEnabled(false);
            return true;
        }
    }

    private boolean kiemTraPassword(){
        String password = txtMatKhauDangky.getEditText().getText().toString().trim();
        if(password.isEmpty()){
            txtMatKhauDangky.setError("Password không được bỏ trống");
            return false;
        }else if(password.matches("((?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).{6,20})")) {
            txtMatKhauDangky.setError(null);
            txtMatKhauDangky.setErrorEnabled(false);
            return true;

        } else {
            txtMatKhauDangky.setError("Mật khẩu chứa ít nhất 6 ký tự và 1 chữ hoa");
            return false;
        }
    }


}
