#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int data[6];
    int top;
} Sqstack;

Sqstack* InitStack(int size) {
    Sqstack* s = (Sqstack*)malloc(sizeof(Sqstack));
    s->top = -1;
    return s;
}

int StackEmpty(Sqstack* s) {
    return s->top == -1;
}

void Push(Sqstack* s, int e) {
    s->top++;
    s->data[s->top] = e;
}

int Pop(Sqstack* s) {
    int e = s->data[s->top];
    s->top--;
    return e;
}
void move(char from,int n, char to){
    printf("Move disk %d from %c to %c\n", n, from, to);
}
void hanoi(int n, char from, char to, char temp) {
    if(n == 1){
        move(from, 1, to);
    }
    else{
        hanoi(n-1, from, temp, to);
        move(from, n, to);
        hanoi(n-1, temp, to, from);
    }
}

int main(){
    int n;
    printf("Please input the number of disks: ");
    scanf("%d", &n);
    hanoi(n, 'A', 'C', 'B');
    return 0;
}