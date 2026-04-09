package com.eseict.reapi.hams.equip.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_group_data")
public class TbGroupData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
