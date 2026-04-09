package com.eseict.reapi.hams.hams.service;

import com.eseict.reapi.hams.hams.domain.TbHamsDoorEventHist;
import com.eseict.reapi.hams.hams.domain.TbHamsDoorHist;
import com.eseict.reapi.hams.hams.repository.TbHamsDoorEventHistRepository;
import com.eseict.reapi.hams.hams.repository.TbHamsDoorHistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class HamsDoeEventService {

    private final TbHamsDoorHistRepository doorHistRepository;
    private final TbHamsDoorEventHistRepository doorEventHistRepository;

    public HamsDoeEventService(TbHamsDoorHistRepository doorHistRepository,
                               TbHamsDoorEventHistRepository doorEventHistRepository) {
        this.doorHistRepository = doorHistRepository;
        this.doorEventHistRepository = doorEventHistRepository;
    }

    @Transactional
    public boolean saveDoorHist(String eqId, char doorType, char doorState, char doorLockState) {
        TbHamsDoorHist hist = new TbHamsDoorHist();
        hist.setId("HAMS_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        hist.setEqId(eqId);
        hist.setOccurDate(new Date());
        hist.setDoorType(doorType);
        hist.setDoorState(doorState);
        hist.setDoorLockState(doorLockState);
        doorHistRepository.save(hist);
        return true;
    }

    @Transactional
    public boolean saveDoorEventHist(String eqId, String doorType, String occurTarget, String eventType) {
        TbHamsDoorEventHist hist = new TbHamsDoorEventHist();
        hist.setId("HAMS_EVENT_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        hist.setEqId(eqId);
        hist.setOccurDate(new Date());
        hist.setOrdererType("0");
        hist.setUserId("0");
        hist.setOrdererIp("0");
        hist.setDoorType(doorType);
        hist.setOccurTarget(occurTarget);
        hist.setEventType(eventType);
        doorEventHistRepository.save(hist);
        return true;
    }
}
