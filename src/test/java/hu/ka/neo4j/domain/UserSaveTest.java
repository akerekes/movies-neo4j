package hu.ka.neo4j.domain;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/webapp/WEB-INF/neo4j-core.xml")
public class UserSaveTest {

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Transactional
    @Test
    public void testSaveUser() {
        User user = new User();
        user.setName("testUser");
        neo4jTemplate.save(user);
        EndResult<User> users = neo4jTemplate.findAll(User.class);
        assertNotNull(users);
        ArrayList<User> persistedUsers = Lists.newArrayList(users.iterator());
        assertThat(persistedUsers.size(), Matchers.greaterThanOrEqualTo(1));
        assertThat(persistedUsers, hasItem(HasPropertyWithValue.<User>hasProperty("name", is("testUser"))));
    }
}
