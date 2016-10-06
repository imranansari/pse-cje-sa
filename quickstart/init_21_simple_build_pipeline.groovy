import hudson.model.*;
import jenkins.model.*;
import java.util.logging.Logger

Logger logger = Logger.getLogger("init_21_simple_build_pipeline.groovy")

def j = Jenkins.instance

File disableScript = new File(j.rootDir, ".disable-init_21_simple_build_pipeline")
if (disableScript.exists()) {
    logger.info("DISABLED init_21_simple_build_pipeline script")
    return
}

def folderName = 'pipeline-examples'
def folderJob = j.getItem(folderName)

def jobName = "simple-build-pipeline"
def job = folderJob.getItem(jobName)
if (job != null) {
  job.delete()
}
println "--> creating $jobName"
def jobConfigXml = """
<flow-definition plugin="workflow-job@2.1">
  <actions/>
  <description>Example of implementing a custom Pipeline DSL as a plugin. Under the &apos;Snippet Generator&apos;, select the &apos;simpleBulid&apos; Global Variable for help text specific to the custom DSL.</description>
  <displayName>Simple Build Pipeline Example</displayName>
  <keepDependencies>false</keepDependencies>
  <properties>
    <jenkins.model.BuildDiscarderProperty>
      <strategy class="hudson.tasks.LogRotator">
        <daysToKeep>-1</daysToKeep>
        <numToKeep>5</numToKeep>
        <artifactDaysToKeep>2</artifactDaysToKeep>
        <artifactNumToKeep>5</artifactNumToKeep>
      </strategy>
    </jenkins.model.BuildDiscarderProperty>
    <com.cloudbees.opscenter.analytics.reporter.items.AnalyticsJobProperty plugin="operations-center-analytics-reporter@1.8.9">
      <created>1461768624121</created>
    </com.cloudbees.opscenter.analytics.reporter.items.AnalyticsJobProperty>
  </properties>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition" plugin="workflow-cps@2.1">
    <script>simpleBuild { 
    machine = &quot;docker-cloud&quot; 
    docker_image = &quot;kmadel/maven:3.3.3-jdk-8&quot; 
    env = [ FOO : 42, BAR : &quot;YASS&quot; ] 
    git_repo = &quot;https://github.com/beedemo/todo-api.git&quot; 
    before_script = &quot;echo before&quot; 
    script = &apos;mvn validate&apos; 
    after_script = &apos;echo done now&apos; 
    notifications = [ email : &quot;kmadel@cloudbees.com&quot; ] 
}
</script>
    <sandbox>true</sandbox>
  </definition>
  <triggers/>
</flow-definition>
"""

job = folderJob.createProjectFromXML(jobName, new ByteArrayInputStream(jobConfigXml.getBytes("UTF-8")));
logger.info("created $jobName")

 //create marker file to disable scripts from running twice
 disableScript.createNewFile()
