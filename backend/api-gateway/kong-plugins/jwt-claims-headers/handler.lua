local JwtClaimsHeaders = {}
JwtClaimsHeaders.PRIORITY = 1000
JwtClaimsHeaders.VERSION = "1.0"

local jwt = require "resty.jwt"

function JwtClaimsHeaders:access(conf)
  kong.log.debug("[jwt-claims-headers] Plugin hit")
  
  local auth_header = kong.request.get_header("authorization")
  if not auth_header then
    kong.log.debug("[jwt-claims-headers] No Authorization header")
    return
  end

  local token = auth_header:match("Bearer%s+(.+)")
  if not token then
    kong.log.debug("[jwt-claims-headers] No Bearer token found")
    return
  end

  local decoded = jwt:load_jwt(token)
  if not decoded.valid then
    kong.log.err("[jwt-claims-headers] Invalid JWT: " .. (decoded.reason or "unknown"))
    return
  end

  local sub = decoded.payload and decoded.payload.sub
  if sub then
    kong.log.debug("[jwt-claims-headers] Injecting X-User-Sub: " .. sub)
    kong.service.request.set_header("X-User-Sub", sub)
  else
    kong.log.debug("[jwt-claims-headers] No sub claim in JWT")
  end
end

return JwtClaimsHeaders