package hu.ka.neo4j.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import hu.ka.neo4j.domain.FriendWith;
import hu.ka.neo4j.domain.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/webapp/WEB-INF/neo4j-core.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private Neo4jTemplate neo4jTemplate;
    private List<User> existingUsers;

    @Before
    public void setUp() {
        existingUsers = Lists.newArrayList(neo4jTemplate.findAll(User.class).iterator());

        User user1 = new User();
        user1.setName("user1");

        User user2 = new User();
        user2.setName("user2");

        neo4jTemplate.save(user1);
        neo4jTemplate.save(user2);

        FriendWith friendWith = new FriendWith();
        friendWith.setUser(user1);
        friendWith.setFriend(user2);
        neo4jTemplate.save(friendWith);
    }

    @Test
    public void testGetUsers() throws Exception {
        List<User> users = userService.getUsers();

        users.removeAll(existingUsers);
        assertThat(users,
                containsInAnyOrder(
                        hasProperty("name", equalTo("user1")),
                        hasProperty("name", equalTo("user2"))));
    }

    @Test
    public void testGetFriendsOf() {
        List<User> friendsOf = userService.getFriendsOf("user1");
        assertThat(friendsOf, hasSize(1));
        assertThat(friendsOf.iterator().next(), hasProperty("name", equalTo("user2")));
    }
}
