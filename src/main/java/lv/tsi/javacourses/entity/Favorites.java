package lv.tsi.javacourses.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Favorites implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(insertable=false, updatable=false)
    private Long user_id;
    @Column
    private String favorite;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

}
