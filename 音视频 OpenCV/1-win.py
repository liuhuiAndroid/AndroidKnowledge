import cv2

# 创建窗口
cv2.namedWindow('new', cv2.WINDOW_NORMAL)
cv2.resizeWindow('new', 640, 480)
# 显示窗口
cv2.imshow('new', 0)
# 返回键盘动作
key = cv2.waitKey(0)
if(key == 'q'):
    exit()
# 销毁所有窗口
cv2.destroyAllWindows()