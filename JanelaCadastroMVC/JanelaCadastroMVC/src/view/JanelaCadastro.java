package view;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
/**
 * VIEW - a MESMA janela de antes, nas mesmas posicoes.
 *
 * O que saiu daqui:
 * - os tres addActionListener (quem escuta agora e o Controller);
 * - o JOptionPane que montava a mensagem (quem decide o texto e o Controller).
 *
 * O que entrou:
 * - getters dos TRES BOTOES (antes so os campos tinham);
 * - metodos mostrarMensagem, mostrarErro, limparCampos e fechar.
 *
 * Confira com Ctrl+F: nao ha "addActionListener" e nao ha "if" nesta classe.
 */
public class JanelaCadastro extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtMatricula;
	private JTextField txtTelefone;
	// NOVO: os botoes viraram atributos. Antes eram variaveis locais do
	// construtor, porque so o proprio construtor precisava deles para
	// pendurar o ouvinte. Agora quem pendura o ouvinte e o Controller,
	// e para isso ele precisa alcanca-los.
	private JButton btnCadastrar;
	private JButton btnLimpar;
	private JButton btnFechar;
	public JanelaCadastro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(10, 21, 77, 14);
		contentPane.add(lblNome);
		txtNome = new JTextField();
		txtNome.setBounds(110, 18, 96, 20);
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		// 105 e nao 88: com 88 o texto aparece cortado como "Cada...".
		btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.setBounds(238, 17, 105, 22);
		contentPane.add(btnCadastrar);
		JLabel lblMatricula = new JLabel("Matricula");
		lblMatricula.setBounds(10, 60, 76, 14);
		contentPane.add(lblMatricula);
		txtMatricula = new JTextField();
		txtMatricula.setBounds(110, 57, 96, 20);
		contentPane.add(txtMatricula);
		txtMatricula.setColumns(10);
		btnLimpar = new JButton("Limpar");
		btnLimpar.setBounds(238, 56, 105, 22);
		contentPane.add(btnLimpar);
		JLabel lblTelefone = new JLabel("Telefone");
		lblTelefone.setBounds(10, 100, 77, 14);
		contentPane.add(lblTelefone);
		txtTelefone = new JTextField();
		txtTelefone.setBounds(110, 97, 96, 20);
		contentPane.add(txtTelefone);
		txtTelefone.setColumns(10);
		btnFechar = new JButton("Fechar");
		btnFechar.setBounds(238, 96, 105, 22);
		contentPane.add(btnFechar);
	}
	// ---- Grupo 1: entregar componentes ao Controller ------------------
	public JTextField getTxtNome() {
		return txtNome;
	}
	// ATENCAO: na versao antiga este metodo chamava-se getTextMatricula,
	// sem o "tx". Padronizado para getTxtMatricula, igual aos outros dois.
	public JTextField getTxtMatricula() {
		return txtMatricula;
	}
	public JTextField getTxtTelefone() {
		return txtTelefone;
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
	// ---- Grupo 2: capacidades da propria tela -------------------------
	// Recebem o texto PRONTO. A View exibe; nao escolhe o que dizer.
	public void mostrarMensagem(String texto) {
		JOptionPane.showMessageDialog(this, texto);
	}
	public void mostrarErro(String texto) {
		JOptionPane.showMessageDialog(this, texto, "Atencao",
				JOptionPane.WARNING_MESSAGE);
	}
	public void limparCampos() {
		txtNome.setText("");
		txtMatricula.setText("");
		txtTelefone.setText("");
		txtNome.requestFocus();
	}
	public void fechar() {
		dispose();
	}
}
