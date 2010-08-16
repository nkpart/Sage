import sbt._

class SageProject(info: ProjectInfo) extends DefaultProject(info) {
  
  val snapshots = "scala-tools snapshots" at "http://www.scala-tools.org/repo-snapshots"
  val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test"
  
  // TODO Enable when it gets in the main maven repo
  //  val appengine = "com.google.appengine" % "appenginengine-api-1.0-sdk" % "1.3.0" % "compile"

  val metascala = "metascala" %% "metascala" % "0.1"
  val hprops = "nkpart" %% "hprops" % "0.1"
  
  val scalaz_core = "com.googlecode.scalaz" %% "scalaz-core" % "5.0"
}
