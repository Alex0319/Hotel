package by.hotel.command.impl;

import by.hotel.bean.User;
import by.hotel.command.Command;
import by.hotel.command.exception.CommandException;
import by.hotel.dao.IGuestDao;
import by.hotel.dao.daoimpl.GuestDao;

/**
 * Created by 1 on 28.02.2017.
 */
public class Logination implements Command {
    public boolean execute(String request) throws CommandException {
        IGuestDao guestDao = new GuestDao();
        String[] arrOfRequestData = request.split("&");
        String name = arrOfRequestData[1];
        String password = arrOfRequestData[2];

        User user = new User(name,password);
        if (guestDao.authorization(user)){
            return true;
        }
        return false;
    }
}
