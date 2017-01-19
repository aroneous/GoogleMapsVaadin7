package com.vaadin.tapio.googlemaps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;

/**
 * An RPC from client to server that is called when a polygon has been clicked in
 * Google Maps.
 */
public interface PolygonClickedRpc extends ServerRpc {
    void polygonClicked(long polygonId, int x, int y);
}
