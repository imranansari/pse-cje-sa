#CloudBees Private SaaS Edition Managed Master Template Example
This repository provides an example of how you may create custom Docker images to use for managed masters connected to a CloudBees Jenkions Operations Center running on the CloudBees Private SaaS Edition.

###Dockerfile
- The `Dockerfile` starts with a `FROM` value of the CloudBees PSE Master Docker image: `cloudbees/pse-master`. 
- The download of CloudBees Jenkins Enterprise war; this allows overriding the Jenkins Enterprise version included with the PSE image - for example to use a newer image.
- The `COPY ./plugins/* /usr/share/jenkins/ref/plugins/` entry allows installing custom plugins, in this case the `simple-build-for-pipeline` plugin that provides a custom Pipeline DSL.

Besides the `Dockerfile`, the template consists of two primary customization components.
###additional/upgraded plugins listed in the `plugins.txt` file, specifically:
- Latest Pipeline plugins
- GitHub Organization Folder plugin
- Audit Trail plugin

###initilization scirpts (Groovy init scripts that run on Jenkins startup)
- `init_02_pull_remote_pipeline_global_libs.groovy`: Pulls Pipeline Global Library from a GitHub repo into the Jenkins Pipeline repo, making the shared Pipeline Libraries available immediately after initial Jenkins startup
- `init_03_add_sa_credentials.groovy`: Sets up global credentials, retrieving sensitive data from environmental variables; includes credential to connect to GitHub Enterprise
- `init_04_add_ghe_server.groovy`: Configures GitHub Enterprise API endpoint to be used with the GitHub Organization Folder plugin
- `init_05_mail_server_config.groovy`: Configures mail server to allow sending email from Jenkins
- `init_08_audit_trail_config.groovy`: Configures Audit Trail plugin to send data to Logstash via syslog; and on to Elasticsearch to be displayed in custom CJOC analytics dashboard
- `init_20_top-level-folders.groovy`: Creates a folder with a custom icon from the custom Simple Build plugin, used to hold example job(s)
- `init_21_simple_build_pipeline.groovy`: Creates an example Pipeline job that uses DSL from the (Simple Build plugin)[https://github.com/kmadel/simple-build-for-pipeline-plugin]
- `init_99_save.groovy`: Ensures any previous configuration changes are saved  
