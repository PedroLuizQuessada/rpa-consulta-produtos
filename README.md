# rpa-consulta-produtos

### Pré-requisitos da máquina
1) Java 8
2) Acesso a internet durante a execução da automação
3) GoogleDrive (acesso a pasta da automação)
4) Número de Whatsapp vinculado a uma conta Google

<br>

### Instalação
1) Pasta RPAWhatsapp do projeto
2) Pasta para armazenar o perfil usado no ChromeDriver
3) ChromeDriver na versão do Chrome da máquina
4) Arquivo iniciar.bat com o caminho do arquivo application.properties

<br>

### Configurações
###### As configurações devem ser realizadas no arquivo application.properties localizado na pasta RPAWhatsapp
### Práticas
1) rpa.intervalo-minutos: número de minutos que serão aguardados entre uma execução e outra da automação
2) rpa.texto-primeiro: "true" para enviar primeiro as mensagens de texto e depois as imagens ou "false" para o contrário
### Técnicas
1) api.recuperar-dados.link: link ao endpoint do Sistemato para recuperar dados da automação
2) api.registrar-falha.link: link ao endpoint do Sistemato para registrar uma falha de execução da automação
3) api.registrar-execucao.link: link ao endpoint do Sistemato para registrar uma execução da automação
4) api.id-automacao: ID da automação no Sistemato
5) google-drive.path.pendentes: caminho até a pasta "Pendentes" do GoogleDrive da automação
6) google-drive.path.mensagens: caminho até a pasta "Mensagens" do GoogleDrive da automação
7) google-drive.path.processados: caminho até a pasta "Processados" do GoogleDrive da automação
8) rpa.webdriver.path: caminho até o ChromeDriver
9) rpa.browser-exe.path: caminho até a pasta do executável do Google Chrome
10) rpa.porta: porta a ser usada pelo navegador
11) rpa.profile.path: caminho até a pasta criada para armazenar o perfil usado no ChromeDriver
12) rpa.whatsapp.link: link do WhatsappWeb
13) rpa.google-contatos.link: link do Google Contatos

<br>

### Preparação
1) Subir uma planilha excel (.xls ou .xlsx) no diretório "Mensagens" da pasta da automação no GoogleDrive com os textos a serem enviados nas mensagens. Cada célula da primeira linha será enviada seguindo a ordem esquerda para direita. Caso não houverem textos a serem enviados, pular este passo.
2) Subir as imagens (.png ou .jpeg) a serem enviadas no diretório "Mensagens" da pasta da automação no GoogleDrive. Caso não houverem imagens a serem enviadas, pular este passo.

<br>

### Execução
1) Ao tentar acessar o WhatsappWeb, a automação irá verificar se a máquina já se encontra logada, em caso negativo irá aparecer uma mensagem na tela. O usuário deverá logar manualmente e, após logado, clicar no botão "OK"
2) Ao tentar acessar o GoogleContatos, a automação irá verificar se a máquina já se encontra logada, em caso negativo irá aparecer uma mensagem na tela. O usuário deverá logar manualmente e, após logado, clicar no botão "OK"

<br>

### Sistemato
1) A automação seguirá as configurações aplicadas pelo Sistemato
2) O Sistemato será atualizado com possíveis falhas da automação ao longo de sua execução
3) A automação registrará no Sistemato cada uma de suas execuções