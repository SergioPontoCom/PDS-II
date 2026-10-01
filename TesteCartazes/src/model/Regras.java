package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Regras de validacao e conversao dos dados do filme, alinhadas com a
 * tabela Cartazes:
 *
 *   nome_filme          VARCHAR(60) NOT NULL
 *   data_de_lancamento  DATE        NOT NULL   (tela: dd/mm/aaaa)
 *   genero              VARCHAR(60) NOT NULL
 *   indicacao_ET        INT         NOT NULL   (tela: numero, 0 = livre)
 *   duração             VARCHAR(10) NOT NULL
 *   inicio_Session      VARCHAR(15) NOT NULL
 *
 * Nao usa Swing nem SQL: so valida e converte.
 */
public class Regras {

	/** Formato que o usuario digita. "uuuu" + STRICT rejeita 31/02/2026. */
	public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter
			.ofPattern("dd/MM/uuuu")
			.withResolverStyle(ResolverStyle.STRICT);

	private Regras() {
	}

	/** Lanca IllegalArgumentException com mensagem clara se algo estiver errado. */
	public static void validar(Estudante e) {
		exigir(e.getNome_filme(), "o nome", 60);
		lerData(e.getData_de_lancamento());
		exigir(e.getGenero(), "o gênero", 60);
		lerIndicacao(e.getIndicacao_ET());
		exigir(e.getDuração(), "a duração", 10);
		exigir(e.getInicio_Session(), "o início da sessão", 15);
	}

	private static void exigir(String valor, String descricao, int maximo) {
		if (valor == null || valor.trim().isEmpty()) {
			throw new IllegalArgumentException("Preencha " + descricao + ".");
		}
		if (valor.trim().length() > maximo) {
			throw new IllegalArgumentException(
					"Use no máximo " + maximo + " caracteres em " + descricao + ".");
		}
	}

	/** "25/12/2026" -> LocalDate. A coluna do banco e DATE, nao texto. */
	public static LocalDate lerData(String texto) {
		if (texto == null || texto.trim().isEmpty()) {
			throw new IllegalArgumentException("Preencha a Data.");
		}
		try {
			return LocalDate.parse(texto.trim(), FORMATO_DATA);
		} catch (DateTimeParseException erro) {
			throw new IllegalArgumentException("Data inválida. Use dd/mm/aaaa, por exemplo 25/12/2026.");
		}
	}

	/** LocalDate -> "25/12/2026", para mostrar na tela. */
	public static String formatarData(LocalDate data) {
		return data == null ? "" : data.format(FORMATO_DATA);
	}

	/** "14" -> 14. A coluna do banco e INT; "Livre" ou "L" nao cabem nela. */
	public static int lerIndicacao(String texto) {
		if (texto == null || texto.trim().isEmpty()) {
			throw new IllegalArgumentException("Preencha a indicação etária.");
		}
		int valor;
		try {
			valor = Integer.parseInt(texto.trim());
		} catch (NumberFormatException erro) {
			throw new IllegalArgumentException("Indicação etária deve ser um número (0 para livre, 10, 12, 14, 16, 18).");
		}
		if (valor < 0 || valor > 18) {
			throw new IllegalArgumentException("Indicação etária deve ficar entre 0 e 18.");
		}
		return valor;
	}
}
