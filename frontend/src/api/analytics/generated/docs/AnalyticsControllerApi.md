# AnalyticsControllerApi

All URIs are relative to *http://localhost:8083*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getClickEvents**](#getclickevents) | **GET** /short-url/{shortId} | |

# **getClickEvents**
> ShortUrlClickEventsDTO getClickEvents()


### Example

```typescript
import {
    AnalyticsControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AnalyticsControllerApi(configuration);

let shortId: string; // (default to undefined)
let cursor: string; // (optional) (default to undefined)
let size: number; // (optional) (default to 50)

const { status, data } = await apiInstance.getClickEvents(
    shortId,
    cursor,
    size
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **shortId** | [**string**] |  | defaults to undefined|
| **cursor** | [**string**] |  | (optional) defaults to undefined|
| **size** | [**number**] |  | (optional) defaults to 50|


### Return type

**ShortUrlClickEventsDTO**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

