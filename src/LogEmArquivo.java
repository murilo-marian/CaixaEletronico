import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEmArquivo implements ICaixaNotificacao {
    private final String caminhoArquivo;

    public LogEmArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    @Override
    public void notificar(String mensagem) {
        try (FileWriter writer = new FileWriter(caminhoArquivo, true)) {
            String dataHora = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            writer.write("[" + dataHora + "] " + mensagem + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao gravar no log: " + e.getMessage());
        }
    }
}
