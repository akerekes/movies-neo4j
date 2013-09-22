package hu.ka.neo4j.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = "ACTED_IN")
public class ActedIn {
    @GraphId
    private Long id;

    @StartNode
    private Actor actor;
    @EndNode
    private Movie movie;

    private String role;

    public ActedIn() {
    }

    public ActedIn(Actor actor, Movie movie, String role) {
        this.actor = actor;
        this.movie = movie;
        this.role = role;
    }

    public Actor getActor() {
        return actor;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getRole() {
        return role;
    }
}
