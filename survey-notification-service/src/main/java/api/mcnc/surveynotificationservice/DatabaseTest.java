package api.mcnc.surveynotificationservice;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            // MariaDB 드라이버 명시적 로드
            Class.forName("org.mariadb.jdbc.Driver");

            // MariaDB 연결 설정
            String url = "jdbc:mariadb://localhost:3306/survey_db";
            String username = "root";
            String password = "root";

            // DB 연결
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database!");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
