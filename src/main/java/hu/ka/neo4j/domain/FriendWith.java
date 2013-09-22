package hu.ka.neo4j.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = "FRIEND_WITH")
public class FriendWith {
    @GraphId
    private Long id;

    @StartNode
    private User user;
    @EndNode
    private User friend;

    public User getUser() {
        return user;
    }

    public User getFriend() {
        return friend;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }
}
