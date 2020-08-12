package ncars.com.maploation.route;

import android.content.Context;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

/**
 * 路线规划
 */

public class CustomMyRoute implements RouteSearch.OnRouteSearchListener{

    private RouteSearch mRouteSearch;

    private LatLonPoint mStartPoint;//起点，116.335891,39.942295
    private LatLonPoint mEndPoint;//终点，116.481288,39.995576

    private final int ROUTE_TYPE_DRIVE = 2;

    public CustomMyRoute(Context context, OnRoutePlanListener planListener) {
        //初始化 RouteSearch 对象
        mRouteSearch = new RouteSearch(context);
        //设置数据回调监听器
        mRouteSearch.setRouteSearchListener(this);
        mOnRoutePlanListener = planListener;
    }

    /**
     * 设置路线搜索相关参数
     */
    public void setRouteSearch(LatLonPoint mStartPoint, LatLonPoint mEndPoint){
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        //fromAndTo，路径的起点终点；
        //mode，计算路径的模式，可选，默认为速度优先；
        //passedByPoints，途经点，可选；
        //avoidpolygons，避让区域，可选，支持32个避让区域，每个区域最多可有16个顶点。如果是四边形则有4个坐标点，如果是五边形则有5个坐标点。
        //avoidRoad，避让道路，只支持一条避让道路，避让区域和避让道路同时设置，只有避让道路生效。
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, "");
        //发送请求
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }

    /**
     * 处理驾车路线搜索结果
     *
     * @param driveRouteResult
     * @param code
     */
    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int code) {
        //解析result获取算路结果
        if (mOnRoutePlanListener != null){
            if (code == AMapException.CODE_AMAP_SUCCESS){
                mOnRoutePlanListener.OnRoutePlanSuccess(driveRouteResult);
            }else{
                mOnRoutePlanListener.OnRoutePlanFail(code);
            }
        }
    }
    /**
     * 公交路线
     *
     * @param busRouteResult
     * @param i
     */
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    /**
     * 步行路线
     *
     * @param walkRouteResult
     * @param i
     */
    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    /**
     * 骑行路线
     *
     * @param rideRouteResult
     * @param i
     */
    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    private OnRoutePlanListener mOnRoutePlanListener;

    /**
     * 路线规划回调
     */
    public interface OnRoutePlanListener {
        /**
         * 规划成功
         * @param driveRouteResult 路线信息
         */
        void OnRoutePlanSuccess(DriveRouteResult driveRouteResult);

        /**
         * 规划失败
         */
        void OnRoutePlanFail(int code);

    }
}
