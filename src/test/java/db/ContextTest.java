package db;

import com.example.demo.DemoApplication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;



@SpringBootTest(classes = {DemoApplication.class, TestConfig.class})
@ActiveProfiles("test")
public class ContextTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void contextUp() {
        assertNotNull(entityManager, "EntityManager no debe ser nulo");
        assertTrue(entityManager.isOpen(), "EntityManager debe estar abierto");
    }

    @Test
    void contextUpWithTransaction() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus status) {
                assertTrue(status.isNewTransaction(), "Debe ejecutarse dentro de una transacción");

                // Verificación adicional: ejecutar consulta simple
                Object result = entityManager.createNativeQuery("SELECT 1").getSingleResult();
                assertNotNull(result, "Consulta simple debe devolver resultado");
                return null;
            }
        });
    }
}