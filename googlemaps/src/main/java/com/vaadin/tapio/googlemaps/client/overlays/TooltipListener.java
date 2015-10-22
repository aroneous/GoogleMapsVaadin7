package com.vaadin.tapio.googlemaps.client.overlays;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.Point;
import com.google.gwt.maps.client.events.MouseEvent;
import com.google.gwt.maps.client.events.mousemove.MouseMoveMapEvent;
import com.google.gwt.maps.client.events.mousemove.MouseMoveMapHandler;
import com.google.gwt.maps.client.events.mouseout.MouseOutMapEvent;
import com.google.gwt.maps.client.events.mouseout.MouseOutMapHandler;
import com.google.gwt.maps.client.events.mouseover.MouseOverMapEvent;
import com.google.gwt.maps.client.events.mouseover.MouseOverMapHandler;
import com.google.gwt.maps.client.overlays.MapCanvasProjection;
import com.google.gwt.maps.client.overlays.OverlayView;
import com.google.gwt.maps.client.overlays.overlayhandlers.OverlayViewMethods;
import com.google.gwt.maps.client.overlays.overlayhandlers.OverlayViewOnAddHandler;
import com.google.gwt.maps.client.overlays.overlayhandlers.OverlayViewOnDrawHandler;
import com.google.gwt.maps.client.overlays.overlayhandlers.OverlayViewOnRemoveHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;

public class TooltipListener {

    private static final int SHOW_DELAY = 400;
    private static final int CLEAR_DELAY = 250;
    private static final String TOOLTIP_STYLE_CLASS = "tooltip";

    private class MouseOverHandler implements MouseOverMapHandler {
        @Override
        public void onEvent(MouseOverMapEvent event) {
            queueShow(event);
        }
    }

    private class MouseMoveHandler implements MouseMoveMapHandler {
        @Override
        public void onEvent(MouseMoveMapEvent event) {
            queueShow(event);
        }
    }

    private class MouseOutHandler implements MouseOutMapHandler {
        @Override
        public void onEvent(MouseOutMapEvent event) {
            queueClear();
        }
    }

    private final MouseOverHandler mouseOverHandler = new MouseOverHandler();
    private final MouseMoveHandler mouseMoveHandler = new MouseMoveHandler();
    private final MouseOutHandler mouseOutHandler = new MouseOutHandler();

    private MapWidget mapWidget;
    private OverlayView overlayView;
    private DivElement info;
    private boolean tooltipVisible;
    private Timer showTimer;
    private Timer clearTimer;

    public TooltipListener(MapWidget mapWidget, String contents) {
        this.mapWidget = mapWidget;
        this.overlayView = OverlayView.newInstance(mapWidget, new OverlayViewOnDrawHandler() {
            @Override
            public void onDraw(OverlayViewMethods methods) {
            }
        }, new OverlayViewOnAddHandler() {
            @Override
            public void onAdd(OverlayViewMethods methods) {
            }
        }, new OverlayViewOnRemoveHandler() {
            @Override
            public void onRemove(OverlayViewMethods methods) {
            }
        });

        // Create the tooltip element itself
        this.info = DivElement.as(DOM.createDiv());
        info.setClassName(TOOLTIP_STYLE_CLASS);
        info.setInnerText(contents);
    }

    public MouseOverHandler getMouseOverHandler() {
        return mouseOverHandler;
    }

    public MouseMoveHandler getMouseMoveHandler() {
        return mouseMoveHandler;
    }

    public MouseOutHandler getMouseOutHandler() {
        return mouseOutHandler;
    }

    private void showTooltip(MouseEvent event) {
        final MapCanvasProjection projection = overlayView.getProjection();
        final Point pixel = projection.fromLatLngToContainerPixel(event.getLatLng());

        mapWidget.getDiv().appendChild(info);

        double x = pixel.getX() + 10.0;
        double y = pixel.getY() - info.getOffsetHeight() - 10.0;

        final Style style = info.getStyle();
        style.setLeft(x, Unit.PX);
        style.setTop(y, Unit.PX);

        this.tooltipVisible = true;
    }

    private void hideTooltip() {
        mapWidget.getDiv().removeChild(info);
        this.tooltipVisible = false;
    }

    private void queueShow(MouseOverMapEvent event) {
        queueShow(event.getMouseEvent());
    }

    private void queueShow(MouseMoveMapEvent event) {
        queueShow(event.getMouseEvent());
    }

    private void queueShow(final MouseEvent event) {
        if (showTimer != null) {
            showTimer.cancel();
            showTimer = null;
        }
        if (clearTimer != null) {
            clearTimer.cancel();
            clearTimer = null;
        }
        if (!tooltipVisible) {
            showTimer = new Timer() {
                @Override
                public void run() {
                    showTooltip(event);
                    showTimer = null;
                }
            };
            showTimer.schedule(SHOW_DELAY);
        }
    }

    private void queueClear() {
        if (showTimer != null) {
            showTimer.cancel();
            showTimer = null;
        }

        if (tooltipVisible && clearTimer == null) {
            clearTimer = new Timer() {
                @Override
                public void run() {
                    hideTooltip();
                    clearTimer = null;
                }
            };
            clearTimer.schedule(CLEAR_DELAY);
        }
    }
}
