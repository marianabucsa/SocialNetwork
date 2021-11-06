package ro.ubbcluj.map.service;

public class ServiceException extends RuntimeException{
    /**
     * constructor for Service Exception
     * @param err - a string
     */
    public ServiceException(String err){
        super(err);
    }

}
