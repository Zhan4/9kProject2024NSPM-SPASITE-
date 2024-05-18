import java.sql.*;
import java.util.*;

public class DataBaseControl {

    private Connection conn;

    public DataBaseControl(String dbURL, String user, String pass) throws SQLException {
        conn = DriverManager.getConnection(dbURL, user, pass);
    }

    public void insertUser(String surname, String name, String classNumber, String classLiter, int points) {
        postQuery(surname, name, classNumber, classLiter, points);
    }

    public ArrayList<User> getUsers(ArrayList<User> users) {
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

    public void getWinners(ArrayList<User> users){
        getListOfWinners(users);
    }

    private void getListOfWinners(ArrayList<User> users){
        HashMap<String, Integer> tempHash = new HashMap<>();
        for(User temp : users){
            int pointsOfTemp = temp.getPoints();
            String FirstAndLastName = temp.surname + " " + temp.name;
            tempHash.put(FirstAndLastName, pointsOfTemp);
        }
        LinkedHashMap<String, Integer> sortedMap = sortByValue(tempHash);

        int cnt = 0;
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            if(cnt == 3){
                break;
            } else{
                System.out.println("ФИ: " + entry.getKey() + " Очки: " + entry.getValue());
            }
            cnt++;
        }

    }

    public static LinkedHashMap<String, Integer> sortByValue(HashMap<String, Integer> map) {
        // Получение списка записей
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

        // Сортировка списка по значениям
        list.sort(Map.Entry.comparingByValue());

        // Создание LinkedHashMap для сохранения порядка вставки
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
