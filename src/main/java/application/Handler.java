package application;

import java.io.IOException;
import java.sql.SQLException;

public interface Handler {
    ProcessResult process() throws IOException, SQLException;
}
