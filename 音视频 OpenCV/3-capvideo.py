import cv2
# 创建窗口
cv2.namedWindow('video', cv2.WINDOW_AUTOSIZE)
# 获取视频设备
cap = cv2.VideoCapture(0)
while True:
    # 从摄像头读视频帧
    ret, frame = cap.read()
    # 将视频帧在窗口中显示
    cv2.imshow('video', frame)
    # 等待键盘事件，如果为q就退出
    key = cv2.waitKey(10)
    if(key & 0xFF == ord('q')):
        print('exit...')
        break;
    else:
        print('other...')
# 释放VideoCapture
cap.release()
# 销毁所有窗口
cv2.destroyAllWindows()        