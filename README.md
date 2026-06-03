# Trabalho-buddy-arvore-binaria

# Alocador Buddy Binário em Java

# Descrição do Projeto

este projeto implementa um simulador do algoritmo buddy binario utilizando árvore binária, fila encadeada, pilha encadeada e listas encadeadas, conforme solicitado na atividade acadêmica
O sistema simula o gerenciamento de memoria utilizando blocos em potencia de 2, realizando operações de split (divisao) e merge (fusao) de blocos de memoria
a memoria total utilizada é de 32 MB (32768 KB) e o buddy mínimo é 4 KB. O objetivo do projeto é demonstrar como ocorre a alocação dinamica de memoria, fragmentaçao interna e reorganizaçao dos blocos apos liberaçoes

# estruturas de dados implementadas

# arvore binaria

A árvore binária representa a memoria do sistema buddy. Cada no da arvore representa um bloco de memria, podendo estar livre, ocupado ou dividido
Cada nó possui as seguintes inforrmações:

* tamanho do bloco em KB
* estado do bloco
* identifcador do arquivo alocado
* filho esquerdo
* filho direito
* referencia para o no pai

Os estados possiveis sao:

* LIVRE
* OCUPADO
* DIVIDIDO

Quando um bloco precisa ser dividido, são criados dois filhos de mesmo tamanho, representando os buddies

# Fila Encadeada de Pendentes (FIFO)

a fila encadeada ée utilizada para armazenar requisições de memoria que não puderam ser atendidas por falta de espaço disponível
Quando não existe memória suficiente para realizar uma alocação, o pedido entra na fila de pendentes. Sempre que ocorre uma liberação de memória, o sistema tenta novamente alocar os pedidos que estavam aguardando
Caso ainda não exista memoria suficiente, a requisição retorna ao final da fila

as operaçoes implementadas foram:

* enfileirar
* desenfileirar
* espiar
* verificar se está vazia
* tamanho da fila

# pilha encadeada de historico (LIFO)

A pilha e utilizada para armazenar o historico das operações realizadas no sistema
Sempre que ocorre uma alocação ou liberação bem-sucedida, a operação e armazenada na pilha. A funcionalidade de desfazer remove a ultima operação realizada e tenta restaurar o estado anterior da memoria
As operaçoes implementadas foram:

* empilhar
* desempilhar
* verificar se está vazia

# listas encadeadas de blocos Livres

foram implementadas 14 listas encadeadas para armazenar os blocos livres de memoria, organizados pelo tamanho
Os tamanhos disponíveis são:

4 KB, 8 KB, 16 KB, 32 KB, 64 KB, 128 KB, 256 KB, 512 KB, 1024 KB, 2048 KB, 4096 KB, 8192 KB, 16384 KB e 32768 KB.

as listas sao utilizadas para localizar rapidamente blocos livres disponíveis
urante operaçoes de split e merge, os blocos sao removdos e inseridos automaticamente nas listas correspondentes

# funcionamento do buddy system

# alocação de memoria

Quando uma requisição é feita, o sistema verifica qual é a menor potência de 2 capaz de armazenar o tamanho solicitado

exemplos:

Um pedido de 100 KB recebe um bloco de 128 KB
Um pedido de 5 MB recebe um bloco de 8 MB
Essa diferença entre o tamanho solicitado e o tamanho realmente entregue é chamada de fragmentação interna
Após calcular o tamanho ideal, o sistema procura um bloco livre correspondente
Se não existir um bloco do tamanho exato, um bloco maior é dividido pela metade até atingir o tamanho necessário
Se não existir memória suficiente, a requisição é colocada na fila de pendentes

# split (divisao)

O split ocorre quando um bloco livre é maior do que o necessário
Nesse caso, o bloco é dividido em duas partes iguais, chamadas buddies
O processo continua recursivamente até que o bloco alcance o tamanho desejado
A divisão nunca ocorre abaixo do buddy mínimo de 4 KB

# merge (fusao)

Quando um bloco e liberado, o sistema verifica se seu buddy tambem esta livre
Se os dois buddies estiverem livres, eles são unidos novamente formando um bloco maior
Esse processo continua recursivamente ate a raiz da arvore, caso seja possivel
O merge reduz a fragmentação externa e reorganiza a memoria

# funcionalidades do sistema

O sistema possui um menu interativo com as seguintes opções:

1. Alocar memória

2. Liberar memória

3. Desfazer operação

4. Mostrar árvore da memória

5. Mostrar fila de pendentes

6. Exibir BuddyInfo

7. Carregar dataset

8. Encerrar programa

# Dataset

o sistema aceita um arquivo chamado dataset.txt contendo operações de teste

Formato utilizado:

ALOCAR arquivo1 128

LIBERAR arquivo1

O dataset e processado automaticamente e, apos cada operaçaao, o sistema exibe o estado atualizado da memoria
O arquivo utilizado possui casos de teste envolvendo:

* multiplos splits
* merges em cascata
* fila de pendentes
* fragmentaçao interna
* alocações pequenas proximas ao buddy minimo
* reorganização completa da memória

# Como Executar o Projeto

Primeiro e necessário compilar o programa utilizando:

javac Main.java

Depois executar com:

java Main

Para utilizar o dataset:
Selecionar a opção 7 do menu

Informar o nome do arquivo:
dataset.txt

o sistema ira executar automaticamente todas as operaçoes e exibir a evolução da memoria

# Exemplo de Saída

Exemplo da árvore de memória:

[32768KB DIVIDIDO]
[16384KB OCUPADO video01]
[16384KB DIVIDIDO]

Exemplo do BuddyInfo:

4KB -> 0 blocos

8KB -> 1 blocos

16KB -> 2 blocos

32768KB -> 0 blocos

# Conclusao

este projeto permitiu compreender o funcionamento do algoritmo Buddy Binario, incluindo a divisão e fusão de blocos de memoria, alem da utilização prática de estruturas de dados implementadas manualmente com nós encadeados

Também foi possível compreender o funcionamento de filas, pilhas, listas encadeadas e árvores binárias aplicadas a um sistema de gerenciamento de memória
