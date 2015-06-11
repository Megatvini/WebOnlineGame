package MatchMaking;

import java.util.Collection;

/**
 * Created by Nika on 03:59, 6/10/2015.
 */
public interface MatchMaker {
    public void addParticipant(String  participant, Collection<Integer> roomSizes);
    public void addParticipants(Collection<String> arbitraryRoomMates, Collection<Integer> roomSizes);
}
