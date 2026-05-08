---
layout: post
title: "数据结构-线性表·栈·队列"
date: 2026-04-03 20:14:00 +0800
categories: [CPL]
tags: [C]
---

- `free(p)`只释放该空间, 标记该空间为可复用，空间中数据暂时**不会被清除**，指针值未变，需要使用`p=NULL`，防止野指针。  
- 顺序表函数`clearlist(Sqlist &L)`仅使length变为0，所存储值仍也存在，待覆盖。
- 头插法可用于建立逆向链表
- 就地逆转单链表方法
```c++
typedef struct LNode{
    elemtype data;
    struct LNode *next;
}LNode,*LinkList;
LinkList Reverse(Linklist &L){
    if(L->next==NULL||L->next->next) return L;
    LNode *pre = NULL;
    LNode *cur = L->next;
    LNode *next = NULL;
    while (cur != NULL){
        next=cur->next;  //保留直接后继
        cur->next=pre;  //调转方向
        pre=cur;       //后移
        cur=next;
    }
    L->next=pre;  //执行完循环操作后cur为原尾节的next，为NULL，pre为新的首元，令原头指针指向它
}
```
`}LNode,*LinkList;`相当于给`struct LNode`起了两个别名，同时使得`LinkList`直接为指向这种结构的指针，方便后续简便书写。  
- 在涉及到某个函数调用比较方法时，可以考虑使用函数指针，方便运用不同的比较规则

# 栈分为顺序栈和链栈
## SqStack
```c++
typedef struct{
    elemtype *base;   //基址，站地指针
    elemtype *top; //栈顶
    int size;
}*SqStack
```

`top`指向下一个可以入栈的空位置（约定）  
### 栈的应用
- 数制转换
- - eg.10->8
```c++
void conversion(unsigned int n){
    Sqstack* s = InitStack(6);
    while(n){
        Push(s,n%8);
        n/=8;
    }
    while(!StackEmpty(s)){
        printf("%d",Pop(s));
    }
}
```
- - 汉诺塔
