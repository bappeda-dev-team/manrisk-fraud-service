package cc.kertaskerja.manrisk_fraud.dto.risikoKecurangan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RisikoKecuranganDTO {
    @NotBlank(message = "Jenis risiko tidak boleh kosong!")
    private String jenis_risiko;

    @NotBlank(message = "Uraian risiko tidak boleh kosong!")
    private String uraian;
}
