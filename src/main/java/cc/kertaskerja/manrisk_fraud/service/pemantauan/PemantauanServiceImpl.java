package cc.kertaskerja.manrisk_fraud.service.pemantauan;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.dto.pemantauan.PemantauanReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.pemantauan.PemantauanResDTO;
import cc.kertaskerja.manrisk_fraud.entity.Pemantauan;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.helper.Crypto;
import cc.kertaskerja.manrisk_fraud.repository.PemantauanRepository;
import cc.kertaskerja.manrisk_fraud.service.global.PegawaiService;
import cc.kertaskerja.manrisk_fraud.service.global.RencanaKinerjaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PemantauanServiceImpl implements PemantauanService {

    private final PemantauanRepository pemantauanRepository;
    private final RencanaKinerjaService rencanaKinerjaService;
    private final PegawaiService pegawaiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private PemantauanResDTO buildDTOFFromRkAndPemantauan(JsonNode rk, Pemantauan p) {
        return PemantauanResDTO.builder()
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
                .id_pohon(rk.path("id_pohon").asInt())
                .nama_pohon(rk.path("nama_pohon").asText())
                .level_pohon(rk.path("level_pohon").asInt())
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
                .tahun(rk.path("tahun").asText())
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
                .pegawai_id(rk.path("pegawai_id").asText())
                .nama_pegawai(rk.path("nama_pegawai").asText())
                .operasional_daerah(PemantauanResDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .pemilik_risiko(p.getPemilikRisiko())
                .risiko_kecurangan(p.getRisikoKecurangan())
                .deskripsi_kegiatan_pengendalian(p.getDeskripsiKegiatanPengendalian())
                .pic(p.getPic())
                .rencana_waktu_pelaksanaan(p.getRencanaWaktuPelaksanaan())
                .realisasi_waktu_pelaksanaan(p.getRealisasiWaktuPelaksanaan())
                .progres_tindak_lanjut(p.getProgresTindakLanjut())
                .bukti_pelaksanaan_tindak_lanjut(p.getBuktiPelaksanaanTindakLanjut())
                .kendala(p.getKendala())
                .catatan(p.getCatatan())
                .status(p.getStatus() != null ? p.getStatus().name() : null)
                .pembuat(p.getPembuat())
                .verifikator(p.getVerifikator())
                .keterangan(p.getKeterangan())
                .build();
    }

    private PemantauanResDTO buildDTOFFromRkOnly(JsonNode rk) {
        return PemantauanResDTO.builder()
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
                .id_pohon(rk.path("id_pohon").asInt())
                .nama_pohon(rk.path("nama_pohon").asText())
                .level_pohon(rk.path("level_pohon").asInt())
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
                .tahun(rk.path("tahun").asText())
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
                .pegawai_id(rk.path("pegawai_id").asText())
                .nama_pegawai(rk.path("nama_pegawai").asText())
                .operasional_daerah(PemantauanResDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .pemilik_risiko("")
                .risiko_kecurangan("")
                .deskripsi_kegiatan_pengendalian("")
                .pic("")
                .rencana_waktu_pelaksanaan("")
                .realisasi_waktu_pelaksanaan("")
                .progres_tindak_lanjut("")
                .bukti_pelaksanaan_tindak_lanjut("")
                .kendala("")
                .catatan("")
                .status("")
                .keterangan("")
                .build();

    }

    @Override
    public List<PemantauanResDTO> findAllPemantauan(String nip, String tahun) {
        if (!Crypto.isEncrypted(nip)) {
            throw new ResourceNotFoundException("NIP is not encrypted: " + nip);
        }

        Map<String, Object> externalResponse = rencanaKinerjaService.getRencanaKinerja(Crypto.decrypt(nip), tahun);
        Object rkObj = externalResponse.get("rencana_kinerja");

        if (!(rkObj instanceof List)) {
            throw new ResourceNotFoundException("No 'Rencana Kinerja' data found for NIP: " + Crypto.decrypt(nip) + " and year: " + tahun);
        }

        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) rkObj;
        List<Pemantauan> pemantauanList = pemantauanRepository.findAll();

        Map<String, List<Pemantauan>> pemantauanMap = new HashMap<>();
        for (Pemantauan p : pemantauanList) {
            String idRekin = p.getIdRencanaKinerja();
            pemantauanMap.computeIfAbsent(idRekin, k -> new ArrayList<>()).add(p);
        }

        List<PemantauanResDTO> result = new ArrayList<>();

        for (Map<String, Object> rk : rekinList) {
            JsonNode rkNode = objectMapper.convertValue(rk, JsonNode.class);
            String idRekin = rkNode.path("id_rencana_kinerja").asText();

            List<Pemantauan> tempList = pemantauanMap.get(idRekin);

            if (tempList != null && !tempList.isEmpty()) {
                for (Pemantauan p : tempList) {
                    result.add(buildDTOFFromRkAndPemantauan(rkNode, p));
                }
            } else {
                result.add(buildDTOFFromRkOnly(rkNode));
            }
        }

        return result;
    }

    @Override
    public PemantauanResDTO findOnePemantauan(String idRekin) {
        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (rkObj == null) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        return pemantauanRepository.findOneByIdRekin(idRekin)
                .map(p -> buildDTOFFromRkAndPemantauan(rkNode, p))
                .orElseGet(() -> buildDTOFFromRkOnly(rkNode));
    }

    @Override
    @Transactional
    public PemantauanResDTO savePemantauan(PemantauanReqDTO reqDTO) {
        String idRekin = reqDTO.getId_rencana_kinerja();

        if (idRekin == null || idRekin.isEmpty()) {
            throw new ResourceNotFoundException("id_rencana_kinerja is required");
        }

        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        if (pemantauanRepository.existsByIdRencanaKinerja(idRekin)) {
            throw new ResourceNotFoundException("Data identifikasi already exists for id_rencana_kinerja: " + idRekin);
        }

        Map<String, Object> pembuat = pegawaiService.getMappedPegawai(Crypto.decrypt(reqDTO.getNip_pembuat()));
        String nipPembuat = (String) pembuat.get("nip");
        String namaPembuat = (String) pembuat.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipPembuat))
                .nama(namaPembuat)
                .build();

        Pemantauan pemantauan = Pemantauan.builder()
                .idRencanaKinerja(idRekin)
                .pemilikRisiko(reqDTO.getPemilik_risiko())
                .risikoKecurangan(reqDTO.getRisiko_kecurangan())
                .deskripsiKegiatanPengendalian(reqDTO.getDeskripsi_kegiatan_pengendalian())
                .pic(reqDTO.getPic())
                .rencanaWaktuPelaksanaan(reqDTO.getRencana_waktu_pelaksanaan())
                .realisasiWaktuPelaksanaan(reqDTO.getRealisasi_waktu_pelaksanaan())
                .progresTindakLanjut(reqDTO.getProgres_tindak_lanjut())
                .buktiPelaksanaanTindakLanjut(reqDTO.getBukti_pelaksanaan_tindak_lanjut())
                .kendala(reqDTO.getKendala())
                .catatan(reqDTO.getCatatan())
                .status(StatusEnum.PENDING)
                .pembuat(pegawai)
                .build();

        Pemantauan saved = pemantauanRepository.save(pemantauan);

        return buildDTOFFromRkAndPemantauan(rkNode, saved);
    }

    @Override
    @Transactional
    public PemantauanResDTO updatePemantauan(String idRekin, PemantauanReqDTO reqDTO) {
        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");
        JsonNode rkNode = objectMapper.convertValue(rekinDetail.get("rencana_kinerja"), JsonNode.class);

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        Map<String, Object> checkIdRekinUpdated = rencanaKinerjaService.getDetailRencanaKinerja(reqDTO.getId_rencana_kinerja());
        Object checkIdRekinUpdatedObj = checkIdRekinUpdated.get("rencana_kinerja");

        if (checkIdRekinUpdatedObj instanceof Map == false) {
            throw new ResourceNotFoundException("YOUR UPDATED: ID Rencana Kinerja " + reqDTO.getId_rencana_kinerja() + " is not valid");
        }

        Map<String, Object> pembuat = pegawaiService.getMappedPegawai(Crypto.decrypt(reqDTO.getNip_pembuat()));
        String nipPembuat = (String) pembuat.get("nip");
        String namaPembuat = (String) pembuat.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipPembuat))
                .nama(namaPembuat)
                .build();

        Pemantauan pemantauan = pemantauanRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data identifikasi not found for id_rencana_kinerja: " + idRekin));

        pemantauan.setPemilikRisiko(reqDTO.getPemilik_risiko());
        pemantauan.setRisikoKecurangan(reqDTO.getRisiko_kecurangan());
        pemantauan.setDeskripsiKegiatanPengendalian(reqDTO.getDeskripsi_kegiatan_pengendalian());
        pemantauan.setPic(reqDTO.getPic());
        pemantauan.setRencanaWaktuPelaksanaan(reqDTO.getRencana_waktu_pelaksanaan());
        pemantauan.setRealisasiWaktuPelaksanaan(reqDTO.getRealisasi_waktu_pelaksanaan());
        pemantauan.setProgresTindakLanjut(reqDTO.getProgres_tindak_lanjut());
        pemantauan.setBuktiPelaksanaanTindakLanjut(reqDTO.getBukti_pelaksanaan_tindak_lanjut());
        pemantauan.setKendala(reqDTO.getKendala());
        pemantauan.setCatatan(reqDTO.getCatatan());
        pemantauan.setPembuat(pegawai);

        Pemantauan updated = pemantauanRepository.save(pemantauan);

        return buildDTOFFromRkAndPemantauan(rkNode, updated) ;
    }

    @Override
    @Transactional
    public PemantauanResDTO verifyPemantauan(String idRekin, PemantauanReqDTO.UpdateStatusDTO updateDTO) {
        Pemantauan entity = pemantauanRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data identifikasi not found for id_rencana_kinerja: " + idRekin));

        Map<String, Object> verifikator = pegawaiService.getMappedPegawai(Crypto.decrypt(updateDTO.getNip_verifikator()));
        String nipVerifikator = (String) verifikator.get("nip");
        String namaVerifikator = (String) verifikator.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipVerifikator))
                .nama(namaVerifikator)
                .build();

        try {
            StatusEnum status = StatusEnum.valueOf(updateDTO.getStatus());
            entity.setStatus(status);
            entity.setKeterangan(updateDTO.getKeterangan());
            entity.setVerifikator(pegawai);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid status value: " + updateDTO.getStatus());
        }

        Pemantauan updated = pemantauanRepository.save(entity);

        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }
        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        return buildDTOFFromRkAndPemantauan(rkNode, updated);
    }

    @Override
    public void deletePemantauan(String idRekin) {
        Pemantauan pemantauan = pemantauanRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data identifikasi not found for id_rencana_kinerja: " + idRekin));

        pemantauanRepository.delete(pemantauan);
    }
}
