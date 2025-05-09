Gerenciamento de Corridas Olímpicas

Atleta: nome, idade, gênero, país, lista de medalhas
Corrida: edição, local, categoria, gênero, distância, atletas, quantidade máxima de corredores, quantidade mínima de corredores
Resultado da Corrida: atleta, corrida, pódio, tempo
Medalha: tipo

Processos: Inscrição da corrida, resultado da corrida

Regras de negócio:
Inscrição:
+ Um mesmo atleta não pode se inscrever mais de uma vez na mesma corrida;
+ Uma corrida pode ter no máximo 3 atletas do mesmo país;
- Um  atleta só pode competir na corrida de gênero correspondente;
- A quantidade de corredores não pode ultrapassar o limite;

Resultado:
+ Não pode ter mais de um atleta na mesma colocação;
+ O resultado de uma corrida só pode ser registrado se o número de inscrições for maior ou igual que a quantidade mínima de atletas.

Gráficos: Número de participantes por edição, medalhas por atleta.

Relatório: Listar atletas por país, histórico de participações do atleta.
