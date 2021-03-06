package com.wuba.acm.sort;

import java.util.Random;

/**
 * desc : 选择排序
 * date : 2018/12/26
 * <p>
 * n^2
 * 不稳定排序
 * 原地排序
 *
 * @author : dongSen
 */
public class SelectionSort {

    public static void main(String[] args) {
        // 初始化一个随机序列
        final int MAX_SIZE = 10;
        int[] array = new int[MAX_SIZE];
        Random random = new Random();
        for (int i = 0; i < MAX_SIZE; i++) {
            array[i] = random.nextInt(MAX_SIZE);
        }

        // 调用排序方法
        SelectionSort selection = new SelectionSort();
        System.out.print("排序前:	");
        selection.printAll(array);

        selection.selectionSort(array);

        System.out.print("排序后:	");
        selection.printAll(array);
    }

    public void selectionSort(int[] list) {
        // 需要遍历获得最小值的次数
        // 要注意一点，当要排序 N 个数，已经经过 N-1 次遍历后，已经是有序数列
        for (int i = 0; i < list.length - 1; i++) {
            int index = i; // 用来保存最小值得索引

            // 寻找第i个小的数值
            for (int j = i + 1; j < list.length; j++) {
                if (list[index] > list[j]) {
                    index = j;
                }
            }

            // 将找到的第i个小的数值放在第i个位置上
            int temp = list[index];
            list[index] = list[i];
            list[i] = temp;

            System.out.format("第 %d 趟:	", i + 1);
            printAll(list);
        }
    }

    // 打印完整序列
    private void printAll(int[] list) {
        for (int value : list) {
            System.out.print(value + "	");
        }
        System.out.println();
    }

}