package lv.tsi.javacourses.boundary;

import lv.tsi.javacourses.entity.User;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

/**
 * @author Dimitrijs Fedotovs <a href="http://www.bug.guru">www.bug.guru</a>
 */
@SessionScoped
@Named
public class CurrentUser implements Serializable {

    private User signedInUser;

    @PersistenceContext
    private EntityManager em;

    private List<String> favorites;

    public void testMethod() {
        System.out.println("test completed");
    }

    @Transactional
    public void showFavorites() {
        System.out.println("Method showFavorites executed");
        Query q = em.createQuery("SELECT f.favorite FROM Favorites f");
        setFavorites(q.getResultList());
    }

    public boolean isSignedIn() {
        return signedInUser  != null;
    }

    public User getSignedInUser() {
        return signedInUser;
    }

    public void setSignedInUser(User signedInUser) {
        this.signedInUser = signedInUser;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }
}
