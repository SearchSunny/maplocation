package ncars.com.maploation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;

/**
 *
 */

public class MyMapView extends MapView {

    public MyMapView(Context context) {
        super(context);
    }

    public MyMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MyMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MyMapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("mv","MyMapView--dispatchTouchEvent"+ev.getAction());
        return super.dispatchTouchEvent(ev);
    }
}
