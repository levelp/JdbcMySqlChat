import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
    }
}
