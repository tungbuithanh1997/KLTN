package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import ducthuan.com.lamdep.Activity.ChiTietSanPhamActivity;
import ducthuan.com.lamdep.Activity.ThemSanPhamShopCuaToiActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;

public class ShopCuaToiAdapter extends RecyclerView.Adapter<ShopCuaToiAdapter.ViewHolder> {

    Context context;
    ArrayList<SanPham>sanPhams;

    public ShopCuaToiAdapter(Context context, ArrayList<SanPham> sanPhams) {
        this.context = context;
        this.sanPhams = sanPhams;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_sanpham_shopcuatoi,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sanPham = sanPhams.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+sanPham.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSP);
        holder.txtTenSP.setText(sanPham.getTENSP());
        holder.txtLuotThich.setText(sanPham.getLUOTTHICH());
        holder.txtLuotXem.setText(sanPham.getLUOTXEM());
        holder.txtDaBan.setText("Đã bán "+sanPham.getLUOTMUA());

        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        int km = Integer.parseInt(sanPham.getKHUYENMAI());
        int gsp = Integer.parseInt(sanPham.getGIA());
        int giakm = (gsp/100)* (100-km);
        holder.txtGiaSP.setText(String.valueOf(decimalFormat.format(gsp))+"đ");
    }

    @Override
    public int getItemCount() {
        return sanPhams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhSP;
        TextView txtTenSP,txtGiaSP,txtLuotThich,txtLuotXem,txtDaBan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSP = itemView.findViewById(R.id.imgHinhSP);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtGiaSP = itemView.findViewById(R.id.txtGiaSP);
            txtLuotThich = itemView.findViewById(R.id.txtLuotThich);
            txtLuotXem = itemView.findViewById(R.id.txtLuotXem);
            txtDaBan = itemView.findViewById(R.id.txtDaBan);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
                    intent.putExtra("itemsp",sanPhams.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
