import java.sql.*;

public class TesteConexao {
    public static void main(String[] args) {

        // 1. DADOS DA CONEXÃO
        String url   = "jdbc:mysql://localhost:3306/Cinema";
        String user  = "Cinema_cd";
        String senha = "Cinema_pw";

        // 2. CONECTAR (try-with-resources fecha sozinho no fim)
        try (Connection con = DriverManager.getConnection(url, user, senha)) {

            System.out.println("Conectado a: " +
                con.getMetaData().getDatabaseProductName());

            // 3. INSERIR com PreparedStatement (seguro)
            String sqlInsert = "INSERT INTO Cartazes (nome_F, genero, dias_cartaz ) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                ps.setString(1, "FOI PORRA");
                ps.setString(2, "eROTICO");
                ps.setInt(3, 20);
                int linhas = ps.executeUpdate();
                System.out.println(linhas + " Cartaz inserido.");
            }

            // 4. CONSULTAR e ler o ResultSet
            String sqlSelect = "SELECT id_cartaz, nome_F, genero, dias_cartaz FROM Cartazes ORDER BY dias_cartaz DESC";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sqlSelect)) {

                System.out.println("\n--- Cartazes ---");
                while (rs.next()) {
                    System.out.printf("%d | %-14s | %-6s | %d%n",
                        rs.getInt("id_cartaz"),
                        rs.getString("nome_F"),
                        rs.getString("genero"),
                        rs.getInt("dias_cartaz"));
                }
            }

        } catch (SQLException e) { 	
            System.out.println("Erro de banco: " + e.getMessage());
        }
    }
}