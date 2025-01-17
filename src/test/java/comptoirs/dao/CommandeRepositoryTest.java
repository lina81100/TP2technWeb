package comptoirs.dao;

import comptoirs.entity.Commande;
import comptoirs.entity.Ligne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

@DataJpaTest
public class CommandeRepositoryTest {

    @Autowired
    private CommandeRepository commandeDao;

    @Autowired
    private ClientRepository clientDao;

    @Autowired
    private ProduitRepository produitDao;

    private Commande commandeAvecProduits;

    @BeforeEach
    void setUp() {
        var client = clientDao.findById("ALFKI").orElseThrow();
        var produit1 = produitDao.findById(1).orElseThrow();
        var produit2 = produitDao.findById(2).orElseThrow();

        commandeAvecProduits = new Commande();
        commandeAvecProduits.setClient(client);
        commandeAvecProduits.setRemise(new BigDecimal("0.10"));
        commandeAvecProduits.setSaisiele(LocalDate.now());

        commandeAvecProduits.getLignes().add(new Ligne(commandeAvecProduits, produit1, (short) 10));
        commandeAvecProduits.getLignes().add(new Ligne(commandeAvecProduits, produit2, (short) 5));

        commandeDao.save(commandeAvecProduits);
    }

    @Test
    void testCommandeAvecProduits() {
        var commande = commandeDao.findById(commandeAvecProduits.getNumero()).orElseThrow();
        assertEquals(commandeAvecProduits, commande);
        assertEquals(2, commande.getLignes().size());
    }

    @Test
    void testMontantArticles() {
        Integer numeroCommande = 1;
        BigDecimal montant = commandeDao.montantArticles(numeroCommande);

        assertNotNull(montant, "Le montant doit être non nul");
        assertTrue(montant.compareTo(BigDecimal.ZERO) > 0, "Le montant doit être positif");
    }

}