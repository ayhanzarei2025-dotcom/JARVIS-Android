#include <jni.h>
#include <string>
#include <algorithm>
#include <cctype>
static std::string lower(std::string s){std::transform(s.begin(),s.end(),s.begin(),[](unsigned char c){return(char)std::tolower(c);});return s;}
extern "C" JNIEXPORT jstring JNICALL Java_com_jarvis_ai_MainActivity_nativeProcess(JNIEnv* e,jobject,jstring in){
 const char*r=e->GetStringUTFChars(in,nullptr);std::string q=r?r:"";e->ReleaseStringUTFChars(in,r);std::string l=lower(q);
 if(l=="status")return e->NewStringUTF("C++ CORE ONLINE | JNI | TOOL ROUTER | VISION | SQLITE MEMORY | SECURE KEY STORAGE");
 if(l=="help")return e->NewStringUTF("Local: status, help, time, date, battery. Actions: do wifi, do bluetooth, do settings, do home, do back, do recents, do search <query>.");
 if(l=="memory")return e->NewStringUTF("Long-term memory is stored locally in SQLite.");
 return e->NewStringUTF("OPEN-DOMAIN REQUEST: routed to AI provider.");
}