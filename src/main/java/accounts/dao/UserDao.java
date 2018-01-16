package accounts.dao;

import accounts.Executor.Executor;
import accounts.UserProfile;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Roman on 16.01.2018.
 */
public class UserDao {
    private Executor executor;

    public UserDao(Connection connection) {
        executor = new Executor(connection);
    }

    public void insertUser(String login, String password) throws SQLException {
        executor.execUpdate("insert into users (login, password) values ('" + login + "','" + password + "');");
    }

    public UserProfile getUser(String login) throws SQLException {
        return executor.execQuery("select * from users where login = '" + login + "';", resultSet -> {
            resultSet.next();
            return new UserProfile(resultSet.getString(2), resultSet.getString(3), null);
        });
    }

    public boolean isExistTable() throws SQLException {
        return executor.execQuery("show tables from 'h2db'';", resultSet -> {
            return resultSet.getFetchSize() == 1 ;
        });
    }

    public void createTable() throws SQLException {
        executor.execUpdate("create table if not exists users (id bigint auto_increment, login varchar(256), password varchar(256), primary key (id));");
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("drop table users;");
    }
}
