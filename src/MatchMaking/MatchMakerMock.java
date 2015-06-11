package MatchMaking;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Nika on 08:23, 6/10/2015.
 */
public class MatchMakerMock implements MatchMaker {
    private Map<String, Collection<String>> roomMates;

    public MatchMakerMock(Map<String, Collection<String>> roomMates) {
        this.roomMates = roomMates;
    }

    @Override
    public void addParticipant(String participant, Collection<Integer> roomSizes) {

    }

    @Override
    public void addParticipants(Collection<String> arbitraryRoomMates, Collection<Integer> roomSizes) {

    }
}
