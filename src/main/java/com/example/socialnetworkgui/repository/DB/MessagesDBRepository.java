package com.example.socialnetworkgui.repository.DB;

import com.example.socialnetworkgui.domain.ReplyMessage;
import com.example.socialnetworkgui.domain.validator.Validator;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.repository.paging.Page;
import com.example.socialnetworkgui.repository.paging.Pageable;
import com.example.socialnetworkgui.repository.paging.PageableInterface;
import com.example.socialnetworkgui.repository.paging.Paginator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * @param aLong -the id of the entity to be returned
     *              id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws RepositoryException if id is null.
     */
    @Override
    public ReplyMessage findOne(Long aLong) {
        try (PreparedStatement statement = getConnection().prepareStatement("select * from Messages where id=?")) {
            statement.setLong(1, aLong);
            try (ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();
                if (resultSet.wasNull())
                    throw new RepositoryException("Message not found!");

                Long from = resultSet.getLong("sender");
                String toUsers = resultSet.getString("receiver");
                String message = resultSet.getString("message");
                LocalDateTime data = resultSet.getObject(5, LocalDateTime.class);
                Long replyId = resultSet.getLong("replyingto");
                ReplyMessage replyMessage = new ReplyMessage(from, stringToList(toUsers), data, message, replyId);
                replyMessage.setId(aLong);
                validator.validate(replyMessage);
                return replyMessage;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding message in database!\n");
        }
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<ReplyMessage> findAll() {
        Set<ReplyMessage> messages = new HashSet<>();
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * from Messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long from = resultSet.getLong("sender");
                String toUsers = resultSet.getString("receiver");
                String message = resultSet.getString("message");
                LocalDateTime data = resultSet.getObject(5, LocalDateTime.class);
                Long replyingId = resultSet.getLong("replyingto");
                ReplyMessage replyMessage = new ReplyMessage(from, stringToList(toUsers), data, message, replyingId);
                replyMessage.setId(id);
                validator.validate(replyMessage);
                messages.add(replyMessage);
            }
            return messages;
        } catch (SQLException e) {
            throw new RepositoryException("Error finding messages in database!\n");
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
    public ReplyMessage save(ReplyMessage entity) {
        if (entity == null)
            throw new RepositoryException("Entity must not be null!\n");
        validator.validate(entity);
        String sql;
        if (entity.getReplyingToMessage() != null)
            sql = "insert into messages (sender, receiver, message, data, replyingTo) values (?,?,?,?,?)";
        else
            sql = "insert into messages (sender,receiver,message,data) values (?,?,?,?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setLong(1, entity.getFrom());
            ps.setString(2, listToString(entity.getTo()));
            ps.setString(3, entity.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            if (entity.getReplyingToMessage() != null)
                ps.setLong(5, entity.getReplyingToMessage());

            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException("Error saving message in database!\n");
        }
    }

    /**
     * removes the entity with the specified id
     *
     * @param aLong must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws RepositoryException if the given id is null.
     */
    @Override
    public ReplyMessage delete(Long aLong) {
        if (aLong == null)
            throw new RepositoryException("ID must not be null!");

        String sql = "delete from Messages where ID=? ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ReplyMessage rm = findOne(aLong);
            ps.setLong(1, aLong);

            ps.executeUpdate();
            return rm;
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting message in database!\n");
        }
    }

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated,
     * otherwise  returns the entity  - (e.g. id does not exist).
     * @throws RepositoryException if the given entity is null.
     * @throws ValidatorException  if the entity is not valid.
     */
    @Override
    public ReplyMessage update(ReplyMessage entity) {
        if (entity == null)
            throw new RepositoryException("Entity must not be null!\n");
        validator.validate(entity);
        String sql;
        if (entity.getReplyingToMessage() != null)
            sql = "update messages set (sender,receiver,message,data,replyingto) values (?,?,?,?,?) where id=?";
        else
            sql = "update messages set (sender,receiver,message,data) values (?,?,?,?) where id=?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setLong(1, entity.getFrom());
            ps.setString(2, listToString(entity.getTo()));
            ps.setString(3, entity.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            if (entity.getReplyingToMessage() != null) {
                ps.setLong(5, entity.getReplyingToMessage());
                ps.setLong(6, entity.getId());
            } else
                ps.setLong(5, entity.getId());
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException("Error updating message in database!\n");
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
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                len++;
            }
            return len;
        } catch (SQLException e) {
            throw new RepositoryException("Error counting messages in database!\n");
        }
    }

    /**
     * gets all data from repository
     *
     * @return - a hash map
     */
    @Override
    public HashMap<Long, ReplyMessage> getAllData() {
        HashMap<Long, ReplyMessage> messages = new HashMap<>();
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long from = resultSet.getLong("sender");
                String toUsers = resultSet.getString("receiver");
                String message = resultSet.getString("message");
                LocalDateTime data = resultSet.getObject(5, LocalDateTime.class);
                Long replyId = resultSet.getLong("replyingto");
                ReplyMessage replyMessage = new ReplyMessage(from, stringToList(toUsers), data, message, replyId);
                replyMessage.setId(id);
                validator.validate(replyMessage);
                messages.put(id, replyMessage);
            }
            return messages;
        } catch (SQLException e) {
            throw new RepositoryException("Erro getting data from messages table in database!\n");
        }
    }

    /**
     * gets the conversation between 2 users
     *
     * @param id1 - an id of a user
     * @param id2 - an id of a user
     * @return - a list of reply messages
     */
    public List<ReplyMessage> showConversation(Long id1, Long id2) {
        Set<ReplyMessage> messages = new HashSet<>();
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * from Messages where (sender=? and receiver like ? ) or (sender=? and receiver like ?)")) {
            ps.setLong(1, id1);
            ps.setString(2, "%" + id2 + "%");
            ps.setLong(3, id2);
            ps.setString(4, "%" + id1 + "%");
            try (ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    Long from = resultSet.getLong("sender");
                    String toUsers = resultSet.getString("receiver");
                    String message = resultSet.getString("message");
                    LocalDateTime data = resultSet.getObject(5, LocalDateTime.class);
                    Long replyId = resultSet.getLong("replyingto");
                    ReplyMessage replyMessage = new ReplyMessage(from, stringToList(toUsers), data, message, replyId);
                    replyMessage.setId(id);
                    validator.validate(replyMessage);
                    messages.add(replyMessage);
                }
                return messages.stream().sorted(Comparator.comparing(ReplyMessage::getData))
                        .collect(Collectors.toList());
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error getting conversation in database!\n");
        }
    }

    /**
     * gets all messages sent or received by the user
     *
     * @param id - a user id
     * @return - a list of reply messages
     */
    public List<ReplyMessage> findMessagesUser(Long id) {
        Set<ReplyMessage> messages = new HashSet<>();
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * from Messages where sender=? or receiver like ?")) {
            ps.setLong(1, id);
            ps.setString(2, "%" + id + "%");
            try (ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    Long id1 = resultSet.getLong("id");
                    Long from = resultSet.getLong("sender");
                    String toUsers = resultSet.getString("receiver");
                    String message = resultSet.getString("message");
                    LocalDateTime data = resultSet.getObject(5, LocalDateTime.class);
                    Long replyId = resultSet.getLong("replyingto");
                    ReplyMessage replyMessage = new ReplyMessage(from, stringToList(toUsers), data, message, replyId);
                    replyMessage.setId(id1);
                    validator.validate(replyMessage);
                    messages.add(replyMessage);
                }
                return messages.stream().sorted(Comparator.comparing(ReplyMessage::getData))
                        .collect(Collectors.toList());
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding messages from users!\n");
        }
    }

    /**
     * creates a list from the data in a string
     *
     * @param line - a string of numbers separated by ","
     * @return - a list of long numbers
     */
    private List<Long> stringToList(String line) {
        List<String> attributes = Arrays.asList(line.split(","));
        List<Long> toUsers = new ArrayList<>();
        for (String idS : attributes)
            toUsers.add(Long.parseLong(idS));
        return toUsers;
    }

    /**
     * creates a string from the data in a list
     *
     * @param toUsers - a list of user ids, list of long
     * @return - a string of numbers separated by ","
     */
    private String listToString(List<Long> toUsers) {
        StringBuilder rez = new StringBuilder();
        for (Long id : toUsers) {
            rez.append(Long.toString(id)).append(",");
        }
        String rez1 = rez.toString();
        return rez1.substring(0, rez1.length() - 1);
    }

    /**
     * gets all messages received by the user
     *
     * @param id - a user id
     * @return - a list of reply messages
     */
    public List<ReplyMessage> getToReplyForUser(Long id) {
        Set<ReplyMessage> messages = new HashSet<>();
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * from Messages where receiver like ?")) {
            ps.setString(1, "%" + id + "%");
            try (ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    Long id1 = resultSet.getLong("id");
                    Long from = resultSet.getLong("sender");
                    String toUsers = resultSet.getString("receiver");
                    String message = resultSet.getString("message");
                    LocalDateTime data = resultSet.getObject(5, LocalDateTime.class);
                    Long replyId = resultSet.getLong("replyingto");
                    ReplyMessage replyMessage = new ReplyMessage(from, stringToList(toUsers), data, message, replyId);
                    replyMessage.setId(id1);
                    validator.validate(replyMessage);
                    messages.add(replyMessage);
                }
                return messages.stream().sorted(Comparator.comparing(ReplyMessage::getData))
                        .collect(Collectors.toList());
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error getting conversation in database!\n");
        }
    }

    /**
     * gets all messages sent by the user
     *
     * @param id - a user id
     * @return - a list of reply messages
     */
    public List<ReplyMessage> getSentForUser(Long id) {
        Set<ReplyMessage> messages = new HashSet<>();
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * from Messages where sender= ?")) {
            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    Long id1 = resultSet.getLong("id");
                    Long from = resultSet.getLong("sender");
                    String toUsers = resultSet.getString("receiver");
                    String message = resultSet.getString("message");
                    LocalDateTime data = resultSet.getObject(5, LocalDateTime.class);
                    Long replyId = resultSet.getLong("replyingto");
                    ReplyMessage replyMessage = new ReplyMessage(from, stringToList(toUsers), data, message, replyId);
                    replyMessage.setId(id1);
                    validator.validate(replyMessage);
                    messages.add(replyMessage);
                }
                return messages.stream().sorted(Comparator.comparing(ReplyMessage::getData))
                        .collect(Collectors.toList());
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error getting conversation in database!\n");
        }
    }

    @Override
    public Page<ReplyMessage> findAll(PageableInterface pageable) {
        Paginator<ReplyMessage> paginator = new Paginator<ReplyMessage>(pageable, this.findAll());
        return paginator.paginate();
    }
}
