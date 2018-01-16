package accounts;

import accounts.dao.UserDao;
import org.h2.jdbcx.JdbcDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roman on 08.01.2018.
 */
public class AccountService {
//    private final Map<String, UserProfile> loginToProfile;
    private final Map<String, UserProfile> sessionIdToProfile;
    private Connection connection;

    public AccountService() {
//        this.loginToProfile = new HashMap<>();
        this.sessionIdToProfile = new HashMap<>();
        this.connection = getH2Connection();
    }

    public boolean checkUserTable() throws DBException {
        try {
            UserDao userDao = new UserDao(connection);
            return userDao.isExistTable();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException(e);
        }
    }
    public void createUserTable() throws DBException {
        try {
            UserDao userDao = new UserDao(connection);
            userDao.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException(e);
        }
    }

    public void cleanUp() throws DBException {
        try {
            UserDao userDao = new UserDao(connection);
            userDao.dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException(e);
        }
    }

    public void addNewUser(UserProfile userProfile) throws DBException {
//        loginToProfile.put(userProfile.getLogin(), userProfile);
        try {
            UserDao userDao = new UserDao(connection);
            userDao.insertUser(userProfile.getLogin(), userProfile.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException(e);
        }

    }

    public UserProfile getUserByLogin(String login) throws DBException {
//        return loginToProfile.get(login);
        try {
            UserDao userDao = new UserDao(connection);
            return userDao.getUser(login);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException(e);
        }
    }

    public UserProfile getUserBySessionId(String login) {
        return sessionIdToProfile.get(login);
    }

    public void addSession(String sessionId, UserProfile userProfile) {

        sessionIdToProfile.put(sessionId, userProfile);
    }

    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }

    private static Connection getH2Connection() {
        String url = "jdbc:h2:./h2db";
        String user = "tully";
        String pass = "tully";

        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(user);
        dataSource.setPassword(pass);

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
