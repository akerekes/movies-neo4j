package hu.ka.neo4j.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Movie {
    @GraphId
    private Long id;

    private String title;

    @RelatedTo(type = "ACTED_IN", direction = Direction.INCOMING)
    private Set<Actor> actors = new HashSet<Actor>();

    public Movie() {
    }

    public Movie(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
