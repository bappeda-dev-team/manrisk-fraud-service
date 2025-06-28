package cc.kertaskerja.manrisk_fraud.repository;

import cc.kertaskerja.manrisk_fraud.entity.HasilPemantauan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HasilPemantauanRepository extends JpaRepository<HasilPemantauan, String> {

    @Query("SELECT hp FROM HasilPemantauan hp WHERE hp.idRencanaKinerja = :idRekin")
    Optional<HasilPemantauan> findOneByIdRekin(@Param("idRekin") String idRekin);

    boolean existsByIdRencanaKinerja(String idRencanaKinerja);
}
