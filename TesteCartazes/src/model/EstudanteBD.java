package model;

import java.util.ArrayList;
import java.util.List;

/**
 * VERSAO DE TESTE do EstudanteBD: guarda tudo na MEMORIA, sem MySQL.
 *
 * Usa as mesmas regras (Regras.java) e tem os mesmos metodos da versao real (salvar, existeData_de_lancamento,
 * contar, listarTodos, buscarFoto), entao View e Controller sao os MESMOS
 * do projeto de verdade. Ao fechar o programa, os dados somem.
 *
 * Para usar no projeto real, NAO copie este arquivo: copie o EstudanteBD.java
 * que usa o MySQL.
 */
public class EstudanteBD {

	private final List<Estudante> registros = new ArrayList<>();
	private int proximoId = 1;

	public void salvar(Estudante e) {
		// Mesmas regras da versao MySQL (Regras.java).
		Regras.validar(e);
		if (existeData_de_lancamento(e.getData_de_lancamento())) {
			throw new IllegalArgumentException("Data ja cadastrada.");
		}

		// Guarda uma COPIA, como o banco faria.
		Estudante c = new Estudante();
		c.setId(proximoId++);
		c.setNome_filme(e.getNome_filme().trim());
		c.setData_de_lancamento(e.getData_de_lancamento().trim());
		c.setGenero(e.getGenero().trim());
		c.setIndicacao_ET(e.getIndicacao_ET().trim());
		c.setDuração(e.getDuração().trim());
		c.setInicio_Session(e.getInicio_Session().trim());
		c.setFoto(e.temFoto() ? e.getFoto() : null); // sem foto = null
		registros.add(c);
	}

	public boolean existeData_de_lancamento(String data) {
		for (Estudante r : registros) {
			if (Regras.lerData(r.getData_de_lancamento()).equals(Regras.lerData(data))) {
				return true;
			}
		}
		return false;
	}

	public int contar() {
		return registros.size();
	}

	/** Lista SEM a foto, igual ao banco real. */
	public List<Estudante> listarTodos() {
		List<Estudante> lista = new ArrayList<>();
		for (Estudante r : registros) {
			Estudante e = new Estudante();
			e.setId(r.getId());
			e.setNome_filme(r.getNome_filme());
			e.setData_de_lancamento(r.getData_de_lancamento());
			e.setGenero(r.getGenero());
			e.setIndicacao_ET(r.getIndicacao_ET());
			e.setDuração(r.getDuração());
			e.setInicio_Session(r.getInicio_Session());
			lista.add(e); // foto fica null de proposito
		}
		lista.sort((a, b) -> a.getNome_filme().compareToIgnoreCase(b.getNome_filme()));
		return lista;
	}

	public byte[] buscarFoto(int id) {
		for (Estudante r : registros) {
			if (r.getId() == id) {
				return r.getFoto();
			}
		}
		return null;
	}
}
