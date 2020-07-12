package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ducthuan.com.lamdep.Fragment.Fragment_Tab_DanhMuc;
import ducthuan.com.lamdep.Fragment.Fragment_Tab_TaiKhoan;
import ducthuan.com.lamdep.Fragment.Fragment_Tab_LamDep;
import ducthuan.com.lamdep.Fragment.Fragment_Tab_TimKiem;
import ducthuan.com.lamdep.Fragment.Fragment_Tab_TrangChu;
import ducthuan.com.lamdep.R;

public class TrangChuActivity extends AppCompatActivity {

    public static final String base_url = "https://webt2.000webhostapp.com/webt2/";
    //public static final String base_url = "http://172.17.16.153/webt2/";//ktx
    //public static final String base_url = "http://10.45.249.198/webt2/";
    //public static final String base_url = "http://172.17.23.52/webt2/";
    //public static final String base_url = "http://192.168.1.7/webt2/";//kem xoi
    //public static final String base_url = "http://192.168.43.56/webt2/";//quan cafe vk day them

    FrameLayout frameLayout_Content;
    BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (CheckConnect.haveNetworkConnection(getApplicationContext())) {
        setContentView(R.layout.activity_trang_chu);


        addControls();
        addEvents();
        //getDataSanPhamGoiY();
        /*} else {
            CheckConnect.ShowToast_Short(getApplicationContext(), "Vui lòng kiểm tra lại kết nối!!!");
        }*/
    }


    //add cac controls
    private void addControls() {
        frameLayout_Content = findViewById(R.id.frameLayout_Content);
        bottomNav = findViewById(R.id.bottomNav);

        /*//lấy ra navigation menu
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNav.getChildAt(0);
        //lấy vị trí item
        View view = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) view;
        View category = LayoutInflater.from(this).inflate(R.layout.custom_item_thongbao, bottomNavigationMenuView, false);
        itemView.addView(category);*/

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Content,new Fragment_Tab_TrangChu()).commit();


    }

    private void addEvents() {
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment seletedFragment = null;
                switch (menuItem.getItemId()){
                    case R.id.itTrangChu:
                        seletedFragment = new Fragment_Tab_TrangChu();
                        break;
                    case R.id.itDanhMuc:
                        seletedFragment = new Fragment_Tab_DanhMuc();
                        break;
                    case R.id.itTimKiem:
                        seletedFragment = new Fragment_Tab_TimKiem();
                        break;
                    case R.id.itLamDep:
                        seletedFragment = new Fragment_Tab_LamDep();
                        break;
                    case R.id.itTaiKhoan:
                        seletedFragment = new Fragment_Tab_TaiKhoan();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Content,seletedFragment).commit();
                return true;
            }
        });
    }

}
