package com.kakao.ecotour.elastic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class KeywordSearchRegionCountResultDto {

    private String keyword;

    private List<Program> programs;

    static KeywordSearchRegionCountResultDto of(String keyword, SearchResponse response) {
        List<Program> programs = ((Terms) response.getAggregations().get("byRegionName"))
                .getBuckets().stream().map(Program::of).collect(Collectors.toList());
        return new KeywordSearchRegionCountResultDto(keyword, programs);
    }

    @AllArgsConstructor
    private static class Program {

        private String region;   // 서비스 지역 이름

        private long count;   // 키워드를 포함하는 프로그램 개수

        static Program of(Terms.Bucket bucket) {
            return new Program((String) bucket.getKey(), bucket.getDocCount());
        }

    }
}
