package cc.kertaskerja.manrisk_fraud.dto.external;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RencanaKinerjaDTO {
    private String id_rencana_kinerja;
    private Integer id_pohon;
    private String nama_pohon;
    private Integer level_pohon;
    private String nama_rencana_kinerja;
    private String tahun;
    private String status_rencana_kinerja;
    private String pegawai_id;
    private String nama_pegawai;
    private OperasionalDaerah operasional_daerah;

    @Data
    public static class OperasionalDaerah {
        private String kode_opd;
        private String nama_opd;
    }
}
