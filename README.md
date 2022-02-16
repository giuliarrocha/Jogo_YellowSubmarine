# Jogo_YellowSubmarine
Jogo cliente-servidor desenvolvido em Java para a disciplina de Técnicas de Programação.

PARA EXECUTAR OS PROGRAMAS:
NA PASTA SERVIDOR: Compile o arquivo Servidor.java (javac Servidor.java) e depois 
o execute (java Servidor).

NA PASTA CLIENTE: Para cada cliente (máximo 2), compile o arquivo Jogo.java (javac Jogo.java) 
e depois o execute (java Jogo).

INFORMAÇÕES SOBRE O JOGO (YELLOW SUBMARINE):

			OBJETIVO:
	- Coletar o máximo de moedas e baús. O jogador que coletar mais pontos ao final, ganha.

			COMANDOS:
	- Setas: movimentação do submarino (setas para cima, para baixo, para a esquerda, e para a direita);
	- Tecla 'D': submarino dispara um míssel;

	OBS1: Para disparar um míssel, atire, espere ele atingir seu alvo e depois atire outro, se desejar.
	OBS2: Segure uma seta na direção desejada para que o submarino se movimente melhor, se desejar;


		    REGRAS E DESCRIÇÕES:
	- O jogo deve ser jogado por 2 jogadores;
	- O jogador 1 corresponde ao submarino verde e o jogador 2 ao submarino vermelho;
	- Cada moeda coletada corresponde a 10 pontos;
	- Cada baú coletado corresponde a 50 pontos;
	- Se o submarino passar por cima de uma bomba, o jogador perde uma vida e seus pontos descrescem em 10 unidades;
	- O jogador pode lancar um míssel (tecla D) sobre uma bomba para explodí-la. Os mísseis só acertam as bombas;
	- Se o jogador perder suas 3 vidas, o jogador morre, a partida é encerrada e seu adversário é declarado como o vencedor da partida;
	- O jogo possui 3 cenários diferentes: após coletarem todas as moedas e baús de um cenário, o jogo automaticamente muda para outro;
	- Se nenhum jogador morrer até a fase 3, a partida terá fim quando todas as moedas e baús forem coletados do cenário 3. A partida poderá ter um ganhador e um perdedor ou um empate, dependendo da pontuação dos jogadores;
	- Se um jogador sair da partida, é comparado os pontos, caso o jogador que permaneceu tenha atingido mais pontos, ele ganha. Se sua pontuação for menor, ele perde. Se não, haverá um empate.
  
EQUIPE:
Giulia Rossatto Rocha
Larissa de Castro Bonadio
