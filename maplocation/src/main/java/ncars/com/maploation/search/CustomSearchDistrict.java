package ncars.com.maploation.search;

import android.content.Context;

import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义区域搜索
 */

public class CustomSearchDistrict implements DistrictSearch.OnDistrictSearchListener {

    private Context mContext;
    /**
     * 获取搜索返回数据结果接口
     **/
    private OnDistrictSearchResultListener mOnDistrictSearchResultListener;


    public CustomSearchDistrict(Context context, OnDistrictSearchResultListener onDistrictSearchListener) {
        this.mContext = context;
        this.mOnDistrictSearchResultListener = onDistrictSearchListener;
    }

    public void doSearchDistrict(String cityCode) {
        DistrictSearch search = new DistrictSearch(mContext);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords(cityCode);//传入关键字
        query.setShowBoundary(true);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(this);
        search.searchDistrictAsyn();
    }


    @Override
    public void onDistrictSearched(DistrictResult districtResult) {

        if (districtResult.getAMapException().getErrorCode() == 1000) {
            if (districtResult != null && districtResult.getDistrict() != null) {
                ArrayList<DistrictItem> district = districtResult.getDistrict();
                List<DistrictItem> subDistrict = district.get(0).getSubDistrict();
                mOnDistrictSearchResultListener.onSearchSuccess(subDistrict, districtResult.getPageCount());
            } else {
                mOnDistrictSearchResultListener.onSearchFail(districtResult.getAMapException().getErrorCode());
            }
        } else {
            mOnDistrictSearchResultListener.onSearchFail(districtResult.getAMapException().getErrorCode());
        }

    }


    /**
     * 获取搜索Poi返回数据结果接口
     */
    public interface OnDistrictSearchResultListener {
        /**
         * 搜索成功
         *
         * @param districtItem 返回数据
         * @param pageCount    返回该结果的总页数
         */
        void onSearchSuccess(List<DistrictItem> districtItem, int pageCount);

        /**
         * 搜索失败
         *
         * @param reCode 错误代码
         */
        void onSearchFail(int reCode);


    }
}
