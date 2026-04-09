package com.eseict.reapi.hams.hams.service;

import com.eseict.reapi.hams.hams.dto.HamsDoorLockStatDto;
import com.eseict.reapi.hams.hams.dto.HamsDoorStatDto;
import com.eseict.reapi.hams.hams.repository.TbHamsDoorHistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class HamsStatService {

    private static final Map<String, String> DATE_FORMAT_MAP = Map.of(
        "Y", "yyyy",
        "M", "yyyyMM",
        "D", "yyyyMMdd"
    );

    private final TbHamsDoorHistRepository doorHistRepository;

    public HamsStatService(TbHamsDoorHistRepository doorHistRepository) {
        this.doorHistRepository = doorHistRepository;
    }

    /**
     * doorType: 1=door state, 2=door lock state
     * dateType: Y/M/D
     */
    public Object getStat(String eqId, String frontBack, String doorType,
                          String dateType, String startDtm, String endDtm) {
        String dateFormat = DATE_FORMAT_MAP.getOrDefault(dateType, "yyyyMMdd");
        String safeEqId = eqId != null ? eqId : "";
        String safeFrontBack = frontBack != null ? frontBack : "";
        String safeStartDtm = startDtm != null ? startDtm : "";
        String safeEndDtm = endDtm != null ? endDtm : "";

        if ("1".equals(doorType)) {
            // door state (open/close)
            return doorHistRepository.getDoorStatByDateFormat(
                safeEqId, safeFrontBack, safeStartDtm, safeEndDtm, dateFormat
            );
        } else {
            // door lock state (none/unlock/lock/unrecognized/lockError)
            return doorHistRepository.getDoorLockStatByDateFormat(
                safeEqId, safeFrontBack, safeStartDtm, safeEndDtm, dateFormat
            );
        }
    }
}
