import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static CaixaEletronico caixaEletronico;
    static int[] valores = {200, 100, 50, 20, 10, 5, 2};
    public static void main(String[] args) {
        Random random = new Random();
        Map<Integer, Cedula> estoque = new TreeMap<>(Comparator.reverseOrder());
        //gera valores aleatórios para compor o dinheiro inicial do caixa
        for (int i = 0; i < 7; i++) {
            Cedula cedula = new Cedula(valores[i], random.nextInt(0, 7));
            estoque.put(valores[i], cedula);
        }
        LogEmArquivo logEmArquivo = new LogEmArquivo("./event.log");
        List<ICaixaNotificacao> notificadores = new ArrayList<>();
        notificadores.add(logEmArquivo);
        
        caixaEletronico = new CaixaEletronico(estoque, notificadores);

        while (true) {

            // Menu principal
            System.out.println("Sistema de Caixa Eletrônico");
            separadorDeTela();

            System.out.println("Escolha qual função deseja usar:");
            System.out.println("1 - Depositar");
            System.out.println("2 - Sacar");
            System.out.println("3 - (Administrador) Cadastrar cédula");
            System.out.println("4 - (Administrador) Deletar cédula");
            System.out.println("5 - (Administrador) Adicionar quantidade");
            System.out.println("6 - (Administrador) Remover quantidade");
            System.out.println("7 - (Administrador) Checar quantidade");
            System.out.println("8 - (Administrador) Checar valor total");
            System.out.println("9 - Sair");

            switch (scanner.nextInt()) {
                case 1 -> depositar();
                case 2 -> sacar();
                case 3 -> cadastrarCedula();
                case 4 -> deletarCedula();
                case 5 -> adicionarQuantidade();
                case 6 -> removerQuantidade();
                case 7 -> checarQuantidade();
                case 8 -> checarValorTotal();
                default -> System.exit(0);
            }
        }
    }

    public static void separadorDeTela() {
        System.out.println("----------------------------------");
    }

    public static void depositar() {
        separadorDeTela();
        Map<Integer, Cedula> deposito = new HashMap<>();

        //loop perguntando cédula por cédula - adiciona só as que forem > 0
        System.out.println("Digite quantas cédulas de cada tipo serão depositadas:");
        for (int valor : caixaEletronico.getEstoque().keySet()) {
            System.out.print("Quantidade de cédulas de R$" + valor + ": ");
            Cedula cedula = new Cedula(valor, scanner.nextInt());
            deposito.put(valor, cedula);
        }
        try {
            caixaEletronico.depositar(deposito);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e);
        }
        pausar();
    }

    public static void sacar() {
        separadorDeTela();
        System.out.print("Qual o valor do saque?: ");
        int valor = scanner.nextInt();
        try {
            caixaEletronico.sacar(valor);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        pausar();
    }

    public static void cadastrarCedula() {
        separadorDeTela();
        //adiciona uma categoria de cédula nova no sistema (ex: cadastrar cédula de 7 reais)
        System.out.print("Digite o valor da cédula a ser cadastrada: ");
        int valor = scanner.nextInt();
        try {
            caixaEletronico.cadastrarCedula(valor);
            System.out.println("Cédula adicionada no sistema");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        pausar();
    }

    public static void deletarCedula() {
        separadorDeTela();
        System.out.print("Digite o valor da cédula a ser cadastrada: ");
        int valor = scanner.nextInt();
        try {
            caixaEletronico.removerCedula(valor);
            System.out.println("Cédula removida do sistema");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        pausar();
    }

    public static void adicionarQuantidade() {
        separadorDeTela();
        System.out.println("Digite quantas cédulas de cada tipo serão adicionadas:");

        //loop perguntando cédula por cédula - adiciona só as que forem > 0
        for (int valor : caixaEletronico.getEstoque().keySet()) {
            System.out.print("Quantidade de cédulas de R$" + valor + ": ");

            try {
                caixaEletronico.adicionarQuantidade(valor, scanner.nextInt());
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        pausar();
    }

    public static void removerQuantidade() {
        separadorDeTela();
        System.out.println("Digite quantas cédulas de cada tipo serão removidas:");

        //loop perguntando cédula por cédula - adiciona só as que forem > 0
        for (int valor : caixaEletronico.getEstoque().keySet()) {
            System.out.print("Quantidade de cédulas de R$" + valor + ": ");

            try {
                caixaEletronico.removerQuantidade(valor, scanner.nextInt());
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        pausar();
    }

    public static void checarQuantidade() {
        separadorDeTela();
        Map<Integer, Cedula> estoqueAtual = caixaEletronico.consultarQuantidade();
        System.out.println("Quantidade de cada cédula no estoque: ");
        for (Map.Entry<Integer, Cedula> entry : estoqueAtual.entrySet()) {
            System.out.println("Valor da cédula: R$" + entry.getKey() + "; Quantidade: " + entry.getValue().getQuantidade());
        }
        pausar();
    }

    public static void checarValorTotal() {
        separadorDeTela();
        int saldoTotal = caixaEletronico.consultarValorTotal();
        System.out.println("Valor total guardado no caixa eletrônico: " + saldoTotal);
        pausar();
    }

    public static void pausar() {
        System.out.println("\nPressione ENTER para voltar ao menu...");
        scanner.nextLine();
        scanner.nextLine();
    }
}