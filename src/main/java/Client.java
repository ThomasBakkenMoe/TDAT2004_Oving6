import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import java.util.List;

public class Client {

    public static void main(String[] args) {
        oppgave2(); //Pass på at tabellen er tom (eller ikke inneholder de feltene som vi skal sette inn) før man kjører
        oppgave3(); //Oppgave 3 antar at oppgave 2 har blitt kjørt minst en gang først, og at kontoene "ola" og "geir" eksisterer i databasen
                    //linje 14 og 15 i Account.java må kommenteres ut om oppgave 3 skal fungere. Hvis
        //Oppgave 4: Se linje 14 i Account.java. Oppgaven antar at oppgave2() har blitt kjørt med linje 14 og 15 aktive i Account.java
    }

    private static void oppgave2() {
        EntityManagerFactory entityManagerFactory = null;
        AccountDAO accountDAO;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory(("Account"));
            accountDAO = new AccountDAO(entityManagerFactory);

            Account ola = new Account(123456, 500.0, "Ola Nordmann");
            Account geir = new Account(654321, 1000.0, "Geir Jerbex");
            Account lut = new Account(999888, -12.0, "Lut Fattig");

            accountDAO.createNewAccount(ola);
            accountDAO.createNewAccount(geir);
            accountDAO.createNewAccount(lut);

            System.out.println("Henter alle kontoer med mer enn 200 kr i banken");
            List<Account> accountList = accountDAO.getAllAccountsWithBalanceGreaterThan(200.0);
            System.out.println("Resultat:");

            for (Account account: accountList) {
                System.out.println(account);
            }

            System.out.println("\nEndrer navn på " + accountList.get(0));

            accountList.get(0).setOwner("Ola Fossekall Nordmann");

            accountDAO.adjustAccount(accountList.get(0));

            System.out.println("Resultat: " + accountList.get(0));

            System.out.println("\nNy liste, hentet fra database: ");
            accountList = accountDAO.getAllAccountsWithBalanceGreaterThan(200.0);
            for (Account account: accountList) {
                System.out.println(account);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("OPPGAVE 2 UTFØRT");
            if (entityManagerFactory != null) entityManagerFactory.close();
        }
    }

    private static void oppgave3() {

        EntityManagerFactory entityManagerFactory = null;
        AccountDAO accountDAO;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory(("Account"));
            accountDAO = new AccountDAO(entityManagerFactory);

            List<Account> accountList = accountDAO.getAllAccountsWithBalanceGreaterThan(200.0);

            //"overfører" fra den ene kontoen til den andre
            accountList.get(0).withdraw(500.0);
            accountList.get(1).deposit(500.0);

            Thread.sleep(5000); //For å gjøre det enklere å synce når man skal kjøre to parallele klienter i oppgave 2

            accountDAO.adjustAccount(accountList.get(0));
            accountDAO.adjustAccount(accountList.get(1));


        }catch (OptimisticLockException | RollbackException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("OPPGAVE 3 UTFØRT");
            if (entityManagerFactory != null) entityManagerFactory.close();
        }
    }


}
