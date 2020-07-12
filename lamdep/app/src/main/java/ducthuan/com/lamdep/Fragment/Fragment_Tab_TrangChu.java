package ducthuan.com.lamdep.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ducthuan.com.lamdep.Activity.ChatsActivity;
import ducthuan.com.lamdep.Activity.DangNhapActivity;
import ducthuan.com.lamdep.Activity.GioHangActivity;
import ducthuan.com.lamdep.Activity.TimKiemTrangChuActivity;
import ducthuan.com.lamdep.Model.Chat;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Tab_TrangChu extends Fragment {

    View view;
    Toolbar toolbarTrangChu;
    SharedPreferences sharedPreferences;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    TextView txtTimKiem;

    TextView txtGioHang,txtMessage;
    boolean onpause = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_trang_chu,container,false);


        //if (CheckConnect.haveNetworkConnection(getApplicationContext())) {
        addControls();
        addEvents();
        //getDataSanPhamGoiY();
        /*} else {
            CheckConnect.ShowToast_Short(getApplicationContext(), "Vui lòng kiểm tra lại kết nối!!!");
        }*/

        return view;
    }


    //add cac controls
    private void addControls() {
        setHasOptionsMenu(true);
        toolbarTrangChu = view.findViewById(R.id.toolbarTrangChu);
        txtTimKiem = view.findViewById(R.id.txtTimKiem);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarTrangChu);


        sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



    }

    private void addEvents() {
        txtTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimKiemTrangChuActivity.class);
                intent.putExtra("timkiem","trangchu");
                startActivity(intent);
            }
        });
    }

    //lay san pham gio hang
    public void getDataGioHang(final TextView txtGH) {

        sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
        String manv = sharedPreferences.getString("manv", "");
        if (manv.equals("")) {
            txtGH.setVisibility(View.GONE);
        } else {
            DataService dataService = APIService.getService();
            Call<String> callSLSP = dataService.getSoLuongSPGioHang(manv);
            callSLSP.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sl = response.body();
                    if(sl.equals("")){
                        txtGH.setVisibility(View.GONE);
                    }else {
                        txtGH.setVisibility(View.VISIBLE);
                        txtGH.setText(sl);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }

    //khi back ve load lai du lieu o day




   @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
       getActivity().getMenuInflater().inflate(R.menu.menu_trang_chu, menu);
       super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.itGioHang);
        View giaoDienCustomGioHang = MenuItemCompat.getActionView(item);
        txtGioHang = giaoDienCustomGioHang.findViewById(R.id.txtSoLuongSanPhamGioHang);
        getDataGioHang(txtGioHang);

        giaoDienCustomGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
                String manv= sharedPreferences.getString("manv","");
                if(manv.equals("")){
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }else {
                    Intent intent = new Intent(getActivity(), GioHangActivity.class);
                    intent.putExtra("trangchu",1);
                    startActivity(intent);
                }

            }
        });

        MenuItem itMessage = menu.findItem(R.id.itMessage);
        View viewMessage = MenuItemCompat.getActionView(itMessage);
        txtMessage = viewMessage.findViewById(R.id.txtSoLuongMSG);
        getDataMessage(txtMessage);

       viewMessage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
               String manv= sharedPreferences.getString("manv","");
               if(manv.equals("")){
                   startActivity(new Intent(getActivity(), DangNhapActivity.class));
               }else {
                   Intent intent = new Intent(getActivity(), ChatsActivity.class);
                   startActivity(intent);
               }

           }
       });




    }

    private void getDataMessage(final TextView txtMessage) {
        sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
        String manv = sharedPreferences.getString("manv", "");
        if (manv.equals("")) {
            txtMessage.setVisibility(View.GONE);
        } else {

            databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int temp = 0;
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Chat chat = snapshot.getValue(Chat.class);
                        if(firebaseUser!=null){
                            if(chat.isSeen() == false && chat.getReceiver().equals(firebaseUser.getUid())){
                                temp++;
                            }
                        }
                    }
                    if(temp==0){
                        txtMessage.setVisibility(View.GONE);
                    }else {
                        txtMessage.setVisibility(View.VISIBLE);
                        txtMessage.setText(temp+"");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


    /*private void layTenNguoiDungDangNhap() {
        sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
        tennguoidung = sharedPreferences.getString("tennv", "");

        Log.d("tennv", tennguoidung);
        if (!tennguoidung.equals("")) {
            itdangnhap.setTitle(tennguoidung);
        }
    }

    private void layTenNguoiDungGoogle() {
        //Yêu cầu người dùng cung cấp thông tin cơ bản, tên, email, hình ảnh
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //két nối google API client
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity().getApplicationContext(), gso);
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());

        if (googleSignInAccount != null) {
            itdangnhap.setTitle(googleSignInAccount.getDisplayName());
        }
    }

    private void layTenNguoiDungFacebook() {
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null && !accessToken.isExpired()) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        itdangnhap.setTitle(object.getString("name"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            /*case R.id.itDangNhap:
                if (accessToken == null && googleSignInAccount == null && tennguoidung.equals("")) {
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }
                break;

            case R.id.itDangXuat:

                if (accessToken != null) {
                    LoginManager.getInstance().logOut();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tennv", "");
                    editor.putString("manv", "");
                    editor.commit();
                    menu.clear();
                    onCreateOptionsMenu(menu,);
                    *//*AlertDialog.Builder builder = new AlertDialog.Builder(TrangChuActivity.this);
                    builder.setMessage("Bạn có muốn đăng xuất không?");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LoginManager.getInstance().logOut();
                            menu.clear();
                            onCreateOptionsMenu(menu);
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();*//*
                }
                if (googleSignInAccount != null) {
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("tennv", "");
                                    editor.putString("manv", "");
                                    editor.commit();
                                    menu.clear();
                                    onCreateOptionsMenu(menu);
                                }
                            });
                }

                if (!tennguoidung.equals("")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tennv", "");
                    editor.putString("manv", "");
                    editor.commit();
                    menu.clear();
                    onCreateOptionsMenu(menu);
                }
                startActivity(new Intent(getActivity(), TrangChuActivity.class));
                break;
            */
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(onpause==true){
            getDataGioHang(txtGioHang);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onpause = true;
    }
}
