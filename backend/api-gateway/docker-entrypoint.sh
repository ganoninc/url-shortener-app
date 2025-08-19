#!/bin/sh
set -e

mkdir -p /tmp/kong-declarative

envsubst < /kong/declarative/kong.yml.template > /tmp/kong-declarative/kong.yml

export KONG_DECLARATIVE_CONFIG=/tmp/kong-declarative/kong.yml

kong start --conf /dev/null --vv &

# Start tail in background
tail -f /dev/null &
TAIL_PID=$!

# Trap signals and kill tail when container stops
trap "echo 'Stopping...'; kill -TERM $TAIL_PID; exit 0" TERM INT

# Wait for tail to finish (or for signal to kill it)
wait $TAIL_PID