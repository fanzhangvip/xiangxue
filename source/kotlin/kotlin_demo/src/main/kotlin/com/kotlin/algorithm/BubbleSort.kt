package com.kotlin.algorithm

import java.util.*


fun <T> Array<T>.swap(x: Int, y: Int) {
    val tmp = this[x]
    this[x] = this[y]
    this[y] = tmp
}

/**
 * 冒泡排序是一种简单的排序算法。它重复地走访过要排序的数列，一次比较两个元素，如果它们的顺序错误就把它们交换过来。
 * 走访数列的工作是重复地进行直到没有再需要交换，也就是说该数列已经排序完成。这个算法的名字由来是因为越小的元素会经由交换慢慢“浮”到数列的顶端。
1.1 算法描述
比较相邻的元素。如果第一个比第二个大，就交换它们两个；
对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对，这样在最后的元素应该会是最大的数；
针对所有的元素重复以上的步骤，除了最后一个；
重复步骤1~3，直到排序完成。
 */
fun <T : Int> bubbleSort(array: Array<T>) {
    var N: Int = array.size - 1
    var isSorted = false
    while (!isSorted) {
        isSorted = true
        var i = 0
        while (i < N) {
            if (array[i] > array[i + 1]) {
                array.swap(i, i + 1)
                isSorted = false
                println(" ${Arrays.toString(array)} <- $i-${array.size - N}轮")
            }
            i++
        }
        N--
    }

}

fun <T : Int> selectSort(array: Array<T>) {

    var N: Int = array.size - 1
    var i = 0;
    while (i < N) {
        var minIndex = i
        var j = i
        while (j < N) {
            if (array[minIndex] > array[j + 1]) {
                minIndex = (j + 1)
            }
            j++
        }
        println("minIndex = $minIndex")
        if (i != minIndex) {
            array.swap(i, minIndex)
            println(" ${Arrays.toString(array)} <- ${i}轮")
        }
        i++
    }
}


class  TreeNode{
    constructor(data:Any){
        dataNum++
        datas[dataNum -1] = data
    }

    var dataNum:Int = 0
    var nodeNum:Int = 0

    val nodes = arrayOfNulls<TreeNode>(TwoThreeFourTree.MAX_SIZE)
    val datas = arrayOfNulls<Any>(TwoThreeFourTree.MAX_SIZE)

    fun appendData(data:Any,index:Int){
        System.arraycopy(datas,index,datas,index+1,dataNum-index)
        datas[index] = data
        dataNum++
    }

    fun getMidData():Any?{
        return datas[dataNum/2]
    }

    fun appendNode(node: TreeNode){
        if(node == null){
            return
        }
        nodeNum++
        nodes[nodeNum-1] = node
    }
}

 class TwoThreeFourTree<T : Comparable<T>> {

    companion object {
        val MAX_SIZE: Int = 4
    }

    lateinit var root:TreeNode


}


























fun main(args: Array<String>) {
    val arrays = arrayOf(3, 34, 2, 67, 7, 23, 0, 64, 100, 78)
//    bubbleSort(arrays)
//    selectSort(arrays)

}