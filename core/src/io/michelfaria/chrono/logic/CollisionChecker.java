package io.michelfaria.chrono.logic;

import static com.badlogic.gdx.math.Intersector.intersectRectangles;
import static io.michelfaria.chrono.util.ActorUtil.getActorRectangle;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import io.michelfaria.chrono.values.LayerNames;

public class CollisionChecker {

	private TiledMap map;

	public CollisionChecker(TiledMap map) {
		this.map = map;
	}
	
	/**
	 * Returns true if the specified Actor's rectangle collides with
	 * any objects in the "Collision" layer of this class's TiledMap.
	 * 
	 * @param actor Actor to check for collision
	 * @return True if there is a collision
	 */
	public boolean collidesWithMap(Actor actor) {
		Rectangle actorRectangle = getActorRectangle(actor);
		return collidesWithMap(actorRectangle);
	}
	
	/**
	 * Returns true if the specified rectangle collides with
	 * any objects in the "Collision" layer of this class's TiledMap.
	 * 
	 * @param rectangle Rectangle to check for collision
	 * @return True if there is a collision
	 */
	public boolean collidesWithMap(Rectangle rectangle) {
		MapLayer collisionLayer = map.getLayers().get(LayerNames.COLLISION);
		MapObjects objects = collisionLayer.getObjects();
		Array<RectangleMapObject> rectangles = objects.getByType(RectangleMapObject.class);
		
		for (RectangleMapObject rectangleMapObject : rectangles) {
			Rectangle mapRectangle = rectangleMapObject.getRectangle();
			Rectangle intersection = new Rectangle();
			
			if (intersectRectangles(rectangle, mapRectangle, intersection)) {
				return true;
			}
		}
		
		return false;
	}
}
