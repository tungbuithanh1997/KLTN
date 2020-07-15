package ducthuan.com.lamdep.Service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ducthuan.com.lamdep.Model.BaiVietLamDep;
import ducthuan.com.lamdep.Model.ChiTietDonHang;
import ducthuan.com.lamdep.Model.ChiTietSanPham;
import ducthuan.com.lamdep.Model.DanhGia;
import ducthuan.com.lamdep.Model.DiaChiKhachHang;
import ducthuan.com.lamdep.Model.GioHang;
import ducthuan.com.lamdep.Model.LoaiLamDep;
import ducthuan.com.lamdep.Model.LoaiSanPham;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.Model.QuanLyDonHangShop;
import ducthuan.com.lamdep.Model.QuangCao;
import ducthuan.com.lamdep.Model.SanPham;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataService {

    //kiểm tra đăng ký
    @FormUrlEncoded
    @POST("kiemtradangky.php")
    Call<String>kiemTraDangKy(@Field("name") String name,@Field("email") String email, @Field("matkhau") String matkhau);

    //Kiem tra dang nhap
    @FormUrlEncoded
    @POST("kiemtradangnhap.php")
    Call<List<NhanVien>>kiemTraDangNhap(@Field("email") String email, @Field("matkhau") String matkhau);

    //get thông tin nhân viên
    @FormUrlEncoded
    @POST("getnhanvien.php")
    Call<List<NhanVien>>getNhanVien(@Field("tennv") String tennv);

    //Lay danh sach quang cao trang chu
    @GET("quangcaotheongay.php")
    Call<List<QuangCao>>layQuangCaoTheoNgay();

    //Lay danh sach san pham flashsale hom nay
    @FormUrlEncoded
    @POST("getsanphamflashsale.php")
    Call<List<SanPham>>layDanhSachSanPhamFlashSale(@Field("limit") int limit);
    //Lay danh sach san pham flashsale
    @FormUrlEncoded
    @POST("getsanphamflashsalengayketiep.php")
    Call<List<SanPham>>layDanhSachSanPhamFlashSaleNgayKeTiep(@Field("limit") int limit);

    //Lay danh sach loaisp
    @GET("getloaisanpham.php")
    Call<List<LoaiSanPham>>layDanhSachLoaiSanPham();

    //Lay danh sach sp tim kiem
    @GET("getsanphamtimkiem.php")
    Call<List<SanPham>>layDanhSachSanPhamTimKiem();
    //Lay danh sach sp yeu thich
    @GET("getsanphamyeuthich.php")
    Call<List<SanPham>>layDanhSachSanPhamYeuThich();

    //Lay danh sach sp goi y
    @GET("getsanphamgoiy.php")
    Call<List<SanPham>>layDanhSachSanPhamGoiY();

    //lay danh sach sp theo danh muc moi nhat
    @FormUrlEncoded
    @POST("getsanphamtheodanhmuc.php")
    Call<List<SanPham>>getThoiTrangNus(@Field("maloaisp") String maloaisp,@Field("limit") int limit);

    //lay danh sach sp co the ban thich
    @FormUrlEncoded
    @POST("getspcothebanthich.php")
    Call<List<SanPham>>getSPCoTheBanThich(@Field("maloaisp") String maloaisp);

    //lay danh sach sp theo danh muc ban chay
    @FormUrlEncoded
    @POST("getsanphamtheodanhmucbanchay.php")
    Call<List<SanPham>>getThoiTrangNusBanChay(@Field("maloaisp") String maloaisp,@Field("limit") int limit);

    //lay danh sach sp theo danh muc gia tang
    @FormUrlEncoded
    @POST("getsanphamtheodanhmucgiatang.php")
    Call<List<SanPham>>getThoiTrangNusGiaTang(@Field("maloaisp") String maloaisp,@Field("limit") int limit);

    //lay danh sach sp theo danh muc gia giam
    @FormUrlEncoded
    @POST("getsanphamtheodanhmucgiagiam.php")
    Call<List<SanPham>>getThoiTrangNusGiaGiam(@Field("maloaisp") String maloaisp,@Field("limit") int limit);

    //lay chi tiet sp
    @FormUrlEncoded
    @POST("getchitietsanpham.php")
    Call<List<ChiTietSanPham>>getChiTietSanPham(@Field("masp") String masp);

    //them du lieu dang nhap facebook
    @FormUrlEncoded
    @POST("luuthongtinfacebook.php")
    Call<String>ketQuaLuuFB(@Field("name") String name,@Field("email") String email,@Field("hinh") String hinh);

    //thêm đánh giá
    @FormUrlEncoded
    @POST("themdanhgia.php")
    Call<String>ketQuaThemDanhGia(@Field("masp") String masp,@Field("manv") String manv, @Field("tieude") String tieude,
                                  @Field("noidung") String noidung,@Field("sosao") String sosao);

    //get danh gia show chi tiet sp
    @FormUrlEncoded
    @POST("getdanhsachdanhgia.php")
    Call<List<DanhGia>>getDanhGiaCTSP(@Field("masp") String masp);

    //Update them gio hang
    @FormUrlEncoded
    @POST("themgiohang.php")
    Call<String>themGioHang(@Field("masp") String masp,@Field("manv") String manv,@Field("mausac") String mausac,
                            @Field("kichthuoc") String kichthuoc,@Field("soluong") String soluong);

    //get so luong san pham trong gio hang
    @FormUrlEncoded
    @POST("getsoluongsanphamgiohang.php")
    Call<String>getSoLuongSPGioHang(@Field("manv") String manv);

    //get danh sach san pham trong gio hang
    @FormUrlEncoded
    @POST("getsanphamgiohang.php")
    Call<List<GioHang>>getDanhSachSPGioHang(@Field("manv") String manv);

    //Xoa sp trong gio hang
    @FormUrlEncoded
    @POST("xoasanphamgiohang.php")
    Call<String>xoaSanPhamGioHang(@Field("masp") String masp, @Field("manv") String manv);

    //cap nhap so luong gio hang
    @FormUrlEncoded
    @POST("capnhapgiohangtanggiamsoluong.php")
    Call<String>capNhapSLSPGioHang(@Field("masp") String masp, @Field("manv") String manv,@Field("soluong") int soluong);

    //them dia chi khach hang lan dau
    @FormUrlEncoded
    @POST("luuthongtinkhachhang.php")
    Call<String>luuDiaChiKhachHang(@Field("manv") String manv, @Field("tennv") String tennv,@Field("diachi") String diachi
            ,@Field("sodt") String sodt,@Field("email") String email);

    //them dia chi khach hang
    @FormUrlEncoded
    @POST("themdiachikhachhang.php")
    Call<String>themDiaChiKhachHang(@Field("manv") String manv, @Field("tennv") String tennv,@Field("diachi") String diachi
            ,@Field("sodt") String sodt,@Field("email") String email,@Field("macdinh") String macdinh);

    //Kiem tra dia chi khach hang co chua
    @FormUrlEncoded
    @POST("kiemtradiachikhachhang.php")
    Call<List<DiaChiKhachHang>>getDiaChiKhachHangs(@Field("manv") String manv);

    //get dia chi khach hang
    @FormUrlEncoded
    @POST("getdiachikhachhang.php")
    Call<List<DiaChiKhachHang>>getDanhSachDiaChiKhachHangs(@Field("manv") String manv);

    //set dia chi mac dinh
    @FormUrlEncoded
    @POST("setdiachimacdinh.php")
    Call<String>setDiaChiMacDinh(@Field("makh") String makh,@Field("madc") String madc);

    //xoa dia chi
    @FormUrlEncoded
    @POST("xoadiachikhachhang.php")
    Call<String>xoaDiaChiKhachHang(@Field("madc") String madc);


    //Cap nhap trang thai duoc chon san pham gio hang
    @FormUrlEncoded
    @POST("capnhapchongiohang.php")
    Call<String>capNhapChonSPGioHang(@Field("masp") String masp,@Field("manv") String manv,@Field("duocchon") String duocchon);

    //Cập nhập chọn tất cả sản phẩm trong giỏ hàng
    @FormUrlEncoded
    @POST("capnhapchontatcagiohang.php")
    Call<String>capNhapChonTatCaSPGioHang(@Field("manv") String manv,@Field("duocchon") String duocchon);

    //get sản phẩm được chọn
    @FormUrlEncoded
    @POST("getsanphamduocchon.php")
    Call<List<SanPham>>getSanPhamDuocChon(@Field("masp") String masp);

    //get sản phẩm mua ngay
    @FormUrlEncoded
    @POST("getsanphammuangay.php")
    Call<List<GioHang>>getSanPhamMuaNgay(@Field("masp") String masp,@Field("manv") String manv);

    //update phan loai san pham
    @FormUrlEncoded
    @POST("suaphanloaisanpham.php")
    Call<String>suaPhanLoaiSP(@Field("masp") String masp,@Field("manv") String manv,@Field("mausac") String mausac,@Field("kichthuoc") String kichthuoc);

    //get tinh trang thich cua san pham
    @FormUrlEncoded
    @POST("gettinhtrangthichsanpham.php")
    Call<String>getTinhTrangThichSP(@Field("masp") String masp,@Field("manv") String manv);

    //cap nhap luot thich san pham
    @FormUrlEncoded
    @POST("capnhapluotthichsanpham.php")
    Call<String>capNhapLuotThichSP(@Field("masp") String masp,@Field("manv") String manv);

    //get san pham khac cua shop
    @FormUrlEncoded
    @POST("getsanphamkhaccuashop.php")
    Call<List<SanPham>>getSanPhamKhacCuaShop(@Field("mashop") String mashop);

    //Them hoa don
    @FormUrlEncoded
    @POST("themhoadon.php")
    Call<String>themHoaDon(@Field("giohangs") JSONArray jsonArray, @Field("tongtiens") String tongTiens
            , @Field("mashops") String maShops, @Field("loinhans") String loinhans
            , @Field("manv") String manv, @Field("tennguoinhan") String tennguoinhan, @Field("sodt") String sodt
            , @Field("diachi") String diachi,@Field("tongtien") String tongtien,@Field("vanchuyen") String vanchuyen,@Field("thanhtoan") String thanhtoan);

    //Xoa san pham duoc mua gio hang khi thnh toan thanh cong
    @FormUrlEncoded
    @POST("xoasanphamduocmuatronggiohang.php")
    Call<String>xoaSanPhamDuocMuaTrongGioHang(@Field("manv") String manv);

    //get data man hinh thanh toan thanh cong
    @FormUrlEncoded
    @POST("getdatamanhinhdathangthanhcong.php")
    Call<List<ChiTietDonHang>>getDataDatHangThanhCong(@Field("mahdtong") String mahdtong);

    //get san pham theo shop
    @FormUrlEncoded
    @POST("getsanphamtheoshop.php")
    Call<List<SanPham>>getSanPhamTheoShop(@Field("manv") String manv, @Field("limit") int limit);

    //get don hang shop
    @FormUrlEncoded
    @POST("getdonhangshop.php")
    Call<List<QuanLyDonHangShop>>getDonHangShop(@Field("manv") String manv);

    //cap nhap trang thai don hang
    @FormUrlEncoded
    @POST("capnhaptrangthaidonhang.php")
    Call<String>capNhapTrangThaiDonHang(@Field("mahd") String mahd,@Field("trangthai") String trangthai);

    //get don hang khach hang
    @FormUrlEncoded
    @POST("getdonhangkhachhang.php")
    Call<List<QuanLyDonHangShop>>getDonHangKhachHang(@Field("manv") String manv);

    //get san pham shop moi nhat
    @FormUrlEncoded
    @POST("getsanphamshop.php")
    Call<List<SanPham>>getSanPhamShop(@Field("tennv") String tennv,@Field("limit") int limit);

    //get san pham shop ban chay
    @FormUrlEncoded
    @POST("getsanphamshopbanchay.php")
    Call<List<SanPham>>getSanPhamShopBanChay(@Field("tennv") String tennv,@Field("limit") int limit);

    //get san pham shop gia tang
    @FormUrlEncoded
    @POST("getsanphamshopgiatang.php")
    Call<List<SanPham>>getSanPhamShopGiaTang(@Field("tennv") String tennv,@Field("limit") int limit);

    //get san pham shop gia giam
    @FormUrlEncoded
    @POST("getsanphamshopgiagiam.php")
    Call<List<SanPham>>getSanPhamShopGiaGiam(@Field("tennv") String tennv,@Field("limit") int limit);

    //tim kiem san pham trang chu
    @FormUrlEncoded
    @POST("timkiemsanphamtrangchu.php")
    Call<List<SanPham>>timKiemSPTrangChu(@Field("tensp") String tensp,@Field("limit") int limit);

    //tim kiem san pham danh muc
    @FormUrlEncoded
    @POST("timkiemsanphamdanhmuc.php")
    Call<List<SanPham>>timKiemSPDanhMuc(@Field("tensp") String tensp,@Field("maloaisp") String maloaisp,@Field("limit") int limit);

    //tim kiem san pham shop
    @FormUrlEncoded
    @POST("timkiemsanphamshop.php")
    Call<List<SanPham>>timKiemSPShop(@Field("tensp") String tensp,@Field("manv") String manv,@Field("limit") int limit);


    //them san pham da xem
    @FormUrlEncoded
    @POST("capnhapluotxem.php")
    Call<String>capNhapLuotXem(@Field("masp") String masp,@Field("manv") String manv);

    //get san pham da xem
    @FormUrlEncoded
    @POST("getsanphamdaxem.php")
    Call<List<SanPham>>getSanPhamDaXem(@Field("manv") String manv,@Field("limit") int limit);

    //get san pham yeu thich tab tai khoan
    @FormUrlEncoded
    @POST("getsanphamyeuthichtabtaikhoan.php")
    Call<List<SanPham>>getSanPhamYeuThichTabTaiKhoan(@Field("manv") String manv,@Field("limit") int limit);

    //gui file len sv
    @Multipart
    @POST("uploadhinhanh.php")
    Call<String>upLoadHinhAnh(@Part MultipartBody.Part hinh);
    //update thong tin tai khoan
    @FormUrlEncoded
    @POST("capnhapthongtintaikhoan.php")
    Call<String>capNhapThongTinTaiKhoan(@Field("manv") String manv,@Field("hoten") String hoten,@Field("sodt") String sodt,@Field("ngaysinh") String ngaysinh,@Field("gioitinh") String gioitinh,@Field("hinh") String hinh);

    //upload multiple
    @Multipart
    @POST("uploadhinhsanpham.php")
    Call<String> uploadMultipleFilesDynamic(
            @Part("description") RequestBody description,
            @Part("size") RequestBody size,
            @Part List<MultipartBody.Part> files);

    //add product
    @FormUrlEncoded
    @POST("addproduct.php")
    Call<String>addProduct(@Field("tensp") String tensp,@Field("gia") String gia,@Field("khuyenmai") String khuyenmai,
                           @Field("anhlon") String anhlon,@Field("anhnho") String anhnho,@Field("thongtin") String thongtin,
                           @Field("soluong") String soluong,@Field("maloaisp") String maloaisp,@Field("manv") String manv);

    //edit product
    @FormUrlEncoded
    @POST("editproduct.php")
    Call<String>editProduct(@Field("masp") String masp,@Field("tensp") String tensp,@Field("gia") String gia,
                           @Field("khuyenmai") String khuyenmai,@Field("thongtin") String thongtin,
                           @Field("soluong") String soluong,@Field("maloaisp") String maloaisp);

    //delete product
    @FormUrlEncoded
    @POST("deleteproduct.php")
    Call<String>deleteProduct(@Field("masp") String masp);

    //get loai lam dep
    @FormUrlEncoded
    @POST("getloailamdep.php")
    Call<List<LoaiLamDep>>getLoaiLamDep(@Field("malamdep") String malamdep);

    //get danh sach bai viet lam dep
    @FormUrlEncoded
    @POST("getdanhsachbaivietlamdep.php")
    Call<List<BaiVietLamDep>>getDanhSachBaiVietLamDep(@Field("maloailamdep") String maloailamdep);

    //get tinh trang luu bai viet lam dep
    @FormUrlEncoded
    @POST("gettinhtrangluutrubaivietlamdep.php")
    Call<String>getTinhTrangLuuBaiVietLamDep(@Field("mabaiviet") String mabaiviet,@Field("manv") String manv);

    //cap nhap tinh trang luu bai viet lam dep
    @FormUrlEncoded
    @POST("capnhaptinhtrangluutrubaivietlamdep.php")
    Call<String>capNhapLuuTruBaiViet(@Field("mabaiviet") String mabaiviet,@Field("manv") String manv);

    //get danh sach bai viet lam dep luu tru
    @FormUrlEncoded
    @POST("getbaivietlamdepluutru.php")
    Call<List<BaiVietLamDep>>getBaiVietLamDepLuuTru(@Field("manv") String manv);

}
