import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaixaEletronico {
    private Map<Integer, Cedula> estoque;
    private int saldoTotal;
    private List<ICaixaNotificacao> notificadores = new ArrayList<>();

    public CaixaEletronico(Map<Integer, Cedula> estoque, List<ICaixaNotificacao> notificadores) {
        this.estoque = estoque;
        this.notificadores = notificadores;
        int soma = 0;
        for (Map.Entry<Integer, Cedula> entry : estoque.entrySet()) {
            soma += entry.getKey() * entry.getValue().getQuantidade();
        }
        saldoTotal = soma;
    }

    public void cadastrarCedula(int valor) {
        if (estoque.containsKey(valor)) {
            notificar("Cédula de R$" + valor + " já existe no sistema");
            throw new IllegalArgumentException("Cédula de R$" + valor + " já existe no sistema");
        }
        Cedula cedula = new Cedula(valor, 0);
        estoque.put(valor, cedula);
        notificar("Cédula cadastrada no sistema com valor de R$" + valor);

        //sempre dá 0, mas preferi deixar para caso queira mudar a lógica no futuro
        saldoTotal += valor * cedula.getQuantidade();
    }

    public void removerCedula(int valor) {
        if (!estoque.containsKey(valor)) {
            notificar("Tentativa falha de remover cédula do sistema - cédula inexistente - valor: R$" + valor);
            throw new IllegalArgumentException("Cédula de R$" + valor + " não existe no sistema");
        }
        notificar("Cédula de valor R$" + valor + " removida com sucesso do sistema");
        saldoTotal -= valor * estoque.get(valor).getQuantidade();
        estoque.remove(valor);
    }

    public void depositar(Map<Integer, Integer> deposito) {
        int totalAntigo = saldoTotal;
        String adicao = "";
        for (Map.Entry<Integer, Integer> entry : deposito.entrySet()) {
            if (entry.getValue() != 0) {
                adicao += " " + entry.getValue() + "x de R$ " + entry.getKey() + ";";
                try {
                    adicionarQuantidade(entry.getKey(), entry.getValue());
                    adicionarQuantidade(entry.getKey(), entry.getValue());
                } catch (IllegalArgumentException e) {
                    System.out.println("Erro na tentativa de depósito: " + e);
                    notificar("Erro na tentativa de depósito: " + e);
                }
            }
        }
        int valorDepositado = saldoTotal - totalAntigo;
        notificar("Deposito realizado de valor R$" + valorDepositado + ".\nConsiste em:" + adicao);
        System.out.println("Depósito Realizado!");
        System.out.println("Depósito no valor de R$" + (valorDepositado));
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
            notificar("Erro em tentativa de saque: Valor de R$" + valor + " indisponível no caixa");
            throw new IllegalArgumentException("Valor indisponível no caixa");
        }
        if (valor < 0) {
            notificar("Erro em tentativa de saque: Valor inserido por usuário é negativo");
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
            notificar("Erro em tentativa de saque: Não foi possível montar valor de R$" + valor + " com cédulas disponíveis");
            throw new IllegalArgumentException("Não é possível sacar o valor solicitado - falta de cédulas específicas no caixa para sacar o valor exato \n" +
                    "Valor Faltante: R$" + restante + "\n" +
            "Você pode sacar: R$" + (valor - restante));
        }

        System.out.println("Saque realizado!");
        System.out.println("Cédulas entregues:");
        String remocao = "";
        for (Map.Entry<Integer, Integer> entry : cedulasParaEntregar.entrySet()) {
            if (entry.getValue() != 0) {
                remocao += " " + entry.getValue() + "x de R$ " +entry.getKey() + ";";
                removerQuantidade(entry.getKey(), entry.getValue());
            }
        }
        System.out.println(remocao);
        notificar("Saque realizado no valor de R$" + valor + ".\nConteúdo:" + remocao);
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
        notificar("Consulta de valor total realizada");
        System.out.println("Valor total guardado no caixa eletrônico: " + saldoTotal);
    }

    //mostra a quantidade de cada cédula de forma legível
    public void consultarQuantidade() {
        notificar("Consulta de quantidade realizada");
        System.out.println("Quantidade de cada cédula no estoque: ");
        for (Map.Entry<Integer, Cedula> entry : estoque.entrySet()) {
            System.out.println("Valor da cédula: R$" + entry.getKey() + "; Quantidade: " + entry.getValue().getQuantidade());
        }
    }

    private void notificar(String mensagem) {
        for (ICaixaNotificacao notificador : notificadores) {
            notificador.notificar(mensagem);
        }
    }

    public List<ICaixaNotificacao> getNotificadores() {
        return notificadores;
    }

    public void setNotificadores(List<ICaixaNotificacao> notificadores) {
        this.notificadores = notificadores;
    }

    public Map<Integer, Cedula> getEstoque() {
        return estoque;
    }

    public void setEstoque(Map<Integer, Cedula> estoque) {
        this.estoque = estoque;
    }
}
