package com.vaadin.tapio.googlemaps.client.events;

import java.io.Serializable;

import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolygon;

/**
 * Interface for listening polygon click events.
 */
public interface PolygonClickListener extends Serializable {
    /**
     * Handle a PolygonClickEvent.
     *
     * @param clickedPolygon The polygon that was clicked.
     * @param x X coordinate of click position
     * @param y Y coordinate of click position
     */
    void polygonClicked(GoogleMapPolygon clickedPolygon, int x, int y);
}
