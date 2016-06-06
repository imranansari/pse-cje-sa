FROM cloudbees/pse-master:1.0.3

USER root
#override CJE war to use newer version
RUN rm /usr/share/jenkins/jenkins.war
RUN curl -fsSL http://jenkins-updates.cloudbees.com/download/je/1.642.18.1/jenkins.war -o /usr/share/jenkins/jenkins.war \
  && echo "f6118c7785b9d9270dd7086b60e2f86062b70154 /usr/share/jenkins/jenkins.war" | sha1sum -c -

#setup base set of Pipeline Global Libs via init groovy script
COPY ./init.groovy.d/* /usr/share/jenkins/home/init.groovy.d/

#copy list of non-standard plugins to install
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt
COPY ./plugins/* /usr/share/jenkins/ref/plugins/

#change back to jenkins user for RUN/ENTRYPOINT commands
USER jenkins