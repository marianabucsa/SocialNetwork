package com.example.socialnetworkgui.repository.DB;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.validator.Validator;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.utils.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class FriendshipsDBRepository extends AbstractRepoDatabase<Pair, Friendship> {

    /**
     * repository constructor
     *
     * @param url       - database information
     * @param username  - database information
     * @param password  - database information
     * @param validator - database information
     */
    public FriendshipsDBRepository(String url, String username, String password, Validator<Friendship> validator) {
        super(url, username, password, validator);
    }

    /**
     * @param pair -the id of the entity to be returned
     *             id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws RepositoryException if id is null.
     */
    @Override
    public Friendship findOne(Pair pair) {
        try (PreparedStatement statement = getConnection().prepareStatement("select * from Friendships where Id1=? and Id2=?")) {
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
        } catch (SQLException se) {
            throw new RepositoryException("Error finding friendship in database!\n");
        }
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * from Friendships");
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
            throw new RepositoryException("Error finding friendships in database!\n");
        }
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidatorException  if the entity is not valid
     * @throws RepositoryException if the given entity is null.     *
     */
    @Override
    public Friendship save(Friendship entity) {
        if (entity == null)
            throw new RepositoryException("Entity must not be null!\n");
        validator.validate(entity);

        String sql = "insert into Friendships (Id1, Id2, date) values (?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setLong(1, entity.getPair().getId1());
            ps.setLong(2, entity.getPair().getId2());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));//.format(Constants.DATE_TIME_FORMATTER)));

            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException("Error saving friendship in database!\n");
        }
    }

    /**
     * removes the entity with the specified id
     *
     * @param pair must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws RepositoryException if the given id is null.
     */
    @Override
    public Friendship delete(Pair pair) {
        if (pair == null)
            throw new RepositoryException("ID must not be null!\n");
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

            try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
                Friendship friendship = new Friendship(id.getId1(), id.getId2());
                friendship.setId(id);
                ps.setLong(1, id.getId1());
                ps.setLong(2, id.getId2());

                ps.executeUpdate();
                return friendship;
            } catch (SQLException e) {
                throw new RepositoryException("Error deleting friendship in database!\n");
            }
        }
        return null;
    }

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated,
     * otherwise  returns the entity  - (e.g. id does not exist).
     * @throws RepositoryException if the given entity is null.
     * @throws ValidatorException  if the entity is not valid.
     */
    @Override
    public Friendship update(Friendship entity) {

        if (entity == null)
            throw new RepositoryException("Entity must be not null!");
        validator.validate(entity);

        String sql = "update Friendships set Status=? where id1=? and id2=?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setString(1, entity.getStatus());
            ps.setLong(2, entity.getPair().getId1());
            ps.setLong(3, entity.getPair().getId2());


            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException("Error updating friendship in database!\n");
        }
    }


    /**
     * getter for the size of the repository
     *
     * @return - an integer that represents the size of the repository
     */
    @Override
    public int size() {
        int len = 0;
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * from Friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                len++;
            }
            return len;
        } catch (SQLException e) {
            throw new RepositoryException("Error counting friendships in database!\n");
        }
    }

    /**
     * gets all data from repository
     *
     * @return - a hash map
     */
    @Override
    public HashMap<Pair, Friendship> getAllData() {
        HashMap<Pair, Friendship> friendships = new HashMap<>();
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * from Friendships");
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
            throw new RepositoryException("Error getting data from friendships table in database!\n");
        }
    }

    /**
     * Getter for friend requests
     *
     * @param id user id
     * @return All friend requests for an user
     */
    public List<Long> getFriendRequests(Long id) {
        List<Long> friends = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement("select * from Friendships where (Id1=? or Id2=?) and status='pending'")) {
            statement.setLong(1, id);
            statement.setLong(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Long id1 = resultSet.getLong("Id1");
                    Long id2 = resultSet.getLong("Id2");

                    if (!id1.equals(id))
                        friends.add(id1);
                    //if(id1.equals(id))
                    // friends.add(id2);
                    // else
                    //   friends.add(id1);
                }
                return friends;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error getting friend requests form database!\n");
        }
    }

    /**
     * getter for all friends of a user
     *
     * @param id - user id
     * @return a list of ids
     */
    public List<Long> getFriendsUser(Long id) {
        List<Long> friends = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement("select * from Friendships where (Id1=? or Id2=?) and status='approved'")) {
            statement.setLong(1, id);
            statement.setLong(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Long id1 = resultSet.getLong("Id1");
                    Long id2 = resultSet.getLong("Id2");

                    if (id1.equals(id))
                        friends.add(id2);
                    else
                        friends.add(id1);
                }
                return friends;
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
            //throw new RepositoryException("Error getting friends for user in database!\n");
        }
    }

    /**
     * Get the status of a friendship
     *
     * @param pair pair of user id's
     * @return friendship status(string)
     */
    public String getFriendshipStatus(Pair pair) {
        try (PreparedStatement statement = getConnection().prepareStatement("select * from Friendships where Id1=? and Id2=?")) {
            statement.setLong(1, pair.getId1());
            statement.setLong(2, pair.getId2());
            try (ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();
                //Long id1 = resultSet.getLong("Id1");
                // Long id2 = resultSet.getLong("Id2");
                String status = resultSet.getString("status");

                return status;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error getting friendship status in database!\n");
        }
    }

    /**
     * Find a friendship date
     *
     * @param pair pair of user id's
     * @return timestamp ()
     */
    public Timestamp getFriendshipDate(Pair pair) {
        try (PreparedStatement statement = getConnection().prepareStatement("select * from Friendships where Id1=? and Id2=?")) {
            statement.setLong(1, pair.getId1());
            statement.setLong(2, pair.getId2());
            try (ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();
                //Long id1 = resultSet.getLong("Id1");
                // Long id2 = resultSet.getLong("Id2");
                Timestamp timestamp = resultSet.getTimestamp("date");

                return timestamp;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error getting friendship date in database!\n");
        }
    }
}