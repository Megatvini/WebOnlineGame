package MatchMaking;

import java.util.*;

/**
 * Created by Nika on 13:13, 6/11/2015.
 */
public class MatchingManager implements MatchMaker {
    private static final int MAX_GROUP_SIZE = 4;
    private Map<String, Collection<String>> roomMates;
    private FixedRoomSizeMatcher[] fixedSizeRooms;
    private Set<String> waitingList;
    private FixedRoomSizeMatcherFactory factory;

    /**
     * adds new entries to roomMates whenever
     * possible match is found
     * @param roomMates map of player -> set of players in its room
     * @param factory used to create FixedRoomSizeMatcher objects
     */
    public MatchingManager(Map<String, Collection<String>> roomMates, FixedRoomSizeMatcherFactory factory) {
        this.roomMates = roomMates;
        this.factory = factory;
        fixedSizeRooms = new FixedRoomSizeMatcher[MAX_GROUP_SIZE-1];
        waitingList = new HashSet<>();
        initFixedSizeRooms();
    }

    /**
     * initialize room matchMakers with fixed sizes
     */
    private void initFixedSizeRooms() {
        for (int i=2; i<=MAX_GROUP_SIZE; i++)
            fixedSizeRooms[i-2] = factory.getInstance(i);
    }

    /**
     * add participant to MatchMaker Queue
     * @param participant name of a participant
     * @param roomSizes acceptable room sizes for participant
     */
    @Override
    public synchronized void addParticipant(String participant, Collection<Integer> roomSizes) {
        waitingList.add(participant);
        Map<String, Integer> players = new HashMap<>();
        Integer playerRating = 0;
        players.put(participant, playerRating);
        tryToMatch(players, roomSizes);
    }

    /**
     * tries to find patch for
     * @param players players who need match
     * @param roomSizes acceptable room sizes
     */
    private void tryToMatch(Map<String, Integer> players, Collection<Integer> roomSizes) {
        for (Integer roomSize : roomSizes) {
            FixedRoomSizeMatcher fixedMatcher = fixedSizeRooms[roomSize-2];
            Set<Set<String>> match = fixedMatcher.addNewPlayerGroup(players);
            if (match != null) {
                matchFound(match, roomSize);
                return;
            }
        }
    }

    /**
     * adds entries to roomMates
     * @param match set of groups who are matched
     * @param roomSize size of a room of matched players
     */
    private void matchFound(Set<Set<String>> match, Integer roomSize) {
        Set<String> newRoom = new HashSet<>();
        //add each player from match to new room
        match.forEach(x->x.forEach(newRoom::add));

        //remove each player from waiting list
        match.forEach(x->x.forEach(waitingList::remove));

        //put data to roomMates
        newRoom.forEach(x->roomMates.put(x, newRoom));

        //remove players from all fixed size matchmakers system
        for (FixedRoomSizeMatcher matcher : fixedSizeRooms) {
            match.forEach(matcher::removePlayerGroup);
        }
    }


    /**
     * add group of participants to MatchMaker Queue
     * @param arbitraryRoomMates player names who must
     *                           be in the same room
     * @param roomSizes acceptable room sizes for participants
     */
    @Override
    public synchronized void addParticipants(Collection<String> arbitraryRoomMates, Collection<Integer> roomSizes) {
        Map<String, Integer> players = new HashMap<>();
        arbitraryRoomMates.forEach(participant->{
            Integer playerRating = 0;
            players.put(participant, playerRating);
            waitingList.add(participant);
        });
        tryToMatch(players, roomSizes);
    }

    /**
     * @param name of a participant
     * @return true if participant is already in matchmaking queue
     */
    @Override
    public synchronized boolean containsParticipant(String name) {
        return roomMates.containsKey(name) || waitingList.contains(name);
    }
}
