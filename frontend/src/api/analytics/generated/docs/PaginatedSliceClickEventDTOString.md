# PaginatedSliceClickEventDTOString

Cursor-paginated window of click events for this short URL. This list contains only the events included in the current slice, ordered from newest to oldest. The number of items is limited by the requested page size. Use nextCursor in the PaginatedSlice to retrieve the following window of events. 

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**items** | [**Array&lt;ClickEventDTO&gt;**](ClickEventDTO.md) | Items returned in this cursor slice. This list represents only a window of the full dataset, limited by the requested page size, and ordered consistently according to the API’s contract.  | [default to undefined]
**nextCursor** | **string** | Cursor pointing to the next slice. Null when no additional items are available. Clients should pass this cursor to retrieve the following page.  | [optional] [default to undefined]

## Example

```typescript
import { PaginatedSliceClickEventDTOString } from './api';

const instance: PaginatedSliceClickEventDTOString = {
    items,
    nextCursor,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
