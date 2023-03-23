#include <stdio.h>
#include "include/io_utils.h"

int *pointer_at_large;

void DangerousPointer() {
    int a = 2;
    pointer_at_large = &a;
    // ...
    pointer_at_large = NULL;
}

int main(int argc, char* argv[]){
    printf("hello world.\n");
    int a = 10;
    int b;
    // & 地址   * 指针
    int *p = &a;
    PRINT_HEX(p);
    PRINT_HEX(&a);
    PRINT_INT(sizeof(int *));

    PRINT_HEX(*p);
    PRINT_HEX(a);

    // *pp => p
    int **pp = &p;
    PRINT_HEX(*pp);
    PRINT_HEX(p);

    *p = 20;
    PRINT_INT(*p);
    PRINT_INT(a);

    // const 修饰 *
    int *const cp = &a;
    *cp = 2;
    // cp = &b; ERROR

    // 常用
    int const * cp2 = &a; 
    // *cp2 = 2; ERROR
    cp2 = &b; // OK

    int const *const ccp = &a;
    // ccp = &b; ERROR
    // *ccp = 2; ERROR

    int *p2 = NULL;
    // 空指针需要先判断
    if(p2){

    }
    
    // pointer_at_large 野指针
    DangerousPointer();
    PRINT_INT(pointer_at_large);

    int array[] = {0, 1, 2, 3, 4};
    int *ppp = array;
    PRINT_INT(*(ppp+3));
    PRINT_INT(array[3]);
    PRINT_INT(*(array + 3));
    PRINT_INT(ppp[3]);
    int *const array_p = array;

    // left : storage
    // right : value
    return 0;
}