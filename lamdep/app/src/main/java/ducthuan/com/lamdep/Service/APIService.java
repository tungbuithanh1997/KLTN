package ducthuan.com.lamdep.Service;

public class APIService {

    public static final String base_url = "https://webt2.000webhostapp.com/webt2/";
    //public static final String base_url = "http://172.17.16.153/webt2/";
    //public static final String base_url = "http://10.45.249.198/webt2/";
    //public static final String base_url = "http://172.17.23.52/webt2/";
    //public static final String base_url = "http://192.168.1.7/webt2/";
    //public static final String base_url = "http://192.168.43.56/webt2/";

    public static DataService getService(){
        //create: khoi tao nhung phuong thuc ben dataservice de gui len server
        return APIRetrofitClient.getClient(base_url).create(DataService.class);
    }

}