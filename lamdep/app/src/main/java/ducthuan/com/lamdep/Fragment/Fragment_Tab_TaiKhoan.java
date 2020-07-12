package ducthuan.com.lamdep.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Activity.ChatsActivity;
import ducthuan.com.lamdep.Activity.DangNhapActivity;
import ducthuan.com.lamdep.Activity.DanhGiaCuaToiActivity;
import ducthuan.com.lamdep.Activity.GioHangActivity;
import ducthuan.com.lamdep.Activity.DonMuaActivity;
import ducthuan.com.lamdep.Activity.SanPhamDaXemActivity;
import ducthuan.com.lamdep.Activity.SanPhamYeuThichActivity;
import ducthuan.com.lamdep.Activity.ShopCuaToiActivity;
import ducthuan.com.lamdep.Activity.ThongTinTaiKhoanActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.Chat;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Tab_TaiKhoan extends Fragment {
    View view;
    Toolbar toolbar;

    SharedPreferences sharedPreferences;
    TextView txtGioHang,txtMessage;
    ArrayList<NhanVien>nhanViens;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    boolean onpause = false;

    LinearLayout linearLayoutDanhChoNguoiBan;

    TextView txtDangNhapDangKy,txtEmailNV,txtNgayDangKy,txtTenNV,txtBatDauBan,txtQuanLyDonHang;
    ImageView imgHinhNV,imgThongTinTaiKhoan;

    TextView txtSPDaXem,txtSPYeuThich,txtDanhGiaCuaToi,txtLienHe,txtHoTro;

    Button btnDangXuat;
    String tennv = "";
    String manv = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_tai_khoan,container,false);
        addControls();
        setHasOptionsMenu(true);
        addEvents();
        return view;
    }

    private void addEvents() {
        txtDangNhapDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DangNhapActivity.class));
            }
        });

        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tennv.equals("")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tennv", "");
                    editor.putString("manv", "");
                    editor.commit();
                }
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), TrangChuActivity.class));
            }
        });

        imgThongTinTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ThongTinTaiKhoanActivity.class);
                intent.putParcelableArrayListExtra("nhanvien",nhanViens);
                getActivity().startActivity(intent);
            }
        });

        txtQuanLyDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!manv.equals("")){
                    startActivity(new Intent(getActivity(), DonMuaActivity.class));

                }else {
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }


            }
        });

        txtBatDauBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShopCuaToiActivity.class);
                intent.putParcelableArrayListExtra("nhanvien",nhanViens);
                getActivity().startActivity(intent);
            }
        });

        txtSPDaXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!manv.equals("")){
                    Intent intent = new Intent(getActivity(), SanPhamDaXemActivity.class);
                    intent.putExtra("manv",manv);
                    startActivity(intent);


                }else {
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }
            }
        });

        txtSPYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!manv.equals("")){
                    Intent intent = new Intent(getActivity(), SanPhamYeuThichActivity.class);
                    intent.putExtra("manv",manv);
                    startActivity(intent);


                }else {
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }
            }
        });

        txtDanhGiaCuaToi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!manv.equals("")){
                    Intent intent = new Intent(getActivity(), DanhGiaCuaToiActivity.class);
                    intent.putExtra("manv",manv);
                    startActivity(intent);


                }else {
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }
            }
        });

    }

    private void addControls() {
        toolbar = view.findViewById(R.id.toolbar);
        txtDangNhapDangKy = view.findViewById(R.id.txtDangNhapDangKy);
        btnDangXuat = view.findViewById(R.id.btnDangXuat);
        txtEmailNV = view.findViewById(R.id.txtEmailNV);
        txtNgayDangKy = view.findViewById(R.id.txtNgayDangKy);
        txtTenNV = view.findViewById(R.id.txtTenNV);
        txtBatDauBan = view.findViewById(R.id.txtBatDauBan);
        imgThongTinTaiKhoan = view.findViewById(R.id.imgThongTinTaiKhoan);
        txtQuanLyDonHang = view.findViewById(R.id.txtQuanLyDonHang);
        linearLayoutDanhChoNguoiBan = view.findViewById(R.id.linearLayoutDanhChoNguoiBan);
        txtSPDaXem = view.findViewById(R.id.txtSPDaXem);
        txtSPYeuThich = view.findViewById(R.id.txtSPYeuThich);
        txtDanhGiaCuaToi = view.findViewById(R.id.txtDanhGiaCuaToi);
        imgHinhNV = view.findViewById(R.id.imgHinhNV);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        sharedPreferences = getActivity().getSharedPreferences("dangnhap",Context.MODE_PRIVATE);
        tennv = sharedPreferences.getString("tennv","");
        manv = sharedPreferences.getString("manv","");


        if(!tennv.equals("")){
            imgThongTinTaiKhoan.setVisibility(View.VISIBLE);
            btnDangXuat.setVisibility(View.VISIBLE);
            txtDangNhapDangKy.setVisibility(View.GONE);
            txtEmailNV.setVisibility(View.VISIBLE);
            txtNgayDangKy.setVisibility(View.VISIBLE);
            txtBatDauBan.setVisibility(View.VISIBLE);
            linearLayoutDanhChoNguoiBan.setVisibility(View.VISIBLE);
            getNhanVien();
        }

    }

    public void getNhanVien(){
        DataService dataService = APIService.getService();
        Call<List<NhanVien>>callback = dataService.getNhanVien(tennv);
        callback.enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                nhanViens = (ArrayList<NhanVien>) response.body();
                if(nhanViens.size()>0){
                    txtTenNV.setText(nhanViens.get(0).getTENNV());
                    txtEmailNV.setText(nhanViens.get(0).getTENDANGNHAP());
                    txtNgayDangKy.setText("Thành viên từ "+nhanViens.get(0).getNGAYDANGKY());
                    if(nhanViens.get(0).getHINH()!=null){
                        String hinh = nhanViens.get(0).getHINH().toString();
                        Picasso.with(getActivity()).load(TrangChuActivity.base_url+hinh).placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinhNV);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_trang_chu,menu);
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
                    if(firebaseUser!=null){
                        int temp = 0;
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Chat chat = snapshot.getValue(Chat.class);
                            if(chat.isSeen() == false && chat.getReceiver().equals(firebaseUser.getUid())){
                                temp++;
                            }
                        }
                        if(temp==0){
                            txtMessage.setVisibility(View.GONE);
                        }else {
                            txtMessage.setVisibility(View.VISIBLE);
                            txtMessage.setText(temp+"");
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(onpause == true){
            getDataGioHang(txtGioHang);
            getNhanVien();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onpause = true;
    }
}
