#CloudBees Private SaaS Edition Managed Master Template Example
This repository provides an example of how you may create custom Docker images to use for [Managed Masters](https://go.cloudbees.com/docs/cloudbees-documentation/pse-admin-guide/master-provisioning.html#master-provisioning_master-provisioning) connected to a CloudBees Jenkions Operations Center running on the CloudBees Private SaaS Edition. 

The JAVA_OPTS property `-Djenkins.install.runSetupWizard=false` is set so the Jenkins 2 Setup Wizard will be skipped; rather the specific set of plugins specified in [`cje_plugins.txt`](license-activated/cje_plugins.txt) will be installed; thus streamlining the provisioning process.

It includes an example of using Managed Master environmental variables to create dynamic properties for global and/or job configuration.

###Dockerfile
- The `Dockerfile` starts with a `FROM` value of the CloudBees PSE Master Docker image: `cloudbees/pse-master`. 
- The download of CloudBees Jenkins Enterprise war; this allows overriding the Jenkins Enterprise version included with the PSE image - for example to use a newer image.
- The `COPY ./plugins/* /usr/share/jenkins/ref/plugins/` entry allows installing custom plugins, in this case the `simple-build-for-pipeline` plugin that provides a custom Pipeline DSL.
- The [`init_01_install_plugins.groovy`](license-activated/init_01_install_plugins.groovy) license-activated script automates the installation of specified plugins in the [`cje_plugins.txt`](license-activated/cje_plugins.txt) file.

Besides the `Dockerfile`, the template consists of two primary customization components.

###additional/upgraded plugins listed in the `plugins.txt` file, specifically:
- Audit Trail plugin
- HTTP Request plugin

###initilization scripts (Groovy init scripts that run on Jenkins startup)
#####Regular init scripts (on startup)
- `init_03_add_sa_credentials.groovy`: CURRENTLY DISABLED Sets up global credentials, retrieving sensitive data from environmental variables; includes credential to connect to GitHub Enterprise
- `init_05_mail_server_config.groovy`: Configures mail server to allow sending email from Jenkins
- `init_99_save.groovy`: Ensures any previous configuration changes are saved, sets flags not re-run certain scripts, and on restart initializes the custom `quickstart` hook

#####License Activated scripts
- `init_01_install_plugins.groovy`: Installs specific set of plugins, thereby skipping the Jenkin 2.x Setup Wizard

#####Quickstart scripts - a custom init groovy hook that fires after required plugins are installed and after a necessary restart
- `init_08_audit_trail_config.groovy`: Configures Audit Trail plugin to send data to Logstash via syslog; and on to Elasticsearch to be displayed in custom CJOC analytics dashboard
- `init_12_http_request_global_config.groovy`: Creates Basic Digest Authentication entry for HttpRequest plugin for use with the Pipeline External shared library - REQUIRES environment variable `ES_AUTH_CREDENTIALS_ID` to be set to Jenkins Credential ID for Elasticsearch
- `init_22_github_org_project.groovy`: Creates a GitHub Organization Folder project based on the environemtnat variables `GITHUB_ORG` and `GITHUB_CREDENTIALS_ID`
  - Includes an [Pipeline External Shared Library](https://github.com/jenkinsci/workflow-cps-global-lib-plugin#defining-external-libraries) - currently hard coded to https://github.com/beedemo/workflowLibs.git
  - Includes two [Custom MultiBranch Project Factories](https://go.cloudbees.com/docs/cloudbees-documentation/cje-user-guide/chapter-workflow.html#chapter-workflow_pipeline-custom-factories) for the marker files `Dockerfile` and `pom.xml` with scripts pulled from https://github.com/beedemo/custom-marker-pipelines.git
