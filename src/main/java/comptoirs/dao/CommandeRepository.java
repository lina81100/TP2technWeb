package comptoirs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import comptoirs.entity.Commande;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    @Query("SELECT SUM(l.quantite * l.produit.prixUnitaire * (1 - c.remise)) FROM Ligne l " +
            "JOIN l.commande c WHERE c.numero = :numeroCommande")
    BigDecimal montantArticles(Integer numeroCommande);

    @Query("SELECT c.numero AS numero, c.port AS port, " +
            "(SELECT COALESCE(SUM(l.quantite * l.produit.prixUnitaire * (1 - c.remise)), 0) FROM Ligne l WHERE l.commande.numero = c.numero) AS montantTotal " +
            "FROM Commande c WHERE c.client.code = :codeClient")
    List<CommandeProjection> findCommandesByClientCode(String codeClient);
}