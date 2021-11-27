package lapr1_projeto_omen;

import java.util.Scanner;
import java.io.PrintWriter;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.FileTerminal;
import com.panayotis.gnuplot.terminal.GNUPlotTerminal;
import java.io.File;
import java.io.FileNotFoundException;

public class LAPR1_PROJETO_OMEN {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {
        // Alteração dos parâmetros apenas para fins de teste durante o desenvolvimeto
        /*args = new String[2];
        args[0] = "-nome";
        args[1] = "DAYTON.csv";
        args[2] = "-resolucao";
        args[3] = "4";
        args[4] = "-modelo";
        args[5] = "1";
        args[6] = "-tipoOrdenacao";
        args[7] = "1";
        args[8] = "-parModelo";
        args[9] = "1";
        args[10] = "-momentoPrevisao";
        args[11] = "2017";*/
        if (args.length == 2) {
            if (args[0].equals("-nome")) {
                File file = new File(args[1]);
                if (file.exists()) {
                    String nomeFicheiro = args[1].split(".csv")[0];
                    menu(file, nomeFicheiro);
                } else {
                    System.out.println("ERRO! O ficheiro " + args[1] + " não existe.");
                }
            } else {
                System.out.println("ERRO! Parâmetro inválido.");
                System.out.println("Parâmetros necessários: '-nome'");
                System.out.println("Exemplo: java -jar nome programa.jar -nome ts_nome_da_serie_temporal.csv");
            }
        } else if (args.length == 12) {
            if (args[0].equals("-nome") && args[2].equals("-resolucao") && args[4].equals("-modelo")
                    && args[6].equals("-tipoOrdenacao") && args[8].equals("-parModelo")
                    && args[10].equals("-momentoPrevisao")) {
                if (verificarParametros(args[1], args[3], args[5], args[7], args[9], args[11])) {
                    modoNaoInterativo(args[1], args[3], args[5], args[7], args[9], args[11]);
                }
            } else {
                System.out.println("ERRO! Parâmetro(s) inválido(s).");
                System.out.println("Parâmetros necessários: '-nome' '-resolucao' '-modelo' '-tipoOrdenacao' '-parModelo' '-momentoPrevisao'");
                System.out.println("Exemplo:  java -jar nome programa.jar -nome nome_ts_nome_da_serie_temporal.csv -resolucao X -modelo M -tipoOrdenacao T -parModelo nAlpha -momentoPrevisao D");
            }
        } else {
            System.out.println("ERRO! Número de parâmetros inválido.");
            System.out.println("Exemplo para usar modo interativo: java -jar nome programa.jar -nome ts_nome_da_serie_temporal.csv");
            System.out.println("Exemplo para usar modo não interativo:  java -jar nome programa.jar nome ts_nome_da_serie_temporal.csv -resolucao X -modelo M -tipoOrdenacao T -parModelo nAlpha -momentoPrevisao D");
        }
    }

    public static void menu(File file, String nomeFicheiro) throws FileNotFoundException {
        int periodo = 0;
        int numDados = contarDados(file);
        String[] datetime = new String[numDados];
        int[] dados = new int[numDados];
        for (int i = 0; i < numDados; i++) {
            datetime[i] = "";
        }
        int nDadosPeriodo = 0;
        String[] datetimePeriodo = new String[contarDados(file)];
        int[] dadosPeriodo = new int[contarDados(file)];
        for (int i = 0; i < numDados; i++) {
            datetimePeriodo[i] = "";
        }
        int tipoMMovel = 0;
        int nMMSimples = 0;
        double alphaMMEPesada = 0;
        double[] dadosMediaMovel = {};
        lerFicheiro(file, datetime, dados);
        String diretorio = "output";
        File dir = new File(diretorio);
        if (!dir.exists()) {
            dir.mkdir();
        }
        infoMenu();
        int menu = lerInt("Dígito: ");
        boolean sair;
        if (menu == 6) {
            sair = true;
        } else {
            sair = false;
        }
        while (!sair) {
            switch (menu) {
                case 1:
                    periodo = pedirPeriodo(periodo, numDados, dados, datetime, dadosPeriodo, datetimePeriodo);
                    nDadosPeriodo = escolherDadosPeriodo(periodo, dados, datetime, dadosPeriodo, datetimePeriodo);
                    obterOcorrencias(dadosPeriodo, nDadosPeriodo);
                    break;
                case 2:
                    periodo = pedirPeriodo(periodo, numDados, dados, datetime, dadosPeriodo, datetimePeriodo);
                    nDadosPeriodo = escolherDadosPeriodo(periodo, dados, datetime, dadosPeriodo, datetimePeriodo);
                    int[] dadosPeriodoOrdenado = new int[nDadosPeriodo];
                    String[] datetimePeriodoOrdenado = new String[nDadosPeriodo];
                    for (int i = 0; i < nDadosPeriodo; i++) {
                        dadosPeriodoOrdenado[i] = dadosPeriodo[i];
                        datetimePeriodoOrdenado[i] = datetimePeriodo[i];
                    }
                    System.out.println("1 - Ordenação crescente");
                    System.out.println("2 - Ordenação decrescente");
                    int ord;
                    do {
                        ord = lerInt("Digite uma opção válida: ");
                    } while (ord != 1 && ord != 2);
                    //final long START = System.nanoTime();
                    //insertionSort(true);
                    //bubbleSort(true);
                    if (ord == 1) {
                        mergeSort(dadosPeriodoOrdenado, datetimePeriodoOrdenado, 0, nDadosPeriodo - 1, true);
                    } else {
                        mergeSort(dadosPeriodoOrdenado, datetimePeriodoOrdenado, 0, nDadosPeriodo - 1, false);
                    }
                    //System.out.println("Time taken : " + ((END - START) / 1e+9) + " seconds");
                    drawGraph(dadosPeriodoOrdenado, dadosPeriodoOrdenado.length, diretorio, datetimePeriodoOrdenado, "Serie_Ordenada", periodo, true);

                    //for (int i = 0; i < 30; i++) System.out.println(dadosPeriodoOrdenado[i]);
                    break;
                case 3:
                    periodo = pedirPeriodo(periodo, numDados, dados, datetime, dadosPeriodo, datetimePeriodo);
                    nDadosPeriodo = escolherDadosPeriodo(periodo, dados, datetime, dadosPeriodo, datetimePeriodo);
                    dadosMediaMovel = new double[nDadosPeriodo];
                    double[] arr = calcularMediaMovel(dadosMediaMovel, dadosPeriodo, nDadosPeriodo, diretorio, datetimePeriodo, periodo, false);
                    tipoMMovel = (int) arr[0];
                    nMMSimples = (int) arr[1];
                    alphaMMEPesada = arr[2];
                    if (tipoMMovel == 1) {
                        double[] temp = new double[nDadosPeriodo - nMMSimples + 1];
                        for (int i = 0; i < nDadosPeriodo - nMMSimples + 1; i++) {
                            temp[i] = dadosMediaMovel[i];
                        }
                        dadosMediaMovel = temp;
                    }
                    System.out.println("O erro Médio Absoluto é " + erroMedioAbsoluto(nDadosPeriodo, dadosPeriodo, dadosMediaMovel, tipoMMovel, nMMSimples));
                    break;
                case 4:
                    periodo = pedirPeriodo(periodo, numDados, dados, datetime, dadosPeriodo, datetimePeriodo);
                    nDadosPeriodo = escolherDadosPeriodo(periodo, dados, datetime, dadosPeriodo, datetimePeriodo);
                    dadosMediaMovel = new double[nDadosPeriodo];
                    double[] arr2 = calcularMediaMovel(dadosMediaMovel, dadosPeriodo, nDadosPeriodo, diretorio, datetimePeriodo, periodo, true);
                    tipoMMovel = (int) arr2[0];
                    nMMSimples = (int) arr2[1];
                    alphaMMEPesada = arr2[2];
                    if (tipoMMovel == 1) {
                        double[] temp = new double[nDadosPeriodo - nMMSimples + 1];
                        for (int i = 0; i < nDadosPeriodo - nMMSimples + 1; i++) {
                            temp[i] = dadosMediaMovel[i];
                        }
                        dadosMediaMovel = temp;
                    }
                    String data;
                    if (periodo == 11 || periodo == 12 || periodo == 13 || periodo == 14 || periodo == 2) {
                        System.out.print("Digite a data para previsão (DDMMAAAA): ");
                        data = scanner.nextLine();
                    } else if (periodo == 3) {
                        System.out.print("Digite a data para previsão (MMAAAA): ");
                        data = scanner.nextLine();
                    } else {
                        System.out.print("Digite a data para previsão (AAAA): ");
                        data = scanner.nextLine();
                    }
                    double previsao = previsao(periodo, data, tipoMMovel, nDadosPeriodo, dadosPeriodo, datetimePeriodo, nMMSimples, alphaMMEPesada);
                    if (previsao == -1) {
                        System.out.println("Digitou uma data inválida");
                    } else {
                        System.out.println("A previsão para a data digitada é " + previsao);
                    }
                    break;
                case 5:
                    System.out.println("(O ficheiro a ser lido deve estar no mesmo diretório do executável)");
                    System.out.print("Digite o nome do ficheiro CSV a ser lido: ");
                    String nomeFile = scanner.nextLine();
                    file = new File(nomeFile);
                    if (file.exists()) {
                        numDados = contarDados(file);
                        datetime = new String[numDados];
                        dados = new int[numDados];
                        for (int i = 0; i < numDados; i++) {
                            datetime[i] = "";
                        }
                        lerFicheiro(file, datetime, dados);
                    } else {
                        System.out.println("O ficheiro " + nomeFile + " não existe.");
                    }
                    break;
                case 6:
                    sair = true;
                    break;
                default:
                    System.out.println("ERRO! Dígito inválido.");
                    break;
            }
            if (!sair) {
                infoMenu();
                menu = lerInt("Dígito: ");
            }
        }
    }

    public static void infoMenu() {
        for (int i = 0; i < 40; i++) {
            System.out.print("=-");
        }
        System.out.println("=");
        System.out.println("1 - Calcular o número de observaçõoes que ocorrem num conjunto de intervalos");
        System.out.println("2 - Ordenar os valores da série temporal por ordem crescente ou decrescente");
        System.out.println("3 - Suavização da serie temporal, através da Média Móvel");
        System.out.println("4 - Prever o valor de uma observação");
        System.out.println("5 - Carregar nova série temporal");
        System.out.println("6 - Encerrar programa");
        for (int i = 0; i < 40; i++) {
            System.out.print("=-");
        }
        System.out.println("=");
    }

    public static boolean verificarParametros(String nomeFicheiro, String periodo, String tipoMMovel, String tipoOrdenacao, String nAlpha, String dataPrevisao) throws FileNotFoundException {
        boolean flag = true;
        File file = new File(nomeFicheiro);
        if (file.exists()) {
            int res = Integer.parseInt(periodo);
            if ((res >= 11 && res <= 14) || (res >= 2 && res <= 4)) {
                int modelo = Integer.parseInt(tipoMMovel);
                if (modelo == 1 || modelo == 2) {
                    if (Integer.parseInt(tipoOrdenacao) == 1 || Integer.parseInt(tipoOrdenacao) == 2) {
                        int[] dados = new int[contarDados(file)];
                        String[] datetime = new String[contarDados(file)];
                        lerFicheiro(file, datetime, dados);
                        int[] dadosPeriodo = new int[dados.length];
                        String[] datetimePeriodo = new String[datetime.length];
                        int nDadosPeriodo = escolherDadosPeriodo(res, dados, datetime, dadosPeriodo, datetimePeriodo);
                        if (modelo == 1) {
                            int nMMSimples = Integer.parseInt(nAlpha);
                            if (nMMSimples >= 1 && nMMSimples < nDadosPeriodo) {
                                if (verificarData(dataPrevisao, res, modelo, nMMSimples, nDadosPeriodo, datetimePeriodo)) {

                                } else {
                                    flag = false;
                                    System.out.println("ERRO! Parâmetro '-momentoPrevisao' inválido. Data inválida.");
                                }
                            } else {
                                flag = false;
                                System.out.println("ERRO! Parâmetro '-parModelo' inválido. Para o modelo 1, pode tomar valores inteiros positivos menores que o número de observações da série original.");
                            }
                        } else {
                            double alphaMMEPesada = Double.parseDouble(nAlpha);
                            if (alphaMMEPesada > 0 && alphaMMEPesada <= 1) {

                            } else {
                                flag = false;
                                System.out.println("ERRO! Parâmetro '-parModelo' inválido. Para o modelo 2, está no intervalo ]0,1].");
                            }
                        }
                    } else {
                        flag = false;
                        System.out.println("ERRO! Parâmetro '-tipoOrdenacao' inválido. Pode tomar os valores 1 ou 2.");
                    }
                } else {
                    flag = false;
                    System.out.println("ERRO! Parâmetro '-modelo' inválido. Pode tomar os valores 1 ou 2.");
                }
            } else {
                flag = false;
                System.out.println("ERRO! Parâmetro '-resolucao' inválido. Pode tomar os valores 11,12,13,14,2,3,4.");
            }
        } else {
            flag = false;
            System.out.println("ERRO! O ficheiro " + nomeFicheiro + " não existe.");
        }
        return flag;
    }

    public static void modoNaoInterativo(String nomeFich, String resolucao, String modelo, String tipoOrdenacao, String nAlpha, String dataPrevisao) throws FileNotFoundException {
        File file = new File(nomeFich);
        int[] dados = new int[contarDados(file)];
        String[] datetime = new String[contarDados(file)];
        lerFicheiro(file, datetime, dados);
        String nomeFicheiro = nomeFich.split(".csv")[0];
        String diretorio = nomeFicheiro + "-" + resolucao + "-" + modelo + "-" + tipoOrdenacao + "-" + nAlpha + "-" + dataPrevisao;
        File dir = new File(diretorio);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File terminal = new File(diretorio + "/terminal.txt");
        PrintWriter writer = new PrintWriter(terminal);
        int periodo = Integer.parseInt(resolucao);
        int[] dadosPeriodo = new int[dados.length];
        String[] datetimePeriodo = new String[datetime.length];
        int nDadosPeriodo = escolherDadosPeriodo(periodo, dados, datetime, dadosPeriodo, datetimePeriodo);
        obterOcorrenciasNInterativo(dadosPeriodo, nDadosPeriodo, writer);
        int[] dadosPeriodoOrdenado = new int[nDadosPeriodo];
        String[] datetimePeriodoOrdenado = new String[nDadosPeriodo];
        for (int i = 0; i < nDadosPeriodo; i++) {
            dadosPeriodoOrdenado[i] = dadosPeriodo[i];
            datetimePeriodoOrdenado[i] = datetimePeriodo[i];
        }
        int ord = Integer.parseInt(tipoOrdenacao);
        if (ord == 1) {
            mergeSort(dadosPeriodoOrdenado, datetimePeriodoOrdenado, 0, nDadosPeriodo - 1, true);
        } else {
            mergeSort(dadosPeriodoOrdenado, datetimePeriodoOrdenado, 0, nDadosPeriodo - 1, false);
        }
        drawGraph(dadosPeriodoOrdenado, dadosPeriodoOrdenado.length, diretorio, datetimePeriodoOrdenado, "Serie_Ordenada", periodo, false);
        int tipoMMovel = Integer.parseInt(modelo);
        double[] dadosMediaMovel = new double[nDadosPeriodo];
        int nMMSimples = 0;
        double alphaMMEPesada = 0;
        if (tipoMMovel == 1) {
            nMMSimples = mediaMovelSimples(dadosMediaMovel, dadosPeriodo, nDadosPeriodo, diretorio, datetimePeriodo, periodo, false, false, Integer.parseInt(nAlpha));
            double[] temp = new double[nDadosPeriodo - nMMSimples + 1];
            for (int i = 0; i < nDadosPeriodo - nMMSimples + 1; i++) {
                temp[i] = dadosMediaMovel[i];
            }
            dadosMediaMovel = temp;
        } else {
            alphaMMEPesada = mediaMovelEPesada(dadosMediaMovel, dadosPeriodo, nDadosPeriodo, diretorio, datetimePeriodo, periodo, false, false, Double.parseDouble(nAlpha));
        }
        writer.println("O erro Médio Absoluto é " + erroMedioAbsoluto(nDadosPeriodo, dadosPeriodo, dadosMediaMovel, tipoMMovel, nMMSimples));
        String data = dataPrevisao;
        double previsao = previsao(periodo, data, tipoMMovel, nDadosPeriodo, dadosPeriodo, datetimePeriodo, nMMSimples, alphaMMEPesada);
        writer.println("A previsão para a data digitada é " + previsao);
        writer.close();
    }

    public static void lerFicheiro(File file, String[] datetime, int[] dados) throws FileNotFoundException {
        Scanner ler = new Scanner(file);
        int cont = 0;
        while (ler.hasNextLine()) {
            String linha = ler.nextLine();
            if (verificarLinha(linha)) {
                datetime[cont] = linha.split(",")[0];
                dados[cont] = Integer.parseInt(linha.split(",")[1]);
                cont++;
            }
        }
    }

    public static int contarDados(File file) throws FileNotFoundException {
        Scanner ler = new Scanner(file);
        int num = 0;
        while (ler.hasNextLine()) {
            if (verificarLinha(ler.nextLine())) {
                num++;
            }
        }
        return num;
    }

    public static int pedirPeriodo(int periodo, int numDados, int[] dados, String[] datetime, int[] dadosPeriodo, String[] datetimePeriodo) {
        do {
            infoPeriodo();
            periodo = lerInt("Introduza o número do periodo que quer: ");
        } while (!verificarPeriodo(periodo));
        for (int i = 0; i < numDados; i++) {
            dadosPeriodo[i] = 0;
        }
        for (int i = 0; i < numDados; i++) {
            datetimePeriodo[i] = "";
        }
        return periodo;
    }

    public static void infoPeriodo() {
        System.out.println("11 - manhã");
        System.out.println("12 - tarde");
        System.out.println("13 - noite");
        System.out.println("14 - madrugada");
        System.out.println(" 2 - diário ");
        System.out.println(" 3 - mensal");
        System.out.println(" 4 - anual");
    }

    public static boolean verificarLinha(String linha) {
        boolean verificacao = true;
        if (linha == null) {
            return false;
        }
        if (linha.split(",").length != 2) {
            verificacao = false;
        } else {
            try {
                int numero = Integer.parseInt(linha.split(",")[1]);
            } catch (NumberFormatException e) {
                verificacao = false;
            }
        }
        return verificacao;
    }

    public static int lerInt(String msg) {
        int numero = 0;
        boolean flag = false;
        do {
            System.out.print(msg);
            //Perguntar ao stor se não for necessário retira-se o try
            try {
                numero = Integer.parseInt(scanner.nextLine().trim());
                flag = true;
            } catch (NumberFormatException e) {
                System.out.println("Introduziu um número inválido!");
            }
        } while (!flag);
        return numero;

    }

    public static boolean verificarPeriodo(int numero) {
        if (numero == 11 || numero == 12 || numero == 13 || numero == 14 || numero == 2 || numero == 3 || numero == 4) {
            return true;
        } else {
            return false;
        }
    }

    public static int escolherDadosPeriodo(int periodo, int[] dados, String[] datetime, int[] dadosPeriodo, String[] datetimePeriodo) {
        int nDadosPeriodo;
        int j = 0, diaA = 0, mesA = 0, anoA = 0;
        for (int i = 0; i < dados.length; i++) {
            String dia = datetime[i].split(" ")[0];
            int hora = Integer.parseInt(datetime[i].split(" ")[1].split(":")[0]);
            boolean flag = false;
            switch (periodo) {
                case 11:
                    j = colocarValorPeriodoHora(6, 12, hora, i, j, dados, datetime, dadosPeriodo, datetimePeriodo);
                    break;
                case 12:
                    j = colocarValorPeriodoHora(12, 18, hora, i, j, dados, datetime, dadosPeriodo, datetimePeriodo);
                    break;
                case 13:
                    j = colocarValorPeriodoHora(18, 24, hora, i, j, dados, datetime, dadosPeriodo, datetimePeriodo);
                    break;
                case 14:
                    j = colocarValorPeriodoHora(0, 6, hora, i, j, dados, datetime, dadosPeriodo, datetimePeriodo);
                    break;
                case 2:
                    int diaI = Integer.parseInt(dia.split("-")[2]);
                    if (i == 0) {
                        diaA = diaI;
                        dadosPeriodo[j] += dados[i];
                    } else {
                        if (diaI != diaA) {
                            datetimePeriodo[j] = datetime[i - 1];
                            j++;
                        }
                        diaA = diaI;
                        dadosPeriodo[j] += dados[i];
                    }
                    break;
                case 3:
                    int mesI = Integer.parseInt(dia.split("-")[1]);
                    if (i == 0) {
                        mesA = mesI;
                        dadosPeriodo[j] += dados[i];
                    } else {
                        if (mesI != mesA) {
                            datetimePeriodo[j] = datetime[i - 1];
                            j++;
                        }
                        mesA = mesI;
                        dadosPeriodo[j] += dados[i];
                    }
                    break;
                case 4:
                    int anoI = Integer.parseInt(dia.split("-")[0]);
                    if (i == 0) {
                        anoA = anoI;
                        dadosPeriodo[j] += dados[i];
                    } else {
                        if (anoI != anoA) {
                            datetimePeriodo[j] = datetime[i - 1];
                            j++;
                        }
                        anoA = anoI;
                        dadosPeriodo[j] += dados[i];
                    }
                    break;
            }
        }
        if (periodo >= 2 && periodo <= 4) {
            datetimePeriodo[j] = datetime[datetime.length - 1];
            nDadosPeriodo = j + 1;
        } else {
            nDadosPeriodo = j;
        }
        return nDadosPeriodo;
    }

    public static int colocarValorPeriodoHora(int horaIni, int horaFim, int hora, int i, int j, int[] dados, String[] datetime, int[] dadosPeriodo, String[] datetimePeriodo) {
        if (hora >= horaIni && hora < horaFim) {
            dadosPeriodo[j] += dados[i];
        }
        if (hora == horaFim - 1 || (i == dados.length - 1 && (hora >= horaIni && hora < horaFim))) {
            datetimePeriodo[j] = datetime[i];
            j++;
        }
        return j;
    }

    public static void drawGraph(int[] data, int length, String diretorio, String[] datetime, String nomeTab1, int periodo, boolean interativo) throws FileNotFoundException {
        JavaPlot p = new JavaPlot();
        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINESPOINTS);
        myPlotStyle.setLineWidth(1);
        myPlotStyle.setLineType(NamedPlotColor.BLUE);
        myPlotStyle.setPointType(7);
        myPlotStyle.setPointSize(1);

        double tab[][] = new double[length][2];
        for (int i = 0; i < length; i++) {
            tab[i][0] = (double) i;
            tab[i][1] = (double) data[i];
        }
        DataSetPlot s = new DataSetPlot(tab);
        s.setTitle("Dados no período especificado");
        s.setPlotStyle(myPlotStyle);

        p.addPlot(s);
        
        if (interativo) {
            p.plot();
            askSave(tab, null, p, diretorio, datetime, nomeTab1, null, periodo);
        } else {
            convertPNG(p, diretorio, interativo, "ordenacao");
            convertCSV(datetime, tab, null, nomeTab1, null, diretorio, periodo, interativo, "ordenacao");
        }
    }

    public static int qntdOcorrencias(int[] dados, double mediaGlobal, int Intervalo, int nDadosPeriodo) {
        int contador = 0;
        for (int i = 0; i < nDadosPeriodo; i++) {
            switch (Intervalo) {
                case 1:
                    if (dados[i] < (mediaGlobal - 0.2 * mediaGlobal)) {
                        contador++;
                    }
                    break;
                case 2:
                    if ((mediaGlobal - 0.2 * mediaGlobal) <= dados[i] && (mediaGlobal + 0.2 * mediaGlobal) >= dados[i]) {
                        contador++;
                    }
                    break;
                case 3:
                    if (dados[i] >= (mediaGlobal + 0.2 * mediaGlobal)) {
                        contador++;
                    }
                    break;
                default:
                    System.out.println("Introduziu um intervalo errado");
                    return -1;
            }
        }
        return contador;
    }

    public static void obterOcorrencias(int[] dadosPeriodo, int nDadosPeriodo) {
        int ocorrencias;
        double mediaGlobal = mediaGlobal(dadosPeriodo, nDadosPeriodo);
        System.out.printf("Intervalo menor que %.2f \n", (mediaGlobal - 0.2 * mediaGlobal));
        ocorrencias = qntdOcorrencias(dadosPeriodo, mediaGlobal, 1, nDadosPeriodo);
        System.out.println(ocorrencias);
        System.out.printf("Intervalo entre %.2f e %.2f \n", (mediaGlobal - 0.2 * mediaGlobal), (mediaGlobal + 0.2 * mediaGlobal));
        ocorrencias = qntdOcorrencias(dadosPeriodo, mediaGlobal, 2, nDadosPeriodo);
        System.out.println(ocorrencias);
        System.out.printf("Intervalo maior que %.2f  \n", (mediaGlobal + 0.2 * mediaGlobal));
        ocorrencias = qntdOcorrencias(dadosPeriodo, mediaGlobal, 3, nDadosPeriodo);
        System.out.println(ocorrencias);
        //ocorrencias = qntdOcorrencias(dados,1779.5,4);
        //System.out.println(ocorrencias);
    }

    public static void obterOcorrenciasNInterativo(int[] dadosPeriodo, int nDadosPeriodo, PrintWriter writer) throws FileNotFoundException {
        int ocorrencias;
        //PrintWriter writer = new PrintWriter(file);
        double mediaGlobal = mediaGlobal(dadosPeriodo, nDadosPeriodo);
        writer.printf("Intervalo menor que %.2f \n", (mediaGlobal - 0.2 * mediaGlobal));
        ocorrencias = qntdOcorrencias(dadosPeriodo, mediaGlobal, 1, nDadosPeriodo);
        writer.println(ocorrencias);
        writer.printf("Intervalo entre %.2f e %.2f \n", (mediaGlobal - 0.2 * mediaGlobal), (mediaGlobal + 0.2 * mediaGlobal));
        ocorrencias = qntdOcorrencias(dadosPeriodo, mediaGlobal, 2, nDadosPeriodo);
        writer.println(ocorrencias);
        writer.printf("Intervalo maior que %.2f  \n", (mediaGlobal + 0.2 * mediaGlobal));
        ocorrencias = qntdOcorrencias(dadosPeriodo, mediaGlobal, 3, nDadosPeriodo);
        writer.println(ocorrencias);
    }

    public static void bubbleSort(boolean crescente, int[] dados) {
        if (crescente) {
            for (int i = 0; i < dados.length - 1; i++) {
                for (int x = i + 1; x < dados.length; x++) {
                    if (dados[x] <= dados[i]) {
                        int temp = dados[i];
                        dados[i] = dados[x];
                        dados[x] = temp;
                    }
                }
            }
        } else {
            for (int i = 0; i < dados.length - 1; i++) {
                for (int x = i + 1; x < dados.length; x++) {
                    if (dados[x] >= dados[i]) {
                        int temp = dados[i];
                        dados[i] = dados[x];
                        dados[x] = temp;
                    }
                }
            }
        }
    }

    public static void insertionSort(boolean crescente, int[] dados) {
        if (crescente) {
            for (int j = 1; j < dados.length; j++) {
                int key = dados[j];
                int i = j - 1;
                while ((i > -1) && (dados[i] >= key)) {
                    dados[i + 1] = dados[i];
                    i = i - 1;
                }
                dados[i + 1] = key;
            }
        } else {
            for (int j = 1; j < dados.length; j++) {
                int key = dados[j];
                int i = j - 1;
                while ((i > -1) && (dados[i] <= key)) {
                    dados[i + 1] = dados[i];
                    i = i - 1;
                }
                dados[i + 1] = key;
            }
        }
    }

    public static void sort(int[] dados, String[] datetime, int l, int m, int r, boolean crescente) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int[] L = new int[n1];
        int[] R = new int[n2];
        String[] sL = new String[n1];
        String[] sR = new String[n2];
        for (int i = 0; i < n1; ++i) {
            L[i] = dados[l + i];
            sL[i] = datetime[l + i];
        }
        for (int j = 0; j < n2; ++j) {
            R[j] = dados[m + 1 + j];
            sR[j] = datetime[m + 1 + j];
        }
        int i = 0, j = 0;
        int k = l;
        if (crescente) {
            while (i < n1 && j < n2) {
                if (L[i] <= R[j]) {
                    dados[k] = L[i];
                    datetime[k] = sL[i];
                    i++;
                } else {
                    dados[k] = R[j];
                    datetime[k] = sR[j];
                    j++;
                }
                k++;
            }
            while (i < n1) {
                dados[k] = L[i];
                datetime[k] = sL[i];
                i++;
                k++;
            }
            while (j < n2) {
                dados[k] = R[j];
                datetime[k] = sR[j];
                j++;
                k++;
            }
        } else {
            while (i < n1 && j < n2) {
                if (L[i] >= R[j]) {
                    dados[k] = L[i];
                    datetime[k] = sL[i];
                    i++;
                } else {
                    dados[k] = R[j];
                    datetime[k] = sR[j];
                    j++;
                }
                k++;
            }
            while (i < n1) {
                dados[k] = L[i];
                datetime[k] = sL[i];
                i++;
                k++;
            }
            while (j < n2) {
                dados[k] = R[j];
                datetime[k] = sR[j];
                j++;
                k++;
            }
        }
    }

    public static void mergeSort(int[] dados, String[] datetime, int l, int r, boolean crescente) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(dados, datetime, l, m, crescente);
            mergeSort(dados, datetime, m + 1, r, crescente);
            sort(dados, datetime, l, m, r, crescente);
        }
    }

    public static double mediaGlobal(int[] dados, int length) {
        double somatorio = 0;
        for (int i = 0; i < length; i++) {
            somatorio += dados[i];
        }
        somatorio = somatorio / length;
        // somatorio  = Double.parseDouble(df2.format(somatorio));
        return somatorio;

    }

    public static double[] calcularMediaMovel(double[] dadosMediaMovel, int[] dadosPeriodo, int nDadosPeriodo, String diretorio, String[] datetimePeriodo, int periodo, boolean previsao) throws FileNotFoundException {
        System.out.println("1 - Média Móvel Simples");
        System.out.println("2 - Média Móvel Exponencialmente Pesada");
        int tipoMMovel;
        do {
            tipoMMovel = lerInt("Digite uma opção válida: ");
        } while (tipoMMovel != 1 && tipoMMovel != 2);
        int nMMSimples = 0;
        double alphaMMEPesada = 0;
        if (tipoMMovel == 1) {
            nMMSimples = mediaMovelSimples(dadosMediaMovel, dadosPeriodo, nDadosPeriodo, diretorio, datetimePeriodo, periodo, previsao, true, 0);
        } else {
            alphaMMEPesada = mediaMovelEPesada(dadosMediaMovel, dadosPeriodo, nDadosPeriodo, diretorio, datetimePeriodo, periodo, previsao, true, 0);
        }
        double[] arr = {tipoMMovel, nMMSimples, alphaMMEPesada};
        return arr;
    }

    public static int mediaMovelSimples(double[] dadosMediaMovel, int[] dadosPeriodo, int nDadosPeriodo, String diretorio, String[] datetimePeriodo, int periodo, boolean previsao, boolean interativo, int n) throws FileNotFoundException {
        int nMMSimples;
        if (interativo) {
            do {
                nMMSimples = lerInt("Introduza o valor de n (n >= 1): ");
            } while (nMMSimples < 1 || nMMSimples >= nDadosPeriodo);
        } else {
            nMMSimples = n;
        }
        for (int i = nMMSimples - 1; i < nDadosPeriodo; i++) {
            for (int k = 0; k <= nMMSimples - 1; k++) {
                dadosMediaMovel[i - nMMSimples + 1] += dadosPeriodo[i - k] / (double) nMMSimples;
            }
        }
        //for (int i = 0; i < 10; i++) System.out.println(dadosMediaMovel[i]);

        if (!previsao) {
            drawGraphMMovel(nMMSimples - 1, nDadosPeriodo, dadosPeriodo, dadosMediaMovel, diretorio, datetimePeriodo, periodo, interativo);
        }

        return nMMSimples;
    }

    public static double mediaMovelEPesada(double[] dadosMediaMovel, int[] dadosPeriodo, int nDadosPeriodo, String diretorio, String[] datetimePeriodo, int periodo, boolean previsao, boolean interativo, double alpha) throws FileNotFoundException {
        double alphaMMEPesada;
        if (interativo) {
            do {
                System.out.print("Introduza um valor de alpha (]0,1]): ");
                alphaMMEPesada = scanner.nextDouble();
            } while (alphaMMEPesada <= 0 || alphaMMEPesada > 1);
            scanner.nextLine();
        } else {
            alphaMMEPesada = alpha;
        }
        dadosMediaMovel[0] = dadosPeriodo[0];
        for (int i = 1; i < nDadosPeriodo; i++) {
            dadosMediaMovel[i] = alphaMMEPesada * dadosPeriodo[i] + (1 - alphaMMEPesada) * dadosMediaMovel[i - 1];
        }
        //for (int i = 0; i < 10; i++) System.out.println(dadosMediaMovel[i]);
        if (!previsao) {
            drawGraphMMovel(0, nDadosPeriodo, dadosPeriodo, dadosMediaMovel, diretorio, datetimePeriodo, periodo, interativo);
        }

        return alphaMMEPesada;
    }

    public static void drawGraphMMovel(int atraso, int nDadosPeriodo, int[] dadosPeriodo, double[] dadosMediaMovel, String diretorio, String[] datetime, int periodo, boolean interativo) throws FileNotFoundException {
        JavaPlot p = new JavaPlot();

        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINES);
        myPlotStyle.setLineWidth(1);
        myPlotStyle.setLineType(NamedPlotColor.BLUE);
        myPlotStyle.setPointType(7);
        myPlotStyle.setPointSize(1);
        double tab[][] = new double[nDadosPeriodo][2];
        for (int i = 0; i < nDadosPeriodo; i++) {
            tab[i][0] = (double) i;
            tab[i][1] = (double) dadosPeriodo[i];
        }
        DataSetPlot s = new DataSetPlot(tab);
        s.setTitle("Dados no período especificado");
        s.setPlotStyle(myPlotStyle);

        PlotStyle myPlotStyle2 = new PlotStyle();
        myPlotStyle2.setStyle(Style.LINES);
        myPlotStyle2.setLineWidth(1);
        myPlotStyle2.setLineType(NamedPlotColor.RED);
        myPlotStyle2.setPointType(7);
        myPlotStyle2.setPointSize(1);
        double tab2[][] = new double[dadosMediaMovel.length][2];
        for (int i = 0; i < dadosMediaMovel.length; i++) {
            tab2[i][0] = (double) i + atraso;
            tab2[i][1] = (double) dadosMediaMovel[i];
        }
        DataSetPlot s2 = new DataSetPlot(tab2);
        s2.setTitle("Suavização");
        s2.setPlotStyle(myPlotStyle2);

        p.addPlot(s);
        p.addPlot(s2);
        
        if (interativo) {
            p.plot();
            askSave(tab, tab2, p, diretorio, datetime, "Serie_Original", "Media_Movel", periodo);
        } else {
            convertPNG(p, diretorio, interativo, "mediaMovel");
            convertCSV(datetime, tab, tab2, "SerieOriginal", "MediaMovel", diretorio, periodo, interativo, "mediaMovel");
        }
    }

    public static double erroMedioAbsoluto(int nDadosPeriodo, int[] dadosPeriodo, double[] dadosMediaMovel, int tipoMMovel, int nMMSimples) {
        double erroMedio = 0;
        for (int i = 0; i < nDadosPeriodo; i++) {
            if (tipoMMovel == 1) {
                if (i >= nMMSimples - 1) {
                    erroMedio += Math.abs(dadosMediaMovel[i - nMMSimples + 1] - dadosPeriodo[i]);
                }
            } else {
                erroMedio += Math.abs(dadosMediaMovel[i] - dadosPeriodo[i]);
            }
        }
        erroMedio = erroMedio / nDadosPeriodo;
        return erroMedio;
    }

    public static void convertPNG(JavaPlot p, String diretorio, boolean interativo, String nome) {
        String nome_ficheiro;
        if (interativo) {
            scanner.nextLine();
            System.out.println("Indique o nome para o ficheiro PNG");
            System.out.print("Nome do ficheiro: ");
            nome_ficheiro = scanner.nextLine();
        } else {
            nome_ficheiro = nome;
        }
        File file = new File(nome_ficheiro);
        GNUPlotTerminal terminal = new FileTerminal("png", diretorio + "/" + nome_ficheiro + ".png");
        p.setTerminal(terminal);
        p.setKey(JavaPlot.Key.OFF);
        p.plot();

    }

    public static void convertCSV(String[] datetime, double[][] tab1, double[][] tab2, String nomeTab1, String nomeTab2, String diretorio, int periodo, boolean interativo, String nome) {
        String nome_ficheiro, nomeColunas = "Datetime," + nomeTab1;
        if (tab2 != null) {
            nomeColunas += "," + nomeTab2;
        }
        nomeColunas += "\n";
        if (interativo) {
            System.out.println("Indique o nome para o ficheiro CSV");
            System.out.print("Nome do ficheiro: ");
            nome_ficheiro = scanner.nextLine();
        } else {
            nome_ficheiro = nome;
        }
        File file = new File(nome_ficheiro);
        try (PrintWriter writer = new PrintWriter(new File(diretorio + "/" + nome_ficheiro + ".csv"))) {
            writer.write(nomeColunas);
            for (int i = 0; i < tab1.length; i++) {
                String date;
                if ((periodo >= 11 && periodo <= 14) || periodo == 2) {
                    date = datetime[i].split(" ")[0];
                } else if (periodo == 3) {
                    date = datetime[i].substring(0, 7);
                } else {
                    date = datetime[i].substring(0, 4);
                }
                if (tab2 == null) {
                    writer.write(date + "," + Double.toString(tab1[i][1]) + "\n");
                } else {
                    int atraso = tab1.length - tab2.length;
                    if (i < atraso) {
                        writer.write(date + "," + Double.toString(tab1[i][1]) + "\n");
                    } else {
                        writer.write(date + "," + Double.toString(tab1[i][1]) + "," + Double.toString(tab2[i - atraso][1]) + "\n");
                    }
                }
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void askSave(double[][] tab1, double[][] tab2, JavaPlot p, String diretorio, String[] datetime, String nomeTab1, String nomeTab2, int periodo) throws FileNotFoundException {
        System.out.println("Introduza uma opção válida");
        System.out.println("1: Não guardar dados");
        System.out.println("2: Guardar o gráfico em formato 'png'");
        System.out.println("3: Guardar os dados em formato 'csv'");
        System.out.println("4: Guardar o gráfico e os dados em formato 'png' e 'csv' respetivamente ");
        System.out.print("Dígito: ");
        int opcao = scanner.nextInt();
        boolean flag = true;
        while (flag) {
            switch (opcao) {
                case (1):
                    scanner.nextLine();
                    flag = false;
                    break;
                case (2):
                    convertPNG(p, diretorio, true, null);
                    flag = false;
                    break;
                case (3):
                    scanner.nextLine();
                    if (tab2 == null) {
                        convertCSV(datetime, tab1, null, nomeTab1, nomeTab2, diretorio, periodo, true, null);
                    } else {
                        convertCSV(datetime, tab1, tab2, nomeTab1, nomeTab2, diretorio, periodo, true, null);
                    }
                    flag = false;
                    break;
                case (4):
                    convertPNG(p, diretorio, true, null);
                    if (tab2 == null) {
                        convertCSV(datetime, tab1, null, nomeTab1, nomeTab2, diretorio, periodo, true, null);
                    } else {
                        convertCSV(datetime, tab1, tab2, nomeTab1, nomeTab2, diretorio, periodo, true, null);
                    }
                    flag = false;
                    break;
                default:
                    System.out.println("Introduza uma opção válida");
                    System.out.print("Introduza uma nova opção: ");
                    opcao = scanner.nextInt();
                    break;
            }

        }
    }

    public static double previsao(int periodo, String data, int tipoMMovel, int nDadosPeriodo, int[] dadosPeriodo, String[] datetimePeriodo, int nMMSimples, double alphaMMEPesada) {
        double previsao = -1;
        if (verificarData(data, periodo, tipoMMovel, nMMSimples, nDadosPeriodo, datetimePeriodo)) {
            int i = indexData(data, nDadosPeriodo, datetimePeriodo);
            if (i == -1) {
                if (tipoMMovel == 1) {
                    previsao = 0;
                    for (int k = nDadosPeriodo - 1; k >= nDadosPeriodo - nMMSimples; k--) {
                        previsao += dadosPeriodo[k] / nMMSimples;
                    }
                } else {
                    double y = dadosPeriodo[0];
                    for (int j = 1; j < nDadosPeriodo; j++) {
                        previsao = alphaMMEPesada * dadosPeriodo[j] + (1 - alphaMMEPesada) * y;
                        y = previsao;
                    }
                }
            } else {
                if (tipoMMovel == 1) {
                    previsao = 0;
                    for (int k = 0; k < nMMSimples; k++) {
                        previsao += dadosPeriodo[i - 1 - k] / nMMSimples;
                    }
                } else {
                    double y = dadosPeriodo[0];
                    for (int j = 1; j <= i; j++) {
                        previsao = alphaMMEPesada * dadosPeriodo[j] + (1 - alphaMMEPesada) * y;
                        y = previsao;
                    }
                }
            }
        }
        return previsao;
    }

    public static boolean verificarData(String data, int periodo, int tipoMMovel, int nMMSimples, int nDadosPeriodo, String[] datetimePeriodo) {
        boolean flag = true;
        if (periodo == 11 || periodo == 12 || periodo == 13 || periodo == 14 || periodo == 2) {
            if (data.length() != 8) {
                flag = false;
            }
            int dia = Integer.parseInt(data.substring(0, 2));
            int mes = Integer.parseInt(data.substring(2, 4));
            int ano = Integer.parseInt(data.substring(4));
            if (diaValido(dia, mes, ano)) {
                if (tipoMMovel == 1) {
                    for (int i = 0; i < nMMSimples; i++) {
                        if (dia == 1) {
                            if (mes == 1) {
                                if (!verificarAno(ano, nDadosPeriodo, datetimePeriodo)) {
                                    flag = false;
                                    break;
                                }
                                mes = 12;
                                ano--;
                            } else {
                                if (!verificarMesAno(mes, ano, nDadosPeriodo, datetimePeriodo)) {
                                    flag = false;
                                    break;
                                }
                                mes--;
                            }
                            if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
                                dia = 31;
                            } else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
                                dia = 30;
                            } else {
                                if (ano % 4 == 0) {
                                    dia = 29;
                                } else {
                                    dia = 28;
                                }
                            }
                        } else {
                            if (!verificarDiaMesAno(dia, mes, ano, nDadosPeriodo, datetimePeriodo)) {
                                flag = false;
                                break;
                            }
                            dia--;
                        }
                    }
                } else {
                    if (dia == 1) {
                        if (mes == 1) {
                            if (!verificarAno(ano, nDadosPeriodo, datetimePeriodo)) {
                                flag = false;
                            }
                        } else {
                            if (!verificarMesAno(mes, ano, nDadosPeriodo, datetimePeriodo)) {
                                flag = false;
                            }
                        }
                    } else {
                        if (!verificarDiaMesAno(dia, mes, ano, nDadosPeriodo, datetimePeriodo)) {
                            flag = false;
                        }
                    }
                }
            } else {
                flag = false;
            }
        } else if (periodo == 3) {
            if (data.length() != 6) {
                flag = false;
            }
            int mes = Integer.parseInt(data.substring(0, 2));
            int ano = Integer.parseInt(data.substring(2));
            if (mes <= 12) {
                if (tipoMMovel == 1) {
                    for (int i = 0; i < nMMSimples; i++) {
                        if (mes == 1) {
                            if (!verificarAno(ano, nDadosPeriodo, datetimePeriodo)) {
                                flag = false;
                                break;
                            }
                            mes = 12;
                            ano--;
                        } else {
                            if (!verificarMesAno(mes, ano, nDadosPeriodo, datetimePeriodo)) {
                                flag = false;
                                break;
                            }
                            mes--;
                        }
                    }
                } else {
                    if (mes == 1) {
                        if (!verificarAno(ano, nDadosPeriodo, datetimePeriodo)) {
                            flag = false;
                        }
                    } else {
                        if (!verificarMesAno(mes, ano, nDadosPeriodo, datetimePeriodo)) {
                            flag = false;
                        }
                    }
                }
            } else {
                flag = false;
            }
        } else {
            if (data.length() != 4) {
                flag = false;
            }
            int ano = Integer.parseInt(data);
            if (tipoMMovel == 1) {
                for (int i = 0; i < nMMSimples; i++) {
                    if (!verificarAno(ano, nDadosPeriodo, datetimePeriodo)) {
                        flag = false;
                        break;
                    }
                    ano--;
                }
            } else {
                if (!verificarAno(ano, nDadosPeriodo, datetimePeriodo)) {
                    flag = false;
                }
            }
        }
        return flag;

    }

    public static boolean verificarMesAno(int mes, int ano, int nDadosPeriodo, String[] datetimePeriodo) {
        boolean flag = false;
        if (mes == 1) {
            mes = 12;
            ano--;
        } else {
            mes--;
        }
        for (int i = 0; i < nDadosPeriodo; i++) {
            int m = Integer.parseInt(datetimePeriodo[i].split(" ")[0].split("-")[1]);
            int a = Integer.parseInt(datetimePeriodo[i].split(" ")[0].split("-")[0]);
            if (mes == m && ano == a) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static boolean verificarAno(int ano, int nDadosPeriodo, String[] datetimePeriodo) {
        boolean flag = false;
        ano--;
        for (int i = 0; i < nDadosPeriodo; i++) {
            int a = Integer.parseInt(datetimePeriodo[i].split(" ")[0].split("-")[0]);
            if (ano == a) {
                flag = true;
                break;
            }

        }
        return flag;
    }

    public static boolean verificarDiaMesAno(int dia, int mes, int ano, int nDadosPeriodo, String[] datetimePeriodo) {
        boolean flag = false;
        if (dia == 1) {
            if (mes == 1) {
                mes = 12;
                ano--;
            } else {
                mes--;
            }
            if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
                dia = 31;
            } else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
                dia = 30;
            } else {
                if (ano % 4 == 0) {
                    dia = 29;
                } else {
                    dia = 28;
                }
            }
        } else {
            dia--;
        }
        for (int i = 0; i < nDadosPeriodo; i++) {
            int d = Integer.parseInt(datetimePeriodo[i].split(" ")[0].split("-")[2]);
            int m = Integer.parseInt(datetimePeriodo[i].split(" ")[0].split("-")[1]);
            int a = Integer.parseInt(datetimePeriodo[i].split(" ")[0].split("-")[0]);
            if (dia == d && mes == m && ano == a) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static int indexData(String data, int nDadosPeriodo, String[] datetimePeriodo) {
        int index = -1, tipo = 0, dia = 0, mes = 0, ano = 0;
        switch (data.length()) {
            case 8:
                tipo = 1;
                dia = Integer.parseInt(data.substring(0, 2));
                mes = Integer.parseInt(data.substring(2, 4));
                ano = Integer.parseInt(data.substring(4));
                break;
            case 6:
                tipo = 2;
                mes = Integer.parseInt(data.substring(0, 2));
                ano = Integer.parseInt(data.substring(2));
                break;
            case 4:
                tipo = 3;
                ano = Integer.parseInt(data);
                break;
        }
        for (int i = 0; i < nDadosPeriodo; i++) {
            int d = Integer.parseInt(datetimePeriodo[i].split(" ")[0].split("-")[2]);
            int m = Integer.parseInt(datetimePeriodo[i].split(" ")[0].split("-")[1]);
            int a = Integer.parseInt(datetimePeriodo[i].split(" ")[0].split("-")[0]);
            if (tipo == 1) {
                if (dia == d && mes == m && ano == a) {
                    index = i;
                }
            } else if (tipo == 2) {
                if (mes == m && ano == a) {
                    index = i;
                }
            } else {
                if (ano == a) {
                    index = i;
                }
            }
        }
        return index;
    }

    public static boolean diaValido(int dia, int mes, int ano) {
        boolean res = false;
        if (mes <= 12) {
            if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
                if (dia <= 31) {
                    res = true;
                }
            } else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
                if (dia <= 30) {
                    res = true;
                }
            } else {
                if (ano % 4 == 0) {
                    if (dia <= 29) {
                        res = true;
                    }
                } else {
                    if (dia <= 28) {
                        res = true;
                    }
                }
            }
        }
        return res;
    }
}
