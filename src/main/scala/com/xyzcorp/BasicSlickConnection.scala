package com.xyzcorp

import slick.jdbc.H2Profile.api._
import slick.lifted.QueryBase

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object BasicSlickConnection extends SlickDAO {
    def main(args: Array[String]): Unit = {
        val db = Database.forConfig("h2mem1")
        try {
            val setup = DBIO.seq(
                // Create the tables, including primary and foreign keys
                (suppliers.schema ++ coffees.schema).create,

                // Insert some suppliers
                suppliers += (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199"),
                suppliers += (49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460"),
                suppliers += (150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966"),
                // Equivalent SQL code:
                // insert into SUPPLIERS(SUP_ID, SUP_NAME, STREET, CITY, STATE, ZIP) values (?,?,?,?,?,?)

                // Insert some coffees (using JDBC's batch insert feature, if supported by the DB)
                coffees ++= Seq(
                    ("Colombian", 101, 7.99, 0, 0),
                    ("French_Roast", 49, 8.99, 0, 0),
                    ("Espresso", 150, 9.99, 0, 0),
                    ("Colombian_Decaf", 101, 8.99, 0, 0),
                    ("French_Roast_Decaf", 49, 9.99, 0, 0)
                )
                // Equivalent SQL code:
                // insert into COFFEES(COF_NAME, SUP_ID, PRICE, SALES, TOTAL) values (?,?,?,?,?)
            )

            val setupFuture = db.run(setup)
        } finally db.close


        val rep = coffees.filter(_.price > 8.0).map(_.name)
        //    println(rep)

        val q = for (c <- coffees) yield c.name
        val a = q.result
        val f = db.run(a)

        f.onComplete { t =>
            t.fold(t => t.printStackTrace(), s => println(s"Result: $s"))
        }

        Thread.sleep(10000)
    }
}
