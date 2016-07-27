import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.common.StandardCredentials
import com.google.common.collect.Iterables

import jenkins.model.Jenkins

import java.util.logging.Logger

Logger logger = Logger.getLogger("init.init_03_add_sa_credentials.groovy")

File disableScript = new File(Jenkins.getInstance().getRootDir(), ".disable-init_03_add_sa_credentials")
//currently using credentials from CJOC so want this disabled, still a good example of dynamically creating credentials
disableScript.createNewFile()
//only run after restart after license is activated, and only onece
if (!disableScript.exists()) {

  def jenkins = Jenkins.getInstance()
  SystemCredentialsProvider credentialsProvider = Iterables.getOnlyElement(jenkins.getExtensionList(SystemCredentialsProvider.class))
  def env = System.getenv()

  logger.info("using beedemo password from .env file")
  Credentials dockerRegistryBeedemo = new UsernamePasswordCredentialsImpl(
          CredentialsScope.GLOBAL,
          "docker-registry-login",
          "hub.docker registry credentials",
          "beedemo",
           env['DOCKERHUB_BEEDEMO'])

  credentialsProvider.credentials.add(dockerRegistryBeedemo)
  logger.info("added hub.docker registry credentials for beedemo")

  Credentials githubTokenDev1 = new UsernamePasswordCredentialsImpl(
          CredentialsScope.GLOBAL,
          "3ebff2f8-1013-42ff-a1e4-6d74e99f4ca1",
          "github.beescloud.com dev1 token",
          "dev1",
          env['GITHUB_BEESCLOUD_DEV1'])

  credentialsProvider.credentials.add(githubTokenDev1)
  logger.info("added github.beescloud.com credentials for dev1")

  Credentials githubTokenBeedemoUser = new UsernamePasswordCredentialsImpl(
          CredentialsScope.GLOBAL,
          "beedemo-user-token",
          "github.com beedemo-user token",
          "beedemo-user",
          env['GITHUB_BEEDEMO_USER_TOKEN'])

  credentialsProvider.credentials.add(githubTokenBeedemoUser)
  logger.info("added github.com credentials for beedemo-user")
   //create marker file to disable scripts from running twice
   def marker = new File(jenkins.rootDir, ".disable-init_03_add_sa_credentials")
   marker.createNewFile()
} else {
  logger.info("DISABLED init_03_add_sa_credentials script")
  return
}
