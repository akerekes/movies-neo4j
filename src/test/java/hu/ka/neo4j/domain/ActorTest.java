package hu.ka.neo4j.domain;

import java.util.Set;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.helpers.collection.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/webapp/WEB-INF/neo4j-core.xml")
@Transactional
public class ActorTest {
    @Autowired
    private Neo4jTemplate neo4jTemplate;
    private Actor actor;
    private Movie movie;

    @Before
    public void setUp() {
        actor = new Actor();
        actor = neo4jTemplate.save(actor);
        movie = new Movie("TestMovie");
        movie = neo4jTemplate.save(movie);
    }

    @Test
    public void testActedIn() throws Exception {
        neo4jTemplate.save(actor.actedIn(movie, "testRole"));

        Set<Movie> savedMovies = neo4jTemplate.findAll(Actor.class).single().movies;
        Assert.assertThat(savedMovies, IsCollectionWithSize.hasSize(1));
        String rel = neo4jTemplate.query("start a=node({id}) match a -[r:ACTED_IN]-> () return r.role", MapUtil.map("id", actor.getId())).to(String.class).single();
        Assert.assertThat(rel, is("testRole"));
    }
}
