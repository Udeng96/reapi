package com.eseict.reapi.hams.hams.domain;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tb_hams_door_event_hist", schema = DbSchema.HS_S_SERVICE)
public class TbHamsDoorEventHist {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "eq_id")
    private String eqId;

    @Column(name = "occur_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date occurDate;

    @Column(name = "orderer_type")
    private String ordererType;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "orderer_ip")
    private String ordererIp;

    @Column(name = "door_type")
    private String doorType;

    @Column(name = "occur_target")
    private String occurTarget;

    @Column(name = "event_type")
    private String eventType;

    public void setId(String id) { this.id = id; }
    public void setEqId(String eqId) { this.eqId = eqId; }
    public void setOccurDate(Date occurDate) { this.occurDate = occurDate; }
    public void setOrdererType(String ordererType) { this.ordererType = ordererType; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setOrdererIp(String ordererIp) { this.ordererIp = ordererIp; }
    public void setDoorType(String doorType) { this.doorType = doorType; }
    public void setOccurTarget(String occurTarget) { this.occurTarget = occurTarget; }
    public void setEventType(String eventType) { this.eventType = eventType; }
}
