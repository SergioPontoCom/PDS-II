package model;

public class Estudante {

	private int id;
	private String nome_filme, data_de_lancamento, genero, indicacao_ET, duração, inicio_Session;
	private byte[] foto; // poster do filme; pode ser null

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome_filme() {
		return nome_filme;
	}

	public void setNome_filme(String nome_filme) {
		this.nome_filme = nome_filme;
	}

	public String getData_de_lancamento() {
		return data_de_lancamento;
	}

	public void setData_de_lancamento(String data_de_lancamento) {
		this.data_de_lancamento = data_de_lancamento;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getIndicacao_ET() {
		return indicacao_ET;
	}

	public void setIndicacao_ET(String indicacao_ET) {
		this.indicacao_ET = indicacao_ET;
	}

	public String getDuração() {
		return duração;
	}

	public void setDuração(String duração) {
		this.duração = duração;
	}

	public String getInicio_Session() {
		return inicio_Session;
	}

	public void setInicio_Session(String inicio_Session) {
		this.inicio_Session = inicio_Session;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	/** Util para a tela decidir se mostra o poster ou o aviso de vazio. */
	public boolean temFoto() {
		return foto != null && foto.length > 0;
	}

}
