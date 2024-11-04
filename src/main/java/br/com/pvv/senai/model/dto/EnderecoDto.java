package br.com.pvv.senai.model.dto;

import br.com.pvv.senai.entity.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;

public class EnderecoDto extends GenericDto<Endereco> {

	@Schema(description = "Identificador único do endereço", example = "12345")
    private long Id;
    @Schema(description = "Código de Endereçamento Postal", example = "12345-678")
    private String CEP;
    @Schema(description = "Nome da cidade", example = "São Paulo")
    private String cidade;
    @Schema(description = "Nome da rua ou avenida", example = "Avenida Paulista")
    private String logradouro;
    @Schema(description = "Número do imóvel", example = "1000")
    private int numero;
    @Schema(description = "Complemento do endereço", example = "Apto 101")
    private String complemento;
    @Schema(description = "Nome do bairro", example = "Bela Vista")
    private String bairro;
	@Schema(description = "Estado do endereço", example = "SP")
	private String estado;
    @Schema(description = "Ponto de referência próximo ao endereço", example = "Próximo ao MASP")
    private String pontoDeReferencia;
	

	@Override
	protected Class<Endereco> getType() {
		return Endereco.class;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String getCEP() {
		return CEP;
	}

	public void setCEP(String cEP) {
		CEP = cEP;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPontoDeReferencia() {
		return pontoDeReferencia;
	}

	public void setPontoDeReferencia(String pontoDeReferencia) {
		this.pontoDeReferencia = pontoDeReferencia;
	}


}
