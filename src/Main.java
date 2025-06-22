import java.util.*;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        Map<Integer, Cedula> estoque = new TreeMap<>(Comparator.reverseOrder());
        int[] valores = {200, 100, 50, 20, 10, 5, 2};
        for (int i = 0; i < 7; i++) {
            Cedula cedula = new Cedula(valores[i], random.nextInt(0, 5));
            estoque.put(valores[i], cedula);
        }
        
        CaixaEletronico caixaEletronico = new CaixaEletronico(estoque);
        caixaEletronico.consultarValorTotal();
        caixaEletronico.consultarQuantidade();

        caixaEletronico.sacar(200);
    }
}