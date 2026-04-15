
#include <sys/types.h>
#include <pthread.h>
#include <jni.h>
#include <string>
#include "obfuscate.h"

#include "StrEnc.h"
#include "Tools.h"
#include "HackShooter.h"
#include "json.hpp"
#include "Includes.h"


#include <sys/types.h>
#include <pthread.h>
#include <jni.h>
#include <string>
#define OBFUSCATE(str) OBF(str)

using json = nlohmann::json;

int expiredDate;
static bool DaddyXerr0r = false;

std::string credit;
std::string modname;
std::string token;
std::string g_Licence;
bool xConnected = false, xServerConnection = false, memek = false;

jstring native_Check(JNIEnv *env, jclass clazz, jobject mContext, jstring mUserKey) {
    auto userKey = env->GetStringUTFChars(mUserKey, 0);

    std::string hwid = userKey;
    hwid += GetAndroidID(env, mContext);
    /*https://vip-key.xyz/connect*/ 
    StrEnc(")*]TgImhXiAYluS>2/-}p9(**t; <X2<\\3X?kX+G>)#eO[", "\x41\x5E\x29\x24\x14\x73\x42\x47\x39\x19\x28\x77\x08\x16\x7E\x4D\x1C\x57\x54\x07\x5F\x6A\x66\x6B\x61\x31\x64\x73\x7D\x19\x62\x13\x2C\x46\x3A\x53\x02\x3B\x04\x24\x51\x47\x4D\x00\x2C\x2F", 46).c_str();
    /*https://api.dc-s.xyz/terabaapxerr0r/public/connect*/ 
    StrEnc(")*]TgImhXiAYluS>2/-}p9(**t; <X2<\\3X?kX+G>)#eO[", "\x41\x5E\x29\x24\x14\x73\x42\x47\x39\x19\x28\x77\x08\x16\x7E\x4D\x1C\x57\x54\x07\x5F\x6A\x66\x6B\x61\x31\x64\x73\x7D\x19\x62\x13\x2C\x46\x3A\x53\x02\x3B\x04\x24\x51\x47\x4D\x00\x2C\x2F", 46).c_str();
    /*https://api.dc-s.xyz/terabaapxerr0r/public/connect*/ 
    StrEnc(")*]TgImhXiAYluS>2/-}p9(**t; <X2<\\3X?kX+G>)#eO[", "\x41\x5E\x29\x24\x14\x73\x42\x47\x39\x19\x28\x77\x08\x16\x7E\x4D\x1C\x57\x54\x07\x5F\x6A\x66\x6B\x61\x31\x64\x73\x7D\x19\x62\x13\x2C\x46\x3A\x53\x02\x3B\x04\x24\x51\x47\x4D\x00\x2C\x2F", 46).c_str();
    /*https://api.dc-s.xyz/terabaapxerr0r/public/connect*/ 
    StrEnc(")*]TgImhXiAYluS>2/-}p9(**t; <X2<\\3X?kX+G>)#eO[", "\x41\x5E\x29\x24\x14\x73\x42\x47\x39\x19\x28\x77\x08\x16\x7E\x4D\x1C\x57\x54\x07\x5F\x6A\x66\x6B\x61\x31\x64\x73\x7D\x19\x62\x13\x2C\x46\x3A\x53\x02\x3B\x04\x24\x51\x47\x4D\x00\x2C\x2F", 46).c_str();
    
    hwid += GetDeviceModel(env);
    hwid += GetDeviceBrand(env);

    std::string UUID = GetUUID(env, hwid.c_str());

    std::string errMsg;

    struct MemoryStruct chunk{};
    chunk.memory = (char *) malloc(1);
    chunk.size = 0;

    CURL *curl;
    CURLcode res;
    curl = curl_easy_init();
    if (curl) {
         // std::string url = OBFUSCATE("https://ayansyed.x10.mx/public/connect");
         std::string url = OBFUSCATE("https://eaglecheats.zakecheat.site/connect");
         
        curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, /*POST*/ StrEnc(",IL=", "\x7C\x06\x1F\x69", 4).c_str());
        curl_easy_setopt(curl, CURLOPT_URL,url.c_str());
        curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);
        curl_easy_setopt(curl, CURLOPT_DEFAULT_PROTOCOL, /*https*/ StrEnc("!mLBO", "\x49\x19\x38\x32\x3C", 5).c_str());
        struct curl_slist *headers = NULL;
        headers = curl_slist_append(headers, /*Content-Type: application/x-www-form-urlencoded*/ StrEnc("@;Ls\\(KP4Qrop`b#d3094/r1cf<c<=H)AiiBG6i|Ta66s2[", "\x03\x54\x22\x07\x39\x46\x3F\x7D\x60\x28\x02\x0A\x4A\x40\x03\x53\x14\x5F\x59\x5A\x55\x5B\x1B\x5E\x0D\x49\x44\x4E\x4B\x4A\x3F\x04\x27\x06\x1B\x2F\x6A\x43\x1B\x10\x31\x0F\x55\x59\x17\x57\x3F", 47).c_str());
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);

        char data[4096];
        sprintf(data, /*game=PUBG&user_key=%s&serial=%s*/ StrEnc("qu2yXK,YkJyGD@ut0.u~Nb'5(:.:chK", "\x16\x14\x5F\x1C\x65\x1B\x79\x1B\x2C\x6C\x0C\x34\x21\x32\x2A\x1F\x55\x57\x48\x5B\x3D\x44\x54\x50\x5A\x53\x4F\x56\x5E\x4D\x38", 31).c_str(), userKey, UUID.c_str());
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, data);

        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteMemoryCallback);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *) &chunk);

        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0L);
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, 0L);

        res = curl_easy_perform(curl);
        if (res == CURLE_OK) {
            try {
                json result = json::parse(chunk.memory);
                if (result[/*status*/ StrEnc("(>_LBm", "\x5B\x4A\x3E\x38\x37\x1E", 6).c_str()] == true) {
                    std::string token = result[/*data*/ StrEnc("fAVA", "\x02\x20\x22\x20", 4).c_str()][/*token*/ StrEnc("{>3Lr", "\x0F\x51\x58\x29\x1C", 5).c_str()].get<std::string>();
                      expiredDate = result[/*data*/ StrEnc("fAVA", "\x02\x20\x22\x20", 4).c_str()][/*rng*/ StrEnc("+n,", "\x59\x00\x4B", 3).c_str()].get<time_t>();
					  
					//  ALL_MENU_CODE = result["data"]["ALL_MENU_CODE"].get<std::string>();
					  
                    if (expiredDate + 30 > time(0)) {
                        std::string auth = /*PUBG*/ StrEnc("Q*) ", "\x01\x7F\x6B\x67", 4).c_str();;
                        auth += "-";
                        auth += userKey;
                        auth += "-";
                        auth += UUID;
                        auth += "-";
                        std::string license = OBFUSCATE("DIAMONDYT");
                        auth += license.c_str();
                        std::string outputAuth = Tools::CalcMD5(auth);
                        
                        DaddyXerr0r = true;
                        xServerConnection = true;
                        memek = true;
                        if (DaddyXerr0r) {
                            pthread_t t;
                        }
                        g_Licence = result["data"]["EXP"].get<std::string>();
                    }
                } else {
                    errMsg = result[/*reason*/ StrEnc("LW(3(c", "\x3E\x32\x49\x40\x47\x0D", 6).c_str()].get<std::string>();
                }
            } catch (json::exception &e) {
                errMsg = "{";
                errMsg += e.what();
                errMsg += "}\n{";
                errMsg += chunk.memory;
                errMsg += "}";
            }
        } else {
            errMsg = curl_easy_strerror(res);
        }
    }
    curl_easy_cleanup(curl);
    return DaddyXerr0r ? env->NewStringUTF(/*OK*/ StrEnc("8q", "\x77\x3A", 2).c_str()) : env->NewStringUTF(errMsg.c_str());
}

int Register1(JNIEnv *env) {
    JNINativeMethod methods[] = {{"suckmydick", "(Landroid/content/Context;Ljava/lang/String;)Ljava/lang/String;", (void *) native_Check}};

    jclass clazz = env->FindClass("com/pubgm/activity/LoginActivity");
    if (!clazz)
        return -1;

    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != 0)
        return -1;

    return 0;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    vm->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (Register1(env) != 0)
        return -1;
    return JNI_VERSION_1_6;
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_pubgm_server_ApiServer_EXP(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(g_Licence.c_str());
}
