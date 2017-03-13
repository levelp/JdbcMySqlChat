import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.util.Random;

public class MySqlChatTest extends Assert {

    @BeforeClass
    public static void beforeAllTests() throws ClassNotFoundException {
        // Загрузка драйвера
        // Драйверов можно загрузить сколько угодно
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    @Test
    public void connectAndSelect() throws Exception {
        // Открываем подключение
        // протокол://имя_сервера:порт/Имя_Базы_Данных
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/chat" +
                        // Если не указывать TimeZone, возникает ошибка:
                        // java.sql.SQLException:
                        // The server time zone value '...'
                        // is unrecognized or represents
                        // more than one time zone.
                        "?serverTimezone=UTC",
                // TODO: Разобраться как себя будет вести код в разных часовых поясах
                "root", "levelp");

        Statement query = con.createStatement();
        ResultSet resultSet = query.executeQuery(
                "SELECT * FROM user");
        while (resultSet.next()) {
            System.out.println(
                    resultSet.getString("nickname"));
        }


        PreparedStatement insert =
                con.prepareStatement("INSERT INTO user(nickname, password) " +
                        "VALUES(?, ?)");
        PreparedStatement delete =
                con.prepareStatement("DELETE FROM user " +
                        "WHERE nickname = ?");
        Random random = new Random();
        for (int i = 111; i <= 119; i++) {
            String nickname = "user" + i;
            String password = "password " + random.nextInt();
            insert.setString(1, nickname);
            insert.setString(2, password);

            try {
                insert.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException ex) {
                delete.setString(1, nickname);
                delete.executeUpdate();

                insert.executeUpdate();
            }
        }
    }
}
