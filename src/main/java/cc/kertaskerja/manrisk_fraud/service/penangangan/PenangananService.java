package cc.kertaskerja.manrisk_fraud.service.penangangan;

import cc.kertaskerja.manrisk_fraud.dto.PenangananDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PenangananService {

    List<PenangananDTO> findAllPenanganan(String nip, String tahun);

    PenangananDTO findOnePenanganan(String idRekin);

    PenangananDTO savePenanganan(PenangananDTO penangananDTO);

    PenangananDTO updatePenanganan(String idRekin, PenangananDTO penangananDTO);

    PenangananDTO updateStatusPenanganan(String idRekin, PenangananDTO.UpdateStatusDTO updateDTO);

    void deletePenanganan(String idRekin);
}
