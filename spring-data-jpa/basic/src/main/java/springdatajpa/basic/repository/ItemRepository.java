package springdatajpa.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springdatajpa.basic.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}