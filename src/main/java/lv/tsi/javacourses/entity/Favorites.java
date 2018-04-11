package lv.tsi.javacourses.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Favorites implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User user;
    @Column
    private String favorite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

}
