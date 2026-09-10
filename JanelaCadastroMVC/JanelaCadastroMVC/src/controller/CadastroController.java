package controller;

import model.Estudante;
import model.EstudanteBD;
import view.JanelaCadastro;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * CONTROLLER - e aqui que volta a morar o codigo que estava dentro dos tres
 * addActionListener da janela antiga.
 *
 * Antes havia TRES ouvintes anonimos, um por botao. Agora ha UM
 * actionPerformed, e ele descobre qual botao foi clicado.
 */
public class CadastroController implements ActionListener {
	private EstudanteBD bd;
	private JanelaCadastro view;

	public CadastroController(EstudanteBD bd, JanelaCadastro view) {
		this.bd = bd;
		this.view = view;
		// O "this" e o proprio Controller se registrando como ouvinte.
		// Pegue a view, peca o botao, registre um ouvinte, e o ouvinte
		// e este objeto aqui.
		this.view.getBtnCadastrar().addActionListener(this);
		this.view.getBtnLimpar().addActionListener(this);
		this.view.getBtnFechar().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// e.getSource() devolve o componente que originou o evento.
		// == e nao equals: a pergunta e "e exatamente aquele objeto?".
		if (e.getSource() == this.view.getBtnCadastrar()) {
			cadastrar();
		} else if (e.getSource() == this.view.getBtnLimpar()) {

			this.view.limparCampos();

		} else if (e.getSource() == this.view.getBtnFechar()) {

			this.view.fechar();

		}
	}

	private void cadastrar() {
		// 1. pede os dados a View
		Estudante estudante = new Estudante();
		estudante.setNome(this.view.getTxtNome().getText());
		estudante.setMatricula(this.view.getTxtMatricula().getText());
		estudante.setTelefone(this.view.getTxtTelefone().getText());
		// 2. entrega ao Model, que aplica as regras e grava
		try {
			this.bd.salvar(estudante);

		} catch (IllegalArgumentException erro) {

			// Repassa a mensagem sem saber qual regra falhou.
			this.view.mostrarErro(erro.getMessage());
			return; // sem o return, a mensagem de sucesso viria em seguida

		}
		// 3. deu certo
		this.view.mostrarMensagem("Cadastrado. Total no banco: " + this.bd.contar());
		this.view.limparCampos();
	}

	public void iniciarTela() {
		this.view.setVisible(true);
	}
}