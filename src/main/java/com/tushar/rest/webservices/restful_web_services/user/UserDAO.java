package com.tushar.rest.webservices.restful_web_services.user;

import java.util.ArrayList;
import java.util.List; 
import java.time.LocalDate;

import java.util.function.Predicate;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {
    private static List<User> users = new ArrayList<>();
    private static int usersCount = 0;
     // Correct syntax error and replace List with ArrayList
    static{
        users.add(new User(++usersCount, "John", LocalDate.now().minusYears(25))); // Add import statement for LocalDate
        users.add(new User(++usersCount, "Jane", LocalDate.now().minusYears(30))); // Add import statement for LocalDate
        users.add(new User(++usersCount, "Alex", LocalDate.now().minusYears(35))); // Add import statement for LocalDate
    }

    public List<User> findAll(){
        return users;
    }
    public User findOne(int id) {
    	Predicate<? super User> predicate = user -> user.getId().equals(id); 
    	return users.stream().filter(predicate).findFirst().get();
    }

    public User save(User user){
        user.setId(++usersCount);
        users.add( user );
        return user;
    }
}
