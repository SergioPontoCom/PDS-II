package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * MODEL - grava e le no MySQL, e aplica as regras.
 *
 * Todo o SQL do sistema esta aqui. Procure "javax.swing" neste arquivo: nao ha
 * nenhum, e e isso que permite testar esta classe sem abrir tela.
 */
public class EstudanteBD {
	// Os tres dados da conexao ficam num lugar so. Mudou o servidor,
	// muda uma linha - nao quatro.
	private static final String URL = "jdbc:mysql://localhost:3306/escola";
	private static final String USER = "aluno_cd";
	private static final String SENHA = "aluno_pw";

	// Conexao NOVA a cada chamada. Guardar uma unica conexao num atributo
	// parece economico, mas o servidor a derruba por inatividade e a
	// aplicacao inteira para de funcionar ate ser reiniciada.
	private Connection abrir() throws SQLException {
		return DriverManager.getConnection(URL, USER, SENHA);
	}

	public void salvar(Estudante e) {
		// ---- REGRAS: sempre antes de gravar --------------------------
		if (e.getNome().trim().isEmpty()) {
			throw new IllegalArgumentException("Preencha o nome.");

		}
		if (e.getMatricula().trim().isEmpty()) {
			throw new IllegalArgumentException("Preencha a matricula.");

		}
		if (existeMatricula(e.getMatricula())) {
			throw new IllegalArgumentException("Matricula ja cadastrada.");

		}
		String sql = "INSERT INTO cadastro (nome, matricula, telefone) VALUES (?, ?, ?)";
		// try-with-resources: conexao e comando sao fechados sozinhos,
		// mesmo se der excecao no meio.
		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql)) {

			// Indices comecam em 1, nunca em 0.
			// O valor viaja SEPARADO do comando: por isso um nome como
			// O'Brien grava sem quebrar o SQL.
			ps.setString(1, e.getNome().trim());
			ps.setString(2, e.getMatricula().trim());
			ps.setString(3, e.getTelefone().trim());
			ps.executeUpdate();
		} catch (SQLException erro) {

			throw new RuntimeException("Erro ao gravar: " + erro.getMessage(), erro);

		}
	}
	public boolean existeMatricula(String matricula) {
		String sql = "SELECT id FROM cadastro WHERE matricula = ?";
		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, matricula.trim());
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next(); // achou pelo menos uma linha?

			}
		} catch (SQLException erro) {

			throw new RuntimeException("Erro ao consultar: " + erro.getMessage(), erro);

		}
	}
	public int contar() {
		String sql = "SELECT COUNT(*) FROM cadastro";
		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1); // primeira coluna do resultado

			}
			return 0;

		} catch (SQLException erro) {

			throw new RuntimeException("Erro ao contar: " + erro.getMessage(), erro);

		}
	}
	public List<Estudante> listarTodos() {
		String sql = "SELECT nome, matricula, telefone FROM cadastro ORDER BY nome";
		List<Estudante> lista = new ArrayList<>();
		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				Estudante e = new Estudante();
				e.setNome(rs.getString("nome"));
				e.setMatricula(rs.getString("matricula"));
				e.setTelefone(rs.getString("telefone"));
				lista.add(e);
			}
		} catch (SQLException erro) {

			throw new RuntimeException("Erro ao listar: " + erro.getMessage(), erro);

		}
		// Devolve objetos, e nao o ResultSet: ele morre quando a conexao fecha.
		return lista;
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