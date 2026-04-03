---
layout: post
title: "C语言和数据结构日常随笔"
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
