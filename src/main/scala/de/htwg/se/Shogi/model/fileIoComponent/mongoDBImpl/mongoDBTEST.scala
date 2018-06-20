package de.htwg.se.Shogi.model.fileIoComponent.mongoDBImpl

import com.mongodb.casbah.Imports._
import com.mongodb.DBObject
import com.mongodb.casbah.{ MongoClient }
import com.mongodb.casbah.commons.MongoDBObject
import org.mongodb.scala.{ MongoCredential, ServerAddress }

object mongoDBTEST {

  def main(args: Array[String]): Unit = {

    val SERVER = "192.168.99.100"
    val PORT = 27017
    val DATABASE = "TestDataBase"
    val COLLECTION = "stocks"
    val server = new ServerAddress(SERVER, PORT)
    val credentials = MongoCredential.createCredential("root", "admin", "1234hot5".toArray)
    val mongoClient = MongoClient(server, List(credentials))
    val db = mongoClient.getDB(DATABASE)

    case class Stock(symbol: String, price: String)

    def buildMongoDbObject(stock: Stock): DBObject = {
      val builder = MongoDBObject.newBuilder
      builder += "symbol" -> stock.symbol
      builder += "price" -> stock.price
      builder.result
    }

    val query = MongoDBObject("symbol" -> "Netflix")
    //    db.getCollection(COLLECTION).findAndRemove(query)
    db.getCollection(COLLECTION).remove("symbol" $regex "Netflix")
    //    val apple = Stock("Apple", "600")
    //    val google = Stock("Google", "650")
    //    val netflix = Stock("Netflix", "60")

    //    db.getCollection(COLLECTION).insert(buildMongoDbObject(apple))
    //    db.getCollection(COLLECTION).insert(buildMongoDbObject(google))
    //    db.getCollection(COLLECTION).insert(buildMongoDbObject(netflix))
    val find = db.getCollection(COLLECTION).find()
    while (find.hasNext) {
      println(print(find.next()))
    }

  }
}