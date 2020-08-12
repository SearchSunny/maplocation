package ncars.com.maploation.search;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.List;

/**
 * 自定义POI搜索
 */

public class CustomMapSearch implements PoiSearch.OnPoiSearchListener {

    private static final String TAG = CustomMapSearch.class.getName();

    /**
     * poi返回的结果
     **/
    private PoiResult poiResult;
    /**
     * 每页显示的数据
     **/
    private int pageSize = 20;
    /**
     * 当前页面，从0开始计数
     **/
    private int currentPage = 0;
    /**
     * Poi查询条件类
     **/
    private PoiSearch.Query query;
    /**
     * POI搜索
     **/
    private PoiSearch poiSearch;
    /**
     * poi数据
     **/
    private List<PoiItem> poiItems;

    private Context mContext;
    /**
     * 获取搜索Poi返回数据结果接口
     **/
    private OnPoiSearchResultListener mPoiSerchResultListener;


    public CustomMapSearch(Context context, OnPoiSearchResultListener onPoiSearchListener) {

        this.mContext = context;
        this.mPoiSerchResultListener = onPoiSearchListener;
    }


    /**
     * 根据关键字检索POI
     * keyWord-poiType 二选其一
     *
     * @param keyWord 表示搜索字符串
     * @param poiType 表示poi搜索类型
     * @param poiArea 表示poi搜索区域（空字符串代表全国）
     * @param pageNum 表示查第几页
     */
    public void doSearchKeyword(String keyWord, String poiType, String poiArea, int pageNum) {
        query = new PoiSearch.Query(keyWord, poiType, poiArea);
        query.setPageSize(pageSize);// 设置每页最多返回多少条poiitem
        query.setPageNum(pageNum);// 设置查第一页
        query.setCityLimit(true);

        poiSearch = new PoiSearch(mContext, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 检索周边POI
     * keyWord-poiType 二选其一
     *
     * @param keyWord   表示搜索字符串
     * @param poiType   表示poi搜索类型
     * @param poiArea   表示poi搜索区域（空字符串代表全国）
     * @param latitude  表示经度
     * @param longitude 表示纬度
     * @param pageNum   表示查第几页
     */
    public void doSearchBound(String keyWord, String poiType, String poiArea, double latitude, double longitude, int pageNum) {
        //用给定的经度和纬度构造一个LatLonPoint
        LatLonPoint lp = new LatLonPoint(latitude, longitude);

        currentPage = 0;
        query = new PoiSearch.Query(keyWord, poiType, poiArea);
        query.setPageSize(pageSize);// 设置每页最多返回多少条poiitem
        query.setPageNum(pageNum);// 设置查第一页

        if (lp != null) {
            poiSearch = new PoiSearch(mContext, query);
            poiSearch.setOnPoiSearchListener(this);
            // 设置搜索区域为以lp点为圆心，其周围1000米范围
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 1000, true));//
            poiSearch.searchPOIAsyn();// 异步搜索

        }
    }

    //

    /**
     * 返回POI搜索异步处理的结果
     *
     * @param result
     * @param rcode
     */
    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    if (poiItems != null && poiItems.size() > 0) {
                        mPoiSerchResultListener.onSearchSuccess(poiItems, poiResult.getPageCount());
                    } else {
                        mPoiSerchResultListener.onSearchFail(rcode);
                    }
                }
            } else {
                mPoiSerchResultListener.onSearchFail(rcode);
            }
        }

    }

    /**
     * poi id搜索的结果回调
     *
     * @param poiItem
     * @param i
     */
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }

    /**
     * 获取搜索Poi返回数据结果接口
     */
    public interface OnPoiSearchResultListener {
        /**
         * 搜索成功
         *
         * @param poiItems  返回数据
         * @param pageCount 返回该结果的总页数
         */
        void onSearchSuccess(List<PoiItem> poiItems, int pageCount);

        /**
         * 搜索失败
         *
         * @param reCode 错误代码
         */
        void onSearchFail(int reCode);


    }

}
