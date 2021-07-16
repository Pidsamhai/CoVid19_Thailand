# native-lib.cpp

```c
#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_github_pidsamhai_covid19thailand_utilities_Keys_rapidCovid19Api(JNIEnv *env) {
    std::string api_key = "API_KEY";
    return env->NewStringUTF(api_key.c_str());
}
```