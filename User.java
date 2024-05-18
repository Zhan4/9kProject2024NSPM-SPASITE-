public class User {
    public String name;
    public String surname;
    protected String classNumber;
    protected String classLiter;

    protected int points;

    protected User(String surname, String name, String classNumber, String classLiter, int points){
        this.name = name;
        this.surname = surname;
        this.classNumber = classNumber;
        this.classLiter = classLiter;
        this.points = points;
    }

    public void showInfo(){
        System.out.println(name + " " + surname + " Из класса " + classNumber + classLiter + " Очков: " + points);
    }
    protected int getPoints(){
        return points;
    }

}
