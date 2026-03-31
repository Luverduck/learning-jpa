package hello.jpa.valuetype;

import hello.jpa.entity.Address;
import hello.jpa.entity.AddressEntity;
import hello.jpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Set;

public class ValueTypeCollection {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 생성
            Member member = new Member();
            member.setName("member");
            member.setHomeAddress(new Address("city1", "street", "10000"));
            // 엔티티의 값 타입 컬렉션에 요소 추가
            member.getFavoriteFoods().add("chicken");
            member.getFavoriteFoods().add("pizza");
            member.getFavoriteFoods().add("hamburger");
            // 엔티티의 값 타입 컬렉션에 요소 추가
            member.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
            member.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));
            // 엔티티 저장
            System.out.println("=============== 엔티티 저장 ===============");
            entityManager.persist(member);
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();
            // 엔티티 조회
            System.out.println("=============== 엔티티 조회 ===============");
            Member findMember = entityManager.find(Member.class, member.getId());
            System.out.println("--------------- 값 타입 컬렉션 조회 ---------------");
            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood = " + favoriteFood);
            }
            List<AddressEntity> addressHistory = findMember.getAddressHistory();
            for (AddressEntity addressEntity : addressHistory) {
                System.out.println("address.getCity = " + addressEntity.getAddress().getCity());
            }
            // 엔티티 수정
            System.out.println("=============== 엔티티 수정 ===============");
            System.out.println("--------------- 값 타입 컬렉션 수정 ---------------");
            findMember.getFavoriteFoods().remove("chicken");
            findMember.getFavoriteFoods().add("apple");
            for (AddressEntity addressEntity : addressHistory) {
                Address address = addressEntity.getAddress();
                if (address.getCity().equals("old1")) {
                    addressEntity.setAddress(new Address("new1", address.getStreet(), address.getZipcode()));
                }
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
