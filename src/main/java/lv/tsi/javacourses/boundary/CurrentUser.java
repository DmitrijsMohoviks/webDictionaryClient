package lv.tsi.javacourses.boundary;

import lv.tsi.javacourses.control.Util;
import lv.tsi.javacourses.entity.Favorites;
import lv.tsi.javacourses.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import lv.tsi.javacourses.boundary.TranslationForm;

/**
 * @author Dimitrijs Fedotovs <a href="http://www.bug.guru">www.bug.guru</a>
 */
@SessionScoped
@Named
public class CurrentUser implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(CurrentUser.class);

    private User signedInUser;

    @PersistenceContext
    private EntityManager em;

    private List<String> favorites;

    private String[] words;




    @Transactional
    public void showFavorites() {
        System.out.println("Method showFavorites executed");
        Query q = em.createQuery("SELECT f.favorite FROM Favorites f where f.user = :user")
                .setParameter("user", getSignedInUser());
        logger.info("signedInUser " + signedInUser.getEmail());
        setFavorites(q.getResultList());
    }

    @Transactional
    public void addToFavorites() {
        System.out.println("Method addToFavorites executed");
        Query q = em.createQuery("SELECT f.favorite FROM Favorites f where f.user = :user")
                .setParameter("user", getSignedInUser());
        setFavorites(q.getResultList());
        words = TranslationForm.words;
        StringBuilder alreadyFavorite = new StringBuilder();
        for (String word: words) {
            if (favorites.contains(word)) {
                alreadyFavorite = alreadyFavorite.append(word);
            } else {
                Favorites favorite = new Favorites();
                favorite.setUser(getSignedInUser());
                favorite.setFavorite(word);
                em.persist(favorite);
            }
            if (alreadyFavorite.length() > 0) {
                Util.addError("translationForm:textInput", alreadyFavorite.toString() + "- already in Favorites");
            }

        }
    }

    @Transactional
    public void deleteFromFavorites(String fv) {
        System.out.println("delete method executed");
        Long fv_id = ((Long) em.createQuery("select f.id from Favorites f where f.favorite = :favorite and f.user = :user")
                .setParameter("favorite", fv)
                .setParameter("user", getSignedInUser())
                .getSingleResult());
        Favorites favorite = em.find(Favorites.class, fv_id);
        Query q = em.createNativeQuery("DELETE FROM Favorites where ID = " + fv_id);
        q.executeUpdate();
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
