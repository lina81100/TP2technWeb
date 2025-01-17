package comptoirs;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

import comptoirs.dao.UnitesCommandeesParProduit;
import comptoirs.entity.*;
import comptoirs.dao.*;

@Component
@Log4j2 // Génère le 'logger' pour afficher les messages de trace
public class ConsoleApp implements CommandLineRunner {

    @Autowired
    private ProduitRepository produitDAO;

    @Autowired
    private CategorieRepository categorieDAO;

    @Autowired
    private ClientRepository clientDAO;

    @Autowired
    private CommandeRepository commandeDAO;

    @Override
    /*
     * Equivalent de la méthode 'main' pour une application Spring Boot
     **/
    public void run(String... args) throws Exception {

        tapezEnterPourContinuer();

        log.info("Recherche par clé");
        Optional<Produit> op = produitDAO.findById(1);
        op.ifPresent(p -> log.info("On a trouvé le produit : {}", p));

        tapezEnterPourContinuer();

        log.info("Insertion d'une catégorie avec plusieurs produits");
        Categorie nouvelleCat = new Categorie("Ma Nouvelle Catégorie");
        Produit np1 = new Produit("Un nouveau produit", nouvelleCat);
        Produit np2 = new Produit("Autre produit", nouvelleCat);
        nouvelleCat.getProduits().add(np1);
        nouvelleCat.getProduits().add(np2);
        categorieDAO.save(nouvelleCat);

        tapezEnterPourContinuer();

        log.info("Exécution d'une requête 'custom' JPQL");
        int codeCategorie = 1;
        List<UnitesCommandeesParProduit> resultat = produitDAO.produitsVendusJPQL(codeCategorie);
        resultat.forEach( // Une autre syntaxe pour itérer sur une liste !
            ligne -> log.info("Pour {} on a vendu {} unités", ligne.getNomProduit(), ligne.getUnitesCommandees())
        );        
         
        tapezEnterPourContinuer();

        log.info("Même requête en SQL natif");
        resultat = produitDAO.produitsVendusSQL(codeCategorie);
        resultat.forEach( 
            ligne -> log.info("Pour {} on a vendu {} unités", ligne.getNomProduit(), ligne.getUnitesCommandees())
        );        
      
        tapezEnterPourContinuer();    
        log.info("Pour un client, on trouve son adresse 'Embedded'");
        Optional<Client> ocl = clientDAO.findById("BONAP");
        ocl.ifPresent(cl -> log.info("On a trouvé l'adresse de 'BONAP' : {}", cl.getAdresse()));

        tapezEnterPourContinuer();    
        log.info("Recherche par nom de société");
        Client cli = clientDAO.findBySociete("Alfreds Futterkiste").orElseThrow();
        log.info("On a trouvé le client {}",cli);

        tapezEnterPourContinuer();
        log.info("Nombre de produits différents commandés par un client");
        log.info("Le client 'ALFKI' a commandé {} produits différents", clientDAO.countDistinctProduitsByCode("ALFKI"));

        tapezEnterPourContinuer();
        log.info("Nombre de produits différents commandés par chaque client");
        clientDAO.produitsParClient().forEach(
            ppc -> log.info("Le client {} a commandé {} produits différents", ppc.getSociete(), ppc.getNombre())
        );
    }

    public static void tapezEnterPourContinuer() throws IOException  {
        System.out.println("Tapez \"ENTER\" pour continuer...");
        System.in.read();
    }
}
