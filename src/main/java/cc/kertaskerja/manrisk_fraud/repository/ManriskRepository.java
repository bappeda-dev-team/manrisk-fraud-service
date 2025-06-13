package cc.kertaskerja.manrisk_fraud.repository;

import cc.kertaskerja.manrisk_fraud.entity.Manrisk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManriskRepository extends JpaRepository<Manrisk, String> {
}
