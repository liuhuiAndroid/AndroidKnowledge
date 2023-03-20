//
// Created by sec on 2022/11/6.
//

#ifndef MUSICPLAYER_MUSICPLAYSTATUS_H
#define MUSICPLAYER_MUSICPLAYSTATUS_H

class MusicPlayStatus {

public:
    bool exit;
    bool seek = false;
    bool pause = false;
    bool load = true;
public:
    MusicPlayStatus();
};


#endif //MUSICPLAYER_MUSICPLAYSTATUS_H
