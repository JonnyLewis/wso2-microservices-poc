#!/usr/bin/env bash

mysql_flags="-u root --socket=/tmp/mysql.sock"

echo "Executing SQL scripts..."
cat /tmp/scripts/*.sql  > /tmp/scripts/all-files.sql

mysql $mysql_flags <<EOSQL
    source /tmp/scripts/all-files.sql
EOSQL