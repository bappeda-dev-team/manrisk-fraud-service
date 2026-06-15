package cc.kertaskerja.manrisk_fraud.service.manajemenRisiko;

import cc.kertaskerja.manrisk_fraud.dto.ManajemenRisikoDTO;
import cc.kertaskerja.manrisk_fraud.entity.*;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.helper.Crypto;
import cc.kertaskerja.manrisk_fraud.repository.*;
import cc.kertaskerja.manrisk_fraud.service.external.rekin.RencanaKinerjaService;
import cc.kertaskerja.manrisk_fraud.service.global.PegawaiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManajemenRisikoServiceImpl implements ManajemenRisikoService {

    private final IdentifikasiRepository identifikasiRepository;
    private final AnalisaRepository analisaRepository;
    private final PenangananRepository penangananRepository;
    private final PemantauanRepository pemantauanRepository;
    private final HasilPemantauanRepository hasilPemantauanRepository;
    private final RencanaKinerjaService rekinService;
    private final PegawaiService pegawaiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private ManajemenRisikoDTO buildManajemenRisikoDTO(
            JsonNode rk,
            Identifikasi identifikasi,
            Analisa analisa,
            Penangangan penanganan,
            Pemantauan pemantauan,
            HasilPemantauan hasilPemantauan) {

        ManajemenRisikoDTO.ManajemenRisikoDTOBuilder builder = ManajemenRisikoDTO.builder()
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
                .id_pohon(rk.path("id_pohon").asInt(0))
                .nama_pohon(rk.path("nama_pohon").asText())
                .level_pohon(rk.path("level_pohon").asInt(0))
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
                .tahun(rk.path("tahun").asText())
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
                .pegawai_id(rk.path("pegawai_id").asText())
                .nama_pegawai(rk.path("nama_pegawai").asText())
                .operasional_daerah(ManajemenRisikoDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build());

        if (identifikasi != null) {
            builder
                    .id(identifikasi.getId())
                    .nama_risiko(identifikasi.getNamaRisiko())
                    .jenis_risiko(identifikasi.getJenisRisiko())
                    .uraian(identifikasi.getUraian())
                    .kemungkinan_kecurangan(identifikasi.getKemungkinanKecurangan())
                    .indikasi(identifikasi.getIndikasi())
                    .kemungkinan_pihak_terkait(identifikasi.getKemungkinanPihakTerkait());
        } else {
            builder
                    .jenis_risiko("")
                    .uraian("")
                    .kemungkinan_kecurangan("")
                    .indikasi("")
                    .kemungkinan_pihak_terkait("");
        }

        if (analisa != null) {
            builder
                    .penyebab(analisa.getPenyebab())
                    .akibat(analisa.getAkibat())
                    .skala_dampak_sebelum_pengendalian(analisa.getSkalaDampak())
                    .skala_kemungkinan_sebelum_pengendalian(analisa.getSkalaKemungkinan())
                    .tingkat_risiko_sebelum_pengendalian(analisa.getTingkatRisiko())
                    .level_risiko_sebelum_pengendalian(analisa.getLevelRisiko());
        } else {
            builder
                    .penyebab("")
                    .akibat("")
                    .skala_dampak_sebelum_pengendalian(0)
                    .skala_kemungkinan_sebelum_pengendalian(0)
                    .tingkat_risiko_sebelum_pengendalian(0)
                    .level_risiko_sebelum_pengendalian("");
        }

        if (penanganan != null) {
            builder
                    .existing_control(penanganan.getExistingControl())
                    .jenis_perlakuan_risiko(penanganan.getJenisPerlakuanRisiko())
                    .rencana_perlakuan_risiko(penanganan.getRencanaPerlakuanRisiko())
                    .biaya_perlakuan_risiko(penanganan.getBiayaPerlakuanRisiko())
                    .target_waktu(penanganan.getTargetWaktu())
                    .pic(penanganan.getPic());
        } else {
            builder
                    .existing_control("")
                    .jenis_perlakuan_risiko("")
                    .rencana_perlakuan_risiko("")
                    .biaya_perlakuan_risiko("")
                    .target_waktu("")
                    .pic("");
        }

        if (pemantauan != null) {
            builder
                    .risiko_kecurangan(pemantauan.getRisikoKecurangan())
                    .deskripsi_kegiatan_pengendalian(pemantauan.getDeskripsiKegiatanPengendalian())
                    .rencana_waktu_pelaksanaan(pemantauan.getRencanaWaktuPelaksanaan())
                    .realisasi_waktu_pelaksanaan(pemantauan.getRealisasiWaktuPelaksanaan())
                    .progres_tindak_lanjut(pemantauan.getProgresTindakLanjut())
                    .bukti_pelaksanaan_tindak_lanjut(pemantauan.getBuktiPelaksanaanTindakLanjut())
                    .kendala(pemantauan.getKendala());
        } else {
            builder
                    .risiko_kecurangan("")
                    .deskripsi_kegiatan_pengendalian("")
                    .rencana_waktu_pelaksanaan("")
                    .realisasi_waktu_pelaksanaan("")
                    .progres_tindak_lanjut("")
                    .bukti_pelaksanaan_tindak_lanjut("")
                    .kendala("");
        }

        if (hasilPemantauan != null) {
            builder
                    .skala_dampak_setelah_pengendalian(hasilPemantauan.getSkalaDampak())
                    .skala_kemungkinan_setelah_pengendalian(hasilPemantauan.getSkalaKemungkinan())
                    .tingkat_risiko_setelah_pengendalian(hasilPemantauan.getTingkatRisiko())
                    .level_risiko_setelah_pengendalian(hasilPemantauan.getLevelRisiko())
                    .status_hasil_pemantauan(hasilPemantauan.getStatus() != null ? hasilPemantauan.getStatus().name() : null)
                    .keterangan_hasil_pemantauan(hasilPemantauan.getKeterangan());
        } else {
            builder
                    .skala_dampak_setelah_pengendalian(0)
                    .skala_kemungkinan_setelah_pengendalian(0)
                    .tingkat_risiko_setelah_pengendalian(0)
                    .level_risiko_setelah_pengendalian("")
                    .status_hasil_pemantauan("")
                    .keterangan_hasil_pemantauan("");
        }

        return builder.build();
    }

    @Override
    public List<ManajemenRisikoDTO> showAllData(String nip, String tahun) {
        if (!Crypto.isEncrypted(nip)) {
            throw new ResourceNotFoundException("NIP is not encrypted: " + nip);
        }

        Map<String, Object> resRekin = rekinService.getRencanaKinerja(Crypto.decrypt(nip), tahun);
        Object rkObj = resRekin.get("rencana_kinerja");

        if (!(rkObj instanceof List)) {
            throw new ResourceNotFoundException("No 'Rencana Kinerja' data found for NIP: " + Crypto.decrypt(nip) + " and year: " + tahun);
        }

        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) rkObj;
        List<Identifikasi> identifikasiList = identifikasiRepository.findAll();
        List<Analisa> analisaList = analisaRepository.findAll();
        List<Penangangan> penangananList = penangananRepository.findAll();
        List<Pemantauan> pemantauanList = pemantauanRepository.findAll();
        List<HasilPemantauan> hasilPemantauanList = hasilPemantauanRepository.findAll();

        Map<String, Identifikasi> identifikasiMap = identifikasiList.stream()
                .collect(Collectors.toMap(Identifikasi::getIdRencanaKinerja, i -> i, (a, b) -> a));
        Map<String, Analisa> analisaMap = analisaList.stream()
                .collect(Collectors.toMap(Analisa::getIdRencanaKinerja, a -> a, (a, b) -> a));
        Map<String, Penangangan> penangananMap = penangananList.stream()
                .collect(Collectors.toMap(Penangangan::getIdRencanaKinerja, p -> p, (a, b) -> a));
        Map<String, Pemantauan> pemantauanMap = pemantauanList.stream()
                .collect(Collectors.toMap(Pemantauan::getIdRencanaKinerja, p -> p, (a, b) -> a));
        Map<String, HasilPemantauan> hasilPemantauanMap = hasilPemantauanList.stream()
                .collect(Collectors.toMap(HasilPemantauan::getIdRencanaKinerja, h -> h, (a, b) -> a));

        List<ManajemenRisikoDTO> result = new ArrayList<>();

        for (Map<String, Object> rekinDetail : rekinList) {
            JsonNode rkNode = objectMapper.convertValue(rekinDetail, JsonNode.class);
            String idRekin = rkNode.path("id_rencana_kinerja").asText();

            Identifikasi identifikasi = identifikasiMap.get(idRekin);
            Analisa analisa = analisaMap.get(idRekin);
            Penangangan penanganan = penangananMap.get(idRekin);
            Pemantauan pemantauan = pemantauanMap.get(idRekin);
            HasilPemantauan hasilPemantauan = hasilPemantauanMap.get(idRekin);

            ManajemenRisikoDTO dto = buildManajemenRisikoDTO(rkNode, identifikasi, analisa, penanganan, pemantauan, hasilPemantauan);

            result.add(dto);
        }

        return result;
    }
}

