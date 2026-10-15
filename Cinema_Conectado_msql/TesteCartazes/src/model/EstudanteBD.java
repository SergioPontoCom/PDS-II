package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * MODEL - grava e le no MySQL (banco Cinema) e aplica as regras (Regras.java).
 * Nao ha javax.swing aqui, entao da para testar sem abrir tela.
 */
public class EstudanteBD {

	private static final String URL = "jdbc:mysql://localhost:3306/Cinema";
	private static final String USER = "aluno_cd";
	private static final String SENHA = "aluno_pw";

	// Conexao NOVA a cada chamada.
	private Connection abrir() throws SQLException {
		return DriverManager.getConnection(URL, USER, SENHA);
	}

	public void salvar(Estudante e) {
		// 1. regras do proprio registro (Regras.java)
		Regras.validar(e);

		// 2. regra que depende dos OUTROS registros
		if (existeData_de_lancamento(e.getData_de_lancamento())) {
			throw new IllegalArgumentException("Data ja cadastrada.");
		}

		String sql = "INSERT INTO Cartazes (nome_filme, data_de_lancamento, genero, "
				+ "indicacao_ET, `duração`, inicio_Session, foto) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, e.getNome_filme().trim());
			ps.setDate(2, java.sql.Date.valueOf(Regras.lerData(e.getData_de_lancamento())));
			ps.setString(3, e.getGenero().trim());
			ps.setInt(4, Regras.lerIndicacao(e.getIndicacao_ET()));
			ps.setString(5, e.getDuração().trim());
			ps.setString(6, e.getInicio_Session().trim());

			if (e.temFoto()) {
				ps.setBytes(7, e.getFoto());
			} else {
				ps.setNull(7, Types.BLOB);
			}
			ps.executeUpdate();

		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao gravar: " + erro.getMessage(), erro);
		}
	}

	public boolean existeData_de_lancamento(String data) {
		String sql = "SELECT id FROM Cartazes WHERE data_de_lancamento = ?";
		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setDate(1, java.sql.Date.valueOf(Regras.lerData(data)));
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao consultar: " + erro.getMessage(), erro);
		}
	}

	public int contar() {
		String sql = "SELECT COUNT(*) FROM Cartazes";
		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			return rs.next() ? rs.getInt(1) : 0;

		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao contar: " + erro.getMessage(), erro);
		}
	}

	/** Lista SEM a foto (use buscarFoto(id) quando precisar). */
	public List<Estudante> listarTodos() {
		String sql = "SELECT id, nome_filme, data_de_lancamento, genero, indicacao_ET, "
				+ "`duração`, inicio_Session FROM Cartazes ORDER BY nome_filme";
		List<Estudante> lista = new ArrayList<>();

		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Estudante e = new Estudante();
				e.setId(rs.getInt("id"));
				e.setNome_filme(rs.getString("nome_filme"));
				// banco devolve yyyy-MM-dd; a tela usa dd/MM/aaaa
				e.setData_de_lancamento(
						Regras.formatarData(rs.getDate("data_de_lancamento").toLocalDate()));
				e.setGenero(rs.getString("genero"));
				e.setIndicacao_ET(rs.getString("indicacao_ET"));
				e.setDuração(rs.getString("duração"));
				e.setInicio_Session(rs.getString("inicio_Session"));
				lista.add(e); // foto fica null de proposito
			}
		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao listar: " + erro.getMessage(), erro);
		}
		return lista;
	}

	public byte[] buscarFoto(int id) {
		String sql = "SELECT foto FROM Cartazes WHERE id = ?";
		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getBytes("foto") : null;
			}
		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao buscar foto: " + erro.getMessage(), erro);
		}
	}

	/** Teste de ambiente: rode esta classe antes de depurar qualquer botao. */
	public static void main(String[] args) {
		EstudanteBD bd = new EstudanteBD();
		try (Connection con = bd.abrir()) {
			System.out.println("Conexao OK com " + con.getCatalog());
			System.out.println("Cadastros no banco: " + bd.contar());
		} catch (SQLException e) {
			System.out.println("Falha: " + e.getMessage());
		}
	}
}