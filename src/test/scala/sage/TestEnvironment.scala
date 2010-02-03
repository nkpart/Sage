package sage

import com.google.apphosting.api.ApiProxy

import java.util.{HashMap => JHashMap}
import java.util.{Map => JMap}

class TestEnvironment extends ApiProxy.Environment {
  def getAppId = "test"
  
  def getVersionId = "1.0"

  def getEmail: String = {
    throw new UnsupportedOperationException()
  }

  def isLoggedIn: Boolean = {
    throw new UnsupportedOperationException()
  }

  def isAdmin: Boolean = {
    throw new UnsupportedOperationException()
  }

  def getAuthDomain: String = {
    throw new UnsupportedOperationException()
  }

  def getRequestNamespace: String = ""

  def getAttributes: JMap[String, Object] = {
    val map = new JHashMap[String, Object]();
    map.put("com.google.appengine.server_url_key", "http://localhost:8080");
    map
  }
}