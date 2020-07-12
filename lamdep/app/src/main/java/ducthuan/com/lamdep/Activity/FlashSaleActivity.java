package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ducthuan.com.lamdep.Adapter.FlashSaleAdapter;
import ducthuan.com.lamdep.Adapter.FlashSaleNgayKeTiepAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashSaleActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    TabLayout tabLayout;

    TextView txtTimeGio,txtTimePhut, txtTimeGiay ;

    RecyclerView rvSanPham;
    FlashSaleAdapter flashSaleAdapter;
    FlashSaleNgayKeTiepAdapter flashSaleNgayKeTiepAdapter;
    ArrayList<SanPham>sanPhams;

    TextView txtGioHang,txtBatDauKetThuc;
    boolean onpause = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_sale);

        addControls();
        dongHoFlashSale();
        getSanPhamFlashSale();
        addEvents();

    }

    private void getSanPhamFlashSale() {
        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.layDanhSachSanPhamFlashSale(0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    flashSaleAdapter = new FlashSaleAdapter(FlashSaleActivity.this,sanPhams);
                    rvSanPham.setLayoutManager(new LinearLayoutManager(FlashSaleActivity.this));
                    rvSanPham.setNestedScrollingEnabled(true);
                    rvSanPham.setHasFixedSize(true);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(FlashSaleActivity.this,DividerItemDecoration.VERTICAL);
                    rvSanPham.addItemDecoration(dividerItemDecoration);
                    rvSanPham.setAdapter(flashSaleAdapter);
                    flashSaleAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }

    private void getSanPhamFlashSaleNgayKeTiep() {
        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.layDanhSachSanPhamFlashSaleNgayKeTiep(0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    rvSanPham.setEnabled(false);
                    flashSaleNgayKeTiepAdapter = new FlashSaleNgayKeTiepAdapter(FlashSaleActivity.this,sanPhams);
                    rvSanPham.setLayoutManager(new LinearLayoutManager(FlashSaleActivity.this));
                    rvSanPham.setNestedScrollingEnabled(true);
                    rvSanPham.setHasFixedSize(true);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(FlashSaleActivity.this,DividerItemDecoration.VERTICAL);
                    rvSanPham.addItemDecoration(dividerItemDecoration);
                    rvSanPham.setAdapter(flashSaleNgayKeTiepAdapter);
                    flashSaleNgayKeTiepAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);

        txtTimeGio = findViewById(R.id.txtTimeGio);
        txtTimePhut = findViewById(R.id.txtTimePhut);
        txtTimeGiay = findViewById(R.id.txtTimeGiay);
        rvSanPham = findViewById(R.id.rvSanPham);
        tabLayout = findViewById(R.id.tabLayout);
        txtBatDauKetThuc = findViewById(R.id.txtBatDauKetThuc);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        txtBatDauKetThuc.setText("KẾT THÚC TRONG");
                        sanPhams.clear();
                        getSanPhamFlashSale();
                        break;
                    case 1:
                        txtBatDauKetThuc.setText("BẮT ĐẦU TRONG");
                        sanPhams.clear();
                        getSanPhamFlashSaleNgayKeTiep();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void dongHoFlashSale() {

        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        int giay = calendar.get(Calendar.SECOND);
        int tongs = 24*60*60 - (gio*60*60 + phut * 60 + giay);

        new CountDownTimer(tongs * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);

                txtTimeGio.setText(String.format("%02d", hours));
                txtTimePhut.setText(String.format("%02d", minutes));
                txtTimeGiay.setText(String.format("%02d", seconds));
            }

            public void onFinish() {
                this.start();
            }
        }.start();
    }


    //lay san pham gio hang
    public void getDataGioHang(final TextView txtGH) {

        sharedPreferences = getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trang_chu,menu);

        MenuItem item = menu.findItem(R.id.itGioHang);
        View giaoDienCustomGioHang = MenuItemCompat.getActionView(item);
        txtGioHang = giaoDienCustomGioHang.findViewById(R.id.txtSoLuongSanPhamGioHang);
        getDataGioHang(txtGioHang);

        giaoDienCustomGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
                String manv= sharedPreferences.getString("manv","");
                if(manv.equals("")){
                    startActivity(new Intent(FlashSaleActivity.this, DangNhapActivity.class));
                }else {
                    Intent intent = new Intent(FlashSaleActivity.this, GioHangActivity.class);
                    intent.putExtra("trangchu",1);
                    startActivity(intent);
                }

            }
        });
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
