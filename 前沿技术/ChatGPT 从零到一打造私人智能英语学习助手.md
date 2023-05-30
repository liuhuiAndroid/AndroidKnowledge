### 第1章 课程 简介 及 GPT 简述

##### 1-1 导学

- OpenAI 发布 ChatGPT、GPT@3.5、GPT@4
- 国内厂商跟进发布文心一言、通义千问等大语言模型产品
- 基于大语言模型的衍生产品遍地开花

正确姿势：

- 工作代码：AI Copilot 辅助
- ChatGPT 文本辅助

##### 1-2 GPT发展时间线

##### 1-3 GPT案例介绍

##### 1-4 AI 能力回顾

##### 1-5 AI 的局限性

### 第2章 风靡全球 的 ChatGPT 的前世今生以及背后的原理

2-1 为什么是 OpenAI

2-4 GPT 模型应用场景-概述

2-5 问答场景案例介绍-有主题的回答提出的问题

2-6 分类场景-关键词提取、内容分类、文本验测（情感）

2-7 翻译场景

2-9 代码场景

2-10 聊天场景-像人一样聊天

2-11 应用案例-文本转换

2-12 GPT 模型的缺陷

2-13 GPT 缺陷的解决方案

2-14 Prompt 框架

2-15 RIO 原则案例

2-16 Prompt 最佳实践

2-17 最佳实践案例

### 第3章 实战-Al工具箱，包括项目搭建

##### 3-1 基础概念

Token：计算基本单元

Model：模型选型

Prompt：用户指令

Token 换算：英文：0.75 word = 1 token；中文：1汉字 = 2 token

Token 限制：输入内容与输出内容的总计 token 数

##### 3-3 技术选型 & 技术背景

- Cloudflare Workers：脚本转发请求

  官网：https://developers.cloudflare.com/workers/

- Cloudflare Page：应用直接部署在可以访问API的位置

- Vercel：应用直接部署在可以访问API的位置；Vercel Edge function

- .xyz Domain：https://www.name.com/zh-cn/ 购买域名

- Nextjs Application（Edge function）

##### 3-4 技术选型-框架与工具

1. 基础
   1. React & Hook
   2. Typescript
   3. Localstorage
2. 框架/库
   1. 框架：Next.js
   2. UI：Mantine UI：https://github.com/mantinedev/ui.mantine.dev
   3. 样式：Tailwind：https://github.com/tailwindlabs/tailwindcss
3. API
   1. Vercel Edge function
   2. Cloudflare Proxy
4. 部署
   1. Github
   2. Vercel

##### 3-5 AI 助理需求梳理与技术方案梳理

##### 3-6 初始化 Next.js 项目并配置基础开发依赖

```shell
npx create-next-app ai_assistant
npm i @mantine/core @mantine/hooks @mantine/next @mantine/notifications @tabler/icons-react axios clsx -S
```

##### 3-7 OpenAI API 转发与 Proxy 配置

Open AI 官方文档：https://platform.openai.com/docs/api-reference

接口：

1. POST https://api.openai.com/v1/completions
2. POST https://api.openai.com/v1/chat/completions

##### 3-8 聊天功能开发，实现基础对话功能

##### 3-9 实现聊天信息本地持久化

##### 3-11 -实现多轮对话能力

##### 3-12 -支持流式响应

##### 3-13 -支持流式响应

##### 3-14 -前端解码流数据