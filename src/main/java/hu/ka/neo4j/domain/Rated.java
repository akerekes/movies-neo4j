package hu.ka.neo4j.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = "HAS_RATED")
public class Rated {

    public static final int RATING_RANGE = 5;

    @GraphId
    private Long id;

    @StartNode
    private User user;
    @EndNode
    private Movie movie;
    private Integer rating;

    public User getUser() {
        return user;
    }

    public Movie getMovie() {
        return movie;
    }

    public Integer getRating() {
        return rating;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
