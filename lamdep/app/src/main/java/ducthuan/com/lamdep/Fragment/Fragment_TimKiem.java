package ducthuan.com.lamdep.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Activity.TimKiemHangDauActivity;
import ducthuan.com.lamdep.Adapter.SanPhamTimKiemAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_TimKiem extends Fragment {
    View view;
    RecyclerView rvTimKiemHangDau;
    ArrayList<SanPham>sanPhamTimKiems;
    SanPhamTimKiemAdapter sanPhamTimKiemAdapter;

    TextView txtXemThemTimKiem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tim_kiem, container,false);

        addControls();
        getDataSanPhamTimKiem();
        addEvents();

        return view;
    }

    private void addEvents() {
        txtXemThemTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimKiemHangDauActivity.class);
                intent.putParcelableArrayListExtra("sanphamtimkiems",sanPhamTimKiems);
                startActivity(intent);
            }
        });

    }

    private void addControls() {
        rvTimKiemHangDau = view.findViewById(R.id.rvTimKiemHangDau);
        txtXemThemTimKiem = view.findViewById(R.id.txtXemThemTimKiem);
    }

    private void getDataSanPhamTimKiem() {
        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.layDanhSachSanPhamTimKiem();
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhamTimKiems = (ArrayList<SanPham>) response.body();
                if(sanPhamTimKiems.size()>0){
                    sanPhamTimKiemAdapter = new SanPhamTimKiemAdapter(getActivity(), sanPhamTimKiems);
                    rvTimKiemHangDau.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                    rvTimKiemHangDau.setHasFixedSize(true);
                    rvTimKiemHangDau.setAdapter(sanPhamTimKiemAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }
}
