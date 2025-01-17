package comptoirs.entity;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Produit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) // la clé est autogénérée par la BD, On ne veut pas de "setter"
	private Integer reference = null;

	@NonNull
	@Column(unique=true, length = 255)	
	private String nom;

	@ToString.Exclude
	private int fournisseur = 1;

	private String quantiteParUnite = "Une boîte de 12";

	private BigDecimal prixUnitaire = BigDecimal.TEN;

	@ToString.Exclude
	private short unitesEnStock = 0;

	@ToString.Exclude
	private short unitesCommandees = 0;

	@ToString.Exclude
	private short niveauDeReappro = 0;

	private short indisponible = 0;

	@ManyToOne(optional = false)
	@NonNull
	private Categorie categorie ;

	@ToString.Exclude
	@OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
	private List<Ligne> lignes = new LinkedList<>();


}
