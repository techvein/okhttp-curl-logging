Curl styled logging interceptor for OkHttp3

Logs network request as CURL command:
```
 curl  -X GET \
  -H 'Accept: application/json' \
  -H 'Host: api.github.com' \
  -H 'Connection: Keep-Alive' \
  -H 'Accept-Encoding: gzip' \
  -H 'User-Agent: okhttp/3.12.1' \
  -d 'key=value' \
  'https://api.github.com/users/techvein'
```

## Installation
Add it your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Then add the dependency
```
dependencies {
    implementation 'com.github.techvein:okhttp-curl-logging:1.0.4'
}
```

### Usage:

- with OkHttpClient

```
private val okHttpClient = OkHttpClient.Builder()
    .addNetworkInterceptor(CurlHttpLoggingInterceptor()) // Add this line.
    .build()
```

- with Ktor

```
val httpClient = HttpClient(OkHttp) {
    engine {
        addNetworkInterceptor(CurlHttpLoggingInterceptor()) // Add this line.
    }
}
```

and you will see a curl log on Logcat, then you can copy the log. 

![image](https://user-images.githubusercontent.com/1450486/52203044-852bad80-28b3-11e9-88e0-4d809377a06c.png) 

