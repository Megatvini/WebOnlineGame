package MatchMaking;

import java.util.*;

/**
 * Created by Nika on 13:13, 6/11/2015.
 */
public class MatchingManager implements MatchMaker {
    private static final int MAX_GROUP_SIZE = 4;
    private Map<String, Collection<String>> roomMates;
    private FixedRoomSizeMatcher[] fixedSizeRooms;
    FixedRoomSizeMatcherFactory factory;

    public MatchingManager(Map<String, Collection<String>> roomMates, FixedRoomSizeMatcherFactory factory) {
        this.roomMates = roomMates;
        this.factory = factory;
        fixedSizeRooms = new FixedRoomSizeMatcher[MAX_GROUP_SIZE];
        initFixedSizeRooms();
    }

    private void initFixedSizeRooms() {
        for (int i=2; i<=fixedSizeRooms.length; i++)
            fixedSizeRooms[i-2] = factory.getInstance(i);
    }

    @Override
    public synchronized void addParticipant(String participant, Collection<Integer> roomSizes) {
        Map<String, Integer> players = new HashMap<>();
        Integer playerRating = 0;
        players.put(participant, playerRating);
        tryToMatch(players, roomSizes);
    }

    private void tryToMatch(Map<String, Integer> players, Collection<Integer> roomSizes) {
        for (Integer roomSize : roomSizes) {
            Set<Set<String>> match = fixedSizeRooms[roomSize-2].addNewPlayerGroup(players);
            if (match != null) {
                matchFound(match, roomSize);
                return;
            }
        }
    }

    private void matchFound(Set<Set<String>> match, Integer roomSize) {
        Set<String> newRoom = new HashSet<>();
        //add each player from match to new room
        match.forEach(x->x.forEach(newRoom::add));

        //put data to roomMates
        newRoom.forEach(x->roomMates.put(x, newRoom));

        //remove players from all fixed size matchmakers system
        match.forEach(fixedSizeRooms[roomSize - 2]::removePlayerGroup);
    }

    @Override
    public synchronized void addParticipants(Collection<String> arbitraryRoomMates, Collection<Integer> roomSizes) {
        Map<String, Integer> players = new HashMap<>();
        arbitraryRoomMates.forEach(participant->{
            Integer playerRating = 0;
            players.put(participant, playerRating);
        });
        tryToMatch(players, roomSizes);
    }
}
