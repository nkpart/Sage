package sage

import com.google.appengine.api.datastore._
import dev.LocalDatastoreService
import com.google.appengine.tools.development.ApiProxyLocalImpl
import com.google.apphosting.api.ApiProxy

import org.scalatest.BeforeAndAfterAll

trait DatastoreSuite {
  self: BeforeAndAfterAll =>
  
  var proxy: ApiProxyLocalImpl = null
  
  implicit var datastoreService: DatastoreService = null
  
  override def beforeAll {
    ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment())
    ApiProxy.setDelegate(new ApiProxyLocalImpl(new java.io.File(".")){})

    proxy = ApiProxy.getDelegate().asInstanceOf[ApiProxyLocalImpl]
    proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, java.lang.Boolean.TRUE.toString())
    
    datastoreService = DatastoreServiceFactory.getDatastoreService
  }

  override def afterAll {
    ApiProxy.setDelegate(null)
    ApiProxy.setEnvironmentForCurrentThread(null)

    val datastoreService = proxy.getService(LocalDatastoreService.PACKAGE).asInstanceOf[LocalDatastoreService]
    datastoreService.clearProfiles()
  }
}
