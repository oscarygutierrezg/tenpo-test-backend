package com.tempo.test.dto.history;

import com.tempo.test.model.CalledHistory;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface CalledHistoryMapper {
	CalledHistoryDto toDto(CalledHistory calledHistory);
	CalledHistory toModel(CalledHistoryDto calledHistory);
}
