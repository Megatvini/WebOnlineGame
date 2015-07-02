package MatchMaking;

import java.util.*;

/**
 * Created by Nika on 16:34, 6/11/2015.
 */
public class FixedRoomSizeMatcher {
    private final int roomSize;

    //list of players who are waiting to be matched
    //each entry on this list is map playerName -> playerRating
    private List<Map<String, Integer>> waitingList;

    public FixedRoomSizeMatcher(int roomSize) {
        this.roomSize = roomSize;
        waitingList = new ArrayList<>();
    }

    /**
     * slow implementation!
     * @param players map of players -> rating
     * @return if found set of players that should be matched
     * else returns null
     */
    public Set<Set<String>> addNewPlayerGroup(Map<String, Integer> players) {
        if (players.size() == roomSize) {
            Set<Set<String>> res = new HashSet<>();
            res.add(players.keySet());
            return res;
        }
        int rating = getRatingAverage(players);
        sortWaitingList(rating);
        return findMatch(players);
    }

    /**
     * tries to find match for players
     * @param players group that needs to be matched
     * @return Set of groups of players who are matched
     * or null if match was impossible
     * always matches groups with closest possible
     * average rating
     */
    private Set<Set<String>> findMatch(Map<String, Integer> players) {
        Set<Set<String>> res = new HashSet<>();
        res.add(players.keySet());
        if (!findRecursiveMatch(res, 0, players.size())) {
            res = null;
            waitingList.add(players);
        }
        return res;
    }

    /**
     * recursive function for finding match
     * @param res current state of matched groups
     * @param index this is index of a first player
     * who has not been considered in matching yet
     * @param curSize current size of a matched group
     * @return true if match was found and in this case
     * res contains the match
     * if match was not found, res is left unchanged
     */
    private boolean findRecursiveMatch(Set<Set<String>> res, int index, int curSize) {
        if (curSize == roomSize) return true;
        if (index >= waitingList.size()) return false;

        for (int i=index; i<waitingList.size(); i++) {
            Set<String> set = waitingList.get(i).keySet();
            res.add(set);
            if (findRecursiveMatch(res, index+1, curSize+set.size())) return true;
            res.remove(set);
        }
        return false;
    }


    /**
     * sort WaitingList increasingly
     * groups who have average rating
     * closer to rating are smaller
     * @param rating player rating
     */
    private void sortWaitingList(int rating) {
        Collections.sort(waitingList, (o1, o2) -> {
            int firstDiff = Math.abs(getRatingAverage(o1) - rating);
            int secondDiff = Math.abs(getRatingAverage(o2) - rating);
            return firstDiff - secondDiff;
        });
    }

    /**
     *
     * @param players mapping playerName -> playerRating
     * @return average rating of players
     */
    private int getRatingAverage(Map<String, Integer> players) {
        int sum = 0;
        for (int n:players.values()) sum+=n;
        return sum/players.size();
    }

    /**
     * removes players from matching queue
     * @param players group of players that no longer need to be matched
     */
    public void removePlayerGroup(Set<String> players) {
        for (int i=0; i<waitingList.size(); i++) {
            Set<String> set = waitingList.get(i).keySet();
            if (setEquals(set, players)) {
                waitingList.remove(i);
                return;
            }
        }
    }

    /**
     * @param set1 firstSet
     * @param set2 secondSet
     * @return true iff two sets contain same elements
     */
    private boolean setEquals(Set<String> set1, Set<String> set2) {
        if (set1.size() != set2.size()) return false;
        Set<String> union = new HashSet<>();
        union.addAll(set1);
        union.addAll(set2);
        return union.size() == set1.size();
    }
}
