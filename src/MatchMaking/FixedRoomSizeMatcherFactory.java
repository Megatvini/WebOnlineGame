package MatchMaking;

/**
 * Created by Nika on 17:15, 6/11/2015.
 */

/**
 * used to create new FixedRoomSizeMatcher objects;
 */
public class FixedRoomSizeMatcherFactory {
    public FixedRoomSizeMatcher getInstance(int roomSize) {
        return new FixedRoomSizeMatcher(roomSize);
    }
}
