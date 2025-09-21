package com.example.globetrotter.view;

import com.gluonhq.maps.MapView;
import com.gluonhq.maps.MapPoint;

import com.gluonhq.maps.tile.TileRetriever;
import com.gluonhq.maps.tile.TileRetrieverProvider;

public class Map {

    private final MapView mapView;

    public Map() {
        // Force the service loader to load custom tile retriever first
        TileRetriever retriever = TileRetrieverProvider.getInstance().load();
        System.out.println("Loaded TileRetriever: " + retriever.getClass().getName());

        mapView = new MapView();
        mapView.setManaged(true);
        mapView.setCenter(new MapPoint(-25.5,120));

        // Zoom factor for the MapView node itself
        double zoomFactor = 1;
        mapView.setScaleX(zoomFactor);
        mapView.setScaleY(zoomFactor);
    }

    public MapView getMapView() {
        return mapView;
    }

    public void dispose() {

    }
}
