package hu.ka.neo4j.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity
public class IsA {
    @GraphId
    private Long id;

    @StartNode
    private Actor actor;
    @EndNode
    private User user;

    public Actor getActor() {
        return actor;
    }

    public User getUser() {
        return user;
    }
}
