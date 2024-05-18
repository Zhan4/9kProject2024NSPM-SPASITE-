import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String [] args) throws SQLException {


            ArrayList<User> users = new ArrayList<>();

            Scanner in = new Scanner(System.in);

            String dbURL = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String pass = "postgres9k";

            String request;

            DataBaseControl dataBaseControl = new DataBaseControl(dbURL, user, pass);
            while(true) {
                System.out.println("Введите тип запроса");
                request = in.next();
                System.out.println();

                if (request.equals("POST")) {
                    String inSurname = in.next();
                    String inName = in.next();
                    String inClassNumber = in.next();
                    String inClassLiter = in.next();
                    dataBaseControl.insertUser(inSurname, inName, inClassNumber, inClassLiter, 0);
                } else if (request.equals("GET")) {
                    dataBaseControl.getUsers(users);

                    for (User userTest : users) {
                        userTest.showInfo();
                    }
                } else if (request.equals("PUT")) {
                    String inSurname = in.next();
                    String inName = in.next();
                    int toAddPoints = in.nextInt();
                    boolean isAdminAdding = true;
                    dataBaseControl.setPoints(inSurname, inName, toAddPoints, isAdminAdding);
                } else if(request.equals("GET_WINNERS")){
                    dataBaseControl.getWinners(users);
                } else {
                    System.out.println("Ты по английски пиши рашан ");
                }
            }

    }
}
