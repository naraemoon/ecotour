package com.kakao.ecotour.kakaoapi;

import com.kakao.ecotour.exception.ApiNotFoundAddressException;
import com.kakao.ecotour.jpa.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@PropertySource("classpath:/kakaoapi.properties")
public class SearchAddressService {

    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${kakao.api.url}")
    private String apiUrl;

    // Region 반환
    public Region getRegion(String address) {

        String[] addressArr = address.split(" ");
        String regionName = addressArr.length < 2 ? addressArr[0] : addressArr[0] + " " + addressArr[1];

        ResponseVO response = getResponse(regionName);

        try {
            return response.getRegion()
                    .orElseGet(() -> getResponse(addressArr[0]).getRegion()
                            .orElseThrow(ApiNotFoundAddressException::new));
        } catch (ApiNotFoundAddressException e) {
            log.debug(e.getMessage());
            return new Region("reg" + address.hashCode(), address);
        }

    }

    // REST API 요청
    private ResponseVO getResponse(String regionName) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ResponseVO> response
                = restTemplate.exchange(apiUrl + regionName, HttpMethod.GET, new HttpEntity<>(getHttpHeaders()), ResponseVO.class);
        return response.getBody();
    }

    // Header
    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", apiKey);
        return httpHeaders;
    }


}
