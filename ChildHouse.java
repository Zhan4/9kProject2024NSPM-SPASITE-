public class ChildHouse {
    public String name;
    public String adres;
    public double XCorr;
    public double YCorr;

    protected int foodNeed;
    protected int clothesNeed;
    protected int moneyNeed;

    ChildHouse(String name, String adres, int XCorr, int YCorr, int foodNeed, int clothesNeed, int moneyNeed ){
        this.adres = adres;
        this.name = name;
        this.XCorr = XCorr;
        this.YCorr = YCorr;
    }

    protected void setFoodNeed(int level){
        foodNeed = level;
    }
    protected  void setClothesNeed(int level){
        clothesNeed = level;
    }
    protected void setMoneyNeed(int level){
        moneyNeed = level;
    }

}
