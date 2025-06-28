package cc.kertaskerja.manrisk_fraud.repository;

import cc.kertaskerja.manrisk_fraud.entity.RisikoKecurangan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RisikoKecuranganRepository extends JpaRepository<RisikoKecurangan, Long> {
    @Query("""
        SELECT r FROM RisikoKecurangan r
        WHERE :jenisRisiko = 'RISIKO'
           OR UPPER(r.jenisRisiko) = UPPER(:jenisRisiko)
    """)
    List<RisikoKecurangan> findByJenisRisikoFlexible(@Param("jenisRisiko") String jenisRisiko);
}
