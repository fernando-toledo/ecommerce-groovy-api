# pismo-teste

Desenvolver duas Apis distintas:

produtos-api
compras-api

Escopo

As api's devem se comunicar para o desenvolvimento de uma funcionalidade: realizar uma venda Todas as definições a seguir devem ser feitas por você, devendo considerar as melhores opções dentro do prazo estipulado, sempre buscando a melhor qualidade possível, lembrando que todas as decisões deverão ser justificadas em uma próxima etapa:

O que foi utilizado

Linguagem: Groovy

Frameworks/Ferramentas: 
	- Vertx
	- Vertx Web
	- Junit
	- RestAssured
	- vertx Unit
	- JWT auth
	- Maven

Banco(s) de dados: Sem banco (Temporarimente in-memory)

Nuvem :
	Heroku
https://verticle-compra.herokuapp.com
https://verticle-produto.herokuapp.com

Mecanismo de controle:
	JWT auth (em implementação)

Endpoints
get'/api/v1/compra*'
get'/api/v1/compra/:id'
post'/api/v1/compra/:id'	
get'/api/v1/produto*'
get'/api/v1/produto'
post'/api/v1/produto'
get'/api/v1/produto/:id'
put'/api/v1/produto/:id'
delete'/api/v1/produto/:id'
post'/api/v1/produto/:produto/inventario'
put'/api/v1/produto/:produto/inventario'

Pendências:
Sistema de log.
Esquema de exceptions.
Docs.
Controle de acesso.
cenários BDD (given/when/then)
