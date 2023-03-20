import cv2
from cv2 import imwrite

# 创建窗口
cv2.namedWindow('img', cv2.WINDOW_NORMAL)
img = cv2.imread('/Users/sec/Pictures/unnamed.jpeg')

while True:
    # 显示窗口
    cv2.imshow('img', img)
    # 返回键盘动作
    key = cv2.waitKey(0)
    if(key & 0xFF == ord('q')):
        print('exit...')
        break;
    elif(key & 0xFF == ord('s')):
        imwrite('/Users/sec/Pictures/android.png', img)
    else:
        print('other...')
# 销毁所有窗口
cv2.destroyAllWindows()        