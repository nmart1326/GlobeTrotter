package com.example.globetrotter.view;

import com.gluonhq.maps.MapView;
import com.gluonhq.maps.MapPoint;

public class Map {

    private final MapView mapView;

    public Map() {
        mapView = new MapView();
        mapView.setZoom(4.5);
        mapView.setCenter(new MapPoint(-26,133.7751));
    }

    public MapView getMapView() {
        return mapView;
    }

    public void dispose() {

    }
}
