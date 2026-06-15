package cc.kertaskerja.manrisk_fraud.repository;

import cc.kertaskerja.manrisk_fraud.entity.TujuanEx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TujuanExRepository extends JpaRepository<TujuanEx, Long> {

    Optional<TujuanEx> findByKodeTujuanOpdAndTahun(String kodeTujuanOpd, Integer tahun);

    Optional<TujuanEx> findFirstByKodeTujuanOpd(String kodeTujuanOpd);
}