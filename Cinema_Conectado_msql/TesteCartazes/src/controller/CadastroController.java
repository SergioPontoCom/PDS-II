package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Estudante;
import model.EstudanteBD;
import model.Imagens;
import view.JanelaCadastro;

/**
 * CONTROLLER - faz a ponte entre a View e o Model.
 *
 * Um unico actionPerformed descobre qual botao foi clicado.
 * Para as fotos:
 *   - a View abre o JFileChooser e exibe a imagem;
 *   - o Model (Imagens) le, reduz e converte;
 *   - o Controller so liga uma coisa na outra.
 */
public class CadastroController implements ActionListener, ListSelectionListener {

	private EstudanteBD bd;
	private JanelaCadastro view;

	// Poster ja reduzido (bytes JPEG), guardado enquanto o usuario ainda nao
	// clicou em Cadastrar: faz parte do "rascunho" da tela.
	private byte[] fotoEscolhida;

	// Filmes que estao na lista da tela, na mesma ordem. Serve para
	// descobrir o id do filme selecionado (a lista da View so tem texto).
	private List<Estudante> emExibicao = new ArrayList<>();

	public CadastroController(EstudanteBD bd, JanelaCadastro view) {
		this.bd = bd;
		this.view = view;
		this.view.getBtnCadastrar().addActionListener(this);
		this.view.getBtnLimpar().addActionListener(this);
		this.view.getBtnFechar().addActionListener(this);
		this.view.getBtnEscolherFoto().addActionListener(this);
		this.view.getLista().addListSelectionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.view.getBtnCadastrar()) {
			cadastrar();
		} else if (e.getSource() == this.view.getBtnLimpar()) {
			this.fotoEscolhida = null;
			this.view.limparCampos();
		} else if (e.getSource() == this.view.getBtnFechar()) {
			this.view.fechar();
		} else if (e.getSource() == this.view.getBtnEscolherFoto()) {
			escolherFoto();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// A selecao dispara dois eventos (o "durante" e o "final"); so o
		// final interessa.
		if (!e.getValueIsAdjusting()) {
			mostrarSelecionado();
		}
	}

	private void cadastrar() {
		// 1. pede os dados a View
		Estudante estudante = new Estudante();
		estudante.setNome_filme(this.view.getTxtNome().getText());
		estudante.setData_de_lancamento(this.view.getTxtData_de_lancamento().getText());
		estudante.setGenero(this.view.getTxtGenero().getText());
		estudante.setIndicacao_ET(this.view.getTxtIndicacao().getText());
		estudante.setDuração(this.view.getTxtDuração().getText());
		estudante.setInicio_Session(this.view.getTxtInicio_Session().getText());
		estudante.setFoto(this.fotoEscolhida); // null se nao escolheu poster

		// 2. entrega ao Model, que aplica as regras e grava
		try {
			this.bd.salvar(estudante);
		} catch (IllegalArgumentException erro) {
			this.view.mostrarErro(erro.getMessage());
			return;
		} catch (RuntimeException erro) {
			// erro de banco (conexao, coluna inexistente, pacote grande...)
			this.view.mostrarErro(erro.getMessage());
			return;
		}

		// 3. deu certo
		this.view.mostrarMensagem("Cadastrado. Total no banco: " + this.bd.contar());
		this.fotoEscolhida = null;
		this.view.limparCampos(); // tambem limpa o poster da moldura
		atualizarLista();
	}

	/** Pede o arquivo a View, manda o Model reduzir, e mostra o resultado. */
	private void escolherFoto() {
		File arquivo = this.view.escolherArquivo();
		if (arquivo == null) {
			return; // o usuario cancelou: nao e erro
		}
		try {
			byte[] bytes = Imagens.lerReduzida(arquivo); // reduz e vira JPEG
			BufferedImage img = Imagens.deBytes(bytes); // o que a tela mostra
			this.fotoEscolhida = bytes; // so vira registro ao cadastrar
			this.view.mostrarFoto(img);
		} catch (IOException erro) {
			this.view.mostrarErro("Nao foi possivel ler a imagem: " + erro.getMessage());
		}
	}

	/** Busca o poster do filme selecionado na lista, so desse filme. */
	private void mostrarSelecionado() {
		int linha = this.view.getLista().getSelectedIndex();
		if (linha < 0 || linha >= this.emExibicao.size()) {
			return;
		}
		Estudante filme = this.emExibicao.get(linha);
		try {
			byte[] bytes = this.bd.buscarFoto(filme.getId());
			BufferedImage img = Imagens.deBytes(bytes); // null se nao tem poster
			this.view.mostrarFoto(img);
		} catch (IOException | RuntimeException erro) {
			this.view.mostrarErro("Nao foi possivel carregar o poster: " + erro.getMessage());
		}
	}

	/** Recarrega a lista de filmes (sem os posters). */
	private void atualizarLista() {
		this.emExibicao = this.bd.listarTodos();
		List<String> textos = new ArrayList<>();
		for (Estudante f : this.emExibicao) {
			textos.add(f.getNome_filme() + "  (" + f.getData_de_lancamento() + ")");
		}
		this.view.mostrarLista(textos);
	}

	public void iniciarTela() {
		try {
			atualizarLista();
		} catch (RuntimeException erro) {
			this.view.mostrarErro(erro.getMessage());
		}
		this.view.setVisible(true);
	}
}
