package com.kakao.ecotour.jpa;

import com.kakao.ecotour.elastic.RecommendProgramDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EcoProgramRepository extends JpaRepository<EcoProgram, Long> {
}
