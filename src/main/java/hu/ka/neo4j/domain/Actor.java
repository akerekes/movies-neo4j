package hu.ka.neo4j.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Actor {
    @GraphId
    @Indexed(indexName = "actorId")
    private Long id;

    @RelatedTo(type = "ACTED_IN", direction = Direction.OUTGOING)
    Set<Movie> movies = new HashSet<Movie>();

    @Indexed(unique = true, indexName = "actor_name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    @RelatedToVia(type = "ACTED_IN", direction = Direction.OUTGOING)
//    Set<ActedIn> actedIns = new HashSet<ActedIn>();

    public ActedIn actedIn(Movie movie, String role) {
        return new ActedIn(this, movie, role);
//        movies.add(movie);
    }

    public Long getId() {
        return id;
    }
}
