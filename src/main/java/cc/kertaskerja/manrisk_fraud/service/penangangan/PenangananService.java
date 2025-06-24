package cc.kertaskerja.manrisk_fraud.service.penangangan;

import cc.kertaskerja.manrisk_fraud.dto.penanganan.PenangananReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.penanganan.PenangananResDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PenangananService {

    List<PenangananResDTO> findAllPenanganan(String nip, String tahun);

    PenangananResDTO findOnePenanganan(String idRekin);

    PenangananResDTO savePenanganan(PenangananReqDTO reqDTO);

    PenangananResDTO updatePenanganan(String idRekin, PenangananReqDTO reqDTO);

    PenangananResDTO verifyPenanganan(String idRekin, PenangananReqDTO.UpdateStatusDTO updateDTO);

    void deletePenanganan(String idRekin);
}
