package cc.kertaskerja.manrisk_fraud.service.identifikasi;

import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiDTO;
import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiReqDTO;
import cc.kertaskerja.manrisk_fraud.entity.Identifikasi;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IdentifikasiService {
    List<IdentifikasiDTO> findAllIdentifikasi(String nip, String tahun);

    IdentifikasiDTO findOneIdentifikasi(String idManrisk);

    IdentifikasiDTO saveIdentifikasi(IdentifikasiDTO identifikasiDTO);
}



//{
//        "success": true,
//        "status_code": 200,
//        "message": "Retrieved 2 data successfully",
//        "data": [
//        {
//        "id": 1,
//        "id_rencana_kinerja": "REKIN-PEG-2025-92835",
//        "id_pohon": 2730,
//        "nama_pegawai": "akun test level 3",
//        "pegawai_id": "akun_test_level_3",
//        "nama_pohon": "Melakukan asistensi penyusunan perencanaan pembangunan",
//        "level_pohon": 6,
//        "nama_rencana_kinerja": "Melakukan asistensi penyusunan perencanaan pembangunan-Cek Rincian Belanja",
//        "tahun": "2025",
//        "status_rencana_kinerja": "aktif",
//        "operasional_daerah": {
//        "kode_opd": "5.01.5.05.0.00.01.0000",
//        "nama_opd": "Badan Perencanaan Pembangunan Riset, dan Inovasi Daerah"
//        },
//        "nama_risiko": "Nama risiko",
//        "jenis_risiko": "Jenis risiko",
//        "kemungkinan_kecurangan": "Kemungkinan kecurangan",
//        "indikasi": "tidak ada",
//        "kemungkinan_pihak_terkait": "kemungkinan pihak terkait",
//        "status": "Pending"
//        "keterangan": "Tidak ada",
//        "created_at": "2025-06-11 17:29:18.790",
//        },
//        {
//        "id": 2,
//        "id_rencana_kinerja": "REKIN-PEG-2025-92835",
//        "id_pohon": 2730,
//        "nama_pegawai": "akun test level 3",
//        "pegawai_id": "akun_test_level_3",
//        "nama_pohon": "Melakukan asistensi penyusunan perencanaan pembangunan",
//        "level_pohon": 6,
//        "nama_rencana_kinerja": "Melakukan asistensi penyusunan perencanaan pembangunan-Cek Rincian Belanja",
//        "tahun": "2025",
//        "status_rencana_kinerja": "aktif",
//        "operasional_daerah": {
//        "kode_opd": "5.01.5.05.0.00.01.0000",
//        "nama_opd": "Badan Perencanaan Pembangunan Riset, dan Inovasi Daerah"
//        },
//        "nama_risiko": "-",
//        "jenis_risiko": "-",
//        "kemungkinan_kecurangan": "-",
//        "indikasi": "-",
//        "kemungkinan_pihak_terkait": "-",
//        "status": "-"
//        "keterangan": "-",
//        }
//        ],
//        "timestamp": [
//        2025,
//        6,
//        14,
//        14,
//        43,
//        30,
//        680842000
//        ]
//        }