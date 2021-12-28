package com.example.socialnetworkgui.repository.DB;


import com.example.socialnetworkgui.domain.Entity;
import com.example.socialnetworkgui.domain.validator.Validator;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.RepositoryException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractRepoDatabase<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private String url;
    private String username;
    private String password;
    protected Validator<E> validator;
    protected static Connection connection = null;

    /**
     * repository constructor
     * @param url - database information
     * @param username - database information
     * @param password - database information
     * @param validator - database information
     */
    public AbstractRepoDatabase(String url, String username, String password, Validator<E> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    protected Connection getConnection(){
        try{
            if(connection==null || connection.isClosed())
                connection=getNewConnection();
        }catch (SQLException e){
            throw new RepositoryException("Error connecting to database!\n");
        }
        return connection;
    }

    private Connection getNewConnection(){
        Connection con =null;
        try{
            con = DriverManager.getConnection(url, username, password);
        }catch (SQLException se){
            throw new RepositoryException("Error connecting to database!\n");
        }
        return con;
    }

    protected static void closeConnection(){
        try{
            connection.close();
        }catch (SQLException e){
            throw new RepositoryException("Error closing connection!\n");
        }
    }
}
