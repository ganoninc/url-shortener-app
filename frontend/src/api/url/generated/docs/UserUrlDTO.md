# UserUrlDTO

Response containing the shortened URL id, the original URL, and its creation date

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**shortId** | **string** | The shortened identifier of the URL | [default to undefined]
**originalUrl** | **string** | The original URL | [default to undefined]
**createdAt** | **string** | The creation date | [default to undefined]

## Example

```typescript
import { UserUrlDTO } from './api';

const instance: UserUrlDTO = {
    shortId,
    originalUrl,
    createdAt,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
