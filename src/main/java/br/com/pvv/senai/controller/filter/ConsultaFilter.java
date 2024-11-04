package br.com.pvv.senai.controller.filter;

import br.com.pvv.senai.entity.Consulta;
import br.com.pvv.senai.entity.Paciente;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public class ConsultaFilter implements IFilter<Consulta> {

	private String reason;
	private int pageSize;
	private int pageNumber;
	private Long patientId;

	public ConsultaFilter(Map<String, String> params) {
		setReason(params.get("reason"));
		setPageNumber(params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber")) : 0);
		setPageSize(params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize")) : 10);
		if (params.get("patientId") != null) {
			setPatientId(Long.parseLong(params.get("patientId")));
		}
	}

	@Override
	public Example<Consulta> example() {

		var matcher = ExampleMatcher.matchingAny().withMatcher("reason", match -> match.ignoreCase().contains())
				.withIgnoreNullValues();

		var probe = new Consulta();
		probe.setReason(getReason());

		if (getPatientId() != null) {
			var paciente = new Paciente();
			paciente.setId(getPatientId());
			probe.setPatient(paciente);
		}

		return Example.of(probe, matcher);
	}

	@Override
	public Pageable getPagination() {
		return PageRequest.of(getPageNumber(), getPageSize());
	}

	public final String getReason() {
		return reason;
	}

	public final void setReason(String reason) {
		this.reason = reason;
	}

	public final int getPageSize() {
		return pageSize;
	}

	public final void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public final int getPageNumber() {
		return pageNumber;
	}

	public final void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Long getPatientId() { return patientId; }

	public void setPatientId(Long patientId) { this.patientId = patientId; }
}
