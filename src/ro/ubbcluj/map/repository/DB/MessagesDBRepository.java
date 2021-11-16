package ro.ubbcluj.map.repository.DB;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.ReplyMessage;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validator.Validator;
import ro.ubbcluj.map.repository.RepositoryException;
import ro.ubbcluj.map.utils.Pair;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class MessagesDBRepository extends AbstractRepoDatabase<Long, ReplyMessage> {

    /**
     * repository constructor
     *
     * @param url       - database information
     * @param username  - database information
     * @param password  - database information
     * @param validator - database information
     */
    public MessagesDBRepository(String url, String username, String password, Validator<ReplyMessage> validator) {
        super(url, username, password, validator);
    }

    private List<Long> stringToList(String line) {
        List<String> attributes = Arrays.asList(line.split(","));
        List<Long> toUsers = new ArrayList<>();
        for (String idS : attributes)
            toUsers.add(Long.parseLong(idS));
        return toUsers;
    }

    private String listToString(List<Long> toUsers) {
        StringBuilder rez = new StringBuilder();
        for (Long id : toUsers) {
            rez.append(Long.toString(id)).append(",");
        }
        return rez.toString();
    }

    @Override
    public ReplyMessage findOne(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Messages where ID=?")) {
                statement.setLong(1, aLong);
                try (ResultSet resultSet = statement.executeQuery()) {

                    resultSet.next();
                    if (resultSet.wasNull())
                        throw new RepositoryException("Message not found!");

                    Long from = resultSet.getLong("From");
                    String toUsers = resultSet.getString("To");
                    String message = resultSet.getString("Message");
                    LocalDateTime data = resultSet.getObject(5, LocalDateTime.class);
                    Long replyId = resultSet.getLong("ReplyMsg");
                    ReplyMessage replyMessage = new ReplyMessage(from, stringToList(toUsers), message, replyId);
                    replyMessage.setId(aLong);
                    validator.validate(replyMessage);
                    return replyMessage;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding message!");
        }
    }

    @Override
    public Iterable<ReplyMessage> findAll() {
        Set<ReplyMessage> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from Messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                Long from = resultSet.getLong("From");
                String toUsers = resultSet.getString("To");
                String message = resultSet.getString("Message");
                LocalDateTime data = resultSet.getObject(5, LocalDateTime.class);
                Long replyId = resultSet.getLong("ReplyMsg");
                ReplyMessage replyMessage = new ReplyMessage(from, stringToList(toUsers), message, replyId);
                replyMessage.setId(id);
                validator.validate(replyMessage);
                messages.add(replyMessage);
            }
            return messages;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public ReplyMessage save(ReplyMessage entity) {
        if (entity == null)
            throw new RepositoryException("Entity must not be null!\n");
        validator.validate(entity);

        String sql = "insert into Messages (from,to,message,date,replyMsg) values ( ?,?,?,?,?)ó";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getFrom());
            ps.setString(2, listToString(entity.getTo()));
            ps.setString(3,entity.getMessage());
            java.util.Date today = new java.util.Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(today.getTime());
            ps.setTimestamp(4, timestamp);
            ps.setLong(5,entity.getMessageToReplay());

            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public ReplyMessage delete(Long aLong) {
        if (aLong == null)
            throw new RepositoryException("ID must not be null!");

        String sql = "delete from Messages where ID=? ";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ReplyMessage rm =findOne(aLong);
            ps.setLong(1, aLong);

            ps.executeUpdate();
            return rm;
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting user!");
        }
    }

    @Override
    public ReplyMessage update(ReplyMessage entity) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public HashMap<Long, ReplyMessage> getAllData() {
        return null;
    }

}
