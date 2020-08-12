package ncars.com.maploation.search;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * 用以实现通过地理编码进行查询的功能(坐标地址)
 */

public class CustomSearchRegeocode implements GeocodeSearch.OnGeocodeSearchListener {


    private Context mContext;
    /**
     * 获取搜索返回数据结果接口
     **/
    private OnRegeocodeSearchResultListener mOnGeocodeSearchResultListener;


    public CustomSearchRegeocode(Context context, OnRegeocodeSearchResultListener onGeocodeSearchResultListener) {
        this.mContext = context;
        this.mOnGeocodeSearchResultListener = onGeocodeSearchResultListener;
    }

    public void doSearchRegeocode(LatLonPoint point) {
        doSearchRegeocode(point, 200, GeocodeSearch.AMAP);
    }

    // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
    public void doSearchRegeocode(LatLonPoint point, float radius, String latLonType) {
        RegeocodeQuery geocodeQuery = new RegeocodeQuery(point, radius, latLonType);
        GeocodeSearch geocodeSearch = new GeocodeSearch(mContext);
        geocodeSearch.getFromLocationAsyn(geocodeQuery);
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 根据给定的地理名称和查询城市，返回地理编码的结果列表
     *
     * @param geocodeResult
     * @param resultID      返回结果成功或者失败的响应码。1000为成功
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int resultID) {
    }

    /**
     * 根据给定的经纬度和最大结果数返回逆地理编码的结果列表
     *
     * @param regeocodeResult
     * @param resultID        返回结果成功或者失败的响应码。1000为成功
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int resultID) {
        if (resultID == 1000) {
            if (regeocodeResult != null) {
                mOnGeocodeSearchResultListener.onSearchSuccess(regeocodeResult);
            } else {
                mOnGeocodeSearchResultListener.onSearchFail(resultID);
            }
        } else {
            mOnGeocodeSearchResultListener.onSearchFail(resultID);
        }
    }

    /**
     * 获取搜索地理名称返回数据结果接口
     */
    public interface OnRegeocodeSearchResultListener {
        /**
         * 搜索成功
         *
         * @param geocodeAddress 地理编码返回的结果
         */
        void onSearchSuccess(RegeocodeResult geocodeAddress);

        /**
         * 搜索失败
         *
         * @param reCode 错误代码
         */
        void onSearchFail(int reCode);

    }


}
