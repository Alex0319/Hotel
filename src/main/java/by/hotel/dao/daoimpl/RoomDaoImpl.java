package by.hotel.dao.daoimpl;

import by.hotel.bean.Room;
import by.hotel.bean.RoomType;
import by.hotel.bean.User;
import by.hotel.dao.AbstractDao;
import by.hotel.dao.RoomDao;
import by.hotel.dao.constants.Constants;
import by.hotel.dao.exception.DAOException;
import by.hotel.servlet.MainServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static by.hotel.dao.constants.Constants.*;

public class RoomDaoImpl extends AbstractDao implements RoomDao {
    public List<Integer> getId() throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Integer> arrayId = new ArrayList<Integer>();
        try {
            connection = getConnection();
            statement = connection.prepareStatement(Constants.GET_ALL_ID_ROOMS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                arrayId.add(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            closeConnection(connection, statement, resultSet);
        }
        return arrayId;
    }

    public List<Room> getRooms() throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Room> rooms = new ArrayList<Room>();
        try {
            connection = getConnection();
            statement = connection.prepareStatement(Constants.GET_ALL_ROOMS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Room room = new Room();
                RoomType roomType = new RoomType(resultSet.getInt("room_type.id"),
                                                 resultSet.getInt("rooms_count"),
                                                 resultSet.getInt("beds_count"),
                                                 resultSet.getInt("cost_per_day"),
                                                 resultSet.getString("additional_info"));
                room.setId(resultSet.getInt("id"));
                room.setRoomType(roomType);
                room.setFloor(resultSet.getInt("floor"));
                room.setPhone(resultSet.getString("phone"));
                rooms.add(room);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            closeConnection(connection, statement, resultSet);
        }
        return rooms;
    }

    public void addRoom(Room room) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(ADD_ROOM);
            statement = fillStatement(statement, room);
            statement.execute();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    public void removeRoom(Room room) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement.setInt(1, room.getId());
            statement = connection.prepareStatement(REMOVE_ROOM);
            statement.execute();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    public void updateRoom(Room room) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(UPDATE_ROOM);
            statement = fillStatement(statement, room);
            statement.execute();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    private PreparedStatement fillStatement(PreparedStatement statement, Room room) throws SQLException {
        statement.setInt(1, room.getRoomType().getId());
        statement.setInt(2, room.getFloor());
        statement.setString(3, room.getPhone());
        return statement;
    }
}
