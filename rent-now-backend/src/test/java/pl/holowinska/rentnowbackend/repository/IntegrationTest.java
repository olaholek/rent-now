package pl.holowinska.rentnowbackend.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.holowinska.rentnowbackend.RentNowBackendApplication;

@SpringBootTest(classes = RentNowBackendApplication.class)
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
@ExtendWith(MockitoExtension.class)
public class IntegrationTest {
}
