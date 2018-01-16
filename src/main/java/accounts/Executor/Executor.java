package accounts.Executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Roman on 16.01.2018.
 */
public class Executor {
    private Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }
    public void execUpdate(String update) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(update);
        stmt.close();
    }

    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet resultSet = stmt.getResultSet();
        T result = handler.handle(resultSet);
        return result;
    }
}
