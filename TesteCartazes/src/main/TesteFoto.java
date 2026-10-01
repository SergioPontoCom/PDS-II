package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import model.Imagens;

/**
 * Teste de console da parte de fotos - nao abre janela nem usa banco.
 * Rode ESTA classe antes de testar o poster pela tela.
 *
 * Casos: imagem grande, imagem pequena, PNG com transparencia e arquivo
 * que nao e imagem.
 */
public class TesteFoto {

	public static void main(String[] args) throws IOException {

		// 1. imagem grande (proporcao de poster, 1600x2400)
		File grande = criarImagem(1600, 2400, false);
		testar("grande", grande);

		// 2. imagem ja pequena: nao deve ser alterada
		File pequena = criarImagem(300, 450, false);
		testar("pequena", pequena);

		// 3. PNG com transparencia: precisa virar JPEG sem falhar
		File comAlfa = criarImagem(800, 1200, true);
		testar("png com transparencia", comAlfa);

		// 4. arquivo que nao e imagem
		File texto = File.createTempFile("TesteFoto", ".txt");
		Files.writeString(texto.toPath(), "isto nao e uma imagem");
		try {
			Imagens.lerReduzida(texto);
			System.out.println("arquivo invalido -> ERRO: nao lancou excecao");
		} catch (IOException e) {
			System.out.println("arquivo invalido -> " + e.getMessage());
		}

		// 5. registro sem foto: deBytes devolve null, sem excecao
		System.out.println("sem foto -> deBytes devolve: " + Imagens.deBytes(null));
	}

	private static void testar(String nome, File arquivo) throws IOException {
		BufferedImage original = ImageIO.read(arquivo);
		byte[] bytes = Imagens.lerReduzida(arquivo);
		BufferedImage volta = Imagens.deBytes(bytes);

		System.out.println(nome + ":");
		System.out.println("  original : " + original.getWidth() + "x" + original.getHeight()
				+ "  " + arquivo.length() / 1024 + " KB");
		System.out.println("  reduzida : " + volta.getWidth() + "x" + volta.getHeight()
				+ "  " + bytes.length / 1024 + " KB");
	}

	private static File criarImagem(int l, int a, boolean alfa) throws IOException {
		BufferedImage img = new BufferedImage(l, a,
				alfa ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setColor(new Color(255, 193, 7));
		g.fillRect(0, 0, l, a);
		g.setColor(new Color(20, 20, 20));
		g.fillOval(l / 4, a / 4, l / 2, a / 2);
		g.dispose();
		File f = File.createTempFile("poster", ".png");
		ImageIO.write(img, "png", f);
		return f;
	}
}
