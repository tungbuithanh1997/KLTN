package ducthuan.com.lamdep.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Activity.BaiVietLamDepLuuTruActivity;
import ducthuan.com.lamdep.Activity.DangNhapActivity;
import ducthuan.com.lamdep.Activity.LoaiLamDepActivity;
import ducthuan.com.lamdep.Model.BaiVietLamDep;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Tab_LamDep extends Fragment implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    String manv = "";
    View view;
    CardView cardDaDep,cardTrangDiem,cardTocDep,cardMacDep,cardDangDep,cardTapLuyen;
    TextView txtMucLuuTru;

    ArrayList<BaiVietLamDep>baiVietLamDeps;

    boolean onpause = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_lam_dep,container,false);
        addControls();
        getBaiVietLuuTru();
        addEvents();


        return view;
    }

    private void getBaiVietLuuTru() {
        if(!manv.equals("")){
            DataService dataService = APIService.getService();
            Call<List<BaiVietLamDep>>call = dataService.getBaiVietLamDepLuuTru(manv);
            call.enqueue(new Callback<List<BaiVietLamDep>>() {
                @Override
                public void onResponse(Call<List<BaiVietLamDep>> call, Response<List<BaiVietLamDep>> response) {
                    baiVietLamDeps = (ArrayList<BaiVietLamDep>) response.body();
                    if(baiVietLamDeps.size()>0){
                        txtMucLuuTru.setText("Mục lưu trữ ("+baiVietLamDeps.size()+")");
                    }else {
                        txtMucLuuTru.setText("Mục lưu trữ");
                    }
                }

                @Override
                public void onFailure(Call<List<BaiVietLamDep>> call, Throwable t) {

                }
            });
        }
    }

    private void addControls() {
        cardDaDep = view.findViewById(R.id.cardDaDep);
        cardTrangDiem = view.findViewById(R.id.cardTrangDiem);
        cardTocDep = view.findViewById(R.id.cardTocDep);
        cardMacDep = view.findViewById(R.id.cardMacDep);
        cardDangDep = view.findViewById(R.id.cardDangDep);
        cardTapLuyen = view.findViewById(R.id.cardTapLuyen);
        txtMucLuuTru = view.findViewById(R.id.txtMucLuuTru);

        sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
        manv = sharedPreferences.getString("manv","");

    }

    private void addEvents() {
        cardDaDep.setOnClickListener(this);
        cardTrangDiem.setOnClickListener(this);
        cardTocDep.setOnClickListener(this);
        cardMacDep.setOnClickListener(this);
        cardDangDep.setOnClickListener(this);
        cardTapLuyen.setOnClickListener(this);
        txtMucLuuTru.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.cardDaDep:
                intent = new Intent(getContext(), LoaiLamDepActivity.class);
                intent.putExtra("loailamdep","1");
                intent.putExtra("tenloailamdep","Da đẹp");
                startActivity(intent);
                break;
            case R.id.cardTrangDiem:
                intent = new Intent(getContext(), LoaiLamDepActivity.class);
                intent.putExtra("loailamdep","2");
                intent.putExtra("tenloailamdep","Trang điểm");
                startActivity(intent);
                break;
            case R.id.cardTocDep:
                intent = new Intent(getContext(), LoaiLamDepActivity.class);
                intent.putExtra("loailamdep","3");
                intent.putExtra("tenloailamdep","Tóc đẹp");
                startActivity(intent);
                break;
            case R.id.cardMacDep:
                intent = new Intent(getContext(), LoaiLamDepActivity.class);
                intent.putExtra("loailamdep","4");
                intent.putExtra("tenloailamdep","Mặc đẹp");
                startActivity(intent);
                break;
            case R.id.cardDangDep:
                intent = new Intent(getContext(), LoaiLamDepActivity.class);
                intent.putExtra("loailamdep","5");
                intent.putExtra("tenloailamdep","Dáng đẹp");
                startActivity(intent);
                break;
            case R.id.cardTapLuyen:
                intent = new Intent(getContext(), LoaiLamDepActivity.class);
                intent.putExtra("loailamdep","6");
                intent.putExtra("tenloailamdep","Tập luyện");
                startActivity(intent);
                break;
            case R.id.txtMucLuuTru:
                if(manv.equals("")){
                    startActivity(new Intent(getContext(), DangNhapActivity.class));
                }else {
                    if(baiVietLamDeps.size()>0){
                        intent = new Intent(getContext(), BaiVietLamDepLuuTruActivity.class);
                        intent.putParcelableArrayListExtra("baivietluutru",baiVietLamDeps);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getContext(), "Chưa có bài viết nào được lưu trữ", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(onpause==true){
            getBaiVietLuuTru();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onpause = true;
    }
}
