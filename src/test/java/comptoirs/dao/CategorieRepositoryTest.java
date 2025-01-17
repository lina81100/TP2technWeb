package comptoirs.dao;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import lombok.extern.log4j.Log4j2;

import comptoirs.entity.Categorie;

@DataJpaTest
@Log4j2 // Génère le 'logger' pour afficher les messages de trace        
class CategorieRepositoryTest {
	@Autowired
	private CategorieRepository categoryDAO;

	@Test
	void compterLesEntites() {
		log.info("Compter les entités");

		long nombre = categoryDAO.count(); // 'count' donne le nombre d'enregistrements

		assertEquals(8, nombre, "Le jeu de test contient 8 catégories");
	}

	@Test
	void listerLesEntites() {
		log.info("Lister les entités");

		List<Categorie> liste = categoryDAO.findAll(); // Renvoie la liste des entités dans la table

		log.info("Liste des entités: {}", liste);
	}
	
	@Test
	void touverParCle() {
		log.info("Trouver une entité par sa clé");

		int codePresent = 1;
		Optional<Categorie> resultat = categoryDAO.findById(codePresent);
		// On s'assure qu'on trouvé le résultat
		assertTrue(resultat.isPresent(), "Cette catégorie existe");
		Categorie c = resultat.get();
		assertEquals("Boissons", c.getLibelle());

		log.info("Entité trouvée: {}", c);
	}

	@Test
	void entiteInconnue()  {
		log.info("Chercher une entité inconnue");
		int codeInconnu = 99;

		Optional<Categorie> resultat = categoryDAO.findById(codeInconnu);

		assertFalse(resultat.isPresent(), "Cette catégorie n'existe pas");

	}

	@Test
	void creerUneEntite()  {
		log.info("Créer une entité");
		Categorie nouvelle = new Categorie();
		nouvelle.setLibelle("essai");
		nouvelle.setDescription("essai");
		assertNull(nouvelle.getCode(), "L'entité n'a pas encore de clé");
		categoryDAO.save(nouvelle); // 'save' enregistre l'entite dans la base
		Integer nouvellecle = nouvelle.getCode(); // La clé a été auto-générée lors de l'enregistrement
		assertNotNull(nouvellecle, "Une nouvelle clé doit avoir été générée");
		log.info("Nouvelle entité: {}", nouvelle);
	}

	@Test
	void erreurCreationEntite() {
		log.info("Créer une entité avec erreur");
		Categorie nouvelle = new Categorie();
		nouvelle.setLibelle("Boissons");  // Ce libellé existe dans le jeu de test
		nouvelle.setDescription("essai");
		try { // L'enregistrement peut générer des exceptions (ex : violation de contrainte d'intégrité)
			categoryDAO.save(nouvelle);
			fail("Les libellés doivent être tous distincts, on doit avoir une exception");
		} catch (DataIntegrityViolationException e) {
			// Si on arrive ici c'est normal, on a eu l'exception attendue
		}

		assertNull(nouvelle.getCode(), "La clé n'a pas été générée, l'entité n'est pas enregistrée");
	}

	@Test
	void onNePeutPasDetruireUneCategorieQuiADesProduits() {
		log.info("Détruire une catégorie avec des produits");
		Categorie boissons = categoryDAO.getOne(1);
		assertEquals("Boissons", boissons.getLibelle());
		// Il y a des produits dans la catégorie 'Boissons'
		assertFalse(boissons.getProduits().isEmpty());
		// Si on essaie de détruire cette catégorie, on doit avoir une exception
		// de violation de contrainte d'intégrité
		assertThrows(DataIntegrityViolationException.class, () -> {
			categoryDAO.delete(boissons);
			categoryDAO.flush(); // Pour forcer la validation de la transaction
		});
	}

}
