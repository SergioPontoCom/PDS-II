package model;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Utilitario de imagens: le do disco, redimensiona e converte para bytes.
 *
 * Fica no model porque nao desenha nada na tela - so TRANSFORMA dados.
 * Nao ha import de javax.swing aqui, so java.awt.image e javax.imageio.
 *
 * ATENCAO: o arquivo precisa se chamar Imagens.java (com I maiusculo),
 * porque a classe publica e Imagens.
 */
public class Imagens {

	/** Lado maximo da imagem que vai para o banco. */
	public static final int LADO_MAXIMO = 600;

	private Imagens() {
	}

	/** Le o arquivo escolhido pelo usuario, reduz e devolve os bytes JPEG. */
	public static byte[] lerReduzida(File arquivo) throws IOException {

		BufferedImage original = ImageIO.read(arquivo);

		// ImageIO.read devolve null (e NAO lanca excecao) quando o arquivo
		// nao e uma imagem que ele saiba ler.
		if (original == null) {
			throw new IOException("Formato de imagem nao suportado: " + arquivo.getName());
		}

		BufferedImage menor = reduzir(original, LADO_MAXIMO);
		return paraJpeg(menor);
	}

	/** Reduz mantendo a PROPORCAO (escala calculada sobre o maior lado). */
	public static BufferedImage reduzir(BufferedImage origem, int ladoMaximo) {

		int largura = origem.getWidth();
		int altura = origem.getHeight();

		if (largura <= ladoMaximo && altura <= ladoMaximo) {
			return origem;
		}

		double escala = (largura >= altura)
				? (double) ladoMaximo / largura
				: (double) ladoMaximo / altura;

		int novaLargura = (int) Math.round(largura * escala);
		int novaAltura = (int) Math.round(altura * escala);

		BufferedImage destino = new BufferedImage(novaLargura, novaAltura,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g = destino.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(origem, 0, 0, novaLargura, novaAltura, null);
		g.dispose();

		return destino;
	}

	/**
	 * Converte a imagem em bytes JPEG, que e o que vai para o banco.
	 *
	 * Um PNG com transparencia vem com canal alfa, e o encoder de JPEG nao
	 * sabe gravar alfa: ImageIO.write devolveria false e nada seria escrito.
	 * Por isso a imagem e redesenhada em RGB (fundo branco) antes.
	 */
	public static byte[] paraJpeg(BufferedImage img) throws IOException {

		BufferedImage rgb = img;
		if (img.getType() != BufferedImage.TYPE_INT_RGB) {
			rgb = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = rgb.createGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, rgb.getWidth(), rgb.getHeight());
			g.drawImage(img, 0, 0, null);
			g.dispose();
		}

		ByteArrayOutputStream saida = new ByteArrayOutputStream();
		if (!ImageIO.write(rgb, "jpg", saida)) {
			throw new IOException("Nao foi possivel converter a imagem para JPEG.");
		}
		return saida.toByteArray();
	}

	/** Caminho de volta: bytes do banco viram imagem de novo. */
	public static BufferedImage deBytes(byte[] dados) throws IOException {
		if (dados == null || dados.length == 0) {
			return null; // registro sem foto: nao e erro
		}
		return ImageIO.read(new ByteArrayInputStream(dados));
	}
}
