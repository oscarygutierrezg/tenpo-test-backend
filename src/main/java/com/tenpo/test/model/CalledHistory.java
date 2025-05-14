package com.tenpo.test.model;

import com.tenpo.test.model.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "called_history")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CalledHistory {

	@Id
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
	@Column(length = 36, nullable = false, updatable = false)
	private String id;
	@Column(name = "parameters")
	private String parameters;
	@Column(name = "endpoint")
	private String endpoint;
	@Column(name = "response")
	private String response;
	@Enumerated(EnumType.STRING)
	private Status status;
	@Column(name = "date")
	private LocalDateTime date;
}
