package accounts.Executor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Roman on 16.01.2018.
 */
public interface ResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException ;
}
