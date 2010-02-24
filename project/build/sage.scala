import sbt._

class SageProject(info: ProjectInfo) extends DefaultProject(info) {
    // 
    // Credentials.add("Secure Area", "localhost", "admin", "admin123")
    // 
    // override def managedStyle = ManagedStyle.Maven
    // val publishTo = "Secure Area" at "http://localhost:8080/"
    // 
    
  val snapshots = "scala-tools snapshots" at "http://www.scala-tools.org/repo-snapshots"
  val scalatest = "org.scalatest" % "scalatest" % "1.0.1-for-scala-2.8.0.Beta1-RC7-with-test-interfaces-0.3-SNAPSHOT" % "test"
  
  // TODO Enable when it gets in the main maven repo
  //  val appengine = "com.google.appengine" % "appengine-api-1.0-sdk" % "1.3.0" % "compile"

  val metascala = "metascala" %% "metascala" % "0.1"  
  val hprops = "nkpart" %% "hprops" % "0.1"
  
  val scalaz_core = "com.googlecode.scalaz" % "scalaz-core_2.8.0.Beta1" % "5.0-SNAPSHOT"
}
