package springdatajpa.basic.repository;

public interface UsernameOnly {

    // @Value("#{target.username + ' / ' + target.age}")
    String getUsername();

}