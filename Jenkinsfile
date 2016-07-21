node('docker-cloud') {
def pseImage
stage 'Build Docker Image'
    checkout scm
    pseImage = docker.build "kishorebhatia/pse-master:1.651.3.1"
stage 'Publish Docker Image'
  sh "docker -v"
  //use withDockerRegistry to make sure we are logged in to docker hub registry
  withDockerRegistry(registry: [credentialsId: 'docker-hub-kb']) { 
    pseImage.push()
  }
}
