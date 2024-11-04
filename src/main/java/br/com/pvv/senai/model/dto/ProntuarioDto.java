package br.com.pvv.senai.model.dto;

import java.util.List;

import br.com.pvv.senai.entity.Consulta;
import br.com.pvv.senai.entity.Exame;
import br.com.pvv.senai.entity.Paciente;
import io.swagger.v3.oas.annotations.media.Schema;

public class ProntuarioDto {
	@Schema(description = "Informações do paciente", 
            example = "{ \"nome\": \"João Silva\", \"idade\": 45, \"sexo\": \"Masculino\" }")
    private Paciente paciente;

    @Schema(description = "Lista de exames realizados pelo paciente", 
            example = "[{ \"tipo\": \"Sangue\", \"data\": \"2024-10-31\", \"resultado\": \"Normal\" }]")
    private List<Exame> exames;

    @Schema(description = "Lista de consultas realizadas pelo paciente", 
            example = "[{ \"data\": \"2024-10-31\", \"medico\": \"Dr. Ana Souza\", \"diagnostico\": \"Hipertensão\" }]")
    private List<Consulta> consultas;


    public ProntuarioDto(Paciente paciente, List<Exame> exames, List<Consulta> consultas) {
        this.paciente = paciente;
        this.exames = exames;
        this.consultas = consultas;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public List<Exame> getExames() {
        return exames;
    }

    public void setExames(List<Exame> exames) {
        this.exames = exames;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}
