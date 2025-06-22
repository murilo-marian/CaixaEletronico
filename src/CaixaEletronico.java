import java.util.HashMap;
import java.util.Map;

public class CaixaEletronico {
    private Map<Integer, Cedula> estoque;
    private int saldoTotal;

    public CaixaEletronico(Map<Integer, Cedula> estoque) {
        this.estoque = estoque;
        int soma = 0;
        for (Map.Entry<Integer, Cedula> entry : estoque.entrySet()) {
            soma += entry.getKey() * entry.getValue().getQuantidade();
        }
        saldoTotal = soma;
    }

    public void cadastrarCedula(int valor) {
        if (estoque.containsKey(valor)) {
            throw new IllegalArgumentException("Cédula de R$" + valor + " já existe no sistema");
        }
        Cedula cedula = new Cedula(valor, 0);
        estoque.put(valor, cedula);
        saldoTotal += valor * cedula.getQuantidade();
    }

    public void removerCedula(int valor) {
        if (!estoque.containsKey(valor)) {
            throw new IllegalArgumentException("Cédula de R$" + valor + " não existe no sistema");
        }
        saldoTotal -= valor * estoque.get(valor).getQuantidade();
        estoque.remove(valor);
    }

    public void depositar(Map<Integer, Integer> deposito) {
        int totalAntigo = saldoTotal;
        for (Map.Entry<Integer, Integer> entry : deposito.entrySet()) {
            adicionarQuantidade(entry.getKey(), entry.getValue());
        }
        System.out.println("Depósito Realizado!");
        System.out.println("Depósito no valor de R$" + (saldoTotal - totalAntigo));
    }

    public void adicionarQuantidade(int valor, int quantidade) {
        Cedula cedula = estoque.get(valor);
        if (cedula == null) {
            throw new IllegalArgumentException("Cédula de R$" + valor + " não está cadastrada no sistema");
        }
        cedula.adicionarQuantidade(quantidade);
        saldoTotal+= valor * quantidade;
    }

    public void sacar(int valor) {
        int restante = valor;
        Map<Integer, Integer> cedulasParaEntregar = new HashMap<>();
        if (valor > saldoTotal) {
            throw new IllegalArgumentException("Valor indisponível no caixa");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("Valores negativos não são permitidos");
        }
        for (Integer valorCedula: estoque.keySet()) {
            int quantidadeNoEstoque = estoque.get(valorCedula).getQuantidade();
            int quantidadeNecessaria = restante / valorCedula;
            int quantidadeUsada = Math.min(quantidadeNecessaria, quantidadeNoEstoque);

            if (quantidadeUsada > 0) {
                cedulasParaEntregar.put(valorCedula, quantidadeUsada);
                restante -= quantidadeUsada * valorCedula;
            }
        }
        if (restante > 0) {
            throw new IllegalArgumentException("Não é possível sacar o valor solicitado - falta de cédulas específicas no caixa para sacar o valor exato \n" +
                    "Valor Faltante: R$" + restante + "\n" +
            "Você pode sacar: R$" + (valor - restante));
        }

        System.out.println("Saque realizado!");
        System.out.println("Cédulas entregues:");
        for (Map.Entry<Integer, Integer> entry : cedulasParaEntregar.entrySet()) {
            System.out.println("Nota: R$" + entry.getKey() + ", quantidade: " + entry.getValue());
            removerQuantidade(entry.getKey(), entry.getValue());
        }
    }

    public void removerQuantidade(int valor, int quantidade) {
        Cedula cedula = estoque.get(valor);
        if (cedula == null) {
            throw new IllegalArgumentException("Cédula de R$" + valor + " não existe no caixa.");
        }
        cedula.removerQuantidade(quantidade);
        saldoTotal-= valor * quantidade;
    }

    //consulta a soma de todas as cédulas
    public void consultarValorTotal() {
        System.out.println("Valor total guardado no caixa eletrônico: " + saldoTotal);
    }

    //mostra a quantidade de cada cédula de forma legível
    public void consultarQuantidade() {
        System.out.println("Quantidade de cada cédula no estoque: ");
        for (Map.Entry<Integer, Cedula> entry : estoque.entrySet()) {
            System.out.println("Valor da cédula: R$" + entry.getKey() + "; Quantidade: " + entry.getValue().getQuantidade());
        }
    }

    public Map<Integer, Cedula> getEstoque() {
        return estoque;
    }

    public void setEstoque(Map<Integer, Cedula> estoque) {
        this.estoque = estoque;
    }
}
