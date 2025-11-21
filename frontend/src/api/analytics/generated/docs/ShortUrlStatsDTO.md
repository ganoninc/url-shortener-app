# ShortUrlStatsDTO

Aggregated statistics for a shortened URL, including total clicks and event history

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**clickCount** | **number** | Total number of recorded clicks | [default to undefined]
**events** | [**Array&lt;ClickEventDTO&gt;**](ClickEventDTO.md) | List of individual click events associated with the shortened URL | [optional] [default to undefined]

## Example

```typescript
import { ShortUrlStatsDTO } from './api';

const instance: ShortUrlStatsDTO = {
    clickCount,
    events,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
