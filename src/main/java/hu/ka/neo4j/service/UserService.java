package hu.ka.neo4j.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import hu.ka.neo4j.domain.User;
import hu.ka.neo4j.repo.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return Lists.newArrayList(userRepo.findAll());
    }

    public List<User> getFriendsOf(String name) {
        return Lists.newArrayList(userRepo.friendsOf(name));
    }
}
