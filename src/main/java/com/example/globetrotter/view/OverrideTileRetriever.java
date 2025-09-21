package com.example.globetrotter.view;

import com.gluonhq.maps.tile.TileRetriever;
import javafx.scene.image.Image;

import java.util.concurrent.CompletableFuture;

public class OverrideTileRetriever implements TileRetriever {

    public OverrideTileRetriever() {
        System.out.println("TracesTrackTileRetriever constructor called!");
    }

    // https://basemaps-api.arcgis.com/arcgis/rest/services/World_Basemap_v2/VectorTileServer/tile/1/0/0.pbf?token=

    private static final String HOST = "https://static-map-tiles-api.arcgis.com/arcgis/rest/services/static-basemap-tiles-service/v1/arcgis/outdoor/static/tile/";
    private static final String API_KEY = "AAPTxy8BH1VEsoebNVZXo8HurLGGQvr8xBwq6kPfaUltn5Urn8soXY7wCLc6ItJlSkpO2jjWWTpz_ASP5gh3d8UVYeEiEiLAO5bfczYpWOgNAGAPvGUMihYXc7lXi31xsF7bisJpnqQPJxNMDaL39mgBH3sjZ7H_GtdQBX-QoI2R-fva9mcg12eEb7GpAXAMtgm9GANyLvfMGLUIVRMvL6aSMwcm9iewwu7cS8jR2eX2PUM.AT1_uYmxm5nK";

    static final String httpAgent;

    // TODO! Trying to wrap the map around isn't working for now
    static String buildImageUrlString(int zoom, long x, long y) {
        long numTiles = 1L << zoom; // 2^zoom
        long wrappedX = ((x % numTiles) + numTiles) % numTiles; // wrap x
        String url = HOST + zoom + "/" + y + "/" + wrappedX + "?token=" + API_KEY;
        // System.out.println("Loading tile: " + url);
        return url;
    }

    @Override
    public CompletableFuture<Image> loadTile(int zoom, long x, long y) {
        String url = buildImageUrlString(zoom, x, y);
        return CompletableFuture.completedFuture(new Image(url, true));
    }

    static {
        String agent = System.getProperty("http.agent");
        if (agent == null) {
            String var10000 = System.getProperty("os.name");
            agent = "(" + var10000 + " / " + System.getProperty("os.version") + " / " + System.getProperty("os.arch") + ")";
        }

        httpAgent = "Gluon Maps/2.0.0 " + agent;
        System.setProperty("http.agent", httpAgent);
    }
}
