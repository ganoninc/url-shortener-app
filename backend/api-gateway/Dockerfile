FROM kong:3.9.1

USER root

# Install dependencies
RUN apt-get update && apt-get install -y luarocks gettext curl && rm -rf /var/lib/apt/lists/*

# Install lua-resty-jwt via luarocks
RUN luarocks install lua-resty-jwt

COPY kong-plugins/jwt-claims-headers /usr/local/share/lua/5.1/kong/plugins/jwt-claims-headers

COPY kong.yml.template /kong/declarative/kong.yml.template

COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

USER kong

ENTRYPOINT ["/docker-entrypoint.sh"]