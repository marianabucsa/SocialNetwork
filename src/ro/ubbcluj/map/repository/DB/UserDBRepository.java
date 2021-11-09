package ro.ubbcluj.map.repository.DB;

import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validator.Validator;
import ro.ubbcluj.map.repository.AbstractRepository;
import ro.ubbcluj.map.repository.RepositoryException;

import java.sql.*;
import java.util.*;

public class UserDBRepository extends AbstractRepository<Long, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    public UserDBRepository(String url, String username, String password, Validator<User> validator) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    private ArrayList<Long> stringToList(String arg) {
        List<String> attributes = Arrays.asList(arg.split(","));
        ArrayList<Long> ids = null;
        if (attributes.size()!=0) {
            ids= new ArrayList<>();
            for (String i : attributes) {
                if(!i.equals(""))
                    ids.add(Long.parseLong(i));
            }
        }
        return ids;
    }

    private String listToString(List<Long> list) {
        String rez = "";
        if (list == null)
            return null;
        for (Long id : list) {
            rez += id + ",";
        }
        if (rez.length() != 0)
            return rez.substring(0, rez.length() - 1);
        else return "";
    }

    @Override
    public User findOne(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Users where ID=?")) {
                statement.setLong(1, aLong);
                try (ResultSet resultSet = statement.executeQuery()) {

                    resultSet.next();
                    Long id = resultSet.getLong("ID");
                    String nume = resultSet.getString("Nume");
                    String prenume = resultSet.getString("Prenume");
                    String email = resultSet.getString("Email");
                    String atributtes = resultSet.getString("friends");
                    User user = new User(prenume, nume, email);
                    user.setId(id);
                    if (atributtes != null) {
                        ArrayList<Long> friends = new ArrayList<>(stringToList(atributtes));
                        user.setFriends(friends);
                    }
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
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
                String atributtes = resultSet.getString("friends");
                User user = new User(prenume, nume, email);
                user.setId(id);
                if (atributtes != null) {
                    ArrayList<Long> friends = new ArrayList<>(stringToList(atributtes));
                    user.setFriends(friends);
                }
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
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
                String atributtes = resultSet.getString("friends");
                User user = new User(prenume, nume, email);
                user.setId(id);
                if (atributtes != null) {
                    ArrayList<Long> friends = new ArrayList<>(stringToList(atributtes));
                    user.setFriends(friends);
                }
                users.put(user.getId(), user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {
        if (entity == null)
            throw new RepositoryException("Entity must not be null!\n");
        validator.validate(entity);

        String sql = "insert into Users (id, Nume, Prenume,Email ) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getFirstName());
            ps.setString(4, entity.getEmail());

            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
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
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public User update(User entity) {
        if (entity == null)
            throw new RepositoryException("Entity must be not null!");
        validator.validate(entity);

        String sql = "update Users set Nume=?, Prenume=?, Email=?, friends=? where ID=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getLastName());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, listToString(entity.getFriends()));
            ps.setLong(5, entity.getId());


            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
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
            throw new RepositoryException(e.getMessage());
        }
    }
}
