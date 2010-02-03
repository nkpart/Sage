import sbt._

class BroTableProject(info: ProjectInfo) extends DefaultProject(info) {
  val snapshots = "scala-tools snapshots" at "http://www.scala-tools.org/repo-snapshots"
  val scalatest = "org.scalatest" % "scalatest" % "1.0.1-for-scala-2.8.0.Beta1-RC7-with-test-interfaces-0.3-SNAPSHOT" % "test"
  
  // TODO Enable when it gets in the main maven repo
//  val appengine = "com.google.appengine" % "appengine-api-1.0-sdk" % "1.3.0" % "compile"

  val scalaz_core = "com.googlecode.scalaz" % "scalaz-core_2.8.0.Beta1-RC8" % "5.0-SNAPSHOT"
}
