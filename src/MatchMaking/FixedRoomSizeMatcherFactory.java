package MatchMaking;

/**
 * Created by Nika on 17:15, 6/11/2015.
 */
public class FixedRoomSizeMatcherFactory {
    public FixedRoomSizeMatcher getInstance(int roomSize) {
        return new FixedRoomSizeMatcher(roomSize);
    }
}
