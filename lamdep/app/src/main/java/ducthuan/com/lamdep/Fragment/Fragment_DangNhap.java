package ducthuan.com.lamdep.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import ducthuan.com.lamdep.Activity.DangNhapActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_DangNhap extends Fragment {

    FirebaseAuth auth;

    TextInputLayout txtEmailDangNhap,txtMatKhauDangNhap;
    TextView txtQuenMatKhau;
    Button btnDangNhap;

    CallbackManager callbackManager;

    LoginButton login_button;
    SignInButton sign_in_button;

    SharedPreferences sharedPreferences;

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount googleSignInAccount;
    public static int RC_SIGN_IN = 111;

    ProgressDialog progressDialog;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dang_nhap, container, false);

        AnhXa();
        Init();
        addEvents();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void showProcessDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            String name = account.getDisplayName();
            String email = account.getEmail();
            String hinh = "";
            if(account.getPhotoUrl()!=null){
                hinh = String.valueOf(account.getPhotoUrl());
            }

            DataService dataService = APIService.getService();
            Call<String>callback = dataService.ketQuaLuuFB(name,email,hinh);
            callback.enqueue(new Callback<String>()
            {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq = response.body();
                    if(kq.trim().equals("Success") || kq.trim().equals("Exist")) {
                        Toast.makeText(getActivity(), "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });

            startActivity(new Intent(getActivity(), TrangChuActivity.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w("lỗi", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void AnhXa() {
        txtEmailDangNhap = view.findViewById(R.id.txtEmailDangNhap);
        txtMatKhauDangNhap = view.findViewById(R.id.txtMatKhauDangNhap);
        txtQuenMatKhau = view.findViewById(R.id.txtQuenMatKhau);
        btnDangNhap = view.findViewById(R.id.btnDangNhap);
        login_button = view.findViewById(R.id.login_button);
        sign_in_button = view.findViewById(R.id.sign_in_button);


    }

    private void Init() {
        loginFacebook();
        loginGoogle();
    }

    private void loginGoogle() {
        sign_in_button.setSize(SignInButton.SIZE_STANDARD);
        setGooglePlusButtonText(sign_in_button, "Kết nối với Google");

        //Yêu cầu người dùng cung cấp thông tin cơ bản, tên, email, hình ảnh
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //két nối google API client
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());

        //kiểm tra kết nối
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.sign_in_button:
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                        showProcessDialog();
                        break;
                }
            }
        });


    }



    //custom button google
    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                tv.setPadding(0,0,30,0);
                return;
            }
        }
    }


    private void loginFacebook() {
        callbackManager = CallbackManager.Factory.create();
        login_button.setFragment(this);
        login_button.setReadPermissions("public_profile","email");
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                        try {
                            String hinh = "";
                            String name = object.getString("name");
                            String email = object.getString("email");
                            if (Profile.getCurrentProfile() != null) {
                                hinh = ImageRequest.getProfilePictureUri(Profile.getCurrentProfile().getId(), 400, 400).toString();
                            }

                            DataService dataService = APIService.getService();
                            Call<String>callback = dataService.ketQuaLuuFB(name,email,hinh);
                            callback.enqueue(new Callback<String>()
                            {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String kq = response.body();
                                    if(kq.trim().equals("Success") || kq.trim().equals("Exist")){
                                        Toast.makeText(getActivity(), "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getActivity(), "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email");
                request.setParameters(parameters);
                request.executeAsync();

                startActivity(new Intent(getActivity(), TrangChuActivity.class));

            }

            @Override
            public void onCancel() {
                //thoát ra
               // Log.d("kiemtra","logout");
            }

            @Override
            public void onError(FacebookException error) {
                //lỗi
                //Log.d("kiemtra","error");
            }
        });



    }



    private void addEvents() {

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!kiemTraEmail() | !kiemTraPassword()){
                    Toast.makeText(getActivity(), "Vui lòng kiểm tra lại email hoặc mật khẩu !", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = txtEmailDangNhap.getEditText().getText().toString();
                String matkhau = txtMatKhauDangNhap.getEditText().getText().toString();

                showProcessDialog();

                auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(email,matkhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("kiemtradangnhap","OK");
                        }else {
                            Log.d("kiemtradangnhap","FAIL");
                        }
                    }
                });


                DataService dataService = APIService.getService();
                Call<List<NhanVien>>callback = dataService.kiemTraDangNhap(email,matkhau);
                callback.enqueue(new Callback<List<NhanVien>>() {
                    @Override
                    public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                        ArrayList<NhanVien> nhanViens = (ArrayList<NhanVien>) response.body();

                        if(nhanViens.size() > 0){
                            sharedPreferences = getActivity().getSharedPreferences("dangnhap", getActivity().MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("tennv", nhanViens.get(0).getTENDANGNHAP());
                            editor.putString("manv",nhanViens.get(0).getMANV());
                            editor.commit();
                            Toast.makeText(getActivity(), "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getActivity(), TrangChuActivity.class));

                        }else {
                            Toast.makeText(getActivity(), "Vui lòng kiểm tra lại email hoặc mật khẩu !", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<NhanVien>> call, Throwable t) {
                        Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    private boolean kiemTraEmail(){
        String email = txtEmailDangNhap.getEditText().getText().toString().trim();
        if(email.isEmpty()){
            txtEmailDangNhap.setError("Email không được bỏ trống");
            return false;
            //Kiểm tra định dạng email
        }else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){//\
            txtEmailDangNhap.setError(null);
            txtEmailDangNhap.setErrorEnabled(false);
            return true;
        } else {
            //txtEmailDangNhap.setError("Email sai định dạng");
            return false;
        }
    }

    private boolean kiemTraPassword(){
        String password = txtMatKhauDangNhap.getEditText().getText().toString().trim();
        if(password.isEmpty()){
            txtMatKhauDangNhap.setError("Password không được bỏ trống");
            return false;
        }else if(password.matches("((?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).{6,20})")) {
            txtMatKhauDangNhap.setError(null);
            txtMatKhauDangNhap.setErrorEnabled(false);
            return true;

        } else {
            //txtMatKhauDangNhap.setError("Mật khẩu chứa ít nhất 6 ký tự và 1 chữ hoa");
            return false;
        }
    }



}
