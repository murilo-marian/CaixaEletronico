import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static CaixaEletronico caixaEletronico;
    static int[] valores = {200, 100, 50, 20, 10, 5, 2};
    public static void main(String[] args) {
        Random random = new Random();
        Map<Integer, Cedula> estoque = new TreeMap<>(Comparator.reverseOrder());
        for (int i = 0; i < 7; i++) {
            Cedula cedula = new Cedula(valores[i], random.nextInt(0, 7));
            estoque.put(valores[i], cedula);
        }
        
        caixaEletronico = new CaixaEletronico(estoque);

        while (true) {

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
                case 1: {
                    depositar();
                    break;
                }
                case 2: {
                    sacar();
                    break;
                }
                case 3: {
                    cadastrarCedula();
                    break;
                }
                case 4: {
                    deletarCedula();
                    break;
                }
                case 5: {
                    adicionarQuantidade();
                    break;
                }
                case 6: {
                    removerQuantidade();
                    break;
                }
                case 7: {
                    caixaEletronico.consultarQuantidade();
                    pausar();
                    break;
                }
                case 8: {
                    caixaEletronico.consultarValorTotal();
                    pausar();
                    break;
                }
                default: {
                    System.exit(0);
                    break;
                }
            }
        }
    }

    public static void separadorDeTela() {
        System.out.println("----------------------------------");
    }

    public static void depositar() {
        separadorDeTela();
        Map<Integer, Integer> deposito = new HashMap<>();
        System.out.println("Digite quantas cédulas de cada tipo serão depositadas:");
        for (int valor : caixaEletronico.getEstoque().keySet()) {
            System.out.print("Quantidade de cédulas de R$" + valor + ": ");
            deposito.put(valor, scanner.nextInt());
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
        Cedula cedula = new Cedula(valor, 0);
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

    public static void pausar() {
        System.out.println("\nPressione ENTER para voltar ao menu...");
        scanner.nextLine();
        scanner.nextLine();
    }
}