# UrlControllerApi

All URIs are relative to *http://localhost:8082*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getMyUrls**](#getmyurls) | **GET** /my-urls | |
|[**shortenUrl**](#shortenurl) | **POST** /shorten | |

# **getMyUrls**
> Array<UrlMapping> getMyUrls()


### Example

```typescript
import {
    UrlControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UrlControllerApi(configuration);

const { status, data } = await apiInstance.getMyUrls();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<UrlMapping>**

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

# **shortenUrl**
> { [key: string]: string; } shortenUrl(shortenURLRequestDTO)


### Example

```typescript
import {
    UrlControllerApi,
    Configuration,
    ShortenURLRequestDTO
} from './api';

const configuration = new Configuration();
const apiInstance = new UrlControllerApi(configuration);

let shortenURLRequestDTO: ShortenURLRequestDTO; //

const { status, data } = await apiInstance.shortenUrl(
    shortenURLRequestDTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **shortenURLRequestDTO** | **ShortenURLRequestDTO**|  | |


### Return type

**{ [key: string]: string; }**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

