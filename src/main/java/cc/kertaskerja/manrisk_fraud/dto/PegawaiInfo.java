package cc.kertaskerja.manrisk_fraud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PegawaiInfo {
    private String nama;
    private String nip;
    private String golongan;
}
