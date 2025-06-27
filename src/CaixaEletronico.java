import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaixaEletronico {
    //Map e não um Set por que acesso direto é mais conveniênte para o desenvolvedor do que busca. Tirando esse fato, Set seria mais indicado.
    private Map<Integer, Cedula> estoque;
    private int saldoTotal;
    private List<ICaixaNotificacao> notificadores;

    /**
     *
     * @param estoque Map com o estoque inicial de cédulas, completo com suas respectivas quantias
     * @param notificadores Lista de notificadores a serem utilizados no sistema. Todos são utilizados sempre que uma notificação é disparada.
     */
    public CaixaEletronico(Map<Integer, Cedula> estoque, List<ICaixaNotificacao> notificadores) {
        this.estoque = estoque;
        this.notificadores = notificadores;
        int soma = 0;
        for (Map.Entry<Integer, Cedula> entry : estoque.entrySet()) {
            soma += entry.getKey() * entry.getValue().getQuantidade();
        }
        saldoTotal = soma;
    }

    /**
     * Adiciona uma cédula completamente nova ao sistema
     * @param valor Valor da nova cédula a ser cadastrada (ex: 7 reais)
     */
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

    /**
     * Remove uma cédula do sistema completamente
     * @param valor Valor da cédula presente no sistema a ser removida (ex: deletar cédula de 2 reais)
     */
    public void removerCedula(int valor) {
        if (!estoque.containsKey(valor)) {
            notificar("Tentativa falha de remover cédula do sistema - cédula inexistente - valor: R$" + valor);
            throw new IllegalArgumentException("Cédula de R$" + valor + " não existe no sistema");
        }
        notificar("Cédula de valor R$" + valor + " removida com sucesso do sistema");
        saldoTotal -= valor * estoque.get(valor).getQuantidade();
        estoque.remove(valor);
    }

    /**
     * Deposita uma quantidade de cada cédula no caixa
     * @param deposito Map que contém o valor da cédula e a sua quantidade a ser depositada.
     */
    public void depositar(Map<Integer, Cedula> deposito) {
        int totalAntigo = saldoTotal;
        StringBuilder adicao = new StringBuilder();
        for (Map.Entry<Integer, Cedula> entry : deposito.entrySet()) {
            if (entry.getValue().getVALOR() != 0) {
                adicao.append(" ").append(entry.getValue()).append("x de R$ ").append(entry.getKey()).append(";");
                try {
                    adicionarQuantidade(entry.getKey(), entry.getValue().getQuantidade());
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

    /**
     * Adiciona uma quantidade específica de uma cédula ao sistema
     * @param valor Valor de cédula que está sendo adicionada a quantidade
     * @param quantidade A quantidade de cédulas adicionadas
     */
    public void adicionarQuantidade(int valor, int quantidade) {
        Cedula cedula = estoque.get(valor);
        if (cedula == null) {
            throw new IllegalArgumentException("Cédula de R$" + valor + " não está cadastrada no sistema");
        }
        cedula.adicionarQuantidade(quantidade);
        saldoTotal+= valor * quantidade;
    }

    /**
     * Sacar um valor do caixa
     * @param valor Valor a ser sacado
     */
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

        //Pega as cédulas de maior ao menor valor, procura sempre entregar a mínima quantidade de cédulas possíveis com
        //esse sistema
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
        StringBuilder remocao = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : cedulasParaEntregar.entrySet()) {
            if (entry.getValue() != 0) {
                remocao.append(" ").append(entry.getValue()).append("x de R$ ").append(entry.getKey()).append(";");
                removerQuantidade(entry.getKey(), entry.getValue());
            }
        }
        System.out.println(remocao);
        notificar("Saque realizado no valor de R$" + valor + ".\nConteúdo:" + remocao);
    }

    /**
     * Remove uma certa quantidade de cédulas de um determinado valor do sistema
     * @param valor Valor do qual a quantidade será subtraída
     * @param quantidade Quantidade a ser subtraída
     */
    public void removerQuantidade(int valor, int quantidade) {
        Cedula cedula = estoque.get(valor);
        if (cedula == null) {
            throw new IllegalArgumentException("Cédula de R$" + valor + " não existe no caixa.");
        }
        cedula.removerQuantidade(quantidade);
        saldoTotal-= valor * quantidade;
    }


    /**
     * Mostra o saldo total do caixa
     */
    public int consultarValorTotal() {
        notificar("Consulta de valor total realizada");
        return saldoTotal;
    }

    /**
     * Mostra a quantidade de cada cédula de forma legível
     */
    public Map<Integer, Cedula> consultarQuantidade() {
        notificar("Consulta de quantidade realizada");
        return getEstoque();
    }

    /**
     * Repete sobre a lista de notificadores, lançando notificações para todas as implementações adicionadas
     * @param mensagem Mensagem a ser enviada para as implementações
     */
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
