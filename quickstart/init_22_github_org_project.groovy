import hudson.model.*;
import jenkins.model.*;

import hudson.security.ACL;
import jenkins.branch.OrganizationFolder;
import jenkins.scm.api.SCMSourceOwner;
import jenkins.scm.api.SCMSourceOwners;
import org.jenkinsci.plugins.github_branch_source.GitHubSCMNavigator;

import java.util.logging.Logger

Logger logger = Logger.getLogger("init_21_simple_build_pipeline.groovy")

def j = Jenkins.instance

File disableScript = new File(j.rootDir, ".disable-init_21_simple_build_pipeline")
if (disableScript.exists()) {
    logger.info("DISABLED init_21_simple_build_pipeline script")
    return
}

def jobName = "beedemo"

println "--> creating $jobName"
def jobConfigXml = """
<jenkins.branch.OrganizationFolder plugin="branch-api@1.10.2">
  <actions>
    <org.jenkinsci.plugins.orgfolder.github.GitHubOrgAction plugin="github-organization-folder@1.4">
      <url>https://github.com/beedemo</url>
      <name>CloudBees Solutions Architecture Pipeline Examples</name>
      <avatar>
https://avatars.githubusercontent.com/u/15805500?v=3
</avatar>
    </org.jenkinsci.plugins.orgfolder.github.GitHubOrgAction>
    <org.jenkinsci.plugins.orgfolder.github.GitHubLink plugin="github-organization-folder@1.4">
      <image>logo</image>
      <url>https://github.com/beedemo</url>
    </org.jenkinsci.plugins.orgfolder.github.GitHubLink>
  </actions>
  <description/>
  <displayName>CloudBees Solutions Architecture Pipeline Examples</displayName>
  <properties>
    <com.cloudbees.hudson.plugins.folder.properties.EnvVarsFolderProperty plugin="cloudbees-folders-plus@3.0">
      <properties/>
    </com.cloudbees.hudson.plugins.folder.properties.EnvVarsFolderProperty>
    <jenkins.branch.NoTriggerOrganizationFolderProperty>
      <branches>.*</branches>
    </jenkins.branch.NoTriggerOrganizationFolderProperty>
  </properties>
  <views>
    <hudson.model.ListView>
      <owner class="jenkins.branch.OrganizationFolder" reference="../../.."/>
      <name>Repositories</name>
      <filterExecutors>false</filterExecutors>
      <filterQueue>false</filterQueue>
      <jobNames>
        <comparator class="hudson.util.CaseInsensitiveComparator"/>
      </jobNames>
      <jobFilters/>
      <columns>
        <hudson.views.StatusColumn/>
        <hudson.views.WeatherColumn/>
        <org.jenkinsci.plugins.orgfolder.github.CustomNameJobColumn plugin="github-organization-folder@1.4">
          <bundle>org.jenkinsci.plugins.orgfolder.github.Messages</bundle>
          <key>ListViewColumn.Repository</key>
        </org.jenkinsci.plugins.orgfolder.github.CustomNameJobColumn>
        <org.jenkinsci.plugins.orgfolder.github.RepositoryDescriptionColumn plugin="github-organization-folder@1.4"/>
      </columns>
      <includeRegex>.*</includeRegex>
      <recurse>false</recurse>
    </hudson.model.ListView>
  </views>
  <viewsTabBar class="hudson.views.DefaultViewsTabBar"/>
  <primaryView>Repositories</primaryView>
  <healthMetrics>
    <com.cloudbees.hudson.plugins.folder.health.WorstChildHealthMetric plugin="cloudbees-folder@5.12"/>
    <com.cloudbees.hudson.plugins.folder.health.AverageChildHealthMetric plugin="cloudbees-folders-plus@3.0"/>
    <com.cloudbees.hudson.plugins.folder.health.JobStatusHealthMetric plugin="cloudbees-folders-plus@3.0">
      <success>true</success>
      <failure>true</failure>
      <unstable>true</unstable>
      <unbuilt>true</unbuilt>
      <countVirginJobs>false</countVirginJobs>
    </com.cloudbees.hudson.plugins.folder.health.JobStatusHealthMetric>
    <com.cloudbees.hudson.plugins.folder.health.ProjectEnabledHealthMetric plugin="cloudbees-folders-plus@3.0"/>
  </healthMetrics>
  <icon class="org.jenkinsci.plugins.orgfolder.github.GitHubOrgIcon" plugin="github-organization-folder@1.4">
    <folder class="jenkins.branch.OrganizationFolder" reference="../.."/>
  </icon>
  <orphanedItemStrategy class="com.cloudbees.hudson.plugins.folder.computed.DefaultOrphanedItemStrategy" plugin="cloudbees-folder@5.12">
    <pruneDeadBranches>true</pruneDeadBranches>
    <daysToKeep>0</daysToKeep>
    <numToKeep>0</numToKeep>
  </orphanedItemStrategy>
  <triggers>
    <com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger plugin="cloudbees-folder@5.12">
      <spec>H H * * *</spec>
      <interval>86400000</interval>
    </com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger>
  </triggers>
  <navigators>
    <org.jenkinsci.plugins.github__branch__source.GitHubSCMNavigator plugin="github-branch-source@1.8.1">
      <repoOwner>beedemo</repoOwner>
      <scanCredentialsId>beedemo-user-github-token</scanCredentialsId>
      <checkoutCredentialsId>SAME</checkoutCredentialsId>
      <pattern>.*</pattern>
      <buildOriginBranch>true</buildOriginBranch>
      <buildOriginBranchWithPR>true</buildOriginBranchWithPR>
      <buildOriginPRMerge>false</buildOriginPRMerge>
      <buildOriginPRHead>false</buildOriginPRHead>
      <buildForkPRMerge>true</buildForkPRMerge>
      <buildForkPRHead>false</buildForkPRHead>
    </org.jenkinsci.plugins.github__branch__source.GitHubSCMNavigator>
  </navigators>
  <projectFactories>
    <org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProjectFactory plugin="workflow-multibranch@2.8"/>
    <com.cloudbees.workflow.multibranch.CustomMultiBranchProjectFactory plugin="cloudbees-workflow-template@2.3">
      <factory>
        <marker>pom.xml</marker>
        <definition class="org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition" plugin="workflow-cps@2.10">
          <scm class="hudson.plugins.git.GitSCM" plugin="git@2.5.3">
            <configVersion>2</configVersion>
            <userRemoteConfigs>
              <hudson.plugins.git.UserRemoteConfig>
                <url>
https://github.com/beedemo/custom-marker-pipelines.git
</url>
              </hudson.plugins.git.UserRemoteConfig>
            </userRemoteConfigs>
            <branches>
              <hudson.plugins.git.BranchSpec>
                <name>*/master</name>
              </hudson.plugins.git.BranchSpec>
            </branches>
            <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
            <submoduleCfg class="list"/>
            <extensions/>
          </scm>
          <scriptPath>pom-Jenkinsfile</scriptPath>
        </definition>
      </factory>
    </com.cloudbees.workflow.multibranch.CustomMultiBranchProjectFactory>
    <com.cloudbees.workflow.multibranch.CustomMultiBranchProjectFactory plugin="cloudbees-workflow-template@2.3">
      <factory>
        <marker>Dockerfile</marker>
        <definition class="org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition" plugin="workflow-cps@2.10">
          <scm class="hudson.plugins.git.GitSCM" plugin="git@2.5.3">
            <configVersion>2</configVersion>
            <userRemoteConfigs>
              <hudson.plugins.git.UserRemoteConfig>
                <url>
https://github.com/beedemo/custom-marker-pipelines.git
</url>
              </hudson.plugins.git.UserRemoteConfig>
            </userRemoteConfigs>
            <branches>
              <hudson.plugins.git.BranchSpec>
                <name>*/master</name>
              </hudson.plugins.git.BranchSpec>
            </branches>
            <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
            <submoduleCfg class="list"/>
            <extensions/>
          </scm>
          <scriptPath>Dockerfile-Jenkinsfile</scriptPath>
        </definition>
      </factory>
    </com.cloudbees.workflow.multibranch.CustomMultiBranchProjectFactory>
  </projectFactories>
</jenkins.branch.OrganizationFolder>

"""

job = j.createProjectFromXML(jobName, new ByteArrayInputStream(jobConfigXml.getBytes("UTF-8")));
job.save()
ACL.impersonate(ACL.SYSTEM, new Runnable() {
                @Override public void run() {
                    for (final SCMSourceOwner owner : SCMSourceOwners.all()) {
                        if (owner instanceof OrganizationFolder) {
                            OrganizationFolder orgFolder = (OrganizationFolder) owner;
                            for (GitHubSCMNavigator navigator : orgFolder.getNavigators().getAll(GitHubSCMNavigator.class)) {
                                orgFolder.scheduleBuild();
                            }
                        }
                    }
                }
            });
logger.info("created $jobName")

 //create marker file to disable scripts from running twice
 disableScript.createNewFile()