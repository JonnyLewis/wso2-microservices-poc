#!/bin/bash
set -e

echo "Loading customers service docker image..."
docker load < customers-service-0.3.tar

echo "Loading credits service docker image..."
docker load < credits-service-0.3.tar

echo "Loading loans service docker image..."
docker load < loans-service-0.3.tar

echo "Loading loan applications service docker image..."
docker load < loan-applications-0.3.tar

echo "Loading wso2apim docker image..."
docker load < wso2apim-2.1.0-v2.tar

echo "Loading wso2apim analytics service docker image..."
docker load < wso2apim-analytics-2.1.0-v2.tar

echo "Loading mysql docker image..."
docker load < wso2ei-bps-6.1.1-v2.tar

