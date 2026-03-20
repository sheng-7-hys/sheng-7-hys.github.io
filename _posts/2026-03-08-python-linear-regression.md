---
layout: post
title: "Python 机器学习入门：线性回归"
date: 2026-03-08 14:30:00 +0800
categories: [技术, 人工智能]
tags: [Python, 机器学习, 线性回归]
---

机器学习是人工智能的核心分支，而线性回归是最基础也最重要的算法之一。本文记录我的学习笔记。

## 什么是线性回归

线性回归是一种用来预测连续值的监督学习算法。它假设输入特征与输出之间存在线性关系：

```
y = w₁x₁ + w₂x₂ + ... + b
```

## 用 Python 实现

```python
import numpy as np
from sklearn.linear_model import LinearRegression

# 准备数据
X = np.array([[1], [2], [3], [4], [5]])
y = np.array([2, 4, 5, 4, 5])

# 训练模型
model = LinearRegression()
model.fit(X, y)

# 预测
print(f"斜率: {model.coef_[0]:.2f}")
print(f"截距: {model.intercept_:.2f}")
print(f"预测 x=6 时: {model.predict([[6]])[0]:.2f}")
```

## 评估指标

- **MSE**（均方误差）：衡量预测值与真实值的平均平方差
- **R²**（决定系数）：越接近 1 说明模型拟合越好

线性回归虽然简单，但理解它的原理对后续学习更复杂的模型非常有帮助！
