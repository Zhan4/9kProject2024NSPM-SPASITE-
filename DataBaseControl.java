import java.sql.*;
import java.util.*;
import java.util.stream.Collector;

public class DataBaseControl {

    private Connection conn;

    public DataBaseControl(String dbURL, String user, String pass) throws SQLException {
        conn = DriverManager.getConnection(dbURL, user, pass);
    }

    public void insertUser(String surname, String name, String classNumber, String classLiter, int points) {
        postQuery(surname, name, classNumber, classLiter, points);
    }

    public ArrayList<User> updateUsers(ArrayList<User> users) {
        getQuery(users);
        return users;
    }

    public void setPoints(String surname, String name, int toAddPoints, boolean isAdminAdding){
        putQuery( surname,  name,  toAddPoints,  isAdminAdding);
    }

    private void postQuery(String surname, String name, String classNumber, String classLiter, int points) {
        try {
            String postQuery = "INSERT INTO users_db (surname, name, class, liter, points) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(postQuery);
            pstmt.setString(1, surname);
            pstmt.setString(2, name);
            pstmt.setString(3, classNumber);
            pstmt.setString(4, classLiter);
            pstmt.setInt(5, points);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Новая строка успешно добавлена в базу данных.");
            } else {
                System.out.println("Ошибка при добавлении новой строки в базу данных.");
            }
            pstmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    private void getQuery(ArrayList<User> users) {
        try {
            if (conn != null) {
                users.clear();
                String getQuery = "SELECT * FROM users_db";
                PreparedStatement stmt = conn.prepareStatement(getQuery);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String surname = rs.getString("surname");
                    String name = rs.getString("name");
                    String classNumber = rs.getString("class");
                    String classLiter = rs.getString("liter");
                    int points = rs.getInt("points");
                    User userCycle = new User(surname, name, classNumber, classLiter, points);
                    users.add(userCycle);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private void putQuery(String surname, String name, int toAddPoints, boolean isAdminAdding) {
        try {
            if (conn != null) {
                if (isAdminAdding) {
                    String getQuery = "SELECT * FROM users_db WHERE surname = ? AND name = ?";
                    PreparedStatement psmst = conn.prepareStatement(getQuery);
                    psmst.setString(1, surname);
                    psmst.setString(2, name);
                    ResultSet rs = psmst.executeQuery();

                    if (rs.next()) {
                        int currentPoints = rs.getInt("points");
                        int resultPoints = toAddPoints + currentPoints;

                        // Выполняем SQL-запрос
                        String updateQuery = "UPDATE users_db SET points = ? WHERE surname = ? AND name = ?";
                        PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                        updateStatement.setInt(1, resultPoints);
                        updateStatement.setString(2, surname);
                        updateStatement.setString(3, name);
                        updateStatement.executeUpdate();

                        updateStatement.close();
                    } else {
                        System.out.println("Пользователь не найден.");
                    }

                    rs.close();
                    psmst.close();
                } else {
                    System.out.println("КТО ТО ПЫТАЕТСЯ ПОЛУЧИТЬ ДОСТУП К ПАНЕЛЕ АДМИНА SOS SOS SOS SOS, CALL 911");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //WINNERS conTROLLING
    public void getWinners(ArrayList<User> users){
        getListOfWinners(users);
    }
    private void getListOfWinners(ArrayList<User> users) {
        // Создаем список для хранения трех максимальных баллов
        ArrayList<Integer> topThreePoints = new ArrayList<>();

        // Находим три максимальных балла
        for (User temp : users) {
            int pointsOfTemp = temp.getPoints();
            if (topThreePoints.size() < 3 || pointsOfTemp > topThreePoints.get(2)) {
                // Если список пустой или балл больше минимального из трех максимальных,
                // добавляем его в список, заменяя при необходимости
                if (topThreePoints.size() >= 3) {
                    topThreePoints.remove(2);
                }
                int i = 0;
                while (i < topThreePoints.size() && pointsOfTemp < topThreePoints.get(i)) {
                    i++;
                }
                topThreePoints.add(i, pointsOfTemp);
            }
        }

        // Сортируем пользователей по убыванию баллов
        users.sort((user1, user2) -> Integer.compare(user2.getPoints(), user1.getPoints()));

        // Выводим пользователей с баллами, равными максимальным
        int cnt = 0;
        for (User temp : users) {
            if (cnt == 3) {
                break;
            }
            int pointsOfTemp = temp.getPoints();
            if (topThreePoints.contains(pointsOfTemp)) {
                System.out.println("ФИ: " + temp.surname + " " + temp.name + " Очки: " + pointsOfTemp);
                cnt++;
            }
        }
    }



}
