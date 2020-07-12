package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import ducthuan.com.lamdep.Activity.ChiTietSanPhamActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoSuuTapYeuThichAdapter extends RecyclerView.Adapter<BoSuuTapYeuThichAdapter.ViewHolder> {

    Context context;
    ArrayList<SanPham> sanPhamTimKiems;

    public BoSuuTapYeuThichAdapter(Context context, ArrayList<SanPham> sanPhamTimKiems) {
        this.context = context;
        this.sanPhamTimKiems = sanPhamTimKiems;
    }

    @NonNull
    @Override
    public BoSuuTapYeuThichAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_bosuutap_yeuthich_activity, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final SanPham sanPhamTimKiem = sanPhamTimKiems.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+sanPhamTimKiem.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSPTK);
        holder.txtLuotMua.setText("Đã bán: "+sanPhamTimKiem.getLUOTMUA());
        holder.txtTenSanPham.setText(sanPhamTimKiem.getTENSP());
        //giá sản phẩm sau khi khuyến mãi
        int km = Integer.parseInt(sanPhamTimKiem.getKHUYENMAI());
        int giachuakm = Integer.parseInt(sanPhamTimKiem.getGIA());
        int gsp = (giachuakm/100)*(100-km);
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        holder.txtGiaSP.setText(decimalFormat.format(gsp)+"đ");
        holder.txtLuotThich.setText("Lượt thích "+sanPhamTimKiem.getLUOTTHICH());

    }


    @Override
    public int getItemCount() {
        return sanPhamTimKiems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgHinhSPTK,imgLike;
        TextView txtTenSanPham,txtLuotMua,txtGiaSP,txtLuotThich;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSPTK = itemView.findViewById(R.id.imgHinhSPTK);
            txtTenSanPham = itemView.findViewById(R.id.txtTenSanPham);
            txtLuotMua = itemView.findViewById(R.id.txtLuotMua);
            txtGiaSP = itemView.findViewById(R.id.txtGiaSP);
            txtLuotThich = itemView.findViewById(R.id.txtLuotThich);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
                    intent.putExtra("itemsp",sanPhamTimKiems.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }

}
