package com.tenpo.test.dto.percentage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class PercentageDto implements Serializable {
	private Double value;
}
