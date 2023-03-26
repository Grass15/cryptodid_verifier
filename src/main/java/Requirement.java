public class Requirement {
    private int age;
    private int balance;
    private int creditScore;
    public Requirement(int age, int balance, int creditScore){
        this.age = age;
        this.balance = balance;
        this.creditScore = creditScore;
    }
    public int getAge(){return this.age;}
    public int getBalance(){return this.balance;}
    public int getCreditScore(){return this.creditScore;}
}
