# UrlControllerApi

All URIs are relative to *http://localhost:8082*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getUserUrls**](#getuserurls) | **GET** /my-urls | Get user\&#39;s URLs|
|[**shortenUrl**](#shortenurl) | **POST** /shorten | Shorten a URL|

# **getUserUrls**
> Array<UserUrlDTO> getUserUrls()

Returns the list of shortened URL created by the user

### Example

```typescript
import {
    UrlControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UrlControllerApi(configuration);

const { status, data } = await apiInstance.getUserUrls();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<UserUrlDTO>**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | List of user\&#39;s URL |  -  |
|**400** | User not logged in |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **shortenUrl**
> ShortenURLResponseDTO shortenUrl(shortenURLRequestDTO)

Creates a shortened URL for the provided original URL

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

**ShortenURLResponseDTO**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Successfully created short URL |  -  |
|**400** | User not logged in |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

