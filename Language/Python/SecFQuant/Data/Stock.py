import time

from jqdatasdk import *
auth('15300936557','Seckill44.') #账号是申请时所填写的手机号；密码为聚宽官网登录密码

# # 1天/分钟行情数据
# df =get_price('000001.XSHE', count=20, end_date='2022-01-30 14:00:00', frequency='daily')
# print(len(df))
# print(df)

# # 获取所有标的信息
# # 获得所有股票列表
# df=get_all_securities(types=['stock'])
# print(df[:2])
stocks=list(get_all_securities(types=['stock']).index)
print(stocks)

# df =get_price(['000001.XSHE', '000002.XSHE'], count=10, end_date='2022-01-30 14:00:00', frequency='daily')
# print(df)

for stock_code in stocks:
    print("正在获取股票行情数据，股票代码：", stock_code)
    df = get_price(stock_code, count=5, end_date='2022-01-30 14:00:00', frequency='daily')
    print(df)
    time.sleep(2)