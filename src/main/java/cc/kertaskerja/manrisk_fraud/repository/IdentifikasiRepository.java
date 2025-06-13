package cc.kertaskerja.manrisk_fraud.repository;

import cc.kertaskerja.manrisk_fraud.entity.Identifikasi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentifikasiRepository extends JpaRepository<Identifikasi, Long> {

    Optional<Identifikasi> findByIdManrisk_IdManrisk(String idManrisk);
}
