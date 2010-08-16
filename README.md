Sage
====

Sage is a scala API for manipulating the App Engine datastore. Is it *heavily* influenced by szeiger's [scala-query][1].

  [1]: http://github.com/szeiger/scala-query

Declaring Models
----------------

        import sage._
        
        import java.lang.{Long => JLong} 
        
        case class Name(value: String) extends NewType[String]
        case class Hat(name: Name, price: JLong)

        object Hats extends Base[Hat]("hats") {
          def * =  "type".typedProp(Name) :: "price".prop[JLong]  >< (Hat <-> Hat.unapply _)
        }
        
Let's go through that. We define our data type, Hat, using a NewType for the name property (types ftw).

To use that as an entity in the datastore, we need a subclass of `Base`, declaring the datastore *kind* of the object.

The method `*` declares how the case class is read and stored within an entity. The order of the properties must match the order of the arguments to the constructor - an incorrect order will lead to a failure at compile time. You build a heterogenous list of properties, then use >< to pass in a function from that list of properties to your case class, and the reverse. "Hat <-> Hat.unapply _" is the most convenient way of declaring this pair of functions.


Inserting Data
--------------

        implicit val datastore: DatastoreService = ...
        
        // Inserting values. Returns the value and the key it is stored under
        val r: Keyed[Hat] = Hats << Hat(Name("Bowler"), 15L)
        
        // Insert many, returns all the new keys and values.
        val rs: List[Keyed[Hat]] = Hats <<++ List(fedora, flatCap)

Updating Data
-------------

Documentation pending! See src/test/scala/sage/updatesSpec.scala


Querying
--------

        implicit val datastore: DatastoreService = ...
        
        Hats.find.query("type" ?== "Bowler").iterable

Parent-Child entities
---------------------

Documentation pending! See src/test/scala/sage/childrenSpec.scala

More
----

Look at specs for more examples.


Installing
----------

1. Sage requires scala 2.8, metascala, and [hprops][1]

  [1]: http://github.com/nkpart/hprops

2.      $ ./sbt update
        $ ./sbt publish-local
  
4. If you aren't using SBT, just copy the jars out of target/ and stick them where you want them. If you are using sbt, modify your project build file to contain the following
5. 
        val sage = "nkpart" %% "sage" % "0.1"

IMPORTANT:
 * For any numeric values, use JLong

