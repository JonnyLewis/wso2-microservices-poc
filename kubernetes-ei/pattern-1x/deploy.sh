#!/usr/bin/env bash

# ------------------------------------------------------------------------
# Copyright 2017 WSO2, Inc. (http://wso2.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License
# ------------------------------------------------------------------------

# methods
function echoBold () {
    echo $'\e[1m'"${1}"$'\e[0m'
}

set -e

# configuration maps
echoBold 'creating configuration maps...'
oc create configmap wso2ei-bps-conf --from-file=conf/bps/conf/
oc create configmap wso2ei-bps-conf-axis2 --from-file=conf/bps/conf/axis2/
oc create configmap wso2ei-bps-conf-datasources --from-file=conf/bps/conf/datasources/
oc create configmap wso2ei-bps-conf-tomcat --from-file=conf/bps/conf/tomcat/
oc create configmap wso2ei-bps-conf-epr --from-file=conf/bps/conf/epr/

# bps
oc create -f resources/volumes
sleep 10s

echoBold 'deploying wso2 ei/bps...'
oc create -f resources/bps
oc create -f resources/routes
