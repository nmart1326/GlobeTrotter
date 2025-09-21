package com.example.globetrotter.view;

import com.gluonhq.maps.tile.TileRetriever;
import javafx.scene.image.Image;

import java.util.concurrent.CompletableFuture;

public class OverrideTileRetriever implements TileRetriever {

    public OverrideTileRetriever() {
        System.out.println("TracesTrackTileRetriever constructor called!");
    }

    private static final String HOST = "https://api.maptiler.com/tiles/satellite-mediumres-2018/";
    private static final String API_KEY = "AqHyscnhDxmUUlar0GzY";

    static final String httpAgent;

    // TODO! Trying to wrap the map around isn't working for now
    static String buildImageUrlString(int zoom, long x, long y) {
        long numTiles = 1L << zoom; // 2^zoom
        long wrappedX = ((x % numTiles) + numTiles) % numTiles; // wrap x
        String url = HOST + zoom + "/" + wrappedX + "/" + y + ".png?key=" + API_KEY;
        System.out.println("Loading tile: " + url);
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
