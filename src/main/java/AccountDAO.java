import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

public class AccountDAO {

    private EntityManagerFactory entityManagerFactory;

    public AccountDAO(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    public void createNewAccount(Account account){
        EntityManager entityManager = getEM();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(account);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeEM(entityManager);
        }
    }

    public List<Account> getAllAccountsWithBalanceGreaterThan(double amount){
        EntityManager entityManager = getEM();

        try {
            Query query = entityManager.createQuery("SELECT OBJECT(o) FROM Account o WHERE o.balance > " + amount);

            //Her klager IntelliJ litt fordi det er en mulighet (I dens øyne) at Listen som returneres fra query ikke kan castes til List<Account>
            return query.getResultList();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeEM(entityManager);
        }

        // Dersom try feiler
        System.out.println("Returning NULL from getAllAccountsWithBalanceGreaterThan()");
        return null;
    }

    public void adjustAccount(Account account) {

        EntityManager entityManager = getEM();

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(account);
            entityManager.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            closeEM(entityManager);
        }
    }

    private EntityManager getEM(){
        return entityManagerFactory.createEntityManager();
    }

    private void closeEM(EntityManager entityManager){
        if (entityManager != null && entityManager.isOpen()){
            entityManager.close();
        }
    }
}
