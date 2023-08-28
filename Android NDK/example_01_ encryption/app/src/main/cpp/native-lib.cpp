#include <jni.h>
#include <string>
#include <android/log.h>
#include "md5.h"

// 额外附加的字符串
static char *EXTRA_SIGNATURE = "DARREN";
// 校验签名
static int is_verify = 0;
static char *PACKAGE_NAME = "com.darren.ndk.day01";
static char *APP_SIGNATURE = "308202e4308201cc020101300d06092a864886f70d010105050030373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b30090603550406130255533020170d3231303831353032343635385a180f32303531303830383032343635385a30373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b300906035504061302555330820122300d06092a864886f70d01010105000382010f003082010a02820101008189d0280f88a9fc6fe7889264a8a00527188a4cfa2f302d3e77036319570a6f3ac6aa15c6c3805f81d829b53e51d89f3e362661132e95db6819c736ba689e961d2c1bd9e3ca381fadefb28b819fe25a5853b321fd10710dd3a3358bf0eb783dbb965262ff37fa8703aa07e9f2f962895409816abe93394d4b61974ff2b3e73410bcf7d4d8918124f8f6a335c456d94bfdc9e3401a8956300f1164f988b5c82fc44d2640d896579a9132ee36baab74d1784273ec270a807691f23d9224458b08dccfa99dec3e84740016962bd0fcda5a831e61dede5be0991c04aea97c26b911bd95cb6d48f819f227459328325f2fdeca1b2dfe318f160eafd46e4fc2145dfb0203010001300d06092a864886f70d010105050003820101002192d4977e62657394e693d2655083383a0c13b73e19401481e07b8e70156a9efde83a42f96253aec9babfcd605f83947d36b28bb863afd07308d54265489d76516f1009e12874d1cb4c8ed0c46c83ee1214b0c93de6b7e76ed19bbd9a3190dae3f81fd67bf7cc80b843af36cd9d72ee3266f571e77659926b53005ada4b7e68f1077196c731a2e242a08fbf2f9e0f7a13cab6f861aa590dad0adccfde385d00749b7f2501f269f1f1fbe4fcde50f336b497189e531531fbbb3688056d8027fa84651cbe6e9f403bc2d626c47065a926a699ba2fca829df4ca022941d2f404642e4245e656adae9367a95c30a662a234271b62292ca5c50b6608dbc4b1944d56";

using namespace std;

/**
 * 修复：JNI DETECTED ERROR IN APPLICATION: input is not valid Modified UTF-8: illegal start byte 0xfe 原创
 * @param bytes
 */
void correctUtfBytes(char *bytes) {
    char three = 0;
    while (*bytes != '\0') {
        unsigned char utf8 = *(bytes++);
        three = 0;
        // Switch on the high four bits.
        switch (utf8 >> 4) {
            case 0x00:
            case 0x01:
            case 0x02:
            case 0x03:
            case 0x04:
            case 0x05:
            case 0x06:
            case 0x07:
                // Bit pattern 0xxx. No need for any extra bytes.
                break;
            case 0x08:
            case 0x09:
            case 0x0a:
            case 0x0b:
            case 0x0f:
                /*
                 * Bit pattern 10xx or 1111, which are illegal start bytes.
                 * Note: 1111 is valid for normal UTF-8, but not the
                 * modified UTF-8 used here.
                 */
                *(bytes - 1) = '?';
                break;
            case 0x0e:
                // Bit pattern 1110, so there are two additional bytes.
                utf8 = *(bytes++);
                if ((utf8 & 0xc0) != 0x80) {
                    --bytes;
                    *(bytes - 1) = '?';
                    break;
                }
                three = 1;
                // Fall through to take care of the final byte.
            case 0x0c:
            case 0x0d:
                // Bit pattern 110x, so there is one additional byte.
                utf8 = *(bytes++);
                if ((utf8 & 0xc0) != 0x80) {
                    --bytes;
                    if (three)--bytes;
                    *(bytes - 1) = '?';
                }
                break;
        }
    }
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_darren_ndk_day01_SignatureUtils_signatureParams(JNIEnv *env, jclass type,
                                                         jstring params_) {
    if (is_verify == 0) {
        return env->NewStringUTF("error_signature");
    }
    const char *params = env->GetStringUTFChars(params_, 0);
    // MD5 签名规则，加点料
    // 1. 字符串前面加点料
    string signature_str(params);
    signature_str.insert(0, EXTRA_SIGNATURE);
    // 2. 后面去掉两位
    signature_str = signature_str.substr(0, signature_str.length() - 2);
    // 3. md5 去加密 C++ 和 Java 是一样的，唯一不同就是需要自己回收内存
    MD5_CTX *ctx = new MD5_CTX();
    MD5Init(ctx);
    MD5Update(ctx, (unsigned char *) signature_str.c_str(), signature_str.length());
    unsigned char digest[16] = {0};
    MD5Final(digest, ctx);
    // 生成 32 位的字符串
    char md5_str[32];
    for (int i = 0; i < 16; i++) {
        // 不足的情况下补0 f = 0f, ab = ab
        sprintf(md5_str, "%s%02x", md5_str, digest[i]);
    }
    correctUtfBytes(md5_str);
    // 释放资源
    env->ReleaseStringUTFChars(params_, params);
    return env->NewStringUTF(md5_str);
}

//extern "C"
/**
    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
    Signature[] signatures = packageInfo.signatures;
    return signatures[0].toCharsString();
 */
// C 调用 Java 代码 JNI 基础
extern "C" JNIEXPORT void JNICALL
Java_com_darren_ndk_day01_SignatureUtils_signatureVerify(JNIEnv *env, jclass type,
                                                         jobject context) {
    // 1. 获取包名
    jclass j_clz = env->GetObjectClass(context);
    jmethodID j_mid = env->GetMethodID(j_clz, "getPackageName", "()Ljava/lang/String;");
    jstring j_package_name = (jstring) env->CallObjectMethod(context, j_mid);
    // 2 . 比对包名是否一样
    const char *c_package_name = env->GetStringUTFChars(j_package_name, NULL);
    if (strcmp(c_package_name, PACKAGE_NAME) != 0) {
        return;
    }
    // 3. 获取签名
    // 3.1 获取 PackageManager
    j_mid = env->GetMethodID(j_clz, "getPackageManager", "()Landroid/content/pm/PackageManager;");
    jobject pack_manager = env->CallObjectMethod(context, j_mid);
    // 3.2 获取 PackageInfo
    j_clz = env->GetObjectClass(pack_manager);
    j_mid = env->GetMethodID(j_clz, "getPackageInfo",
                             "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    jobject package_info = env->CallObjectMethod(pack_manager, j_mid, j_package_name, 0x00000040);
    // 3.3 获取 signatures 数组
    j_clz = env->GetObjectClass(package_info);
    jfieldID j_fid = env->GetFieldID(j_clz, "signatures", "[Landroid/content/pm/Signature;");
    jobjectArray signatures = (jobjectArray) env->GetObjectField(package_info, j_fid);
    // 3.4 获取 signatures[0]
    jobject signatures_first = env->GetObjectArrayElement(signatures, 0);
    // 3.5 调用 signatures[0].toCharsString();
    j_clz = env->GetObjectClass(signatures_first);
    j_mid = env->GetMethodID(j_clz, "toCharsString", "()Ljava/lang/String;");
    jstring j_signature_str = (jstring) env->CallObjectMethod(signatures_first, j_mid);
    const char *c_signature_str = env->GetStringUTFChars(j_signature_str, NULL);
    // 4. 比对签名是否一样
    if (strcmp(c_signature_str, APP_SIGNATURE) != 0) {
        return;
    }
    __android_log_print(ANDROID_LOG_ERROR, "JNI_TAG", "签名校验成功: %s", c_signature_str);
    // 签名认证成功
    is_verify = 1;
}
