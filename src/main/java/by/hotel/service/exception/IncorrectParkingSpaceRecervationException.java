package by.hotel.service.exception;

/**
 * Created by 1 on 20.04.2017.
 */
public class IncorrectParkingSpaceRecervationException extends Exception{
    private static final long serialVersionUID = 1L;
    public IncorrectParkingSpaceRecervationException(){
    }

    public IncorrectParkingSpaceRecervationException(Exception e){
        super(e);
    }

    public IncorrectParkingSpaceRecervationException(String message){
        super(message);
    }

    public IncorrectParkingSpaceRecervationException(String message, Exception e){
        super(message, e);
    }
}
