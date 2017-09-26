# WSO2 Microservices POC


This repository contains a POC implemented for demonstrating following features:

- Implementing microservices in WSO2 MSF4J microservices framework.
- Securing microservices with JWT.
- Implementing integration services with [Ballerinalang](https://ballerinalang.org).
- Exposing microservices and integration services via WSO2 API Manager 2.1.0.

# Getting Started

1. Clone this repository:

   ````bash
   git clone https://github.com/imesh/wso2-microservices-poc.git
   ````
   
2. Build MySQL Docker image:

   ````bash
   cd kubernetes-mysql/base/
   ./build
   ````

3. Build microservices and their Docker images:

   ''''bash
   cd microservices/
   maven clean install
   ''''
   
4. Build WSO2 API Manager Docker images:

   ''''bash
   cd kubernetes-apim/base/
   ./build.sh
   ''''
   
5. Install [Minishift](https://docs.openshift.org/latest/minishift/getting-started/index.html) and OpenShift CLI. 


6. Create a user called admin and assign the cluster-admin role. (Cluster-admin user will be used to deploy OpenShift 
resources):

   ````bash
   oc login -u system:admin
   oc create user admin --full-name=admin
   oc adm policy add-cluster-role-to-user cluster-admin admin
   ````

7. Create a new project called wso2:

   ````bash
   oc new-project wso2 --description="WSO2" --display-name="wso2"
   ````
   
8. Create a service account called wso2svcacct in wso2 project and assign anyuid security context constraint:
   
   ````bash
   oc create serviceaccount wso2svcacct
   oc adm policy add-scc-to-user anyuid -z wso2svcacct -n wso2
   ````

9. Copy the above Docker images into OpenShift nodes or to a Docker registry:
   
   Copy the above Docker images over to the OpenShift Nodes. As an example use docker save command to create a tar file 
   of the required image, scp the tar file to each node, and then use docker load command to load the image from the 
   copied tar file on the nodes. Alternatively, if a private Docker registry is used, transfer the Docker images there.
   
10. Deploy the MySQL Server using the below command:

    ````bash
    cd kubernetes-mysql/
    ./deploy.sh
    ````