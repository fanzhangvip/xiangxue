//
// Created by Zero on 2019/2/28.
//
#include "stdafx.h"
#include <vector>
#include <cstdint>

namespace _0
{
    template <typename T>
    class ClassA
    {
        T a;
        T* b;
        T foo();
        void foo2(T const&);
    };

    template <int Sz>
    class ClassB
    {
        int arr[Sz];
    };
    size_t  a = sizeof(ClassB<3>);
    size_t  b = sizeof(ClassB<7>);

    template <typename T> void FunctionA(T const& param){}

    template <typename T> T FunctionB()
    {
        return T();
    }
}


