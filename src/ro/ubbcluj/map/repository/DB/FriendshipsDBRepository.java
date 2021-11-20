package ro.ubbcluj.map.repository.DB;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.validator.Validator;
import ro.ubbcluj.map.repository.RepositoryException;
import ro.ubbcluj.map.utils.Constants;
import ro.ubbcluj.map.utils.Pair;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class FriendshipsDBRepository extends AbstractRepoDatabase<Pair, Friendship> {

    /**
     * repository constructor
     * @param url - database information
     * @param username - database information
     * @param password - database information
     * @param validator - database information
     */
    public FriendshipsDBRepository(String url, String username, String password, Validator<Friendship> validator) {
        super(url,username,password,validator);
    }

    @Override
    public Friendship findOne(Pair pair) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Friendships where Id1=? and Id2=?")) {
                statement.setLong(1, pair.getId1());
                statement.setLong(2, pair.getId2());
                try (ResultSet resultSet = statement.executeQuery()) {

                    resultSet.next();
                    Long id1 = resultSet.getLong("Id1");
                    Long id2 = resultSet.getLong("Id2");

                    Friendship friendship = new Friendship(id1, id2);
                    friendship.setId(pair);
                    return friendship;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from Friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("Id1");
                Long id2 = resultSet.getLong("Id2");

                Friendship friendship = new Friendship(id1, id2);
                Pair pair = new Pair(id1, id2);
                friendship.setId(pair);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }


    @Override
    public HashMap<Pair, Friendship> getAllData() {
        HashMap<Pair, Friendship> friendships = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from Friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("Id1");
                Long id2 = resultSet.getLong("Id2");

                Friendship friendship = new Friendship(id1, id2);
                Pair pair = new Pair(id1, id2);
                friendship.setId(pair);
                friendships.put(friendship.getId(), friendship);
            }
            return friendships;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Friendship save(Friendship entity) {
        if (entity == null)
            throw new RepositoryException("Entity must not be null!\n");
        validator.validate(entity);

        String sql = "insert into Friendships (Id1, Id2, date) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getPair().getId1());
            ps.setLong(2, entity.getPair().getId2());
            ps.setTimestamp(3, Timestamp.valueOf( LocalDateTime.now()));//.format(Constants.DATE_TIME_FORMATTER)));

            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Friendship delete(Pair pair) {
        if (pair == null)
            throw new RepositoryException("ID must not be null!");
        Pair id = pair;
        boolean friendshipExists = false;
        for (Pair i : getAllData().keySet()) {
            if (i.equals(pair)) {
                friendshipExists = true;
                id = i;
            }
        }
        if (friendshipExists) {

            String sql = "delete from Friendships where id1=? and id2=?";

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                Friendship friendship = new Friendship(id.getId1(), id.getId2());
                friendship.setId(id);
                ps.setLong(1, id.getId1());
                ps.setLong(2, id.getId2());

                ps.executeUpdate();
                return friendship;
            } catch (SQLException e) {
                throw new RepositoryException(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public Friendship update(Friendship entity) {

        if (entity == null)
            throw new RepositoryException("Entity must be not null!");
        validator.validate(entity);

        String sql = "update Friendships set Status=? where id1=? and id2=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getStatus());
            ps.setLong(2, entity.getPair().getId1());
            ps.setLong(3, entity.getPair().getId2());


            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException("Error updating friendship!");
        }
    }


    @Override
    public int size() {
        int len = 0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from Friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                len++;
            }
            return len;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * Getter for friend requests
     * @param id user id
     * @return All friend requests for an user
     */
    public List<Long> getFriendRequests(Long id){
        List<Long> friends=new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Friendships where (Id1=? or Id2=?) and status='pending'")) {
                statement.setLong(1, id);
                statement.setLong(2, id);
                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        Long id1 = resultSet.getLong("Id1");
                        Long id2 = resultSet.getLong("Id2");

                        if(!id1.equals(id))
                            friends.add(id1);
                        //if(id1.equals(id))
                        // friends.add(id2);
                        // else
                        //   friends.add(id1);
                    }
                    return friends;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * getter for all friends of a user
     * @param id - user id
     * @return a list of ids
     */
    public List<Long> getFriendsUser(Long id){
        List<Long> friends=new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Friendships where (Id1=? or Id2=?) and status='approved'")) {
                statement.setLong(1, id);
                statement.setLong(2, id);
                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        Long id1 = resultSet.getLong("Id1");
                        Long id2 = resultSet.getLong("Id2");

                        if(id1.equals(id))
                            friends.add(id2);
                        else
                            friends.add(id1);
                    }
                    return friends;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * Get the status of a friendship
     * @param pair pair of user id's
     * @return friendship status(string)
     */
    public String getFriendshipStatus(Pair pair){
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Friendships where Id1=? and Id2=?")) {
                statement.setLong(1, pair.getId1());
                statement.setLong(2, pair.getId2());
                try (ResultSet resultSet = statement.executeQuery()) {

                    resultSet.next();
                    //Long id1 = resultSet.getLong("Id1");
                    // Long id2 = resultSet.getLong("Id2");
                    String status = resultSet.getString("status");

                    return status;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * Find a friendship date
     * @param pair  pair of user id's
     * @return timestamp ()
     */
    public Timestamp getFriendshipDate(Pair pair){
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Friendships where Id1=? and Id2=?")) {
                statement.setLong(1, pair.getId1());
                statement.setLong(2, pair.getId2());
                try (ResultSet resultSet = statement.executeQuery()) {

                    resultSet.next();
                    //Long id1 = resultSet.getLong("Id1");
                    // Long id2 = resultSet.getLong("Id2");
                    Timestamp timestamp = resultSet.getTimestamp("date");

                    return timestamp;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}