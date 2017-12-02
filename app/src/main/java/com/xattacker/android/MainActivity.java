package com.xattacker.android;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xattacker.android.view.polygon.PolygonMap;
import com.xattacker.android.view.polygon.PolygonRegion;
import com.xattacker.android.view.polygon.PolygonView;
import com.xattacker.android.view.polygon.PolygonViewListener;
import com.xattacker.android.view.polygon.RegionMark;

public class MainActivity extends AppCompatActivity implements PolygonViewListener
{
    private PolygonView _polygonView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        try
        {
            _polygonView = (PolygonView)this.findViewById(R.id.view_polygon);
            _polygonView.setFitToCenter(true);
            _polygonView.setListener(this);

            // map from json resource
            PolygonMap map = PolygonMap.parseFromJson(this, R.raw.region);
            _polygonView.loadMap(map);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void onLoadRegionClick(View aView)
    {
        // you could also add map region by code:
        _polygonView.clearRegions();

        PolygonRegion region = new PolygonRegion();
        region.setRegionColor(Color.YELLOW);
        region.setId("north");
        region.setTitle("北部");
        region.addPoint(new PointF(167, 103));
        region.addPoint(new PointF(194, 142));
        region.addPoint(new PointF(251, 170));
        region.addPoint(new PointF(306, 173));
        region.addPoint(new PointF(322, 162));
        region.addPoint(new PointF(314, 81));
        region.addPoint(new PointF(338, 56));
        region.addPoint(new PointF(330, 32));
        region.addPoint(new PointF(291, 19));
        region.addPoint(new PointF(269, 2));
        region.addPoint(new PointF(254, 30));
        region.addPoint(new PointF(192, 49));

        _polygonView.addRegion(region);
    }

    @Override
    public void onRegionClicked(PolygonRegion aRegion)
    {
        android.util.Log.i("aaa", "onRegionClicked: " + aRegion.getTitle());
    }

    @Override
    public void onRegionMarkClicked(RegionMark aMark, PolygonRegion aRegion)
    {
        android.util.Log.i("aaa", "onRegionMarkClicked: " + aMark.getTitle() + ", " + aRegion.getTitle());
    }
}
