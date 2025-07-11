package cc.kertaskerja.manrisk_fraud.repository;

import cc.kertaskerja.manrisk_fraud.entity.Analisa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnalisaRepository extends JpaRepository<Analisa, Long> {

    @Query("SELECT a FROM Analisa a WHERE a.idRencanaKinerja = :idRekin")
    Optional<Analisa> findOneByIdRekin(@Param("idRekin") String idRekin);

    boolean existsByIdRencanaKinerja(String idRekin);

}
