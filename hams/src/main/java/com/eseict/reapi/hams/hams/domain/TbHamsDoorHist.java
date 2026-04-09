package com.eseict.reapi.hams.hams.domain;

import com.eseict.reapi.common.config.DbSchema;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tb_hams_door_hist", schema = DbSchema.HS_S_SERVICE)
public class TbHamsDoorHist {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "eq_id")
    private String eqId;

    @Column(name = "occur_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date occurDate;

    @Column(name = "door_type")
    private Character doorType;

    @Column(name = "door_state")
    private Character doorState;

    @Column(name = "door_lock_state")
    private Character doorLockState;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEqId() { return eqId; }
    public void setEqId(String eqId) { this.eqId = eqId; }
    public Date getOccurDate() { return occurDate; }
    public void setOccurDate(Date occurDate) { this.occurDate = occurDate; }
    public Character getDoorType() { return doorType; }
    public void setDoorType(Character doorType) { this.doorType = doorType; }
    public Character getDoorState() { return doorState; }
    public void setDoorState(Character doorState) { this.doorState = doorState; }
    public Character getDoorLockState() { return doorLockState; }
    public void setDoorLockState(Character doorLockState) { this.doorLockState = doorLockState; }
}
