// Created by plusminus on 17:53:07 - 25.09.2008
package org.andnav.osm.views.util;

import org.andnav.osm.util.BoundingBoxE6;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.util.constants.OpenStreetMapViewConstants;

import android.graphics.Point;

/**
 * 
 * @author Nicolas Gramlich
 *
 */
public class Util implements OpenStreetMapViewConstants{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public static int[] getMapTileFromCoordinates(final int aLatE6, final int aLonE6, final int zoom, final int[] reuse) {
		return getMapTileFromCoordinates(aLatE6 / 1E6, aLonE6 / 1E6, zoom, reuse);
	}
	
	public static int[] getMapTileFromCoordinates(final double aLat, final double aLon, final int zoom, final int[] aUseAsReturnValue) {
		final int[] out = (aUseAsReturnValue != null) ? aUseAsReturnValue : new int[2];

		out[MAPTILE_LATITUDE_INDEX] = (int) Math.floor((1 - Math.log(Math.tan(aLat * Math.PI / 180) + 1 / Math.cos(aLat * Math.PI / 180)) / Math.PI) / 2 * (1 << zoom));
		out[MAPTILE_LONGITUDE_INDEX] = (int) Math.floor((aLon + 180) / 360 * (1 << zoom));

		return out;
	}
	
	public static Point getPointFromCoordinates(final GeoPoint aGeoPoint, final int aZoom, Point aUseAsReturnValue) {
		Point p = (aUseAsReturnValue != null) ? aUseAsReturnValue : new Point();
		
		final double aLon = aGeoPoint.getLongitudeE6()*1E-6;
		final double aLat = aGeoPoint.getLatitudeE6()*1E-6; 
		p.x = (int) Math.floor((aLon + 180) / 360 * (1 << aZoom));
		p.y = (int) Math.floor((1 - Math.log(Math.tan(aLat * Math.PI / 180) + 1 / Math.cos(aLat * Math.PI / 180)) / Math.PI) / 2 * (1 << aZoom));
		
		return p;
	}
	
	public static BoundingBoxE6 getBoundingBoxFromCoords(final int left, final int top, final int right, final int bottom, final int zoom) {
		return new BoundingBoxE6(tile2lat(top, zoom), tile2lon(right, zoom), tile2lat(bottom, zoom), tile2lon(left, zoom));
	}
	
	// Conversion of a MapTile to a BoudingBox
	
	public static BoundingBoxE6 getBoundingBoxFromMapTile(final int[] aMapTile, final int zoom) {
		final int y = aMapTile[MAPTILE_LATITUDE_INDEX];
		final int x = aMapTile[MAPTILE_LONGITUDE_INDEX];
		return new BoundingBoxE6(tile2lat(y, zoom), tile2lon(x + 1, zoom), tile2lat(y + 1, zoom), tile2lon(x, zoom));
	}
	
	public static GeoPoint getGeoPointFromPos(int x, int y, int aZoom) {
		return new GeoPoint((int)(tile2lat(y, aZoom)*1E6), (int)(tile2lon(x, aZoom)*1E6));
	}
	
	public static double tile2lon(int x, int aZoom) {
		return (x / Math.pow(2.0, aZoom) * 360.0) - 180;
	}

	public static double tile2lat(int y, int aZoom) {
		final double n = Math.PI - ((2.0 * Math.PI * y) / Math.pow(2.0, aZoom));
		return 180.0 / Math.PI * Math.atan(0.5 * (Math.exp(n) - Math.exp(-n)));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}