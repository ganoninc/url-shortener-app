# ClickEventDTO

Represents a single click event associated with a shortened URL

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **number** | Unique database identifier for the click event | [default to undefined]
**timestamp** | **string** | When the click occurred (UTC) | [default to undefined]
**userAgent** | **string** | User agent string of the requester | [default to undefined]
**countryCode** | **string** | ISO country code resolved from the requester\&#39;s IP address. \&#39;-E\&#39; indicates that the country could not be determined. | [default to undefined]

## Example

```typescript
import { ClickEventDTO } from './api';

const instance: ClickEventDTO = {
    id,
    timestamp,
    userAgent,
    countryCode,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
