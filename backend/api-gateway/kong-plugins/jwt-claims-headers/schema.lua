local typedefs = require "kong.db.schema.typedefs"

return {
  name = "jwt-claims-headers",
  fields = {
    { config = {
        type = "record",
        fields = {},
        custom_validator = nil,
      },
    },
  },
}