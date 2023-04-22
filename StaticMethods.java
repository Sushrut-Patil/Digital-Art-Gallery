import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.Objects;

public class StaticMethods {
    public static ImageIcon FetchArt(int counter) throws SQLException, IOException, ClassNotFoundException {

        Connection connection;
        PreparedStatement statement;
        ResultSet resultSet;
        Blob blob = null;

        connection = Main.getConnection();
        String query = "Select image from Art where art_id = (?)";
        statement = connection.prepareStatement(query);
        statement.setInt(1, counter);

        resultSet = statement.executeQuery();
        if (resultSet.next()) {
            blob = resultSet.getBlob("image");
        }
        resultSet.close();
        statement.close();
        connection.close();
        try (ObjectInputStream is = new ObjectInputStream(Objects.requireNonNull(blob).getBinaryStream())) {
            return (ImageIcon) is.readObject();

        }
    }
}
