package com.agitrubard.loansapp.domain.model.response.external.yapikredi;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseDto {

    private List<ExchangeRateDto> exchangeRateList;
}