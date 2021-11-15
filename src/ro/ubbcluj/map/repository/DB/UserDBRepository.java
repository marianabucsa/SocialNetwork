package ro.ubbcluj.map.repository.DB;

import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validator.Validator;
import ro.ubbcluj.map.repository.RepositoryException;

import java.sql.*;
import java.util.*;

public class UserDBRepository extends AbstractRepoDatabase<Long, User> {

    /**
     * repository constructor
     * @param url - database information
     * @param username - database information
     * @param password - database information
     * @param validator - database information
     */
    public UserDBRepository(String url, String username, String password, Validator<User> validator) {
        super(url,username,password,validator);
    }


    @Override
    public User findOne(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Users where ID=?")) {
                statement.setLong(1, aLong);
                try (ResultSet resultSet = statement.executeQuery()) {

                    resultSet.next();
                    if(resultSet.wasNull())
                        throw new RepositoryException("User not found!");
                    Long id = resultSet.getLong("ID");
                    String nume = resultSet.getString("Nume");
                    String prenume = resultSet.getString("Prenume");
                    String email = resultSet.getString("Email");
                    User user = new User(prenume, nume, email);
                    user.setId(id);
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding user!");
        }
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from Users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String nume = resultSet.getString("Nume");
                String prenume = resultSet.getString("Prenume");
                String email = resultSet.getString("Email");
                User user = new User(prenume, nume, email);
                user.setId(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RepositoryException("Error finding users!");
        }
    }

    @Override
    public HashMap<Long, User> getAllData() {
        HashMap<Long, User> users = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from Users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String nume = resultSet.getString("Nume");
                String prenume = resultSet.getString("Prenume");
                String email = resultSet.getString("Email");
                User user = new User(prenume, nume, email);
                user.setId(id);
                users.put(user.getId(), user);
            }
            return users;
        } catch (SQLException e) {
            throw new RepositoryException("Error finding data!");
        }
    }

    @Override
    public User save(User entity) {
        if (entity == null)
            throw new RepositoryException("Entity must not be null!\n");
        validator.validate(entity);

        String sql = "insert into Users (Nume, Prenume,Email ) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getLastName());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getEmail());

            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException("Error saving user!");
        }
    }

    @Override
    public User delete(Long aLong) {
        if (aLong == null)
            throw new RepositoryException("ID must not be null!");

        String sql = "delete from Users where ID=? ";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            User us =findOne(aLong);
            ps.setLong(1, aLong);

            ps.executeUpdate();
            return us;
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting user!");
        }
    }

    @Override
    public User update(User entity) {
        if (entity == null)
            throw new RepositoryException("Entity must be not null!");
        validator.validate(entity);

        String sql = "update Users set Nume=?, Prenume=?, Email=? where ID=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getLastName());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getEmail());
            ps.setLong(4, entity.getId());


            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException("Error updating user!");
        }
    }

    @Override
    public int size() {
        int len = 0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from Users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                len++;
            }
            return len;
        } catch (SQLException e) {
            throw new RepositoryException("Error getting data!");
        }
    }

    /**
     * getter for email of a user
     * @param id - id of the user
     * @return - a String
     */
    public String getEmailFromId(Long id){
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Users where ID=?")) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {

                    resultSet.next();
                    String email=resultSet.getString("Email");
                    if(resultSet.wasNull())
                        throw new RepositoryException("User does not exist!");
                    return email;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("User does not exist!");
        }
    }

    /**
     * getter for id of a user
     * @param email - email of the user
     * @return - a Long
     */
    public Long getIdFromEmail(String email){
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Users where Email=?")) {
                statement.setString(1, email);
                try (ResultSet resultSet = statement.executeQuery()) {

                    resultSet.next();
                    Long id=resultSet.getLong("ID");
                    if(resultSet.wasNull())
                        throw new RepositoryException("User does not exist!");
                    return id;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("User does not exist!");
        }
    }
}
