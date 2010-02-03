Sage
====

Sage is a scala API for manipulating the App Engine datastore. Is it *heavily* influenced by szeiger's [scala-query][1].

  [1] http://github.com/szeiger/scala-query

Installing
==========

1. Your project must be built with 2.8.Beta1-RC8 or later
2. Clone this project and run the following from within the sage project directory: 
  
    $ ./sbt update
    $ ./sbt publish-local
  
3. If you aren't using SBT, just copy the jars out of target/ and stick them where you want them. If you are using sbt, modify your project build file to contain the following
  
    val sage = "prohax" % "sage" % "0.1"

Getting Started
===============

The api is definitely not locked down right now, so the best place to learn how to use it is to read the specs.


