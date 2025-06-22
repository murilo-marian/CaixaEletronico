import java.util.Arrays;

public class Cedula {
    final int VALOR;
    int quantidade;

    public Cedula(int VALOR, int quantidade) {
        if (!isValida(VALOR)) {
            throw new IllegalArgumentException("Valor de cédula inválido: " + VALOR);
        }
        this.VALOR = VALOR;
        this.quantidade = quantidade;
    }

    public boolean isValida() {
        return Arrays.asList(200, 100, 50, 20, 10, 5, 2).contains(VALOR);
    }

    public boolean isValida(int valor) {
        return Arrays.asList(200, 100, 50, 20, 10, 5, 2).contains(valor);
    }

    public int getVALOR() {
        return VALOR;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void adicionarQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Impossível remover números negativos");
        }
        this.quantidade += quantidade;
    }

    public void removerQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Impossível remover números negativos");
        }
        if (this.quantidade < quantidade) {
            throw new IllegalArgumentException("Impossível remover - quantidade atual menor que a quantidade a ser removida");
        }
        this.quantidade -= quantidade;
    }
}
