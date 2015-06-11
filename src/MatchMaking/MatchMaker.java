package MatchMaking;

import java.util.Collection;

/**
 * Created by Nika on 03:59, 6/10/2015.
 */
public interface MatchMaker {
    /**
     * add participant to MatchMaker Queue
     * @param participant name of a participant
     * @param roomSizes acceptable room sizes for participant
     */
    public void addParticipant(String  participant, Collection<Integer> roomSizes);

    /**
     * add group of participants to MatchMaker Queue
     * @param arbitraryRoomMates player names who must
     *                           be in the same room
     * @param roomSizes acceptable room sizes for participants
     */
    public void addParticipants(Collection<String> arbitraryRoomMates, Collection<Integer> roomSizes);
}
