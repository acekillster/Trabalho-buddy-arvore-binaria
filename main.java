import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    static class NoMemoria {

        int tamanhoKb;
        String estado;
        String idArquivo;

        NoMemoria esquerda;
        NoMemoria direita;
        NoMemoria pai;

        public NoMemoria(int tamanhoKb) {
            this.tamanhoKb = tamanhoKb;
            this.estado = "LIVRE";
            this.idArquivo = "";
        }

        public boolean ehFolha() {
            return esquerda == null && direita == null;
        }
    }

    static class NoFila {

        String idArquivo;
        int tamanhoKb;

        NoFila proximo;

        public NoFila(String idArquivo, int tamanhoKb) {
            this.idArquivo = idArquivo;
            this.tamanhoKb = tamanhoKb;
        }
    }

    static class FilaPendentes {

        NoFila inicio;
        NoFila fim;

        int tamanho;

        public void enfileirar(String idArquivo, int tamanhoKb) {

            NoFila novo = new NoFila(idArquivo, tamanhoKb);

            if(inicio == null){
                inicio = novo;
                fim = novo;
            } else {
                fim.proximo = novo;
                fim = novo;
            }

            tamanho++;
        }

        public NoFila desenfileirar() {

            if(inicio == null){
                return null;
            }

            NoFila removido = inicio;

            inicio = inicio.proximo;

            if(inicio == null){
                fim = null;
            }

            tamanho--;

            return removido;
        }

        public NoFila espiar() {
            return inicio;
        }

        public boolean estaVazia() {
            return inicio == null;
        }

        public int tamanho() {
            return tamanho;
        }

        public void mostrarFila() {

            NoFila atual = inicio;

            if(atual == null){
                System.out.println("fila vazia");
                return;
            }

            while(atual != null){

                System.out.println(atual.idArquivo + " - " + atual.tamanhoKb + " KB");

                atual = atual.proximo;
            }
        }
    }

    static class Operacao {

        String tipo;
        String idArquivo;
        int tamanhoKb;

        public Operacao(String tipo,String idArquivo,int tamanhoKb) {

            this.tipo = tipo;
            this.idArquivo = idArquivo;
            this.tamanhoKb = tamanhoKb;
        }
    }

    static class NoPilha {

        Operacao operacao;
        NoPilha proximo;

        public NoPilha(Operacao operacao) {
            this.operacao = operacao;
        }
    }

    static class PilhaHistorico {

        NoPilha topo;

        public void empilhar(Operacao operacao) {

            NoPilha novo = new NoPilha(operacao);

            novo.proximo = topo;
            topo = novo;
        }

        public Operacao desempilhar() {

            if(topo == null){
                return null;
            }

            Operacao operacao = topo.operacao;

            topo = topo.proximo;

            return operacao;
        }

        public boolean estaVazia() {
            return topo == null;
        }
    }

    static class NoLista {

        NoMemoria bloco;
        NoLista proximo;

        public NoLista(NoMemoria bloco) {
            this.bloco = bloco;
        }
    }

    static class ListaLivre {

        NoLista inicio;
        int tamanho;

        public void inserir(NoMemoria bloco) {

            NoLista novo = new NoLista(bloco);

            novo.proximo = inicio;
            inicio = novo;

            tamanho++;
        }

        public void remover(NoMemoria bloco) {

            NoLista atual = inicio;
            NoLista anterior = null;

            while(atual != null){

                if(atual.bloco == bloco){

                    if(anterior == null){
                        inicio = atual.proximo;
                    } else {
                        anterior.proximo = atual.proximo;
                    }

                    tamanho--;
                    return;
                }

                anterior = atual;
                atual = atual.proximo;
            }
        }

        public NoMemoria pegarPrimeiro() {

            if(inicio == null){
                return null;
            }

            return inicio.bloco;
        }

        public boolean estaVazia() {
            return inicio == null;
        }

        public int tamanho() {
            return tamanho;
        }
    }

    static class Buddy {

        NoMemoria raiz;

        ListaLivre[] listasLivres;

        FilaPendentes fila;

        PilhaHistorico pilha;

        int[] tamanhos;

        public Buddy() {

            raiz = new NoMemoria(32768);

            fila = new FilaPendentes();

            pilha = new PilhaHistorico();

            tamanhos = new int[]{
                4,8,16,32,64,128,256,
                512,1024,2048,4096,
                8192,16384,32768
            };

            listasLivres = new ListaLivre[14];

            for(int i = 0; i < 14; i++){
                listasLivres[i] =
                    new ListaLivre();
            }

            listasLivres[13]
                .inserir(raiz);
        }

        public int proximaPotencia2(int tamanho) {

            if(tamanho < 4){
                return 4;
            }

            int valor = 4;

            while(valor < tamanho){
                valor = valor * 2;
            }

            return valor;
        }

        public int pegarIndiceLista(int tamanho) {

            for(int i = 0;i < tamanhos.length;i++){

                if(tamanhos[i] == tamanho){
                    return i;
                }
            }

            return -1;
        }

        public NoMemoria buscarArquivo(
                NoMemoria no,
                String idArquivo
        ){

            if(no == null){
                return null;
            }

            if(
                no.estado.equals("OCUPADO")
                &&
                no.idArquivo.equals(idArquivo)
            ){
                return no;
            }

            NoMemoria esquerda =
                buscarArquivo(no.esquerda,idArquivo);

            if(esquerda != null){
                return esquerda;
            }

            return buscarArquivo(no.direita,idArquivo);
        }

                public NoMemoria fazerSplit(
                NoMemoria bloco,
                int tamanhoDesejado
        ){

            if(bloco == null){
                return null;
            }

            if(!bloco.estado.equals("LIVRE")){
                return null;
            }

            if(bloco.tamanhoKb == tamanhoDesejado){
                return bloco;
            }

            int metade =
                bloco.tamanhoKb / 2;

            if(metade < 4){
                return null;
            }

            int indiceMaior =
                pegarIndiceLista(bloco.tamanhoKb);

            listasLivres[indiceMaior]
                .remover(bloco);

            bloco.estado =
                "DIVIDIDO";

            bloco.esquerda =
                new NoMemoria(metade);

            bloco.direita =
                new NoMemoria(metade);

            bloco.esquerda.pai =
                bloco;

            bloco.direita.pai =
                bloco;

            int indiceMenor = pegarIndiceLista(metade);

            listasLivres[indiceMenor]
                .inserir(bloco.esquerda);

            listasLivres[indiceMenor]
                .inserir(bloco.direita);

            return fazerSplit(bloco.esquerda,tamanhoDesejado);
        }

        public void alocar(String idArquivo,int tamanhoKb){

            int tamanhoReal = proximaPotencia2(tamanhoKb);

            int indice = pegarIndiceLista(tamanhoReal);

            NoMemoria blocoLivre = null;

            for(int i = indice;i < listasLivres.length;i++){

                if(!listasLivres[i].estaVazia()){

                    blocoLivre =
                        listasLivres[i]
                            .pegarPrimeiro();

                    break;
                }
            }

            if(blocoLivre == null){

                fila.enfileirar(idArquivo,tamanhoKb);

                System.out.println("Sem memoria. " + idArquivo + " entrou na fila");

                return;
            }

            NoMemoria blocoFinal = fazerSplit(blocoLivre,tamanhoReal);

            if(blocoFinal == null){

                fila.enfileirar(idArquivo,tamanhoKb);

                return;
            }

            int indiceLista =pegarIndiceLista(blocoFinal.tamanhoKb);

            listasLivres[indiceLista].remover(blocoFinal);

            blocoFinal.estado ="OCUPADO";

            blocoFinal.idArquivo =idArquivo;

            pilha.empilhar(new Operacao("ALOCAR",idArquivo,tamanhoKb)
            );

            System.out.println(idArquivo + " alocado");
        }

        public void liberar(String idArquivo){

            NoMemoria bloco = buscarArquivo(raiz,idArquivo);

            if(bloco == null){

                System.out.println("arquivo nao encontrado");

                return;
            }

            bloco.estado ="LIVRE";

            bloco.idArquivo ="";

            int indice = pegarIndiceLista(bloco.tamanhoKb);

            listasLivres[indice].inserir(bloco);

            pilha.empilhar(new Operacao("liberar",idArquivo,bloco.tamanhoKb));

            fazerMerge(bloco);

            tentarFila();

            System.out.println(idArquivo + " liberado");
        }

        public void fazerMerge(NoMemoria bloco){

            if(bloco == null || bloco.pai == null){
                return;
            }

            NoMemoria pai = bloco.pai;

            NoMemoria buddy;

            if(pai.esquerda == bloco){

                buddy = pai.direita;

            } else {

                buddy = pai.esquerda;
            }

            if(buddy == null){
                return;
            }

            if(buddy.estado.equals("LIVRE") && bloco.estado.equals("LIVRE") && buddy.ehFolha() && bloco.ehFolha()){

                int indiceFilho = pegarIndiceLista(bloco.tamanhoKb);

                listasLivres[indiceFilho].remover(bloco);

                listasLivres[indiceFilho].remover(buddy);

                pai.esquerda = null;

                pai.direita = null;

                pai.estado = "LIVRE";

                int indicePai = pegarIndiceLista(pai.tamanhoKb);

                listasLivres[indicePai].inserir(pai);

                fazerMerge(pai);
            }
        }

        public void tentarFila(){

            int tamanhoFila = fila.tamanho();

            for(int i = 0;i < tamanhoFila;i++){

                NoFila pedido = fila.desenfileirar();

                if(pedido == null){
                    return;
                }

                int tamanhoReal = proximaPotencia2(pedido.tamanhoKb);

                int indice = pegarIndiceLista(tamanhoReal);

                boolean consegue = false;

                for(int j = indice;j < listasLivres.length;j++){

                    if(!listasLivres[j].estaVazia()){
                        consegue = true;
                        break;
                    }
                }

                if(consegue){

                    alocar(pedido.idArquivo,pedido.tamanhoKb);

                } else {

                    fila.enfileirar(pedido.idArquivo,pedido.tamanhoKb);
                }
            }
        }
                public void desfazer(){

            if(pilha.estaVazia()){

                System.out.println("nada para desfazer");

                return;
            }

            Operacao operacao = pilha.desempilhar();

            if(operacao.tipo.equals("ALOCAR")){

                NoMemoria bloco = buscarArquivo(raiz,operacao.idArquivo);

                if(bloco != null){

                    bloco.estado =
                        "LIVRE";

                    bloco.idArquivo =
                        "";

                    int indice = pegarIndiceLista(bloco.tamanhoKb);

                    listasLivres[indice].inserir(bloco);

                    fazerMerge(bloco);

                    System.out.println("Undo de " + operacao.idArquivo);
                }

            } else {

                alocar(operacao.idArquivo,operacao.tamanhoKb);

                System.out.println("Undo de " + operacao.idArquivo);
            }
        }

        public void mostrarArvore(){

            mostrarNo(raiz,0);
        }

        public void mostrarNo(NoMemoria no,int nivel){

            if(no == null){
                return;
            }

            for(int i = 0;i < nivel;i++
            ){
                System.out.print("   ");
            }

            System.out.print("[" + no.tamanhoKb + "KB");

            System.out.print(" " + no.estado);

            if(no.estado.equals("OCUPADO")){

                System.out.print(" " + no.idArquivo);
            }

            System.out.println("]");

            mostrarNo(no.esquerda,nivel + 1);

            mostrarNo(no.direita,nivel + 1);
        }

        public void mostrarListasLivres(){

            System.out.println("\nBuddyInfo");

            for(int i = 0;i < tamanhos.length;i++){

                System.out.println(tamanhos[i] + "KB -> " + listasLivres[i].tamanho() + " blocos");
            }
        }

        public void carregarDataset(String caminho){

            try{

                File arquivo =
                    new File(caminho);

                Scanner leitor =
                    new Scanner(
                        arquivo
                    );

                while(
                    leitor.hasNextLine()
                ){

                    String linha =
                        leitor.nextLine()
                            .trim();

                    if(
                        linha.equals("")
                        ||
                        linha.startsWith("#")
                    ){
                        continue;
                    }

                    String[] partes =
                        linha.split(" ");

                    if(
                        partes[0]
                            .equals("ALOCAR")
                    ){

                        String id =
                            partes[1];

                        int tamanho =
                            Integer.parseInt(
                                partes[2]
                            );

                        System.out.println(
                            "\nALOCAR "
                            + id
                        );

                        alocar(
                            id,
                            tamanho
                        );
                    }

                    if(
                        partes[0]
                            .equals("LIBERAR")
                    ){

                        String id =
                            partes[1];

                        System.out.println(
                            "\nLIBERAR "
                            + id
                        );

                        liberar(id);
                    }

                    mostrarArvore();

                    mostrarListasLivres();

                    System.out.println(
                        "\n----------------------"
                    );
                }

                leitor.close();

            } catch(
                FileNotFoundException e
            ){

                System.out.println(
                    "Arquivo nao encontrado"
                );
            }
        }
    }

    public static void main(
            String[] args
    ){

        Scanner entrada =
            new Scanner(
                System.in
            );

        Buddy buddy =
            new Buddy();

        int opcao = 0;

        while(opcao != 8){

            System.out.println(
                "\n1 - alocar"
            );

            System.out.println(
                "2 - liberar"
            );

            System.out.println(
                "3 - desfazer"
            );

            System.out.println(
                "4 - mostrar memoria"
            );

            System.out.println(
                "5 - mostrar fila"
            );

            System.out.println(
                "6 - buddyInfo"
            );

            System.out.println(
                "7 - carregar dataset"
            );

            System.out.println(
                "8 - sair"
            );

            System.out.print(
                "escolha: "
            );

            opcao =
                entrada.nextInt();

            entrada.nextLine();

            if(opcao == 1){

                System.out.print(
                    "ID: "
                );

                String id =
                    entrada.nextLine();

                System.out.print(
                    "tamanho KB: "
                );

                int tamanho =
                    entrada.nextInt();

                entrada.nextLine();

                buddy.alocar(
                    id,
                    tamanho
                );
            }

            if(opcao == 2){

                System.out.print(
                    "ID: "
                );

                String id =
                    entrada.nextLine();

                buddy.liberar(id);
            }

            if(opcao == 3){

                buddy.desfazer();
            }

            if(opcao == 4){

                buddy.mostrarArvore();
            }

            if(opcao == 5){

                buddy.fila
                    .mostrarFila();
            }

            if(opcao == 6){

                buddy
                    .mostrarListasLivres();
            }

            if(opcao == 7){

                System.out.print(
                    "arquivo: "
                );

                String arquivo =
                    entrada.nextLine();

                buddy.carregarDataset(
                    arquivo
                );
            }
        }

        entrada.close();
    }
}

