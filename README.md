# rpa-consulta-produtos

### Pré-requisitos da máquina
1) Java 8
2) Acesso à internet durante a execução da automação
3) GoogleDrive (acesso à pasta da automação)

<br>

### Instalação
1) Pasta RPAConsultaProdutos do projeto
2) ChromeDriver na versão do Chrome da máquina
3) Arquivo iniciar.bat com o caminho do arquivo application.properties

<br>

### Configurações
###### As configurações devem ser realizadas no arquivo application.properties localizado na pasta RPAConsultaProdutos
### Práticas
1) rpa.intervalo-minutos: número de minutos que serão aguardados entre uma execução e outra da automação
2) rpa.numero-resultados: número de resultados que a automação consultará de cada um dos produtos dentro de cada um dos E-commerces ativos
3) rpa.mercado-livre.ativo: "true" para a automação consultar os produtos no Mercado Livre e "false" para não consultar
### Técnicas
1) api.recuperar-dados.link: link ao endpoint do Sistemato para recuperar dados da automação
2) api.registrar-falha.link: link ao endpoint do Sistemato para registrar uma falha de execução da automação
3) api.registrar-execucao.link: link ao endpoint do Sistemato para registrar uma execução da automação
4) api.id-automacao: ID da automação no Sistemato
5) google-drive.path.pendentes: caminho até a pasta "Pendentes" do GoogleDrive da automação
6) google-drive.path.processados: caminho até a pasta "Processados" do GoogleDrive da automação
7) google-drive.path.resultados: caminho até a pasta "Resultados" do GoogleDrive da automação
8) rpa.webdriver.path: caminho até o ChromeDriver

<br>

### Preparação
1) Subir uma planilha excel (.xls ou .xlsx) no diretório "Pendentes" onde os nomes dos produtos a serem consultados nos E-commerces ativos deverão estar na primeira coluna

<br>

### Execução
1) Ao consultar produtos dentro de um E-commerce, a automação irá recuperar os dados dos resultados mais baratos encontrados
2) Será gerado uma planilha para cada E-coomerce consultado onde na primeira coluna constará o nome do produto, na segunda constará o link e o valor do resultado mais barato encontrado para aquele produto, na terceira constará o link e o valor do segundo resultado mais barato encontrado para aquele produto e assim por diante

<br>

### Sistemato
1) A automação seguirá as configurações aplicadas pelo Sistemato
2) O Sistemato será atualizado com possíveis falhas da automação ao longo da sua execução
3) A automação registrará no Sistemato cada uma das suas execuções