package by.hotel.service.impl;

import by.hotel.bean.Reservation;
import by.hotel.builder.DiscountBuilder;
import by.hotel.builder.ReservationBuilder;
import by.hotel.builder.UserBuilder;
import by.hotel.dao.daoimpl.ReservationDaoImpl;
import by.hotel.dao.exception.DAOException;
import by.hotel.service.CrudServiceExtended;
import by.hotel.service.exception.ServiceException;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ReservationServiceImpl implements CrudServiceExtended<Reservation> {
    ReservationDaoImpl reservationDao = new ReservationDaoImpl();

    public List<String> getAllHeaders() throws ServiceException {
        try {
            return reservationDao.getReservationHeaders();
        }catch (DAOException e){
            throw new ServiceException(e);
        }
    }

    public List<Reservation> getAllEntities() throws ServiceException {
        try {
            return reservationDao.getAllReservations();
        }catch (DAOException e){
            throw new ServiceException(e);
        }
    }

    public void addEntity(Reservation entity) throws ServiceException {
        try {
            reservationDao.addReservation(entity);
        }catch (DAOException e){
            throw new ServiceException(e);
        }
    }

    public void removeEntity(Reservation reservation) throws ServiceException {
        try {
            reservationDao.removeReservation(reservation);
        }catch (DAOException e){
            throw new ServiceException(e);
        }
    }

    public void updateEntity(Reservation entity) throws ServiceException {
        try {
            reservationDao.updateReservation(entity);
        }catch (DAOException e){
            throw new ServiceException(e);
        }
    }

    public Reservation buildEntity(Map<String, String[]> params) throws ServiceException {
        Reservation reservation;
        try {
            reservation =  new ReservationBuilder().id(Integer.parseInt(params.get("id")[0]))
                    .dateIn(new Date(new SimpleDateFormat("MMM dd, yyyy").parse(params.get("dateIn")[0]).getTime()))
                    .dateOut(new Date(new SimpleDateFormat("MMM dd, yyyy").parse(params.get("dateOut")[0]).getTime()))
                    .costAdditionalServices(Integer.parseInt(params.get("costAdditionalServices")[0]))
                    .user(new UserBuilder().id(Integer.parseInt(params.get("id_user")[0])).build())
                    .discount(new DiscountBuilder().id(Integer.parseInt(params.get("id_discount")[0])).build())
                    .build();
        }catch (ParseException e){
            throw new ServiceException(e);
        }
        return reservation;
    }
}
