package comptoirs.dao;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CommandeProjection {
    Integer getNumero();
    String getPort();
    BigDecimal getMontantTotal();
}