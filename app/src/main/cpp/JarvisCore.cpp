#include <jni.h>
#include <string>
#include <sstream>
#include <chrono>
#include <ctime>
#include <iomanip>
#include <algorithm>
#include <cctype>
#include <cmath>
static std::string lower(std::string s){std::transform(s.begin(),s.end(),s.begin(),[](unsigned char c){return (char)std::tolower(c);});return s;}
static std::string now(const char* fmt){auto t=std::chrono::system_clock::to_time_t(std::chrono::system_clock::now());std::tm tm{};localtime_r(&t,&tm);std::ostringstream o;o<<std::put_time(&tm,fmt);return o.str();}
extern "C" JNIEXPORT jstring JNICALL Java_com_jarvis_ai_MainActivity_nativeProcess(JNIEnv* env,jobject,jstring input){
 const char* raw=env->GetStringUTFChars(input,nullptr);std::string q=raw?raw:"";env->ReleaseStringUTFChars(input,raw);std::string l=lower(q);
 std::string out;
 if(l=="status")out="JARVIS 1.021 CORE ONLINE | JNI OK | TOOLS READY | OFFLINE QA READY | VISION CAMERA2";
 else if(l=="help")out="Core tools: status, help, time, date, memory, remember <text>, clear, back, home, volume up/down, wifi settings, bluetooth settings, open <app>, search <query>.";
 else if(l=="time"||l.find("what time")!=std::string::npos)out="Local device time: "+now("%H:%M:%S");
 else if(l=="date"||l.find("what date")!=std::string::npos)out="Local device date: "+now("%Y-%m-%d");
 else if(l.rfind("remember ",0)==0)out="Memory command received by C++ core; Android encrypted persistence will store it.";
 else if(l=="memory")out="Long-term memory is managed by Android SQLite and injected into model context.";
 else if(l=="clear")out="Clear command routed to Android memory layer.";
 else out="OPEN-DOMAIN REQUEST: routed to AI provider or offline assistant.";
 return env->NewStringUTF(out.c_str());
}