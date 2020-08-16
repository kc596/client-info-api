# client-info-api

API to view UA, IP, PROXY and FORWARDED-FOR of client.

## Deploying on aws
1. Setup EC2 instance.
2. Setup Elastic IP.
3. Edit security rules to allow incoming connections.
4. Clone this project.
```git clone https://github.com/kc596/client-info-api.git```
5. Run aws deploy script for Ubuntu 16 else follow equivalent steps.
```sudo sh aws-deploy.sh```
