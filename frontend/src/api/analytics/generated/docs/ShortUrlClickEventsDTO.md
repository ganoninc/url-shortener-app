# ShortUrlClickEventsDTO

Aggregated statistics for a shortened URL, including total clicks and event history

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**totalClicks** | **number** | Total number of recorded clicks | [default to undefined]
**events** | [**PaginatedSliceClickEventDTOString**](PaginatedSliceClickEventDTOString.md) |  | [default to undefined]

## Example

```typescript
import { ShortUrlClickEventsDTO } from './api';

const instance: ShortUrlClickEventsDTO = {
    totalClicks,
    events,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
