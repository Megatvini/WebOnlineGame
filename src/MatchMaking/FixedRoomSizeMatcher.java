package MatchMaking;

import java.util.*;

/**
 * Created by Nika on 16:34, 6/11/2015.
 */
public class FixedRoomSizeMatcher {
    private final int roomSize;
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

    private Set<Set<String>> findMatch(Map<String, Integer> players) {
        Set<Set<String>> res = new HashSet<>();
        res.add(players.keySet());
        if (!findRecursiveMatch(res, 0, players.size())) {
            res = null;
            waitingList.add(players);
        }
        return res;
    }

    private boolean findRecursiveMatch(Set<Set<String>> res, int index, int cursize) {
        if (cursize == roomSize) return true;
        if (index >= waitingList.size()) return false;

        for (int i=index; i<waitingList.size(); i++) {
            Set<String> set = waitingList.get(i).keySet();
            res.add(set);
            if (findRecursiveMatch(res, index+1, cursize+set.size())) return true;
            res.remove(set);
        }
        return false;
    }


    private void sortWaitingList(int rating) {
        Collections.sort(waitingList, (o1, o2) -> {
            int firstDiff = Math.abs(getRatingAverage(o1) - rating);
            int secondDiff = Math.abs(getRatingAverage(o2) - rating);
            return firstDiff - secondDiff;
        });
    }

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

    private boolean setEquals(Set<String> set1, Set<String> set2) {
        if (set1.size() != set2.size()) return false;
        Set<String> union = new HashSet<>();
        union.addAll(set1);
        union.addAll(set2);
        return union.size() == set1.size();
    }
}
