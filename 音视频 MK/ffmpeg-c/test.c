#include <stdio.h>
#include <libavutil/log.h>

int main(int argc, char* argv[]){
    av_log_set_level(AV_LOG_DEBUG);
    av_log(NULL, AV_LOG_INFO, "Hello World. %s\n", "sec");
    return 0;
}
