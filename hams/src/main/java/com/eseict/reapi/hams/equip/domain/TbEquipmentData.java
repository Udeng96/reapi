package com.eseict.reapi.hams.equip.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tb_equipment_data")
public class TbEquipmentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "ip")
    private String ip;

    @Column(name = "port")
    private Long port;

    @Column(name = "jibun_address")
    private String jibunAddress;

    @Column(name = "roadname_address")
    private String roadnameAddress;

    @Column(name = "extra_address")
    private String extraAddress;

    @Column(name = "group_id", insertable = false, updatable = false)
    private Long groupId;

    @Column(name = "usable")
    private String usable;

    @Column(name = "setup_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date setupDate;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private TbGroupData refTbGroupData;

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getLatitude() { return latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public String getIp() { return ip; }
    public Long getPort() { return port; }
    public String getJibunAddress() { return jibunAddress; }
    public String getRoadnameAddress() { return roadnameAddress; }
    public String getExtraAddress() { return extraAddress; }
    public Long getGroupId() { return groupId; }
    public String getUsable() { return usable; }
    public TbGroupData getRefTbGroupData() { return refTbGroupData; }
}
