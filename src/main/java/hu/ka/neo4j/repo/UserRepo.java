package hu.ka.neo4j.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import hu.ka.neo4j.domain.User;

@Repository
public interface UserRepo extends GraphRepository<User> {

    @Query("start user=node:user_name(name={0}) match user-[:FRIEND_WITH]->u return u")
    Iterable<User> friendsOf(String name);
}
