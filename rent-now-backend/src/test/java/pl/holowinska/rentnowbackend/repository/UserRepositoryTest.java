package pl.holowinska.rentnowbackend.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.holowinska.rentnowbackend.model.entities.User;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest extends IntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Test
    public void userShouldBeSaved() {
        //given
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        user.setFirstName("Ola");
        user.setLastName("Holo");
        user.setPhoneNumber("675534211");

        //when
        userRepository.save(user);
        Optional<User> byId = userRepository.findById(uuid);

        //then
        assertTrue(byId.isPresent());
    }

    @Test
    public void userShouldBeDeleted() {
        //given
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        user.setFirstName("Ola");
        user.setLastName("Holo");
        user.setPhoneNumber("675534211");

        //when
        User saved = userRepository.save(user);
        userRepository.delete(saved);
        Optional<User> byId = userRepository.findById(saved.getId());

        //then
        assertFalse(byId.isPresent());
    }

    @Test
    public void userShouldBeUpdated() {
        //given
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        user.setFirstName("Ola");
        user.setLastName("Holo");
        user.setPhoneNumber("675534211");

        //when
        User saved = userRepository.save(user);
        saved.setFirstName("Olek");
        User updated = userRepository.save(saved);

        //then
        assertEquals(updated.getFirstName(), "Olek");
    }
}