import java.util.Scanner;
import java.util.Random;
public class Main {
    static int MAXIMO_TEMPO_EXECUCAO = 65535;
    static int n_processos = 3;

    public static void main(String[] args) {
        int[] tempo_execucao = new int[n_processos];
        int[] tempo_chegada = new int[n_processos];
        int[] prioridade = new int[n_processos];
        int[] tempo_espera = new int[n_processos];
        int[] tempo_restante = new int[n_processos];
        Scanner teclado = new Scanner (System.in);
        popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
        imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

        //Escolher algoritmo
        int alg;
        while(true) {
            System.out.print("Escolha o argoritmo?: [1=FCFS 2=SJF Preemptivo 3=SJF Não Preemptivo  4=Prioridade Preemptivo 5=Prioridade Não Preemptivo  6=Round_Robin  7=Imprime lista de processos 8=Popular processos novamente 9=Sair]: ");
            alg =  teclado.nextInt();

            if (alg == 1) { //FCFS
                FCFS(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
            }
            else if (alg == 2) { //SJF PREEMPTIVO
                SJF(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 3) { //SJF NÃO PREEMPTIVO
                SJF(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 4) { //PRIORIDADE PREEMPTIVO
                PRIORIDADE(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 5) { //PRIORIDADE NÃO PREEMPTIVO
                PRIORIDADE(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 6) { //Round_Robin
                Round_Robin(tempo_execucao, tempo_espera, tempo_restante);
            }
            else if (alg == 7) { //IMPRIME CONTEÚDO INICIAL DOS PROCESSOS
                imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 8) { //REATRIBUI VALORES INICIAIS
                popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 9) {
                break;
            }
        }
    }
    public static void popular_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,  int [] prioridade ){
        Random random = new Random();
        Scanner teclado = new Scanner (System.in);
        int aleatorio;
        System.out.print("Será aleatório?:  ");
        aleatorio =  teclado.nextInt();

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

    public static void SJF(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();
        int[] prioridade_temp = prioridade.clone();

        int n_processos = tempo_execucao.length; // Obter o número de processos

        int tempo_atual = 0;
        int processo_atual = -1;
        int menor_tempo_execucao = MAXIMO_TEMPO_EXECUCAO + 1;
        boolean[] processo_executado = new boolean[n_processos];

        while (true) {
            processo_atual = -1;
            menor_tempo_execucao = MAXIMO_TEMPO_EXECUCAO + 1;

            // Encontra o próximo processo a ser executado
            for (int i = 0; i < n_processos; i++) {
                if (!processo_executado[i] && tempo_chegada[i] <= tempo_atual && tempo_execucao[i] < menor_tempo_execucao) {
                    processo_atual = i;
                    menor_tempo_execucao = tempo_execucao[i];
                }
            }

            // Se não há mais processos a executar, sai do loop
            if (processo_atual == -1)
                break;

            // Se o algoritmo é preemptivo, decrementa o tempo restante do processo atual
            if (preemptivo) {
                tempo_restante[processo_atual]--;

                // Se o processo atual terminou sua execução, atualiza o tempo de espera
                if (tempo_restante[processo_atual] == 0) {
                    tempo_espera[processo_atual] = tempo_atual + 1 - tempo_execucao[processo_atual] - tempo_chegada[processo_atual];
                    processo_executado[processo_atual] = true;
                }
            } else { // Se não é preemptivo, executa o processo até o fim
                int[] tempo_espera_temp = new int[n_processos];
                int tempo_global = 0;

                while (true) {
                    int proximo_processo = -1;
                    int menor_tempo_executa = Integer.MAX_VALUE;

                    // Encontra o próximo processo a ser executado
                    for (int i = 0; i < n_processos; i++) {
                        if (!processo_executado[i] && tempo_chegada[i] <= tempo_global && tempo_execucao[i] < menor_tempo_executa) {
                            proximo_processo = i;
                            menor_tempo_executa = tempo_execucao[i];
                        }
                    }

                    // Se não houver mais processos a executar, sai do loop
                    if (proximo_processo == -1)
                        break;

                    // Calcula o tempo de espera do processo antes de começar a execução
                    tempo_espera_temp[proximo_processo] = tempo_global - tempo_chegada[proximo_processo];

                    // Executa o processo até o fim
                    tempo_global += tempo_execucao[proximo_processo];
                    // Marca o processo como executado
                    processo_executado[proximo_processo] = true;
                }

                // Copia os tempos de espera temporários para o array final
                for (int i = 0; i < n_processos; i++) {
                    tempo_espera[i] = tempo_espera_temp[i];
                }
            }

            // Avança o tempo atual
            tempo_atual++;
        }

        imprime_stats(tempo_espera);
    }


    public static void PRIORIDADE(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();
        int[] prioridade_temp = prioridade.clone();

        //implementar código do Prioridade preemptivo e não preemptivo
        //...
        //

        imprime_stats(tempo_espera);

    }

    public static void Round_Robin(int[] execucao, int[] espera, int[] restante){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();


        //implementar código do Round-Robin
        //...
        //

        imprime_stats(tempo_espera);
    }
}