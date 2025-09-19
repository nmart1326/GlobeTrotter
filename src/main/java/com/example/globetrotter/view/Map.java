package com.example.globetrotter.view;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.scene.Group;

public class Map {

    private final MapView mapView;
    private final Group mapGroup;

    public Map() {
        // Noyal's ArcGIS API key
        ArcGISRuntimeEnvironment.setApiKey("AAPTxy8BH1VEsoebNVZXo8HurLGGQvr8xBwq6kPfaUltn5Urn8soXY7wCLc6ItJlSkpO2jjWWTpz_ASP5gh3d8UVYeEiEiLAO5bfczYpWOgNAGAPvGUMihYXc7lXi31xsF7bisJpnqQPJxNMDaL39mgBH3sjZ7H_GtdQBX-QoI2R-fva9mcg12eEb7GpAXAMtgm9GANyLvfMGLUIVRMvL6aSMwcm9iewwu7cS8jR2eX2PUM.AT1_uYmxm5nK");

        // Initialize MapView
        mapView = new MapView();

        // Load a map from ArcGIS portal
        Portal portal = new Portal("https://www.arcgis.com", true);
        String itemId = "802841aae4dd45778801cd1d375795b9"; // Esri's Childrens Map
        PortalItem portalItem = new PortalItem(portal, itemId);
        ArcGISMap map = new ArcGISMap(portalItem);
        mapView.setMap(map);

        // Initial viewpoint
        double latitude = -25.5; // up vs down
        double longitude = 120; // left vs right
        double scale = 31500000; // smaller = zoom in, bigger = zoom out
        mapView.setViewpoint(new Viewpoint(latitude, longitude, scale));

        // Wrap MapView in a Group for potential transformations
        mapGroup = new Group(mapView);

        // Zoom factor for the MapView node itself
        double zoomFactor = 1.2;
        mapView.setScaleX(zoomFactor);
        mapView.setScaleY(zoomFactor);

        // Automatically resize
        mapView.setManaged(true);
    }

    public MapView getMapView() {
        return mapView;
    }

    public void dispose() {
        if (mapView != null) {
            mapView.dispose();
        }
    }
}
