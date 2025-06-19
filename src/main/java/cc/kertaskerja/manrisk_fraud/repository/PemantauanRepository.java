package cc.kertaskerja.manrisk_fraud.repository;

import cc.kertaskerja.manrisk_fraud.entity.Pemantauan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PemantauanRepository extends JpaRepository<Pemantauan, Long> {

    @Query("SELECT p FROM Pemantauan p WHERE p.idRekin = :idRekin")
    Optional<Pemantauan> findOneByIdRekin(@Param("idRekin") String idRekin);

    boolean existsByIdRekin(String idRekin);
}
