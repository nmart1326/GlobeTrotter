package com.example.globetrotter.view;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.data.Feature;
import javafx.application.Platform;
import java.util.List;

//Vector Translation Layer
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import javafx.scene.paint.Color;
import com.example.globetrotter.service.GeoInfoService;


public class Map {

    private final MapView mapView;

    private final InfoPanel infoPanel;

    //Add GPT API Service
    private final GeoInfoService geoInfoService;

    public Map(InfoPanel infoPanel) {
        this.infoPanel = infoPanel;
        this.geoInfoService = new GeoInfoService();
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

        // Add transparent clickable country, state and city layers
        FeatureLayer countriesLayer = createTransparentLayer(
                "https://services.arcgis.com/P3ePLMYs2RVChkJx/ArcGIS/rest/services/World_Countries_(Generalized)/FeatureServer/0"
        );
        FeatureLayer statesLayer = createTransparentLayer(
                "https://services.arcgis.com/P3ePLMYs2RVChkJx/ArcGIS/rest/services/World_Administrative_Divisions/FeatureServer/0"
        );

        FeatureLayer urbanLayer = createTransparentLayer(
                "https://services.arcgis.com/P3ePLMYs2RVChkJx/ArcGIS/rest/services/World_Urban_Areas/FeatureServer/0"
        );

        map.getOperationalLayers().addAll(List.of(
                countriesLayer,
                statesLayer,
                urbanLayer));

        // Initial viewpoint
        double latitude = -25.5; // up vs down
        double longitude = 120; // left vs right
        double scale = 31500000; // smaller = zoom in, bigger = zoom out
        mapView.setViewpoint(new Viewpoint(latitude, longitude, scale));

        // Zoom factor for the MapView node itself
        double zoomFactor = 1.2;
        mapView.setScaleX(zoomFactor);
        mapView.setScaleY(zoomFactor);

        // Automatically resize
        mapView.setManaged(true);

        // Listen for mouse clicks on the map
        mapView.setOnMouseClicked(event -> {
            // Capture geographic coordinates of the click
            javafx.geometry.Point2D screenPoint = new javafx.geometry.Point2D(event.getX(), event.getY());

            com.esri.arcgisruntime.geometry.Point mapPoint = mapView.screenToLocation(screenPoint);
            double lat = mapPoint.getY();
            double lon = mapPoint.getX();

            ListenableFuture<List<IdentifyLayerResult>> identifyFuture =
                    mapView.identifyLayersAsync(screenPoint, 10, false);

            identifyFuture.addDoneListener(() -> {
                try {
                    List<IdentifyLayerResult> results = identifyFuture.get();

                    //add: merged multi-layer feature handling
                    StringBuilder combinedFeatures = new StringBuilder();
                    boolean foundFeature = false;

                    for (IdentifyLayerResult layerResult : results) {
                        String layerName = layerResult.getLayerContent() != null
                                ? layerResult.getLayerContent().getName()
                                : "Unknown Layer";

                        for (var element : layerResult.getElements()) {
                            java.util.Map<String, Object> attributes = null;

                            if (element instanceof Graphic g) {
                                attributes = g.getAttributes();
                            } else if (element instanceof Feature f) {
                                attributes = f.getAttributes();
                            }

                            if (attributes != null && !attributes.isEmpty()) {
                                foundFeature = true;
                                combinedFeatures.append("\n📍 Clicked Feature (from layer: ")
                                        .append(layerName).append(")\n");
                                for (var entry : attributes.entrySet()) {
                                    combinedFeatures.append(entry.getKey())
                                            .append(" = ")
                                            .append(entry.getValue())
                                            .append("\n");
                                }
                            }
                        }
                    }

                    if (foundFeature) {
                        String featureData = "Coordinates: " + lat + ", " + lon + "\n" + combinedFeatures;
                        // --- added async call ---
                        geoInfoService.explainFeatureAsync(featureData, response ->
                                Platform.runLater(() ->
                                        infoPanel.displayInfo("Geo Info", response))
                            );

                    } else {
                        Platform.runLater(() ->
                                infoPanel.displayInfo("No Data Found", "Try clicking on a country, state, or city area"));
                    }
                    //end of add

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

    }

    // Helper for invisible clickable layers
    private FeatureLayer createTransparentLayer(String serviceUrl) {
        ServiceFeatureTable table = new ServiceFeatureTable(serviceUrl);
        FeatureLayer layer = new FeatureLayer(table);

        SimpleFillSymbol transparentSymbol = new SimpleFillSymbol(
                SimpleFillSymbol.Style.SOLID,
                Color.TRANSPARENT,
                null
        );
        layer.setRenderer(new SimpleRenderer(transparentSymbol));
        return layer;
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
