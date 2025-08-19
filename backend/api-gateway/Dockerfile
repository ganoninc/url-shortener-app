FROM kong:3.9.1

USER root

# Install dependencies
RUN apt-get update && apt-get install -y luarocks gettext curl && rm -rf /var/lib/apt/lists/*

# Install lua-resty-jwt via luarocks
RUN luarocks install lua-resty-jwt

COPY kong-plugins/jwt-claims-headers /usr/local/share/lua/5.1/kong/plugins/jwt-claims-headers

USER kong