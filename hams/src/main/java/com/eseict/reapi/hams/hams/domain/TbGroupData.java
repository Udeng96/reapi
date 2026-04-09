package com.eseict.reapi.hams.hams.domain;

import com.eseict.reapi.common.config.DbSchema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

// TODO: 실제 테이블명 확인 후 @Entity 활성화
// @Entity
// @Table(name = "tb_group_data", schema = DbSchema.HAMS)
public class TbGroupData {

    @Id
    private Long id;

    @Column(name = "parent")
    private Long parent;

    @Column(name = "name")
    private String name;

    @Column(name = "usable")
    private String usable;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @OneToMany(mappedBy = "refTbGroupData")
    @JsonIgnore
    private List<TbEquipmentData> equipmentDatas;

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getUsable() { return usable; }
}
