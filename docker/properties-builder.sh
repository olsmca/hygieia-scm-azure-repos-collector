#!/bin/bash

echo "MONGODB_HOST: $MONGODB_HOST"
echo "MONGODB_PORT: $MONGODB_PORT"

cat > $PROP_FILE <<EOF
# Database Name
dbname=${HYGIEIA_API_ENV_SPRING_DATA_MONGODB_DATABASE:-dashboarddb}

# Database HostName - default is localhost
dbhost=${MONGODB_HOST:-db}

# Database Port - default is 27017
dbport=${MONGODB_PORT:-27017}

# Database Username - default is blank
dbusername=${HYGIEIA_API_ENV_SPRING_DATA_MONGODB_USERNAME:-dashboarduser}

# Database Password - default is blank
dbpassword=${HYGIEIA_API_ENV_SPRING_DATA_MONGODB_PASSWORD:-dbpassword}

# Logging File location
logging.file=./logs/azure-repos.log

# Collector schedule (required)
azure-repos.cron=${AZURE_REPOS_CRON:-0 0/2 * * * *}

# Azure Respos API Version (optional, defaults to current version of 6.0)
azure-repos.apiVersion=${AZURE_REPOS_API_VERSION:-6.0}

# This is the key generated using the Encryption class in core
azure-repos.key=${AZURE_REPOS_KEY:-}
  
# Azure Respos API Token personal access token generated from Azure DevOps and used for making authentiated calls
azure-repos.personalAccessToken=${PERSONAL_ACCESS_TOKEN:-}

# Proxy Host
azure-repos.proxyHost=${PROXY_HOST:-}

# Respos Proxy Port
azure-repos.proxyPort=${PROXY_PORT:-}

# Non Proxy xxx.xxx.xxx.xxx
azure-repos.nonProxy=${NOT_PROXY_HOST:-}

EOF

echo "
===========================================
Properties file created `date`:  $PROP_FILE
Note: personalAccessToken hidden
===========================================
`cat $PROP_FILE | egrep -vi personalAccessToken`
===========================================
END PROPERTIES FILE
===========================================
"

exit 0