package ncars.com.maploation.search;

import android.content.Context;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * 用以实现通过地理编码进行查询的功能(地址转坐标)
 */

public class CustomSearchGeocode implements  GeocodeSearch.OnGeocodeSearchListener{


    private Context mContext;
    /**
     * 获取搜索返回数据结果接口
     **/
    private OnGeocodeSearchResultListener mOnGeocodeSearchResultListener;


    public CustomSearchGeocode(Context context, OnGeocodeSearchResultListener onGeocodeSearchResultListener) {
        this.mContext = context;
        this.mOnGeocodeSearchResultListener = onGeocodeSearchResultListener;
    }


    public void doSearchGeocode(String locationName,String cityCode) {
        GeocodeQuery geocodeQuery = new GeocodeQuery(locationName,cityCode);
        GeocodeSearch geocodeSearch = new GeocodeSearch(mContext);
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 根据给定的地理名称和查询城市，返回地理编码的结果列表
     * @param geocodeResult
     * @param resultID 返回结果成功或者失败的响应码。1000为成功
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int resultID) {
        if (resultID == 1000){
            if (geocodeResult != null){
                mOnGeocodeSearchResultListener.onSearchSuccess(geocodeResult.getGeocodeAddressList().get(0));
            }else{
                mOnGeocodeSearchResultListener.onSearchFail(resultID);
            }
        }else {
            mOnGeocodeSearchResultListener.onSearchFail(resultID);
        }
    }

    /**
     * 根据给定的经纬度和最大结果数返回逆地理编码的结果列表
     * @param regeocodeResult
     * @param resultID 返回结果成功或者失败的响应码。1000为成功
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int resultID) {


    }

    /**
     * 获取搜索地理名称返回数据结果接口
     */
    public interface OnGeocodeSearchResultListener {
        /**
         * 搜索成功
         * @param geocodeAddress 地理编码返回的结果
         */
        void onSearchSuccess(GeocodeAddress geocodeAddress);

        /**
         * 搜索失败
         *
         * @param reCode 错误代码
         */
        void onSearchFail(int reCode);

    }


}
