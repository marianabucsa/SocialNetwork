package com.example.socialnetworkgui.repository.DB;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validator.Validator;
import com.example.socialnetworkgui.domain.validator.ValidatorException;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.utils.AES256;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UserDBRepository extends AbstractRepoDatabase<Long, User> {

    /**
     * repository constructor
     *
     * @param url       - database information
     * @param username  - database information
     * @param password  - database information
     * @param validator - database information
     */
    public UserDBRepository(String url, String username, String password, Validator<User> validator) {
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
    public User findOne(Long aLong) {
        try (PreparedStatement statement = getConnection().prepareStatement("select * from Users where ID=?")) {
            statement.setLong(1, aLong);
            try (ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();
                if (resultSet.wasNull())
                    throw new RepositoryException("User not found!");
                Long id = resultSet.getLong("ID");
                String nume = resultSet.getString("Nume");
                String prenume = resultSet.getString("Prenume");
                String email = resultSet.getString("Email");
                String password = resultSet.getString("Password");
                User user = new User(prenume, nume, email);
                user.setId(id);
                user.setPassword(AES256.decrypt( password));
                return user;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding user in database!\n");
        }
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * from Users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String nume = resultSet.getString("Nume");
                String prenume = resultSet.getString("Prenume");
                String email = resultSet.getString("Email");
                String password = resultSet.getString("Password");
                User user = new User(prenume, nume, email);
                user.setId(id);
                user.setPassword( AES256.decrypt(password));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RepositoryException("Error finding users in database!\n");
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
    public User save(User entity) {
        if (entity == null)
            throw new RepositoryException("Entity must not be null!\n");
        validator.validate(entity);

        String sql = "insert into Users (Nume, Prenume,Email,Password ) values (?, ?, ?,?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setString(1, entity.getLastName());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, AES256.encrypt(entity.getPassword()));

            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
           throw new RepositoryException("Error saving user in database!\n");
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
    public User delete(Long aLong) {
        if (aLong == null)
            throw new RepositoryException("ID must not be null!");

        String sql = "delete from Users where ID=? ";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            User us = findOne(aLong);
            ps.setLong(1, aLong);

            ps.executeUpdate();
            return us;
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting user in database!");
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
    public User update(User entity) {
        if (entity == null)
            throw new RepositoryException("Entity must be not null!");
        validator.validate(entity);

        String sql = "update Users set Nume=?, Prenume=?, Email=?,Password=? where ID=?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {

            ps.setString(1, entity.getLastName());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getEmail());
           // ps.setLong(4, entity.getId());
            ps.setString(4,AES256.encrypt( entity.getPassword()));
            ps.setLong(5, entity.getId());


            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException("Error updating user in database!\n");
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
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * from Users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                len++;
            }
            return len;
        } catch (SQLException e) {
            throw new RepositoryException("Error counting users in database!\n");
        }
    }

    /**
     * gets all data from repository
     *
     * @return - a hash map
     */
    @Override
    public HashMap<Long, User> getAllData() {
        HashMap<Long, User> users = new HashMap<>();
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * from Users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String nume = resultSet.getString("Nume");
                String prenume = resultSet.getString("Prenume");
                String email = resultSet.getString("Email");
                String password = resultSet.getString("Password");
                User user = new User(prenume, nume, email);
                user.setId(id);
                user.setPassword(AES256.decrypt( password));
                users.put(user.getId(), user);
            }
            return users;
        } catch (SQLException e) {
            throw new RepositoryException("Error getting data from users table in database!\n");
        }
    }

    /**
     * getter for email of a user
     *
     * @param id - id of the user
     * @return - a String
     */
    public String getEmailFromId(Long id) {
        try (PreparedStatement statement = getConnection().prepareStatement("select * from Users where ID=?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();
                String email = resultSet.getString("Email");
                if (resultSet.wasNull())
                    throw new RepositoryException("User does not exist!");
                return email;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding user in database!\n");
        }
    }

    /**
     * getter for id of a user
     *
     * @param email - email of the user
     * @return - a Long
     */
    public Long getIdFromEmail(String email) {
        try (PreparedStatement statement = getConnection().prepareStatement("select * from Users where Email=?")) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();
                Long id = resultSet.getLong("ID");
                if (resultSet.wasNull())
                    throw new RepositoryException("User does not exist!");
                return id;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding user in database!\n");
        }
    }
}
