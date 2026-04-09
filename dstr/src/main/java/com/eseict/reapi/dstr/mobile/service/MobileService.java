package com.eseict.reapi.dstr.mobile.service;

import com.eseict.reapi.dstr.event.dto.Paging;
import com.eseict.reapi.dstr.mobile.domain.MonMbDesc;
import com.eseict.reapi.dstr.mobile.domain.MonMbDescLog;
import com.eseict.reapi.dstr.mobile.dto.MobileInfoDto;
import com.eseict.reapi.dstr.mobile.repository.MobileLiveRepository;
import com.eseict.reapi.dstr.mobile.repository.MobilePastRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MobileService {

    private final MobileLiveRepository mobileLiveRepository;
    private final MobilePastRepository mobilePastRepository;

    public MobileService(MobileLiveRepository mobileLiveRepository,
                         MobilePastRepository mobilePastRepository) {
        this.mobileLiveRepository = mobileLiveRepository;
        this.mobilePastRepository = mobilePastRepository;
    }

    public List<MobileInfoDto> getLiveList() {
        return mobileLiveRepository.getLiveList().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public MobileInfoDto getLiveInfo(String userId) {
        return mobileLiveRepository.getLiveByUserId(userId)
            .map(this::toDto)
            .orElse(null);
    }

    public Paging<MobileInfoDto> getPastList(String startDtm, String endDtm, int page, int rows) {
        Pageable pageable = page < 0 ? Pageable.unpaged() : PageRequest.of(page - 1, rows);
        Page<MonMbDescLog> result = mobilePastRepository.getPastList(startDtm, endDtm, pageable);

        List<MobileInfoDto> list = result.getContent().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        return new Paging<>(result.getTotalElements(), list);
    }

    public MobileInfoDto getPastInfo(int seq) {
        return mobilePastRepository.getPastBySeq(seq)
            .map(this::toDto)
            .orElse(null);
    }

    private MobileInfoDto toDto(MonMbDesc m) {
        return new MobileInfoDto(
            m.getUserId(), m.getUserNm(), formatDtm(m.getUserDtm()),
            m.getLat(), m.getLon(), m.getMbUrl(), m.getTitle(),
            m.getDptNm(), m.getMgrNo(), m.getMobileChk()
        );
    }

    private MobileInfoDto toDto(MonMbDescLog m) {
        return new MobileInfoDto(
            m.getUserId(), m.getUserNm(), formatDtm(m.getUserDtm()),
            m.getLat(), m.getLon(), m.getMbUrl(), m.getTitle(),
            m.getDptNm(), m.getMgrNo(), m.getMbChk()
        );
    }

    private String formatDtm(String dtm) {
        if (dtm == null || dtm.length() < 14) return dtm;
        return dtm.substring(0, 4) + "-" + dtm.substring(4, 6) + "-" + dtm.substring(6, 8)
            + " " + dtm.substring(8, 10) + ":" + dtm.substring(10, 12) + ":" + dtm.substring(12, 14);
    }
}
