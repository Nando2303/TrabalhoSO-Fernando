import java.util.Scanner;
import java.util.Random;
public class Main {
    static int MAXIMO_TEMPO_EXECUCAO = 65535;
    static int n_processos = 3;
    static Scanner teclado = new Scanner (System.in);

    public static void main(String[] args) {
        int[] tempo_execucao = new int[n_processos];
        int[] tempo_chegada = new int[n_processos];
        int[] prioridade = new int[n_processos];
        int[] tempo_espera = new int[n_processos];
        int[] tempo_restante = new int[n_processos];

        System.out.print("1 - Sim \n2 - Não \nSerá aleatório?  ");
        int aleatorio =  teclado.nextInt();

        popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade, aleatorio);
        imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

        //Escolher algoritmo
        int alg;
        while(true) {
            System.out.print("\n1 - FCFS \n2 - SJF Preemptivo \n3 - SJF Não Preemptivo  \n4 - Prioridade Preemptivo \n5 - Prioridade Não Preemptivo  \n6 - Round Robin  \n7 - Imprime lista de processos \n8 - Popular processos novamente \n9 - Sair \nEscolha um algoritmo: ");
            alg =  teclado.nextInt();

            if (alg == 1) { //FCFS
                FCFS(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
            }
            else if (alg == 2) { //SJF PREEMPTIVO
                SJF(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
            }
            else if (alg == 3) { //SJF NÃO PREEMPTIVO
                SJFN(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
            }
            else if (alg == 4) { //PRIORIDADE PREEMPTIVO
                PRIORIDADE(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 5) { //PRIORIDADE NÃO PREEMPTIVO
                PRIORIDADEN(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 6) { //Round_Robin
                ROUNDROBIN(tempo_execucao, tempo_espera, tempo_restante);
            }
            else if (alg == 7) { //IMPRIME CONTEÚDO INICIAL DOS PROCESSOS
                imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 8) { //REATRIBUI VALORES INICIAIS
                popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade, aleatorio);
                imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 9) {
                break;
            }
        }
    }
    public static void popular_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,  int [] prioridade, int aleatorio ){
        Random random = new Random();
        Scanner teclado = new Scanner (System.in);

        for (int i = 0; i < n_processos; i++) {
            //Popular Processos Aleatorio
            if (aleatorio == 1){
                tempo_execucao[i] = random.nextInt(10)+1;
                tempo_chegada[i] = random.nextInt(10)+1;
                prioridade[i] = random.nextInt(15)+1;
            }
            //Popular Processos Manual
            else {
                System.out.print("Digite o tempo de execução do processo["+i+"]:  ");
                tempo_execucao[i] = teclado.nextInt();
                System.out.print("Digite o tempo de chegada do processo["+i+"]:  ");
                tempo_chegada[i] = teclado.nextInt();
                System.out.print("Digite a prioridade do processo["+i+"]:  ");
                prioridade[i] = teclado.nextInt();
            }
            tempo_restante[i] = tempo_execucao[i];
        }
    }
    public static void imprime_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,  int []prioridade){
        //Imprime lista de processos
        for (int i = 0; i < n_processos; i++) {
            System.out.println("Processo["+i+"]: tempo_execucao="+ tempo_execucao[i] + " tempo_restante="+tempo_restante[i] + " tempo_chegada=" + tempo_chegada[i] + " prioridade =" +prioridade[i]);
        }
    }
    public static void imprime_stats (int[] espera) {
        int[] tempo_espera = espera.clone();
        //Implementar o calculo e impressão de estatisticas
        double tempo_espera_total = 0;
        for(int i=0; i<n_processos; i++){
            System.out.println("Processo["+i+"]: tempo_espera="+tempo_espera[i]);
            tempo_espera_total = tempo_espera_total + tempo_espera[i];
        }
        System.out.println("Tempo médio de espera: "+(tempo_espera_total/n_processos));
    }
    public static void FCFS(int[] execucao, int[] espera, int[] restante, int[] chegada){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        //int[] tempo_chegada = chegada.clone();
        int processo_em_execucao = 0; //processo inicial no FIFO é o zero
        //implementar código do FCFS
        for (int i=1; i<MAXIMO_TEMPO_EXECUCAO; i++) {
            System.out.println("tempo["+i+"]: processo["+processo_em_execucao+"] restante="+tempo_restante[processo_em_execucao]);
            if (tempo_execucao[processo_em_execucao] == tempo_restante[processo_em_execucao])
                tempo_espera[processo_em_execucao] = i-1;
            if (tempo_restante[processo_em_execucao] == 1) {
                if (processo_em_execucao == (n_processos-1))
                    break;
                else
                    processo_em_execucao++;
            }
            else
                tempo_restante[processo_em_execucao]--;
        }
        //
        imprime_stats(tempo_espera);
    }

    public static void SJF(int[] execucao, int[] espera, int[] restante, int[] chegada) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();

        int tempo_atual = 0;
        int processos_completos = 0;

        while (processos_completos < n_processos) {
            int menor_tempo_execucao = Integer.MAX_VALUE;
            int processo_menor_tempo = -1;
            for (int i = 0; i < n_processos; i++) {
                if (tempo_chegada[i] <= tempo_atual && tempo_restante[i] < menor_tempo_execucao && tempo_restante[i] > 0) {
                    menor_tempo_execucao = tempo_restante[i];
                    processo_menor_tempo = i;
                }
            }
            if (processo_menor_tempo == -1) {
                System.out.println("tempo[" + tempo_atual + "]: Nenhum processo está pronto");
                tempo_atual++;
                continue;
            }
            tempo_restante[processo_menor_tempo]--;
            System.out.println("tempo[" + tempo_atual + "]: Processo[" + processo_menor_tempo + "] restante=" + tempo_restante[processo_menor_tempo]);

            if (tempo_restante[processo_menor_tempo] == 0) {
                processos_completos++;

                int tempo_espera_processo = tempo_atual - tempo_execucao[processo_menor_tempo] + 1 - tempo_chegada[processo_menor_tempo];
                tempo_espera[processo_menor_tempo] = tempo_espera_processo >= 0 ? tempo_espera_processo : 0;
            }
            tempo_atual++;
        }
        imprime_stats(tempo_espera);
    }

    public static void SJFN(int[] execucao, int[] espera, int[] restante, int[] chegada) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();

        int n_processos = tempo_execucao.length;
        int tempo_atual = 0;
        boolean[] processo_executado = new boolean[n_processos];

        while (true) {
            int processo_atual = -1;
            int menor_tempo_execucao = MAXIMO_TEMPO_EXECUCAO + 1;
            for (int i = 0; i < n_processos; i++) {
                if (!processo_executado[i] && tempo_chegada[i] <= tempo_atual && tempo_execucao[i] < menor_tempo_execucao) {
                    processo_atual = i;
                    menor_tempo_execucao = tempo_execucao[i];
                }
            }
            if (processo_atual == -1) {
                System.out.println("tempo[" + tempo_atual + "]: Nenhum processo está pronto");
                tempo_atual++;
                continue;
            }
            while (tempo_restante[processo_atual] > 0) {
                tempo_restante[processo_atual]--;
                System.out.println("tempo[" + tempo_atual + "]: Processo[" + processo_atual + "] restante=" + tempo_restante[processo_atual]);
                if (tempo_restante[processo_atual] == 0) {
                    tempo_espera[processo_atual] = tempo_atual + 1 - tempo_execucao[processo_atual] - tempo_chegada[processo_atual];
                    processo_executado[processo_atual] = true;
                } else {
                    tempo_atual++;
                }
            }
            tempo_atual++;
            boolean todos_completos = true;
            for (int i = 0; i < n_processos; i++) {
                if (!processo_executado[i]) {
                    todos_completos = false;
                    break;
                }
            }
            if (todos_completos) {
                break;
            }
        }
        imprime_stats(tempo_espera);
    }

    public static void PRIORIDADE(int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();
        int[] prioridades = prioridade.clone();
        int n_processos = tempo_execucao.length;

        int tempo_atual = 0;
        boolean[] processo_executado = new boolean[n_processos];

        while (true) {
            int processo_atual = -1;
            int maior_prioridade = Integer.MIN_VALUE;
            for (int i = 0; i < n_processos; i++) {
                if (!processo_executado[i] && tempo_chegada[i] <= tempo_atual && prioridades[i] > maior_prioridade) {
                    processo_atual = i;
                    maior_prioridade = prioridades[i];
                }
            }
            if (processo_atual == -1) {
                System.out.println("tempo[" + tempo_atual + "]: Nenhum processo está pronto");
                tempo_atual++;
                continue;
            }
            tempo_restante[processo_atual]--;
            System.out.println("tempo[" + tempo_atual + "]: Processo[" + processo_atual + "] restante=" + tempo_restante[processo_atual]);
            if (tempo_restante[processo_atual] == 0) {
                tempo_espera[processo_atual] = tempo_atual + 1 - tempo_execucao[processo_atual] - tempo_chegada[processo_atual];
                processo_executado[processo_atual] = true;
            }
            tempo_atual++;
            boolean todos_completos = true;
            for (boolean executado : processo_executado) {
                if (!executado) {
                    todos_completos = false;
                    break;
                }
            }
            if (todos_completos) {
                break;
            }
        }
        imprime_stats(tempo_espera);
    }

    public static void PRIORIDADEN(int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();
        int[] prioridades = prioridade.clone();
        int n_processos = tempo_execucao.length;

        int tempo_atual = 0;
        boolean[] processo_executado = new boolean[n_processos];

        while (true) {
            int processo_atual = -1;
            int maior_prioridade = Integer.MIN_VALUE;
            int menor_tempo_restante = Integer.MAX_VALUE;
            for (int i = 0; i < n_processos; i++) {
                if (!processo_executado[i] && tempo_chegada[i] <= tempo_atual && prioridades[i] > maior_prioridade) {
                    processo_atual = i;
                    maior_prioridade = prioridades[i];
                    menor_tempo_restante = tempo_restante[i];
                } else if (!processo_executado[i] && tempo_chegada[i] <= tempo_atual && prioridades[i] == maior_prioridade && tempo_restante[i] < menor_tempo_restante) {
                    processo_atual = i;
                    menor_tempo_restante = tempo_restante[i];
                }
            }
            if (processo_atual == -1) {
                System.out.println("tempo[" + tempo_atual + "]: Nenhum processo está pronto");
                tempo_atual++;
                continue;
            }
            while (tempo_restante[processo_atual] > 0) {
                tempo_restante[processo_atual]--;
                System.out.println("tempo[" + tempo_atual + "]: Processo[" + processo_atual + "] restante=" + tempo_restante[processo_atual]);
                tempo_atual++;
            }
            tempo_espera[processo_atual] = tempo_atual - tempo_execucao[processo_atual] - tempo_chegada[processo_atual];
            processo_executado[processo_atual] = true;
            boolean todos_completos = true;
            for (boolean executado : processo_executado) {
                if (!executado) {
                    todos_completos = false;
                    break;
                }
            }
            if (todos_completos) {
                break;
            }
        }
        imprime_stats(tempo_espera);
    }

    public static void ROUNDROBIN(int[] execucao, int[] espera, int[] restante) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.println();
        System.out.print("1 - Sim \n2 - Não \nTime-slice será aleatório? ");
        int opcao = scanner.nextInt();
        int quantum = 0;
        if (opcao == 1) {
            quantum = random.nextInt(10) + 1;
            System.out.println("Time-slice: " + quantum);
        } else {
            System.out.print("Digite o time-slice desejado: ");
            quantum = scanner.nextInt();
        }

        int n_processos = tempo_execucao.length;
        int tempo_atual = 0;
        int[] tempos_espera_processo = new int[n_processos];
        boolean[] processo_concluido = new boolean[n_processos];

        while (true) {
            boolean todos_concluidos = true;
            for (int i = 0; i < n_processos; i++) {
                if (tempo_restante[i] > 0) {
                    todos_concluidos = false;
                    for (int j = 0; j < quantum; j++) {
                        if (tempo_restante[i] == 0) {
                            break;
                        }
                        tempo_restante[i]--;
                        System.out.println("tempo[" + tempo_atual + "]: Processo[" + i + "] restante=" + tempo_restante[i]);
                        tempo_atual++;
                        for (int k = 0; k < n_processos; k++) {
                            if (k != i && !processo_concluido[k] && tempo_restante[k] > 0) {
                                tempos_espera_processo[k]++;
                            }
                        }
                    }
                    if (tempo_restante[i] == 0) {
                        processo_concluido[i] = true;
                        tempo_espera[i] = tempos_espera_processo[i];
                    }
                }
            }
            if (todos_concluidos) {
                break;
            }
        }
        imprime_stats(tempo_espera);
    }
}
