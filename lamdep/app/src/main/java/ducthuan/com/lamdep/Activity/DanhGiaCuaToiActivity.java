package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhGiaCuaToiActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    RecyclerView rvSanPham;

    TextView txtGioHang;
    boolean onpause;

    String manv = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia_cua_toi);

        Intent intent = getIntent();
        if(intent.hasExtra("manv")){
            manv = intent.getStringExtra("manv");
            addControls();
            addEvents();
        }
    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        rvSanPham = findViewById(R.id.rvSanPham);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
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
                    startActivity(new Intent(DanhGiaCuaToiActivity.this, DangNhapActivity.class));
                }else {
                    Intent intent = new Intent(DanhGiaCuaToiActivity.this, GioHangActivity.class);
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
