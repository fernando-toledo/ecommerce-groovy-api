package br.com.pismo.produto.repository

import io.vertx.core.json.JsonArray
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.sql.SQLConnection
import io.vertx.ext.sql.UpdateResult

import java.util.stream.Collectors

import br.com.pismo.produto.entity.Produto

class ProdutoRepositoryJDBCSQL implements ProdutoRepository {

	def jdbc

	def public ProdutoRepositoryJDBCSQL(JDBCClient jdbc){
		this.jdbc = jdbc
		createDB()
	}

	@Override
	public int createProduto(expectedName, expectedPrice, expectedCategory, nextHandler) {
		def produto = new Produto(expectedName, expectedPrice, expectedCategory)
		jdbc.getConnection({ ar ->
			SQLConnection connection = ar.result()
			insert(produto, connection, nextHandler)
		});
		return 0
	}

	@Override
	public Produto getProdutoById(Object id, nextHandler) {
		jdbc.getConnection({ ar ->
			SQLConnection connection = ar.result()
			select(id.toString(), connection, nextHandler)
		})
		return null
	}

	@Override
	public List<Produto> getAll(nextHandler) {
		jdbc.getConnection({ ar ->
			SQLConnection connection = ar.result()
			selectAll(connection, nextHandler)
		})
		return null
	}

	@Override
	public void removeAll(nextHandler) {
		products.removeAll{1==1}
	}

	@Override
	public void removeById(Object id, nextHandler) {
		jdbc.getConnection({ ar ->
			SQLConnection connection = ar.result()
			delete(id.toString(), connection, nextHandler)
		})
		return null
	}

	@Override
	public Produto update(Object produto, nextHandler) {
		jdbc.getConnection({ ar ->
			SQLConnection connection = ar.result()
			updateA(produto.getId().toString(), produto, connection, nextHandler)
		});
		return null
	}


	def createDB(){
		jdbc.getConnection({ ar ->
			SQLConnection connection = ar.result()
			connection.execute(
					"CREATE TABLE IF NOT EXISTS produto (id INTEGER IDENTITY, name varchar(100), price integer,  category varchar(100));DELETE FROM produto", { a ->
						if (a.failed()) {							
							connection.close();
							return;
						}
					});
		});
	}

	private void insert(Produto produto, SQLConnection connection, nextHandler) {
		String sql = "INSERT INTO produto (name, price, category) VALUES ?, ?, ?";
		connection.updateWithParams(sql,
				new JsonArray().add(produto.getName()).add(produto.getPrice()).add(produto.getCategory()), { ar ->
					if (ar.failed()) {						
						nextHandler(-1)
						connection.close();
						return;
					}
					UpdateResult result = ar.result();
					Produto w = new Produto()
					w.with{
						it.id = result.getKeys().getInteger(0)
						it.name = produto.getName()
						it.price = produto.getPrice()
						it.category = produto.getCategory()
					}
					nextHandler(w)
				});
	}

	private void delete(String id, SQLConnection connection, nextHandler) {
		connection.updateWithParams("DELETE FROM produto WHERE id=?", new JsonArray().add(id), { ar ->
			nextHandler(id)
		});
	}

	private void select(String id, SQLConnection connection, nextHandler) {
		connection.queryWithParams("SELECT * FROM produto WHERE id=?", new JsonArray().add(id), { ar ->
			if (ar.failed()) {
				nextHandler(-1);
			} else {
				if (ar.result().getNumRows() >= 1) {
					nextHandler(new Produto(ar.result().getRows().get(0)))
				} else {
					nextHandler(-1);
				}
			}
		});
	}

	private void selectAll(SQLConnection connection, nextHandler) {
		connection.query("SELECT * FROM produto", { result ->					
					def produtos = result.result().getRows().stream().map({new Produto(it)}).collect(Collectors.toList())
					nextHandler(produtos)					
			});
	}

	private void updateA(String id, Produto content, SQLConnection connection, nextHandler) {
		String sql = "UPDATE produto SET name=?, price=?, category=? WHERE id=?"
		connection.updateWithParams(
				sql,
				new JsonArray()
				.add(content.getName())
				.add(content.getPrice())
				.add(content.getCategory())
				.add(id), {update ->
					if (update.failed()) {
						nextHandler("Cannot update the product")
						return
					}
					if (update.result().getUpdated() == 0) {
						nextHandler("product not found")
						return
					}
					def p = new Produto()
					p.with{
						it.id =	id.toInteger()
						it.name = content.getName()
						it.price = content.getPrice()
						it.category = content.getCategory()
					}
					nextHandler(p)
				}
				)
	}
}
