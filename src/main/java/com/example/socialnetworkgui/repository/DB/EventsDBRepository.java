package com.example.socialnetworkgui.repository.DB;

import com.example.socialnetworkgui.domain.Event;
import com.example.socialnetworkgui.domain.validator.Validator;
import com.example.socialnetworkgui.repository.RepositoryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class EventsDBRepository extends AbstractRepoDatabase<Long, Event> {
    /**
     * repository constructor
     *
     * @param url       - database information
     * @param username  - database information
     * @param password  - database information
     * @param validator - database information
     */
    public EventsDBRepository(String url, String username, String password, Validator<Event> validator) {
        super(url, username, password, validator);
    }

    @Override
    public Event findOne(Long aLong) {
        try (PreparedStatement statement = getConnection().prepareStatement("select * from Events where ID=?")) {
            statement.setLong(1, aLong);
            try (ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();
                if (resultSet.wasNull())
                    throw new RepositoryException("Event not found!");
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                LocalDateTime startDate = resultSet.getObject(3, LocalDateTime.class);
                LocalDateTime endDate = resultSet.getObject(4, LocalDateTime.class);
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");
                Long organizer = resultSet.getLong("organizer");
                String participants = resultSet.getString("participants");
                Event event;
                if (participants != null) {
                    event = new Event(name, startDate, endDate, description, location, organizer, stringToList(participants));
                } else {
                    event = new Event(name, startDate, endDate, description, location, organizer, null);
                }
                event.setId(id);
                return event;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding event in database!\n");
        }
    }

    @Override
    public Iterable<Event> findAll() {
        Set<Event> events = new HashSet<>();
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * from Events");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                LocalDateTime startDate = resultSet.getObject(3, LocalDateTime.class);
                LocalDateTime endDate = resultSet.getObject(4, LocalDateTime.class);
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");
                Long organizer = resultSet.getLong("organizer");
                String participants = resultSet.getString("participants");
                Event event;
                if (participants != null) {
                    event = new Event(name, startDate, endDate, description, location, organizer, stringToList(participants));
                } else {
                    event = new Event(name, startDate, endDate, description, location, organizer, null);
                }
                event.setId(id);
                events.add(event);
            }
            return events;
        } catch (SQLException e) {
            throw new RepositoryException("Error finding messages in database!\n");
        }
    }

    @Override
    public Event save(Event entity) {
        if (entity == null)
            throw new RepositoryException("Entity must not be null!\n");
        validator.validate(entity);
        String sql = "insert into Events (name, startdate, enddate,description,location,organizer) values (?, ?, ?, ?, ?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getStartDate()));
            ps.setTimestamp(3, Timestamp.valueOf(entity.getEndDate()));
            ps.setString(4, entity.getDescription());
            ps.setString(5, entity.getLocation());
            ps.setLong(6, entity.getOrganizer());
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException("Error saving event in database!\n");
        }
    }

    @Override
    public Event delete(Long aLong) {
        if (aLong == null)
            throw new RepositoryException("ID must not be null!");

        String sql = "delete from Events where ID=? ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            Event event = findOne(aLong);
            ps.setLong(1, aLong);
            ps.executeUpdate();
            return event;
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting event in database!\n");
        }
    }

    @Override
    public Event update(Event entity) {
        if (entity == null)
            throw new RepositoryException("Entity must not be null!\n");
        validator.validate(entity);
        String sql;
        if (entity.getParticipants() != null) {
            sql = "update Events set name=?, startdate=?, enddate=?,description=?,location=?,participants=? where id=?";
        } else {
            sql = "update Events set name=?, startdate=?, enddate=?,description=?,location=? where id=?";
        }
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getStartDate()));
            ps.setTimestamp(3, Timestamp.valueOf(entity.getEndDate()));
            ps.setString(4, entity.getDescription());
            ps.setString(5, entity.getLocation());
            if (entity.getParticipants() != null) {
                if(entity.getParticipants().size()==0)
                    ps.setString(6,null);
                else {
                    ps.setString(6, listToString(entity.getParticipants()));
                }
                ps.setLong(7, entity.getId());
            } else {
                ps.setLong(6, entity.getId());
            }
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException("Error updating event in database!\n");
        }
    }

    @Override
    public int size() {
        int len = 0;
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * from Events");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                len++;
            }
            return len;
        } catch (SQLException e) {
            throw new RepositoryException("Error counting events in database!\n");
        }
    }

    @Override
    public HashMap<Long, Event> getAllData() {
        HashMap<Long, Event> events = new HashMap<>();
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * from Events");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                LocalDateTime startDate = resultSet.getObject(3, LocalDateTime.class);
                LocalDateTime endDate = resultSet.getObject(4, LocalDateTime.class);
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");
                Long organizer = resultSet.getLong("organizer");
                String participants = resultSet.getString("participants");
                Event event;
                if (participants != null) {
                    event = new Event(name, startDate, endDate, description, location, organizer, stringToList(participants));
                } else {
                    event = new Event(name, startDate, endDate, description, location, organizer, null);
                }
                event.setId(id);
                events.put(id, event);
            }
            return events;
        } catch (SQLException e) {
            throw new RepositoryException("Error finding messages in database!\n");
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

    public List<Event> getEventsUser(Long id) {
        List<Event> events = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement("select * from Events where organizer=?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Long id1 = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    LocalDateTime startDate = resultSet.getObject(3, LocalDateTime.class);
                    LocalDateTime endDate = resultSet.getObject(4, LocalDateTime.class);
                    String description = resultSet.getString("description");
                    String location = resultSet.getString("location");
                    Long organizer = resultSet.getLong("organizer");
                    String participants = resultSet.getString("participants");
                    Event event = new Event(name, startDate, endDate, description, location, organizer, null);
                    if (participants != null)
                        event.setParticipants(stringToList(participants));
                    event.setId(id1);
                    events.add(event);
                }
                return events;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //throw new RepositoryException("Error getting events for user in database!\n");
        }
        return null;
    }
}
