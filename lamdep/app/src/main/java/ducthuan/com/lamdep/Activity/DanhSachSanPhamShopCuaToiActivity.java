package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.ShopCuaToiAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachSanPhamShopCuaToiActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String manv = "";
    Toolbar toolbar;
    RecyclerView rvDanhSachSanPham;
    public static ShopCuaToiAdapter shopCuaToiAdapter;
    public static ArrayList<SanPham>sanPhams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham_shop_cua_toi);

        addControls();
        getDanhSachSanPham();
        addEvents();

    }

    private void getDanhSachSanPham() {
        DataService dataService = APIService.getService();
        Call<List<SanPham>> callback = dataService.getSanPhamTheoShop(manv,0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    shopCuaToiAdapter = new ShopCuaToiAdapter(DanhSachSanPhamShopCuaToiActivity.this,sanPhams);
                    rvDanhSachSanPham.setLayoutManager(new GridLayoutManager(DanhSachSanPhamShopCuaToiActivity.this, 2));
                    rvDanhSachSanPham.setHasFixedSize(true);
                    rvDanhSachSanPham.setNestedScrollingEnabled(true);
                    rvDanhSachSanPham.setAdapter(shopCuaToiAdapter);
                    shopCuaToiAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
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
        rvDanhSachSanPham = findViewById(R.id.rvDanhSachSanPham);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        sharedPreferences = getSharedPreferences("dangnhap",MODE_PRIVATE);
        manv = sharedPreferences.getString("manv","");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timkiem_danhsachsanpham_shopcuatoi,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itTimKiem:
                Toast.makeText(this, "Tìm kiếm trong shop của tôi", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
