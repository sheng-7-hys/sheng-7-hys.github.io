---
layout: post
title: "JavaScript 异步编程：从回调到 async/await"
date: 2026-03-15 16:00:00 +0800
categories: [技术, 前端]
tags: [JavaScript, 异步, Promise]
---

JavaScript 的异步编程是前端开发中的重要概念，这篇文章梳理了异步编程的演进历程。

## 回调函数（Callback）

最早的异步方式：

```javascript
setTimeout(() => {
  console.log('1 秒后执行');
}, 1000);
```

嵌套多了就形成「回调地狱」，可读性极差。

## Promise

Promise 解决了回调地狱问题：

```javascript
fetch('/api/data')
  .then(res => res.json())
  .then(data => console.log(data))
  .catch(err => console.error(err));
```

## async / await

ES2017 引入的语法糖，让异步代码像同步一样写：

```javascript
async function getData() {
  try {
    const res = await fetch('/api/data');
    const data = await res.json();
    return data;
  } catch (err) {
    console.error('出错了:', err);
  }
}
```

`async/await` 是目前最推荐的异步写法，代码清晰易读，也便于错误处理。
