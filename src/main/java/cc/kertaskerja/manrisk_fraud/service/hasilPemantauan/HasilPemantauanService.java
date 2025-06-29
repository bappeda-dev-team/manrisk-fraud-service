package cc.kertaskerja.manrisk_fraud.service.hasilPemantauan;

import cc.kertaskerja.manrisk_fraud.dto.hasilPemantauan.HasilPemantauanReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.hasilPemantauan.HasilPemantauanResDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HasilPemantauanService {

    List<HasilPemantauanResDTO> findAll(String nip, String tahun);

    HasilPemantauanResDTO findOne(String idRekin);

    HasilPemantauanResDTO save(HasilPemantauanReqDTO dto);

    HasilPemantauanResDTO update(String idRekin, HasilPemantauanReqDTO dto);

    HasilPemantauanResDTO verify(String idRekin, HasilPemantauanReqDTO.UpdateStatusDTO updateDTO);

    void delete(String idRekin);
}
