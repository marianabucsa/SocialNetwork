package com.example.socialnetworkgui.utils;

import com.example.socialnetworkgui.domain.Entity;
import com.example.socialnetworkgui.domain.User;

import java.util.*;

public class NetworkGraph<Long, E extends Entity<Long>> {
    private final Set<Long> allUsersKey;
    private final Map<Long, LinkedList<Long>> adjList;
    private int lMax = 0;
    private List<Long> pMax = new ArrayList<>();

    /**
     * graph constructor- creates the adjacent list for all users
     *
     * @param allUsers - a map with all the users and their data
     */
    public NetworkGraph(HashMap<Long, User> allUsers) {
        this.allUsersKey = allUsers.keySet();
        adjList = new HashMap<>();
        for (Long id : allUsers.keySet()) {
            User user = allUsers.get(id);
            LinkedList<Long> userFriends = new LinkedList<>();
            if (user.getFriends() != null) {
                for (java.lang.Long id1 : user.getFriends())
                    userFriends.add((Long) id1);
                adjList.put(id, userFriends);
            }
        }
    }

    /**
     * DFS algorithm
     *
     * @param v       - source node
     * @param visited - boolean for the visited users
     * @return - BFS for the source node
     */
    public void DFS(Long v, Map<Long, Boolean> visited) {
        visited.put(v, true);
        for (Long id : allUsersKey) {
            LinkedList<Long> userFriends = adjList.get(v);
            if (userFriends != null) {
                if (userFriends.contains(id) && !visited.get(id)) {
                    DFS(id, visited);
                }
            }
        }
    }

    /**
     * @return the number of connected components
     */
    public int connectedComponents() {
        int count = 0;
        Map<Long, Boolean> visited = new HashMap<>();
        for (Long id : allUsersKey)
            visited.put(id, false);
        for (Long id : allUsersKey) {
            if (!visited.get(id)) {
                DFS(id, visited);
                count += 1;
            }
        }
        return count;
    }


    public void DFSLongest(Long v, Map<Long, Boolean> visited, int len, List<Long> path) {
        visited.put(v, true);
        for (Long id : allUsersKey) {
            LinkedList<Long> userFriends = adjList.get(v);
            if (userFriends != null && userFriends.contains(id) && !visited.get(id)) {
                int clen = len;
                List<Long> cPath = new ArrayList<>(path);
                cPath.add(id);
                DFSLongest(id, visited, ++clen, cPath);
            } else {
                if (len > lMax) {
                    lMax = len;
                    pMax = path;
                }
            }
        }
    }

    public List<Long> longestPath() {
        Map<Long, Boolean> visited = new HashMap<>();
        for (Long id : allUsersKey)
            visited.put(id, false);
        List<Long> allUsersId=allUsersKey.stream().sorted(Comparator.comparingInt(x -> adjList.get(x).size())).toList();
        for (Long id : allUsersId) {
            if (!visited.get(id)) {
                List<Long> path = new ArrayList<>();
                path.add(id);
                DFSLongest(id, visited, 0, path);
            }
        }
        return pMax;
    }
}