package by.hotel.service.impl;

import by.hotel.bean.ParkingSpace;
import by.hotel.builder.ParkingSpaceBuilder;
import by.hotel.dao.daoimpl.ParkingSpaceDaoImpl;
import by.hotel.dao.exception.DAOException;
import by.hotel.service.AbstractService;
import by.hotel.service.CrudService;
import by.hotel.service.exception.IncorrectParkingSpaceLevelException;
import by.hotel.service.exception.IncorrectParkingSpaceRecervationException;
import by.hotel.service.exception.ServiceException;
import by.hotel.service.validator.ValidatorDiscount;
import by.hotel.service.validator.ValidatorParkingSpace;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class ParkingSpaceServiceImpl extends AbstractService implements CrudService<ParkingSpace> {
    private ParkingSpaceDaoImpl parkingSpaceDao = new ParkingSpaceDaoImpl();

    public List<ParkingSpace> getAllEntities() throws ServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            return parkingSpaceDao.getParkingSpaces(connection);
        }catch (DAOException e){
            throw new ServiceException(e);
        }finally {
            closeConnection(connection);
        }
    }

    public void addEntity(ParkingSpace entity) throws ServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            parkingSpaceDao.addParkingSpace(entity,connection);
        }catch (DAOException e){
            throw new ServiceException(e);
        }finally {
            closeConnection(connection);
        }
    }

    public void removeEntity(ParkingSpace parkingSpace) throws ServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            parkingSpaceDao.removeParkingSpace(parkingSpace,connection);
        }catch (DAOException e){
            throw new ServiceException(e);
        }finally {
            closeConnection(connection);
        }
    }

    public void updateEntity(ParkingSpace entity) throws ServiceException {
        Connection connection = null;
        try {
            connection = getConnection();
            parkingSpaceDao.updateParkingSpace(entity,connection);
        }catch (DAOException e){
            throw new ServiceException(e);
        }finally {
            closeConnection(connection);
        }
    }

    public ParkingSpace buildEntity(Map<String, String[]> params) throws ServiceException, IncorrectParkingSpaceLevelException, IncorrectParkingSpaceRecervationException {
        ValidatorParkingSpace validatorParkingSpace = new ValidatorParkingSpace();
        if (validatorParkingSpace.validate(params)) {
            return new ParkingSpaceBuilder().id(Integer.parseInt(params.get("id")[0]))
                    .level(Integer.parseInt(params.get("level")[0]))
                    .reserved(Byte.parseByte(params.get("isReserved")[0]))
                    .build();
        }
        else{
            return null;
        }
    }
}
