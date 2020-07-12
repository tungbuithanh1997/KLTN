package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ducthuan.com.lamdep.R;

public class DoanhThuActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtTongTien, txtThoiGian,txtTuNgay,txtDenNgay;
    LinearLayout linearLayout;
    RecyclerView rvDoanhThu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doanh_thu);
        addControls();
        addEvents();
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
        txtTongTien = findViewById(R.id.txtTongTien);
        txtThoiGian = findViewById(R.id.txtThoiGian);
        txtTuNgay = findViewById(R.id.txtTuNgay);
        txtDenNgay = findViewById(R.id.txtDenNgay);
        linearLayout = findViewById(R.id.linearLayout);
        rvDoanhThu = findViewById(R.id.rvDoanhThu);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

    }
}
