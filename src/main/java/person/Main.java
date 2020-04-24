package person;

import com.github.javafaker.Faker;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;
import java.util.Locale;

@Log4j2
public class Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");
    public static Faker faker = new Faker(new Locale("en"));

    private static Person randomPerson() {

        Address address = Address.builder()
                .country(faker.address().country())
                .state(faker.address().state())
                .city(faker.address().city())
                .streetAddress(faker.address().streetAddress())
                .zip(faker.address().zipCode())
                .build();

        Person person = Person.builder()
                .name(faker.name().name())
                .dob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .gender(faker.options().option(Person.Gender.MALE, Person.Gender.FEMALE))
                .address(address)
                .email(faker.internet().emailAddress())
                .profession(faker.company().profession())
                .build();

        return person;
    }

    public static void main(String[] args) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
        for(int i=0; i<=10; i++) {
            em.persist(randomPerson());
        }
            em.getTransaction().commit();
            em.createQuery("SELECT l FROM Person l ORDER BY l.id", Person.class).getResultList().forEach(log::info);
        } finally {
            em.close();
            emf.close();
        }
    }
}

