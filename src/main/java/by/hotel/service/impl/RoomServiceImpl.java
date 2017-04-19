package by.hotel.service.impl;

import by.hotel.bean.Room;
import by.hotel.builder.RoomBuilder;
import by.hotel.builder.RoomTypeBuilder;
import by.hotel.dao.RoomDao;
import by.hotel.dao.daoimpl.RoomDaoImpl;
import by.hotel.dao.exception.DAOException;
import by.hotel.service.AbstractService;
import by.hotel.service.CrudService;
import by.hotel.service.exception.IncorrectParkingSpaceLevelException;
import by.hotel.service.exception.IncorrectParkingSpaceRecervationException;
import by.hotel.service.exception.ServiceException;
import by.hotel.service.validator.ValidatorParkingSpace;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class RoomServiceImpl extends AbstractService implements CrudService<Room> {
    private RoomDao roomDao = new RoomDaoImpl();

    public List<Room> getAllEntities() throws ServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            return roomDao.getRooms(connection);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            closeConnection(connection);
        }
    }

    public void addEntity(Room entity) throws ServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            roomDao.addRoom(entity, connection);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            closeConnection(connection);
        }
    }

    public void removeEntity(Room room) throws ServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            roomDao.removeRoom(room, connection);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            closeConnection(connection);
        }
    }

    public void updateEntity(Room entity) throws ServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            roomDao.updateRoom(entity, connection);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } finally {
            closeConnection(connection);
        }
    }

    public Room buildEntity(Map<String, String[]> params) throws ServiceException, IncorrectParkingSpaceLevelException, IncorrectParkingSpaceRecervationException {
        ValidatorParkingSpace validatorParkingSpace = new ValidatorParkingSpace();
        if (validatorParkingSpace.validate(params)) {
            return new RoomBuilder().id(Integer.parseInt(params.get("id")[0]))
                    .roomType(new RoomTypeBuilder().id(Integer.parseInt(params.get("id_roomType")[0]))
                            .build())
                    .floor(Integer.parseInt(params.get("floor")[0]))
                    .phone(params.get("phone")[0])
                    .path(params.get("path")[0])
                    .build();
        } else {
            return null;
        }
    }
}