package cc.kertaskerja.manrisk_fraud.repository;

import cc.kertaskerja.manrisk_fraud.entity.Penangangan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PenangananRepository extends JpaRepository<Penangangan, Long> {

    @Query("SELECT p FROM Penangangan p WHERE p.idRekin = :idRekin")
    Optional<Penangangan> findOneByIdRekin(String idRekin);

    boolean existsByIdRekin(String idRekin);
}
