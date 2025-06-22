import java.util.Arrays;

public class Cedula {
    final int VALOR;
    int quantidade;

    public Cedula(int VALOR, int quantidade) {
        this.VALOR = VALOR;
        this.quantidade = quantidade;
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
            throw new IllegalArgumentException("Impossível adicionar números negativos");
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
