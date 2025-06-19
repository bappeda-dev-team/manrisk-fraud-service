package cc.kertaskerja.manrisk_fraud.repository;

import cc.kertaskerja.manrisk_fraud.entity.Identifikasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IdentifikasiRepository extends JpaRepository<Identifikasi, Long> {

    @Query("SELECT i FROM Identifikasi i WHERE i.idRekin = :idRekin")
    Optional<Identifikasi> findOneByIdRekin(@Param("idRekin") String idRekin);

    boolean existsByIdRekin(String idRekin);
}
