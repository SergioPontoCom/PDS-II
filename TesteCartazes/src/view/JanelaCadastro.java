package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class JanelaCadastro extends JFrame {

	private static final long serialVersionUID = 1L;

	// Tamanho do espaço do poster (proporção 2:3, a mesma de um cartaz de cinema)
	private static final int POSTER_LARGURA = 200;
	private static final int POSTER_ALTURA = 300;

	private JPanel contentPane;

	private JTextField txtNome;
	private JTextField txtData_lancamento;
	private JTextField txtGenero;
	private JTextField txtIndicacao;
	private JTextField txtDuração;
	private JTextField txtInicio_Session;

	private JLabel lblPoster;

	// Lista de filmes ja cadastrados (so texto: o poster vem sob demanda)
	private JList<String> lista;
	private DefaultListModel<String> modeloLista;

	private JButton btnEscolherFoto;
	private JButton btnCadastrar;
	private JButton btnLimpar;
	private JButton btnFechar;

	// Cores do tema

	private Color preto = new Color(20, 20, 20);
	private Color amarelo = new Color(255, 193, 7);
	private Color amareloClaro = new Color(255, 215, 80);
	private Color branco = Color.WHITE;

	// Cores novas (apoio): fundo do poster e texto de "sem foto"
	private Color grafite = new Color(38, 38, 38);
	private Color cinzaTexto = new Color(150, 150, 150);

	public JanelaCadastro() {

		setTitle("Cadastro de Filmes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 720);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(15, 20, 15, 20));
		contentPane.setBackground(preto);

		setContentPane(contentPane);

		// Colunas: rótulo | campo | espaço | poster
		// Linhas: título | 6 campos | espaço flexível | lista | botões
		contentPane.setLayout(new MigLayout(
				"",
				"[140px][grow,fill][25px][" + POSTER_LARGURA + "px]",
				"[55px][46px][46px][46px][46px][46px][46px][grow][140px][40px]"
		));

		// =========================
		// TÍTULO
		// =========================

		JLabel lblTitulo = new JLabel("CADASTRO DE FILMES");

		lblTitulo.setForeground(amarelo);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));

		contentPane.add(
				lblTitulo,
				"cell 0 0 4 1,alignx center,aligny center"
		);

		// =========================
		// NOME
		// =========================

		adicionarRotulo("Nome", 1);

		txtNome = new JTextField();
		estilizarCampo(txtNome);
		contentPane.add(txtNome, "cell 1 1,growx,aligny center");

		// =========================
		// DATA DE LANÇAMENTO
		// =========================

		adicionarRotulo("Data (dd/mm/aaaa)", 2);

		txtData_lancamento = new JTextField();
		estilizarCampo(txtData_lancamento);
		txtData_lancamento.setToolTipText("Formato dd/mm/aaaa, por exemplo 25/12/2026");
		contentPane.add(txtData_lancamento, "cell 1 2,growx,aligny center");

		// =========================
		// GÊNERO
		// =========================

		adicionarRotulo("Gênero", 3);

		txtGenero = new JTextField();
		estilizarCampo(txtGenero);
		contentPane.add(txtGenero, "cell 1 3,growx,aligny center");

		// =========================
		// INDICAÇÃO ETÁRIA
		// =========================

		adicionarRotulo("Indicação (idade)", 4);

		txtIndicacao = new JTextField();
		estilizarCampo(txtIndicacao);
		txtIndicacao.setToolTipText("Número: 0 = livre, 10, 12, 14, 16 ou 18");
		contentPane.add(txtIndicacao, "cell 1 4,growx,aligny center");

		// =========================
		// DURAÇÃO
		// =========================

		adicionarRotulo("Duração", 5);

		txtDuração = new JTextField();
		estilizarCampo(txtDuração);
		contentPane.add(txtDuração, "cell 1 5,growx,aligny center");

		// =========================
		// INÍCIO DA SESSÃO
		// =========================

		adicionarRotulo("Início Sessão", 6);

		txtInicio_Session = new JTextField();
		estilizarCampo(txtInicio_Session);
		contentPane.add(txtInicio_Session, "cell 1 6,growx,aligny center");

		// =========================
		// POSTER (lado direito)
		// =========================

		lblPoster = new JLabel("Sem poster", SwingConstants.CENTER);
		lblPoster.setOpaque(true);
		lblPoster.setBackground(grafite);
		lblPoster.setForeground(cinzaTexto);
		lblPoster.setFont(new Font("Arial", Font.ITALIC, 14));

		// Moldura amarela grossa por fora + filete interno escuro,
		// lembrando a moldura de um cartaz exposto no cinema
		lblPoster.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(amarelo, 3),
						BorderFactory.createLineBorder(preto, 4)
				)
		);

		contentPane.add(
				lblPoster,
				"cell 3 1 1 6,w " + POSTER_LARGURA + "!,h " + POSTER_ALTURA + "!,aligny top"
		);

		btnEscolherFoto = new JButton("Escolher poster");
		estilizarBotao(btnEscolherFoto);
		contentPane.add(btnEscolherFoto, "cell 3 7,growx,aligny top,gaptop 10");

		// =========================
		// LISTA DE FILMES CADASTRADOS
		// =========================

		modeloLista = new DefaultListModel<>();
		lista = new JList<>(modeloLista);
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lista.setBackground(grafite);
		lista.setForeground(branco);
		lista.setSelectionBackground(amarelo);
		lista.setSelectionForeground(Color.BLACK);
		lista.setFont(new Font("Arial", Font.PLAIN, 13));

		JScrollPane scroll = new JScrollPane(lista);
		scroll.setBorder(BorderFactory.createLineBorder(amarelo, 1));
		scroll.getViewport().setBackground(grafite);

		contentPane.add(scroll, "cell 0 8 4 1,grow,gaptop 10");

		// =========================
		// BOTÕES (rodapé)
		// =========================

		JPanel painelBotoes = new JPanel(
				new MigLayout("insets 0", "[grow,fill][grow,fill][grow,fill]", "[grow,fill]")
		);
		painelBotoes.setOpaque(false);

		btnCadastrar = new JButton("Cadastrar");
		estilizarBotao(btnCadastrar);

		btnLimpar = new JButton("Limpar");
		estilizarBotao(btnLimpar);

		btnFechar = new JButton("Fechar");
		estilizarBotao(btnFechar);

		painelBotoes.add(btnCadastrar);
		painelBotoes.add(btnLimpar);
		painelBotoes.add(btnFechar);

		contentPane.add(painelBotoes, "cell 0 9 3 1,growx,aligny center");
	}

	// =====================================================
	// RÓTULO PADRÃO
	// =====================================================

	private void adicionarRotulo(String texto, int linha) {

		JLabel lbl = new JLabel(texto);
		lbl.setForeground(branco);
		lbl.setFont(new Font("Arial", Font.BOLD, 13));

		contentPane.add(lbl, "cell 0 " + linha + ",alignx left,aligny center");
	}

	// =====================================================
	// ESTILO DOS CAMPOS
	// =====================================================

	private void estilizarCampo(JTextField campo) {

		campo.setBackground(Color.WHITE);
		campo.setForeground(Color.BLACK);
		campo.setCaretColor(Color.BLACK);

		campo.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(amarelo, 1),
						BorderFactory.createEmptyBorder(3, 5, 3, 5)
				)
		);
	}

	// =====================================================
	// ESTILO DOS BOTÕES
	// =====================================================

	private void estilizarBotao(JButton botao) {

		botao.setBackground(amarelo);
		botao.setForeground(Color.BLACK);
		botao.setFont(new Font("Arial", Font.BOLD, 12));
		botao.setFocusPainted(false);

		botao.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(amareloClaro, 1),
						BorderFactory.createEmptyBorder(6, 10, 6, 10)
				)
		);
	}

	// =====================================================
	// GETTERS
	// =====================================================

	public JTextField getTxtNome() {
		return txtNome;
	}

	public JTextField getTxtData_de_lancamento() {
		return txtData_lancamento;
	}

	public JTextField getTxtGenero() {
		return txtGenero;
	}

	public JTextField getTxtIndicacao() {
		return txtIndicacao;
	}

	public JTextField getTxtDuração() {
		return txtDuração;
	}

	public JTextField getTxtInicio_Session() {
		return txtInicio_Session;
	}

	public JLabel getLblPoster() {
		return lblPoster;
	}

	public JList<String> getLista() {
		return lista;
	}

	public JButton getBtnEscolherFoto() {
		return btnEscolherFoto;
	}

	public JButton getBtnCadastrar() {
		return btnCadastrar;
	}

	public JButton getBtnLimpar() {
		return btnLimpar;
	}

	public JButton getBtnFechar() {
		return btnFechar;
	}

	// =====================================================
	// SETTERS
	// =====================================================

	public void setTxtIndicacao(JTextField txtIndicacao) {
		this.txtIndicacao = txtIndicacao;
	}

	public void setTxtDuração(JTextField txtDuração) {
		this.txtDuração = txtDuração;
	}

	public void setTxtInicio_Session(JTextField txtInicio_Session) {
		this.txtInicio_Session = txtInicio_Session;
	}

	public void setTxtGenero(JTextField txtGenero) {
		this.txtGenero = txtGenero;
	}

	// =====================================================
	// MÉTODOS DA VIEW
	// =====================================================

	public void mostrarMensagem(String texto) {
		JOptionPane.showMessageDialog(this, texto);
	}

	public void mostrarErro(String texto) {
		JOptionPane.showMessageDialog(
				this,
				texto,
				"Atenção",
				JOptionPane.WARNING_MESSAGE
		);
	}

	public void limparCampos() {

		txtNome.setText("");
		txtData_lancamento.setText("");
		txtGenero.setText("");
		txtIndicacao.setText("");
		txtDuração.setText("");
		txtInicio_Session.setText("");

		mostrarFoto(null);

		txtNome.requestFocus();
	}

	/** Troca o conteudo da lista de filmes cadastrados (so os textos). */
	public void mostrarLista(List<String> textos) {
		modeloLista.clear();
		for (String t : textos) {
			modeloLista.addElement(t);
		}
	}

	public void fechar() {
		dispose();
	}

	// =====================================================
	// POSTER
	// =====================================================

	/**
	 * Abre o seletor de arquivos e devolve a imagem escolhida,
	 * ou null se o usuário cancelar.
	 */
	public File escolherArquivo() {

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Escolher poster");
		// Sem o filtro, o usuario escolhe um .docx e so descobre o erro
		// depois. Melhor nao deixar chegar la.
		chooser.setFileFilter(new FileNameExtensionFilter(
				"Imagens (jpg, jpeg, png)", "jpg", "jpeg", "png"));

		int opcao = chooser.showOpenDialog(this);
		if (opcao == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		return null;
	}

	/**
	 * Mostra a imagem no espaço do poster, mantendo a proporção.
	 * Com null, volta para o estado "Sem poster".
	 */
	public void mostrarFoto(BufferedImage img) {

		if (img == null) {
			lblPoster.setIcon(null);
			lblPoster.setText("Sem poster");
			return;
		}

		// Área útil dentro das bordas (3 + 4 de cada lado)
		int maxLargura = POSTER_LARGURA - 14;
		int maxAltura = POSTER_ALTURA - 14;

		double escala = Math.min(
				(double) maxLargura / img.getWidth(),
				(double) maxAltura / img.getHeight()
		);

		int novaLargura = Math.max(1, (int) Math.round(img.getWidth() * escala));
		int novaAltura = Math.max(1, (int) Math.round(img.getHeight() * escala));

		Image reduzida = img.getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);

		lblPoster.setText("");
		lblPoster.setIcon(new ImageIcon(reduzida));
	}
}
