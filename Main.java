import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String [] args) throws SQLException {
            Scanner in = new Scanner(System.in);


            String dbURL = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String pass = "postgres9k";

            String request;
            DataBaseControl dataBaseControl = new DataBaseControl(dbURL, user, pass);

            ArrayList<User> users = new ArrayList<>();
            dataBaseControl.updateUsers(users);
            while(true) {
                System.out.println("Введите тип запроса");
                request = in.next();
                System.out.println();

                if (request.toLowerCase(Locale.ROOT).equals("post")) {
                    String inSurname = in.next();
                    String inName = in.next();
                    String inClassNumber = in.next();
                    String inClassLiter = in.next();
                    dataBaseControl.insertUser(inSurname, inName, inClassNumber, inClassLiter, 0);
                } else if (request.toLowerCase(Locale.ROOT).equals("get")) {
                    dataBaseControl.updateUsers(users);

                    for (User userTest : users) {
                        userTest.showInfo();
                    }
                } else if (request.toLowerCase(Locale.ROOT).equals("put")) {
                    String inSurname = in.next();
                    String inName = in.next();
                    int toAddPoints = in.nextInt();
                    boolean isAdminAdding = true;
                    dataBaseControl.setPoints(inSurname, inName, toAddPoints, isAdminAdding);
                } else if(request.toLowerCase(Locale.ROOT).equals("get_winners")){
                    dataBaseControl.getWinners(users);

                } else {
                    System.out.println("Эт ты когда такую команду придумал, долбан, не знаешь запросы чтоли?");
                }
            }
    }
}
