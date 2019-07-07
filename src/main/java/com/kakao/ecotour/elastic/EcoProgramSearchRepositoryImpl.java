package com.kakao.ecotour.elastic;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EcoProgramSearchRepositoryImpl implements EcoProgramSearchRepository {

    private final Client client;

    public EcoProgramSearchRepositoryImpl(Client client) {
        this.client = client;
    }

    @Override
    public List<EcoProgramDto> findByRegionCode(String regionCode) {

        SearchResponse response = client.prepareSearch()
                .setQuery(QueryBuilders.matchQuery("regionCode", regionCode))
                .execute()
                .actionGet();

        return Arrays.stream(response.getHits().getHits())
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), EcoProgramDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RegionSearchResultDto findByRegionName(String regionName) {
        SearchResponse response = client.prepareSearch()
                .setQuery(QueryBuilders.matchQuery("region", regionName))
                .execute()
                .actionGet();

        return RegionSearchResultDto.of(response);
    }

    @Override
    public KeywordSearchRegionCountResultDto findRegionCountByProgramInfoKeyword(String keyword) {
        SearchResponse response = client.prepareSearch()
                .setQuery(QueryBuilders.matchQuery("prgmInfo", keyword))
                .addAggregation(AggregationBuilders.terms("by_regionName").field("regionName"))
                .setSize(0)
                .execute()
                .actionGet();

        return KeywordSearchRegionCountResultDto.of(keyword, response);
    }

    @Override
    public KeywordFrequencyResultDto findFrequencyByProgramDetailInfoKeyword(String keyword) {
        SearchResponse response = client.prepareSearch()
                .setQuery(QueryBuilders.matchQuery("prgmDetailInfo", keyword))
                .setSize(0)
                .execute()
                .actionGet();

        return KeywordFrequencyResultDto.of(keyword, response);
    }
}