import javax.persistence.*;
import java.io.Serializable;

@Entity

public class Account implements Serializable {

    @TableGenerator(
            name = "tableGenerator",
            allocationSize = 1,
            initialValue = 1
    )

    @Id
    @GeneratedValue(
            strategy = GenerationType.TABLE,
            generator = "tableGenerator"
    )
    private int accountNbr;
    private double balance;
    private String owner;


    //Lås for oppgave 4
    @Version
    private int accountLock;

    public Account(){} //Java Bean constructor

    public Account(int accountNbr, double balance, String owner){
        this.accountNbr = accountNbr;
        this.balance = balance;
        this.owner = owner;
    }

    public void withdraw(double amount){

        if (amount <= 0.0){
            balance += amount;
        }else {
            balance -= amount;
        }

    }

    public void deposit(double amount){

        if (amount >= 0.0){
            balance += amount;
        }
    }

    public int getAccountNbr() {
        return accountNbr;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNbr=" + accountNbr +
                ", balance=" + balance +
                ", owner='" + owner + '\'' +
                '}';
    }

    public static void main(String[] args) {

    }
}
