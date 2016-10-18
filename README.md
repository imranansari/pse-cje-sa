#CloudBees Private SaaS Edition Managed Master Template Example
This repository provides an example of how you may create custom Docker images to use as a [Managed Masters](https://go.cloudbees.com/docs/cloudbees-documentation/pse-admin-guide/master-provisioning.html#master-provisioning_master-provisioning) image templates to be provisioned by CloudBees Jenkions Operations Center running on the CloudBees Private SaaS Edition. 

The JAVA_OPTS property `-Djenkins.install.runSetupWizard=false` is set so the Jenkins 2 Setup Wizard will be skipped; a specific set of plugins specified in [`cje_plugins.txt`](license-activated/cje_plugins.txt) and in the `Dockerfile` will be installed; thus streamlining the provisioning process.

It also includes an example of using Managed Master environmental variables to create dynamic properties for global and/or job configuration.

###Dockerfile
- The `Dockerfile` starts with a `FROM` value of the CloudBees PSE Master Docker image: `cloudbees/pse-master`. 
- The download of CloudBees Jenkins Enterprise war; this allows overriding the Jenkins Enterprise version included with the PSE image - for example to use a newer image.
- The `RUN /usr/local/bin/install-plugins.sh` command installs addition non-bundled plugins.
- The [`init_01_install_plugins.groovy`](license-activated/init_01_install_plugins.groovy) license-activated script automates the installation of specified plugins in the [`cje_plugins.txt`](license-activated/cje_plugins.txt) file.
- The `quickstart` scripts set configuration for the audit trail plugin, and optionally sets up basic auth configuration for the HTTP Request plugin and creates a GitHub Organization Folder job.

Besides the `Dockerfile`, the template consists of two primary customization components.

####additional/upgraded plugins installed:
- audit-trail
- http_request
- docker-commons:1.4.0
- dockerhub-notification
- pipeline-utility-steps
- hipchat 

####initilization scripts (Groovy init scripts that run on Jenkins startup)
#####Regular init scripts (on startup)
- `init_03_add_sa_credentials.groovy`: CURRENTLY DISABLED Sets up global credentials, retrieving sensitive data from environmental variables; includes credential to connect to GitHub Enterprise
- `init_05_mail_server_config.groovy`: Configures mail server to allow sending email from Jenkins
- `init_99_save.groovy`: Ensures any previous configuration changes are saved, sets flags not re-run certain scripts, and on restart initializes the custom `quickstart` hook

#####License Activated scripts
- `init_01_install_plugins.groovy`: Installs specific set of plugins listed in [`cje_plugins.txt`](license-activated/cje_plugins.txt), allowing the the Jenkin 2.x Setup Wizard to be skipped

#####Quickstart scripts - a custom init groovy hook that fires after required plugins are installed and after a necessary restart
- `init_08_audit_trail_config.groovy`: Configures Audit Trail plugin to send data to Logstash via syslog; and on to Elasticsearch to be displayed in custom CJOC analytics dashboard
- `init_12_http_request_global_config.groovy`: Creates Basic Digest Authentication entry for HttpRequest plugin for use with the Pipeline External shared libraries - REQUIRES environment variable `ES_AUTH_CREDENTIALS_ID` to be set to Jenkins Credential ID for Elasticsearch
- `init_22_github_org_project.groovy`: Creates a GitHub Organization Folder project based on the environmental variables `GITHUB_ORG` and `GITHUB_CREDENTIALS_ID` - if the `GIT_HUB` environmental variable is not set, then the job will not be created - will NOT be created if `GITHUB_ORG` is not set
  - Includes a [Pipeline External Shared Library](https://github.com/jenkinsci/workflow-cps-global-lib-plugin#defining-external-libraries) - currently hard coded to https://github.com/beedemo/workflowLibs.git
  - Includes two [Custom MultiBranch Project Factories](https://go.cloudbees.com/docs/cloudbees-documentation/cje-user-guide/chapter-workflow.html#chapter-workflow_pipeline-custom-factories) for the marker files `Dockerfile` and `pom.xml` with scripts pulled from https://github.com/beedemo/custom-marker-pipelines.git
  - [Environmental variables](#github-org-env-variables):
    - `GITHUB_ORG`: the name of the GitHub Organization to create the GitHub Organization Folder project
    - `GITHUB_CREDENTIALS_ID`: the Jenkins credentials id (username GitHub token) to use for scannning and checkouts for the GitHub Organization
