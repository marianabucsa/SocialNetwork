package ro.ubbcluj.map.utils;

import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.repository.file.UserFileRepository;

import java.util.*;

public class Graph {
    private int size;
    private List<User> users;

    /**
     * graph constructor
     * @param size - number of users
     */
    public Graph(int size) {
        this.size = size;
        this.users = new ArrayList<>(size);
    }

    /**
     * adds a user to the graph
     * @param u - user
     */
    public void addUser(User u) {
        users.add(u);
    }

    /**
     * BFS algorithm
     * @param v - source node
     * @param visited - boolean for the visited users
     * @return - BFS for the source node
     */
    public void DFS(int v, boolean[] visited) {
        visited[v] = true;
        for (int u = 0; u < users.size(); u++) {
            List<User> friends = users.get(v).getFriends();
            if (friends != null) {
                if (users.get(v).getFriends().contains(users.get(u)) && !visited[u]) {
                    DFS(u, visited);
                }
            }
        }
    }

    /**
     *
     * @return the number of connected components
     */
    public int connectedComponents() {
        int count = 0;
        boolean[] visited = new boolean[users.size()];
        for (int v = 0; v < users.size(); v++) {
            if (!visited[v]) {
                DFS(v, visited);
                count += 1;
            }
        }
        return count;
    }

    /**
     * generates a list of the users in the largest community
     * @return list of users
     */
    public List<User> largestCommunity() {
        int maxDist = 0;
        int index = -1;
        for (int i = 0; i < users.size(); i++) {
            int[] distances = new int[users.size()];
            BFS(i, distances);
            int max = maxDistance(distances);
            if (max > maxDist) {
                maxDist= max;
                index = i;
            }
        }
        if (index == -1) {
            return null;
        }
        return BFS(index, new int[users.size()]);
    }

    private int maxDistance(int[] paths) {
        int max = -1;
        for (int i : paths) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    /**
     * BFS algorithm
     * @param source - source node
     * @param distances - path length from source to node
     * @return - BFS for the source node
     */
    private List<User> BFS(int source, int[] distances){
        List<User> result = new LinkedList<>();
        result.add(users.get(source));
        boolean[] visited = new boolean[users.size()];
        LinkedList<Integer> queue = new LinkedList<>();
        visited[source] = true;
        distances[source] = 0;
        queue.add(source);
        while (queue.size() != 0) {
            source = queue.poll();
            List<User> friendsList = users.get(source).getFriends();
            if (friendsList != null) {
                for (User n : friendsList) {
                    if (!visited[users.indexOf(n)]) {
                        visited[users.indexOf(n)] = true;
                        result.add(n);
                        distances[users.indexOf(n)] = distances[source] + 1;
                        queue.add(users.indexOf(n));
                    }
                }
            }
        }
        return result;
    }
}