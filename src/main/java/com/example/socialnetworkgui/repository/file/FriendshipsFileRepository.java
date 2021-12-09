package com.example.socialnetworkgui.repository.file;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.validator.Validator;
import com.example.socialnetworkgui.repository.AbstractRepository;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.repository.memory.FriendshipsMemoryRepository;
import com.example.socialnetworkgui.utils.Pair;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class FriendshipsFileRepository extends AbstractRepository<Pair,Friendship> {
    private String fileName;

    /**
     * repository constructor
     * @param fileName - the name of the file that contains the data
     * @param validator - validator for entities of type E
     */
    public FriendshipsFileRepository(Validator<Friendship> validator,String fileName) {
        super(validator);
        this.fileName = fileName;
        readFromFile();
    }

    /**
     * creates a string out of the objects attributes
     * @param entity - an object
     * @return - a string
     */
    private String createEntityAsString(Friendship entity) {
        return entity.getPair().getId1().toString() + ";" + entity.getPair().getId2().toString();
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
                if (attributes.size() == 2) {
                    Friendship friendship = new Friendship(Long.parseLong(attributes.get(0)), Long.parseLong(attributes.get(1)));
                    friendship.setId(new Pair(Long.parseLong(attributes.get(0)), Long.parseLong(attributes.get(1))));
                    super.save(friendship);
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
            for (Friendship entity : super.findAll()) {
                bw.write(createEntityAsString(entity));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryException("Fail to write in file!\n");
        }
    }


    @Override
    public Friendship findOne(Pair pair) {
        return super.findOne(pair);
    }

    @Override
    public Iterable<Friendship> findAll() {
        return super.findAll();
    }

    @Override
    public Friendship save(Friendship entity) {
        boolean friendshipExists=false;
        for(Friendship f: entities.values()){
            if(f.equals(entity))
                friendshipExists=true;
        }
        Friendship friendship =entity;
        if(!friendshipExists)
            friendship=super.save(entity);
        writeToFile();
        return friendship;
    }

    @Override
    public Friendship delete(Pair id) {
        Friendship friendship=null;
        Pair id1=id;
        boolean friendshipExists=false;
        for(Pair i: entities.keySet()){
            if(i.equals(id)) {
                friendshipExists = true;
                id1=i;
            }
        }
        if(friendshipExists)
            friendship=super.delete(id1);
        writeToFile();
        return friendship;
    }

    @Override
    public Friendship update(Friendship entity) {
        Friendship friendship = super.update(entity);
        writeToFile();
        return friendship;
    }
}
