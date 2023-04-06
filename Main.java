import java.sql.*;

public class Main {


    static Integer Counter;
    static String Username;

    public static Integer Lower,Upper;

    public static void setUsername(String username) {
        Username = username;
    }

    public static void setCounter(Integer counter) {
        Counter = counter;
    }

    public static String getUsername() {
        return Username;
    }

    public static Integer getCounter() {
        return Counter;
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/DigitalArtGallery", "root", "root");
    }
//    Random Number Method to Select Random Art
    public static void RandomCounter() {
        ArtCounter();
        int RandomNum;
        RandomNum = (int) (Math.random() * (Upper-Lower+1)) + Lower;
        setCounter(RandomNum);
    }
    public static void ArtCounter() {
        int Number;
        Connection connection;
        PreparedStatement statement;
        ResultSet resultSet;
        try {
            connection = Main.getConnection();
            String sql = "SELECT COUNT(Art_id) FROM ArtDetails";

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            resultSet.next();
            Number = resultSet.getInt(1);
            Lower = 100;
            Upper = Number+Lower-1;
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public static void main(String[] args) {
            RandomCounter();
//        new LoginPage();
        setUsername("Admin1");
        new SearchArt();
//        new ArtSubmissionPage();
    }
}
