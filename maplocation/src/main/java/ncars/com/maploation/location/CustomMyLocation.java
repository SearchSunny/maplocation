package ncars.com.maploation.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;

/**
 * 自定义高德定位控制
 */

public class CustomMyLocation implements LocationSource,AMapLocationListener {

    private static CustomMyLocation mInstance;

    /**
     * 处理定位更新的接口
     */
    private OnLocationChangedListener mListener;

    /**
     * 定位服务类。此类提供单次定位、持续定位、地理围栏、最后位置相关功能。
     */
    private AMapLocationClient mlocationClient;

    /**
     * 定位参数设置，通过这个类可以对定位的相关参数进行设置在AMapLocationClient进行定位时需要这些参数
     */
    private AMapLocationClientOption mLocationOption;

    private Context mContext;

    private OnLocationListener mOnLocationListener;
    /**
     * 定位间距时间(单位毫秒)
     */
    private static final int LOCATION_INTERVAL = 10*1000;

    /**
     * 方法名多了Locked表示是线程安全的，没有其他意义
     */
    /*public synchronized static  CustomMyLocation getInstanceLocked(Context context,OnLocationListener onLocationListener) {
        if (mInstance == null) {
            mInstance = new CustomMyLocation(context,onLocationListener);
        }
        return mInstance;
    }*/

    public CustomMyLocation(Context context, OnLocationListener onLocationListener){

        this.mContext = context;
        this.mOnLocationListener = onLocationListener;
    }

    /**
     * 定位回调监听，当定位完成后调用此方法
     * @param amapLocation 定位信息类。定位完成后的位置信息
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mOnLocationListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                if (mListener != null){
                    mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                }
                mOnLocationListener.OnLocationSuccess(amapLocation);
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                mOnLocationListener.OnLocationFail();
            }
        }

    }

    /**
     * 激活定位
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mContext);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            //高精度定位模式：会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
            //低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
            //mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            //设置是否只定位一次
            //mLocationOption.setOnceLocation(true);
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption.setInterval(LOCATION_INTERVAL);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;

    }

    /**
     * 定位回调
     */
    public interface OnLocationListener {
        /**
         * 定位成功
         * @param amapLocation 定位信息
         */
        void OnLocationSuccess(AMapLocation amapLocation);

        /**
         * 定位失败
         */
        void OnLocationFail();

    }
}
