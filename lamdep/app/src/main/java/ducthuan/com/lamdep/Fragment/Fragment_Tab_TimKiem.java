package ducthuan.com.lamdep.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Activity.TimKiemTrangChuActivity;
import ducthuan.com.lamdep.Adapter.HienThiSanPhamTheoDanhMucAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Tab_TimKiem extends Fragment {
    View view;
    SearchView searchView;
    RecyclerView rvSanPham;
    ArrayList<SanPham> sanPhams;
    HienThiSanPhamTheoDanhMucAdapter hienThiSanPhamTheoDanhMucAdapter;

    LinearLayout linearLayout;
    Button btnThuLaiTuKhoa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_tim_kiem,container,false);
        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                DataService dataService = APIService.getService();
                Call<List<SanPham>> callback = dataService.timKiemSPTrangChu(query,0);
                callback.enqueue(new Callback<List<SanPham>>() {
                    @Override
                    public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                        sanPhams = (ArrayList<SanPham>) response.body();
                        if(sanPhams.size()>0){
                            linearLayout.setVisibility(View.GONE);
                            rvSanPham.setVisibility(View.VISIBLE);
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
                            hienThiSanPhamTheoDanhMucAdapter = new HienThiSanPhamTheoDanhMucAdapter(getActivity(),2,sanPhams);
                            rvSanPham.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rvSanPham.setHasFixedSize(true);
                            rvSanPham.setNestedScrollingEnabled(true);
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
                            rvSanPham.addItemDecoration(dividerItemDecoration);
                            rvSanPham.setAdapter(hienThiSanPhamTheoDanhMucAdapter);
                            hienThiSanPhamTheoDanhMucAdapter.notifyDataSetChanged();
                        }else {
                            rvSanPham.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SanPham>> call, Throwable t) {

                    }
                });



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btnThuLaiTuKhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(Fragment_Tab_TimKiem.this).attach(Fragment_Tab_TimKiem.this).commit();

            }
        });

    }

    private void addControls() {
        searchView = view.findViewById(R.id.searchView);
        rvSanPham = view.findViewById(R.id.rvSanPham);
        linearLayout = view.findViewById(R.id.linearLayout);
        btnThuLaiTuKhoa = view.findViewById(R.id.btnThuLaiTuKhoa);

    }
}
