package com.kakao.ecotour.jpa;

import com.kakao.ecotour.util.ModelMapperUtils;
import com.kakao.ecotour.kakaoapi.RegionInfoRefiner;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "regionSeq", callSuper = false)
@Table(name = "region")
public class RegionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long regionSeq;

    @Column(nullable = false)
    private String regionCode;  // 서비스 지역 코드

    @Column(nullable = false)
    private String regionName;  // 서비스 지역 이름

    @OneToMany(mappedBy = "regionEntityCity")
    private List<EcoProgramEntity> ecoProgramEntityList;

    public RegionEntity(String regionCode, String regionName) {
        this.regionCode = regionCode;
        this.regionName = regionName;
    }

    public static RegionEntity of(RegionInfoRefiner.RegionDTO regionDTO) {
        return ModelMapperUtils.getModelMapper().map(regionDTO, RegionEntity.class);
    }
}
