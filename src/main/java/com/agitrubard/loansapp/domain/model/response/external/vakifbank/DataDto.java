package com.agitrubard.loansapp.domain.model.response.external.vakifbank;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataDto {

    private List<CurrencyDto> currency;
}