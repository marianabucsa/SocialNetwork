package com.example.socialnetworkgui.repository;

public class RepositoryException extends RuntimeException{
    /**
     * constructor for Repository Exception
     * @param err - a string
     */
    public RepositoryException(String err){
        super(err);
    }
}
