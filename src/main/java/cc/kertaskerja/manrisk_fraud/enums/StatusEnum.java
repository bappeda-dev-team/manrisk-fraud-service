package cc.kertaskerja.manrisk_fraud.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum StatusEnum {
    DRAFT,
    PENDING,
    APPROVED,
    REJECTED
}
