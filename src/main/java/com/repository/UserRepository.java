package com.repository;

import com.entity.User;
import com.config.MongoConfig;

import java.util.Optional;
import com.mongodb.client.MongoClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class UserRepository  {

    public static MongoClient mongoCLient;
    public static MongoTemplate mongoTemplate;

    private static UserRepository instance;
    
    public static UserRepository get() {
        if (instance == null) {
            instance = new UserRepository();
        }

        return instance;
    }

    private UserRepository() {
        MongoConfig config = new MongoConfig();
        mongoCLient = config.mongoClient();
        mongoTemplate = new MongoTemplate(mongoCLient, config.getDatabaseName());
    }

    public void addUser(String username, String password) {
        User x = User.createNew(username, password);
        mongoTemplate.insert(x);
    }

    public Optional<User> findByID(String id) {
        var list = mongoTemplate.find(new Query().addCriteria(Criteria.where("id").is(id)), User.class);

        if (list.isEmpty())
            return Optional.empty();

        return Optional.of(list.get(0));
    }

    public Optional<User> findByUsername(String username) {
        var list = mongoTemplate.find(new Query().addCriteria(Criteria.where("username").is(username)), User.class);

        if (list.isEmpty())
            return Optional.empty();

        return Optional.of(list.get(0));
    } 

    public long count() {
        return mongoTemplate.estimatedCount(User.class);
    }
}
