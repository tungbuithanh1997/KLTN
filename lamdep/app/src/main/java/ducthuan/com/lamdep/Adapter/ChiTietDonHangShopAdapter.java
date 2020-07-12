package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.QuanLyDonHangShop;
import ducthuan.com.lamdep.R;

public class ChiTietDonHangShopAdapter extends RecyclerView.Adapter<ChiTietDonHangShopAdapter.ViewHolder>{

    Context context;
    ArrayList<QuanLyDonHangShop>quanLyDonHangShops;

    public ChiTietDonHangShopAdapter(Context context, ArrayList<QuanLyDonHangShop> quanLyDonHangShops) {
        this.context = context;
        this.quanLyDonHangShops = quanLyDonHangShops;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_chitietdonhang_shop,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuanLyDonHangShop quanLyDonHangShop = quanLyDonHangShops.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+quanLyDonHangShop.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSP);
        holder.txtTenSP.setText(quanLyDonHangShop.getTENSP());
        holder.txtPhanLoaiSP.setText(quanLyDonHangShop.getMAUSAC()+", "+quanLyDonHangShop.getKICHTHUOC());

        //giá sản phẩm sau khi khuyến mãi
        int km = Integer.parseInt(quanLyDonHangShop.getKHUYENMAI());
        int giachuakm = Integer.parseInt(quanLyDonHangShop.getGIASP());
        int gsp = (giachuakm/100)*(100-km);
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        holder.txtGiaSP.setText(decimalFormat.format(gsp)+"đ");
        holder.txtSoLuongSP.setText("x "+quanLyDonHangShop.getSOLUONG());
    }

    @Override
    public int getItemCount() {
        return quanLyDonHangShops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhSP;
        TextView txtTenSP,txtPhanLoaiSP,txtGiaSP,txtSoLuongSP;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSP = itemView.findViewById(R.id.imgHinhSP);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtPhanLoaiSP = itemView.findViewById(R.id.txtPhanLoaiSP);
            txtGiaSP = itemView.findViewById(R.id.txtGiaSP);
            txtSoLuongSP = itemView.findViewById(R.id.txtSoLuongSP);
        }
    }
}
