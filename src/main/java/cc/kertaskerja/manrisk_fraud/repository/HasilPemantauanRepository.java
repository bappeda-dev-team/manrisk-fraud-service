package cc.kertaskerja.manrisk_fraud.repository;

import cc.kertaskerja.manrisk_fraud.entity.HasilPemantauan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HasilPemantauanRepository extends JpaRepository<HasilPemantauan, Long> {

    boolean existsByIdRencanaKinerja(String idRekin);
}
