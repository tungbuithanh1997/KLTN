package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

import ducthuan.com.lamdep.Adapter.ChiTietDonHangShopAdapter;
import ducthuan.com.lamdep.Adapter.QuanLyDonHangShopAdapter;
import ducthuan.com.lamdep.Model.QuanLyDonHangShop;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietDonHangShopActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<QuanLyDonHangShop>quanLyDonHangShops;
    ChiTietDonHangShopAdapter chiTietDonHangShopAdapter;
    RecyclerView rvSanPham;


    Spinner spinnerTinhTrang;
    String []trangthais={"Chờ xác nhận","Đang giao","Đã giao","Đã hủy","Trả hàng"};
    ArrayAdapter adapter;

    Spinner spinnerChuyenKhoan;
    String []chuyenkhoans = {"Chưa chuyển khoản","Đã chuyển khoản"};
    ArrayAdapter adapterChuyenKhoan;

    TextView txtTenKH,txtSdt,txtDiaChi,txtPTVanChuyen,txtThanhToan,txtTenShop,txtGheThamShop,txtMaDH,txtNgayMua,txtNgayGiao,txtTongTien;
    Button btnCapNhapTrangThai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang_shop);
        Intent intent = getIntent();
        if(intent.hasExtra("donhang")){
            quanLyDonHangShops = intent.getParcelableArrayListExtra("donhang");
            addControls();
            addEvents();
        }else {
            Toast.makeText(this, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
        }
    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtGheThamShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietDonHangShopActivity.this,ShopActivity.class);
                intent.putExtra("tennv",quanLyDonHangShops.get(0).getTENSHOP());
                startActivity(intent);
            }
        });


        btnCapNhapTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mahd = txtMaDH.getText().toString();
                String trangthai = spinnerTinhTrang.getSelectedItem().toString();

                DataService dataService = APIService.getService();
                Call<String>callback = dataService.capNhapTrangThaiDonHang(mahd,trangthai);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String ketqua = response.body();
                        if(ketqua.equals("OK")){
                            Toast.makeText(ChiTietDonHangShopActivity.this, "Cập nhập trạng thái đơn hàng thành công !", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ChiTietDonHangShopActivity.this, "Cập nhập trạng thái đơn hàng thất bại !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(ChiTietDonHangShopActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }



    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        spinnerTinhTrang = findViewById(R.id.spinnerTinhTrang);
        spinnerChuyenKhoan = findViewById(R.id.spinnerChuyenKhoan);
        btnCapNhapTrangThai = findViewById(R.id.btnCapNhapTrangThai);
        txtTenKH = findViewById(R.id.txtTenKH);
        txtSdt = findViewById(R.id.txtSdt);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtPTVanChuyen = findViewById(R.id.txtPTVanChuyen);
        txtThanhToan = findViewById(R.id.txtThanhToan);
        txtTenShop = findViewById(R.id.txtTenShop);
        txtGheThamShop = findViewById(R.id.txtGheThamShop);
        txtMaDH = findViewById(R.id.txtMaDH);
        txtNgayMua = findViewById(R.id.txtNgayMua);
        txtNgayGiao = findViewById(R.id.txtNgayGiao);
        rvSanPham = findViewById(R.id.rvSanPham);
        txtTongTien = findViewById(R.id.txtTongTien);

        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        adapter = new ArrayAdapter(ChiTietDonHangShopActivity.this,android.R.layout.simple_spinner_dropdown_item,trangthais);
        spinnerTinhTrang.setAdapter(adapter);

        adapterChuyenKhoan = new ArrayAdapter(ChiTietDonHangShopActivity.this,android.R.layout.simple_spinner_dropdown_item,chuyenkhoans);
        spinnerChuyenKhoan.setAdapter(adapterChuyenKhoan);

        //xử lý địa chỉ
        txtTenKH.setText(quanLyDonHangShops.get(0).getTENNGUOINHAN());
        txtSdt.setText(quanLyDonHangShops.get(0).getSODT());
        txtDiaChi.setText(quanLyDonHangShops.get(0).getDIACHI());

        //xử lý vận chuyển
        txtPTVanChuyen.setText(quanLyDonHangShops.get(0).getVANCHUYEN());
        txtNgayGiao.setText("Dự kiến giao hàng vào ngày "+quanLyDonHangShops.get(0).getNGAYGIAO());
        //xử lý thanh toán
        if(quanLyDonHangShops.get(0).getTHANHTOAN().equals("Chuyển khoản")){
            spinnerChuyenKhoan.setVisibility(View.VISIBLE);
            if(quanLyDonHangShops.get(0).getCHUYENKHOAN().equals("Chưa chuyển khoản")){
                spinnerChuyenKhoan.setSelection(0);
            }else if(quanLyDonHangShops.get(0).getCHUYENKHOAN().equals("Đã chuyển khoản")){
                spinnerChuyenKhoan.setSelection(1);
            }

        }
        txtThanhToan.setText(quanLyDonHangShops.get(0).getTHANHTOAN());

        StringTokenizer stringTokenizer = new StringTokenizer(quanLyDonHangShops.get(0).getTENSHOP(),"@");

        txtTenShop.setText(stringTokenizer.nextToken());

        //xu ly hien thi rv
        chiTietDonHangShopAdapter = new ChiTietDonHangShopAdapter(ChiTietDonHangShopActivity.this,quanLyDonHangShops);
        rvSanPham.setHasFixedSize(true);
        rvSanPham.setNestedScrollingEnabled(true);
        rvSanPham.setLayoutManager(new LinearLayoutManager(ChiTietDonHangShopActivity.this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ChiTietDonHangShopActivity.this,DividerItemDecoration.VERTICAL);
        rvSanPham.addItemDecoration(dividerItemDecoration);
        rvSanPham.setAdapter(chiTietDonHangShopAdapter);
        chiTietDonHangShopAdapter.notifyDataSetChanged();

        txtMaDH.setText(quanLyDonHangShops.get(0).getMAHD());
        txtNgayMua.setText(quanLyDonHangShops.get(0).getNGAYMUA());

        txtTongTien.setText(quanLyDonHangShops.get(0).getTONGTIEN());

        if(quanLyDonHangShops.get(0).getTRANGTHAI().equals("Chờ xác nhận")){
            spinnerTinhTrang.setSelection(0);
        }else if(quanLyDonHangShops.get(0).getTRANGTHAI().equals("Đang giao")){
            spinnerTinhTrang.setSelection(1);
        }else if(quanLyDonHangShops.get(0).getTRANGTHAI().equals("Đã giao")){
            spinnerTinhTrang.setSelection(2);
        }else if(quanLyDonHangShops.get(0).getTRANGTHAI().equals("Đã hủy")){
            spinnerChuyenKhoan.setEnabled(false);
            spinnerTinhTrang.setEnabled(false);
            spinnerTinhTrang.setSelection(3);
        }else if(quanLyDonHangShops.get(0).getTRANGTHAI().equals("Trả hàng")){
            spinnerChuyenKhoan.setEnabled(false);
            spinnerTinhTrang.setEnabled(false);
            spinnerTinhTrang.setSelection(4);
        }


    }
}
