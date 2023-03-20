public class Main {

    static int nStartBit = 0;

    public static void main(String[] args) {
        // SPS 数据
        byte[] h265 = hexStringToByteArray("42 01 01 01 60 00 00 03 00 B0 00 00 03 00 00 03 00 5A A0 04 42 00 F0 77 E5 AE E4 C9 2E A5 20 A0 C0 C0 5D A1 42 50".replace(" ", ""));
        nStartBit = 148;
        // 哥伦布编码
        int width = Ue(h265);
        int height = Ue(h265);
        System.out.println("---------width: " + width + ", height: " + height);
    }

    //       000  00101
//    &  000  10000
//     0          次数 1
//       000  00101   00101---》 十进制
//    &  000  01000

    //     0          次数 2

//       000  00101
//    &  000  00100
//    最终的结果 0 1  不是 0  2
//    如果是1  的就结束
//1 00    是 1  不是  2

//    计算  101的十进制

    public void Ue() {
        //        5位  8位的表示
        int nStartBit = 3;
        byte data = 5 & 0xFF;//字节上的5  0000 0101
//统计0 的个数
        int nZeroNum = 0;
        while (nStartBit < 8) {
            if ((data & (0x80 >> (nStartBit))) != 0) {
                break;
            }
            nZeroNum++;
            nStartBit++;
        }

        nStartBit++;
//跳出循环 到外面记录值  000 1  110      110
//                    0001
//        计算  101的十进制
        int dwRet = 0;//1  0
        for (int i = 0; i < nZeroNum; i++) {
            dwRet <<= 1;//0 <<1   1*2=2 11  0   3*2=6
            if ((data & (0x80 >> (nStartBit % 8))) != 0) {
                dwRet += 1;//6+0 dwRet=6
            }
            nStartBit++;
        }
        int value = (1 << nZeroNum) - 1 + dwRet;
        System.out.println(value);
    }

    //    5
    public static int Ue(byte[] pBuff) {
        //        5位  8位的表示
//统计0 的个数
        int nZeroNum = 0;
        while (nStartBit < pBuff.length * 8) {
            if ((pBuff[nStartBit / 8] & (0x80 >> (nStartBit % 8))) != 0) {
                break;
            }
            nZeroNum++;
            nStartBit++;
        }

        nStartBit++;
//跳出循环 到外面记录值  000 1  110      110
//                    0001
//        计算  101的十进制
        int dwRet = 0;//1  0
        for (int i = 0; i < nZeroNum; i++) {
            dwRet <<= 1;//0 <<1   1*2=2 11  0   3*2=6
            if ((pBuff[nStartBit / 8] & (0x80 >> (nStartBit % 8))) != 0) {
                dwRet += 1;//6+0 dwRet=6
            }
            nStartBit++;
        }
        int value = (1 << nZeroNum) - 1 + dwRet;
        System.out.println(value);
        return value;
    }


    public static byte[] hexStringToByteArray(String s) {
        //十六进制转byte数组
        int len = s.length();
        byte[] bs = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bs[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return bs;
    }

    private static int u(int bitIndex, byte[] h264) {
//    位 索引  字节索引   bitIndex 32
//            3           bitIndex位数
//        0   100 0000
//        nStartBit 开始 100换成10进制
        int dwRet = 0;
        for (int i = 0; i < bitIndex; i++) {
            dwRet <<= 1;
            if ((h264[nStartBit / 8] & (0x80 >> (nStartBit % 8))) != 0) {
                dwRet += 1;
            }
            nStartBit++;
        }
        return dwRet;
    }
}
