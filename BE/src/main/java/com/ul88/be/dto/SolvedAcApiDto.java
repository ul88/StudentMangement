package com.ul88.be.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolvedAcApiDto {
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("items")
    private List<SolvedAcDto> items;
}
