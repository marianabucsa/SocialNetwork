package com.example.socialnetworkgui.repository.file;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validator.Validator;
import com.example.socialnetworkgui.repository.AbstractRepository;
import com.example.socialnetworkgui.repository.RepositoryException;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class UserFileRepository extends AbstractRepository<Long, User> {
    private String fileName;

    /**
     * repository constructor
     *
     * @param fileName  - the name of the file that contains the data
     * @param validator - validator for entities of type E
     */
    public UserFileRepository(Validator validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        readFromFile();
    }

    /**
     * creates a string from the user's data
     *
     * @param entity - a user
     * @return - a string
     */
    private String createEntityAsString(User entity) {
        return entity.getId().toString() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";" + entity.getEmail();
    }

    /**
     * reads data from file and loads it in memory
     */
    private void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            entities.clear();
            while ((line = br.readLine()) != null) {
                List<String> attributes = Arrays.asList(line.split(";"));
                if (attributes.size() == 4) {
                    User us = new User(attributes.get(1), attributes.get(2), attributes.get(3));
                    us.setId(Long.parseLong(attributes.get(0)));
                    super.save(us);
                } else
                    throw new RepositoryException("Incomplete line!\n");
            }
        } catch (FileNotFoundException e) {
            throw new RepositoryException("Fail to find or open file!\n");
        } catch (IOException e) {
            throw new RepositoryException("Fail to read from file!\n");
        }
    }

    /**
     * writes data from memory in file
     */
    private void writeToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, false))) {
            for (User entity : super.findAll()) {
                bw.write(createEntityAsString(entity));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryException("Fail to write in file!\n");
        }
    }


    @Override
    public User findOne(Long id) {
        return super.findOne(id);
    }

    @Override
    public Iterable<User> findAll() {
        return super.findAll();
    }

    @Override
    public User save(User entity) {
        readFromFile();
        User us = super.save(entity);
        writeToFile();
        return us;
    }

    @Override
    public User delete(Long id) {
        readFromFile();
        User us = super.delete(id);
        writeToFile();
        return us;
    }

    @Override
    public User update(User entity) {
        readFromFile();
        User us = super.update(entity);
        writeToFile();
        return us;
    }

}
