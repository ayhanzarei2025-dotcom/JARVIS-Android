#include <jni.h>
#include <string>
#include <sstream>
#include <chrono>
#include <ctime>
#include <iomanip>
#include <algorithm>
#include <cctype>
static std::string lower(std::string s){std::transform(s.begin(),s.end(),s.begin(),[](unsigned char c){return (char)std::tolower(c);});return s;}
extern "C" JNIEXPORT jstring JNICALL Java_com_jarvis_ai_MainActivity_nativeProcess(JNIEnv* env,jobject, jstring input){
 const char* raw=env->GetStringUTFChars(input,nullptr);std::string q=raw?raw:"";env->ReleaseStringUTFChars(input,raw);std::string l=lower(q);
 if(l=="status") return env->NewStringUTF("C++ CORE: ONLINE | JNI: OK | LOCAL TOOLS: READY | VISION: CAMERA2");
 if(l=="help") return env->NewStringUTF("Local: status, help, time, memory, remember <text>, clear. Vision: open camera, then capture + analyze.");
 if(l=="time"){
  auto now=std::chrono::system_clock::now();std::time_t tt=std::chrono::system_clock::to_time_t(now);std::tm tm{};
 #if defined(_WIN32)
  localtime_s(&tm,&tt);
 #else
  localtime_r(&tt,&tm);
 #endif
  std::ostringstream o;o<<"Local device time: "<<std::put_time(&tm,"%Y-%m-%d %H:%M:%S");return env->NewStringUTF(o.str().c_str());
 }
 if(l.rfind("remember ",0)==0) return env->NewStringUTF("Memory command received by C++ core. Android layer will persist it.");
 if(l=="memory") return env->NewStringUTF("Persistent memory is managed by Android SQLite.");
 return env->NewStringUTF("OPEN-DOMAIN REQUEST: routed to AI provider.");
}
