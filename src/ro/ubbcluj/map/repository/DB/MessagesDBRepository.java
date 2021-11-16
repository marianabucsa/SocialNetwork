package ro.ubbcluj.map.repository.DB;

import ro.ubbcluj.map.domain.ReplyMessage;
import ro.ubbcluj.map.domain.validator.Validator;
import ro.ubbcluj.map.repository.RepositoryException;

import java.sql.*;
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
        String rez1 = rez.toString();
        return rez1.substring(0, rez1.length() - 1);
    }

    @Override
    public ReplyMessage findOne(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Messages where id=?")) {
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
            throw new RepositoryException(e.getMessage());
        }
    }

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

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getFrom());
            ps.setString(2, listToString(entity.getTo()));
            ps.setString(3, entity.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            if (entity.getReplyingToMessage() != null)
                ps.setLong(5, entity.getReplyingToMessage());

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
            ReplyMessage rm = findOne(aLong);
            ps.setLong(1, aLong);

            ps.executeUpdate();
            return rm;
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting message!");
        }
    }

    public List<ReplyMessage> showConversation(Long id1, Long id2) {
        Set<ReplyMessage> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * from Messages where (sender=? and receiver like ? ) or (sender=? and receiver like ?)")) {
                ps.setLong(1, id1);
                ps.setString(2, "%"+id2+"%");
                ps.setLong(3, id2);
                ps.setString(4, "%"+id1+"%");
                try (ResultSet resultSet = ps.executeQuery()) {

                    while (resultSet.next()) {
                        Long id = resultSet.getLong("id");
                        Long from = resultSet.getLong("sender");
                        String toUsers = resultSet.getString("receiver");
                        String message = resultSet.getString("message");
                        LocalDateTime data = resultSet.getObject(5, LocalDateTime.class);
                        Long replyId = resultSet.getLong("replyingto");
                        ReplyMessage replyMessage = new ReplyMessage(from, stringToList(toUsers),data, message, replyId);
                        replyMessage.setId(id);
                        validator.validate(replyMessage);
                        messages.add(replyMessage);
                    }
                    return messages.stream().sorted(Comparator.comparing(ReplyMessage::getData))
                            .collect(Collectors.toList());
                } catch (SQLException e) {
                    throw new RepositoryException(e.getMessage());
                }
            } catch (SQLException e) {
                throw new RepositoryException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public List<ReplyMessage> findMessagesUser(Long id) {
        Set<ReplyMessage> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * from Messages where sender=? or receiver like ?" )) {
                ps.setLong(1, id);
                ps.setString(2, "%"+id+"%");
                try (ResultSet resultSet = ps.executeQuery()) {

                    while (resultSet.next()) {
                        Long id1 = resultSet.getLong("id");
                        Long from = resultSet.getLong("sender");
                        String toUsers = resultSet.getString("receiver");
                        String message = resultSet.getString("message");
                        LocalDateTime data = resultSet.getObject(5, LocalDateTime.class);
                        Long replyId = resultSet.getLong("replyingto");
                        ReplyMessage replyMessage = new ReplyMessage(from, stringToList(toUsers),data, message, replyId);
                        replyMessage.setId(id1);
                        validator.validate(replyMessage);
                        messages.add(replyMessage);
                    }
                    return messages.stream().sorted(Comparator.comparing(ReplyMessage::getData))
                            .collect(Collectors.toList());
                } catch (SQLException e) {
                    throw new RepositoryException(e.getMessage());
                }
            } catch (SQLException e) {
                throw new RepositoryException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
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
