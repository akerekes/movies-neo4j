package hu.ka.neo4j.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hu.ka.neo4j.domain.ActedIn;
import hu.ka.neo4j.domain.Actor;
import hu.ka.neo4j.domain.FriendWith;
import hu.ka.neo4j.domain.Movie;
import hu.ka.neo4j.domain.Rated;
import hu.ka.neo4j.domain.User;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/webapp/WEB-INF/neo4j-core.xml")
public class DatabaseInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializer.class);

    private static final int USER_CNT = 50;
    private static final int FRIENDSHIP_CNT = 100;
    private static final int ACTOR_CNT = 100;
    private static final int MOVIE_CNT = 10;
    private static final int ACTED_IN_CNT = 100;
    private static final int RATING_CNT = 100;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Test
    public void initializeDb() throws InterruptedException {
        Thread.sleep(2000L);

        runInTransaction(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction() {
                clearDb();
                return null;
            }
        });

        Thread.sleep(2000L);

        runInTransaction(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction() {
                createNewData();
                return null;
            }
        });

    }

    private void runInTransaction(TransactionCallback<Void> transactionCallback) {
        Transaction transaction = null;
        try {
            transaction = neo4jTemplate.getGraphDatabase().beginTx();
            transactionCallback.doInTransaction();
            transaction.success();
        } catch (Throwable t) {
            if (transaction != null) {
                transaction.failure();
            }
            LOGGER.error("Transaction failed", t);
            fail(t.getLocalizedMessage());
        } finally {
            if (transaction != null) {
                transaction.finish();
            }
        }
    }

    private void clearDb() {

        neo4jTemplate.query("start a=node(*)" +
                " match (a) -[r]-> ()" +
                " delete a,r", Collections.<String, Object>emptyMap());

//        neo4jTemplate.query("start a=node(*)" +
//                " delete a", Collections.<String, Object>emptyMap());
    }

    private void createNewData() {
        Random random = new Random();

        List<User> users = new ArrayList<User>();
        List<Actor> actors = new ArrayList<Actor>();
        List<Movie> movies = new ArrayList<Movie>();

        createUsers(users);

        createFriendships(random, users);

        createActors(actors);

        createMovies(movies);

        addActorsIntoMovie(random, actors, movies);

        rateMovies(random, movies, users);
    }

    private void createUsers(List<User> users) {
        for (int i = 0; i < USER_CNT; i++) {
            User u = createUser(i);
            users.add(u);
        }
    }

    private User createUser(int i) {
        User u = new User();
        u.setName("testUser" + i);
        neo4jTemplate.save(u);
        return u;
    }

    private void createFriendships(Random random, List<User> users) {
        for (int i = 0; i < FRIENDSHIP_CNT; i++) {
            createFriendship(users, random);
        }
    }

    private void createFriendship(List<User> users, Random random) {
        User u1 = randomUser(random, users);
        User u2 = randomUser(random, users, u1);
        FriendWith friendWith = new FriendWith();
        friendWith.setUser(u1);
        friendWith.setFriend(u2);
        neo4jTemplate.save(friendWith);
    }

    private void createActors(List<Actor> actors) {
        for (int i = 0; i < ACTOR_CNT; i++) {
            Actor a = createActor(i);
            actors.add(a);
        }
    }

    private Actor createActor(int i) {
        Actor a = new Actor();
        a.setName("testActor" + i);
        neo4jTemplate.save(a);
        return a;
    }

    private void createMovies(List<Movie> movies) {
        for (int i = 0; i < MOVIE_CNT; i++) {
            Movie m = createMovie(i);
            movies.add(m);
        }
    }

    private Movie createMovie(int i) {
        Movie m = new Movie("testMovie" + i);
        neo4jTemplate.save(m);
        return m;
    }

    private void addActorsIntoMovie(Random random, List<Actor> actors, List<Movie> movies) {
        for (int i = 0; i < ACTED_IN_CNT; i++) {
            addActorIntoMovie(random, actors, movies);
        }
    }

    private void addActorIntoMovie(Random random, List<Actor> actors, List<Movie> movies) {
        Actor actor = actors.get(random.nextInt(ACTOR_CNT));
        Movie movie = movies.get(random.nextInt(MOVIE_CNT));
        ActedIn actedIn = actor.actedIn(movie, "role-" + actor.getName() + "-in-" + movie.getTitle());
        neo4jTemplate.save(actedIn);
    }

    private void rateMovies(Random random, List<Movie> movies, List<User> users) {
        for (int i = 0; i < RATING_CNT; i++) {
            rateMovie(random, movies, users);
        }
    }

    private void rateMovie(Random random, List<Movie> movies, List<User> users) {
        User user = users.get(random.nextInt(USER_CNT));
        Movie movie = movies.get(random.nextInt(MOVIE_CNT));
        Rated rated = new Rated();
        rated.setUser(user);
        rated.setMovie(movie);
        rated.setRating(random.nextInt(Rated.RATING_RANGE) + 1);
        neo4jTemplate.save(rated);
    }

    private User randomUser(Random random, List<User> users, User exceptFor) {
        User user = users.get(random.nextInt(USER_CNT));
        while (user == exceptFor) {
            user = users.get(random.nextInt(USER_CNT));
        }
        return user;
    }

    private User randomUser(Random random, List<User> users) {
        return users.get(random.nextInt(USER_CNT));
    }

    private interface TransactionCallback<T> {
        T doInTransaction();
    }
}
