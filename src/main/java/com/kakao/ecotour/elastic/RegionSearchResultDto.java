package com.kakao.ecotour.elastic;

import com.kakao.ecotour.exception.SearchResultNotExistException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class RegionSearchResultDto {

    private String region;  // Region Code

    private List<Program> programs;

    public static RegionSearchResultDto of(SearchResponse response) throws SearchResultNotExistException{

        SearchHit[] hits = response.getHits().getHits();

        try {
            String regionCode = (String) hits[0].getSourceAsMap().get("regionCode");

            List<Program> programs = Arrays.stream(hits)
                    .map(hit -> Program.of(hit.getSourceAsMap()))
                    .collect(Collectors.toList());

            return new RegionSearchResultDto(regionCode, programs);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new SearchResultNotExistException();
        }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Program {

        private String prgmName;   // 프로그램명

        private String theme;   // 테마

        static Program of(Map<String, Object> sourceMap) {
            return new Program((String) sourceMap.get("prgmName"), (String) sourceMap.get("theme"));
        }
    }
}
