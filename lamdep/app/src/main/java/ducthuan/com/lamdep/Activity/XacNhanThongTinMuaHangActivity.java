package ducthuan.com.lamdep.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ducthuan.com.lamdep.Adapter.ChaThongTinDonHangAdapter;
import ducthuan.com.lamdep.Adapter.ConThongTinDonHangAdapter;
import ducthuan.com.lamdep.Model.DiaChiKhachHang;
import ducthuan.com.lamdep.Model.GioHang;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XacNhanThongTinMuaHangActivity extends AppCompatActivity {

    ArrayList<DiaChiKhachHang> diaChiKhachHangs;
    ArrayList<GioHang> gioHangs;
    ChaThongTinDonHangAdapter chaThongTinDonHangAdapter;
    RecyclerView rvThongTinDonHang;
    Intent intent;
    SharedPreferences sharedPreferences;

    Toolbar toolbar;
    TextView txtTongTien, txtDiaChi,txtLoaiPTVanChuyen,txtLoaiPTThanhToan;
    ImageView imgXemThemDiaChi,imgXemThemVanChuyen,imgXemThemThanhToan;
    Button btnThanhToan;
    TextView txtTenNV, txtSdt;

    ArrayList<String> maShops = new ArrayList<>();
    ArrayList<String> tenShops = new ArrayList<>();
    String loiNhans,mashops,tongtiens;

    final int REQUEST_CODE = 69;

    int tongtien = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_nhan_thong_tin_mua_hang);
        intent = getIntent();
        if (intent.hasExtra("giohang")) {
            gioHangs = intent.getParcelableArrayListExtra("giohang");
            addControls();
            addEvents();


        } else {
            Toast.makeText(this, "Đã xảy ra lỗi vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgXemThemDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(XacNhanThongTinMuaHangActivity.this, DiaChiActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });


        imgXemThemVanChuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(XacNhanThongTinMuaHangActivity.this);
                builder.setTitle("Đơn vị vận chuyển");
                builder.setView(R.layout.custom_dialog_phuongthuc_vanchuyen);
                final AlertDialog dialog = builder.create();
                dialog.show();
                final RadioButton rdChuyenPhatTieuChuan = dialog.findViewById(R.id.rdChuyenPhatTieuChuan);
                final RadioButton rdChuyenPhatNhanh = dialog.findViewById(R.id.rdChuyenPhatNhanh);
                final RadioGroup rgVanChuyen = dialog.findViewById(R.id.rgVanChuyen);
                Button btnDongYVanChuyen = dialog.findViewById(R.id.btnDongYVanChuyen);

                btnDongYVanChuyen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ptvc = "";
                        if(rdChuyenPhatTieuChuan.isChecked()){
                            ptvc = rdChuyenPhatTieuChuan.getText().toString();
                        }else if(rdChuyenPhatNhanh.isChecked()){
                            ptvc = rdChuyenPhatNhanh.getText().toString();
                        }

                        txtLoaiPTVanChuyen.setText(ptvc);
                        dialog.dismiss();
                    }
                });

            }
        });

        imgXemThemThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(XacNhanThongTinMuaHangActivity.this);
                builder.setTitle("Phương thức thanh toán");
                builder.setView(R.layout.custom_dialog_phuongthuc_thanhtoan);
                final AlertDialog dialog = builder.create();
                dialog.show();
                final RadioButton rdThanhToanKhiNhanHang = dialog.findViewById(R.id.rdThanhToanKhiNhanHang);
                final RadioButton rdChuyenKhoan = dialog.findViewById(R.id.rdChuyenKhoan);
                Button btnDongYThanhToan = dialog.findViewById(R.id.btnDongYThanhToan);

                btnDongYThanhToan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ptvc = "";
                        if(rdThanhToanKhiNhanHang.isChecked()){
                            ptvc = rdThanhToanKhiNhanHang.getText().toString();
                        }else if(rdChuyenKhoan.isChecked()){
                            ptvc = rdChuyenKhoan.getText().toString();
                        }

                        txtLoaiPTThanhToan.setText(ptvc);
                        dialog.dismiss();
                    }
                });

            }
        });



        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String []loinhans = chaThongTinDonHangAdapter.getLoinhan();
                for (int i = 0; i < loinhans.length; i++) {
                    if(loinhans[i]==null){
                        loinhans[i]="Không có";
                    }
                }
                loiNhans = "";
                for (int i = 0; i < loinhans.length; i++) {
                    if(i == loinhans.length-1){
                        loiNhans+= loinhans[i];
                    }else {
                        loiNhans+= loinhans[i] + "," ;
                    }
                }

                mashops = "";
                for (int i = 0; i < maShops.size(); i++) {
                    if(i == maShops.size()-1){
                        mashops+= maShops.get(i);
                    }else {
                        mashops+= maShops.get(i) + "," ;
                    }
                }

                String[] tongtienss = chaThongTinDonHangAdapter.getTongtienss();

                tongtiens = "";
                for (int i = 0; i < tongtienss.length; i++) {
                    if(i == tongtienss.length-1){
                        tongtiens+= tongtienss[i];
                    }else {
                        tongtiens+= tongtienss[i] + "," ;
                    }
                }

                //tao json post len sever
                JSONArray jsonArray = new JSONArray();
                for(int i=0;i<gioHangs.size();i++)
                {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("masp",gioHangs.get(i).getMASP());
                        jsonObject.put("manv",gioHangs.get(i).getMANV());
                        jsonObject.put("mashop",gioHangs.get(i).getMASHOP());
                        jsonObject.put("mausac",gioHangs.get(i).getMAUSAC());
                        jsonObject.put("kichthuoc",gioHangs.get(i).getKICHTHUOC());
                        jsonObject.put("soluong",gioHangs.get(i).getSOLUONG());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }


                DataService dataService = APIService.getService();
                Call<String>callback = dataService.themHoaDon(jsonArray,tongtiens,mashops,loiNhans,gioHangs.get(0).getMANV()
                        ,txtTenNV.getText().toString(),txtSdt.getText().toString(),txtDiaChi.getText().toString()
                        ,txtTongTien.getText().toString(),txtLoaiPTVanChuyen.getText().toString(),txtLoaiPTThanhToan.getText().toString());
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        final String kq = response.body();
                        if(kq.length()>0){

                            String manv = gioHangs.get(0).getMANV();
                            DataService dataService1 = APIService.getService();
                            Call<String>call1 = dataService1.xoaSanPhamDuocMuaTrongGioHang(manv);
                            call1.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String kq1 = response.body();
                                    if(kq1.equals("OK")){
                                        Toast.makeText(XacNhanThongTinMuaHangActivity.this, "Thanh toán thành công !", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(XacNhanThongTinMuaHangActivity.this,DatHangThanhCongActivity.class);
                                        intent.putExtra("mahdtong", kq);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(XacNhanThongTinMuaHangActivity.this, "Thanh toán thất bại, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });




            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            ArrayList<DiaChiKhachHang>dcselected = data.getParcelableArrayListExtra("dcseleted");
            if (dcselected.size()>0){
                txtTenNV.setText(dcselected.get(0).getTENKH());
                txtSdt.setText(dcselected.get(0).getSODTKH());
                txtDiaChi.setText(dcselected.get(0).getDIACHIKH());
            }else {
                Toast.makeText(this, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        txtTongTien = findViewById(R.id.txtTongTien);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        rvThongTinDonHang = findViewById(R.id.rvThongTinDonHang);
        txtTenNV = findViewById(R.id.txtTenNV);
        txtSdt = findViewById(R.id.txtSdt);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        imgXemThemDiaChi = findViewById(R.id.imgXemThemDiaChi);
        imgXemThemVanChuyen = findViewById(R.id.imgXemThemVanChuyen);
        imgXemThemThanhToan = findViewById(R.id.imgXemThemThanhToan);
        txtLoaiPTVanChuyen = findViewById(R.id.txtLoaiPTVanChuyen);
        txtLoaiPTThanhToan = findViewById(R.id.txtLoaiPTThanhToan);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);


        maShops.add(gioHangs.get(0).getMASHOP());
        tenShops.add(gioHangs.get(0).getTENNV());
        for (int i = 1; i < gioHangs.size(); i++) {
            int dem = 0;
            for (int j = 0; j < maShops.size(); j++) {
                if (gioHangs.get(i).getMASHOP().equals(maShops.get(j))) {
                    dem++;
                }
            }
            if (dem == 0) {
                tenShops.add(gioHangs.get(i).getTENNV());
                maShops.add(gioHangs.get(i).getMASHOP());
            }
        }

        chaThongTinDonHangAdapter = new ChaThongTinDonHangAdapter(XacNhanThongTinMuaHangActivity.this, gioHangs, maShops, tenShops);
        rvThongTinDonHang.setHasFixedSize(true);
        rvThongTinDonHang.setNestedScrollingEnabled(true);
        rvThongTinDonHang.setLayoutManager(new LinearLayoutManager(XacNhanThongTinMuaHangActivity.this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(XacNhanThongTinMuaHangActivity.this, DividerItemDecoration.VERTICAL);
        rvThongTinDonHang.addItemDecoration(dividerItemDecoration);
        rvThongTinDonHang.setAdapter(chaThongTinDonHangAdapter);
        chaThongTinDonHangAdapter.notifyDataSetChanged();


        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        for (int i = 0; i < gioHangs.size(); i++) {
            int giachuakm = Integer.parseInt(gioHangs.get(i).getGIASP());
            int km = Integer.parseInt(gioHangs.get(i).getKHUYENMAI());
            int giasp = (giachuakm / 100) * (100 - km);
            int sl = Integer.parseInt(gioHangs.get(i).getSOLUONG());
            tongtien += sl * giasp;
        }
        txtTongTien.setText(decimalFormat.format(tongtien) + "đ");


        sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
        String manv = sharedPreferences.getString("manv", "");
        DataService dataService = APIService.getService();
        Call<List<DiaChiKhachHang>> callback = dataService.getDiaChiKhachHangs(manv);
        callback.enqueue(new Callback<List<DiaChiKhachHang>>() {
            @Override
            public void onResponse(Call<List<DiaChiKhachHang>> call, Response<List<DiaChiKhachHang>> response) {
                diaChiKhachHangs = (ArrayList<DiaChiKhachHang>) response.body();
                if (diaChiKhachHangs.size() > 0) {
                    txtTenNV.setText(diaChiKhachHangs.get(0).getTENKH());
                    txtSdt.setText(diaChiKhachHangs.get(0).getSODTKH());
                    txtDiaChi.setText(diaChiKhachHangs.get(0).getDIACHIKH());
                }
            }

            @Override
            public void onFailure(Call<List<DiaChiKhachHang>> call, Throwable t) {

            }
        });


    }
}
