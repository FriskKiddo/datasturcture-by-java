package com.kiddo.bloomfilter;

/**
 * @author FriskKiddo
 */
public class BloomFilter<T> {

    /**
     * 二进制向量长度
     */
    private int bitSize;

    /**
     * 二进制向量
     */
    private long[] bits;

    /**
     * 哈希函数个数
     */
    private int hashSize;

    /**
     * @param n 数据规模
     * @param p 误判率
     */
    public BloomFilter(int n, double p) {
        if (n <= 0 || p <= 0 || p >= 1) {
            throw new IllegalArgumentException("Wrong n or p");
        }
        double ln2 = Math.log(2);
        bitSize = (int) (-(n * Math.log(p)) / ln2);
        hashSize = (int) (bitSize * ln2 / n);
        bits = new long[(bitSize + Long.SIZE - 1) / Long.SIZE];
        System.out.println("bitSize = " + bitSize);
        System.out.println("hashSize = " + hashSize);
    }

    public boolean put(T value) {
        nullCheck(value);
        boolean result = false;
        //生成3个索引
        int hash1 = value.hashCode();
        int hash2 = hash1 >> 16;
        for (int i = 0; i < hashSize; i++) {
            int combinedHash = hash1 + (i + hash2);
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            //生成二进位索引
            int index = combinedHash % bitSize;
            if (set(index)) {
                result = true;
            }
        }
        return result;
    }

    public boolean contains(T value) {
        nullCheck(value);
        //生成3个索引
        int hash1 = value.hashCode();
        int hash2 = hash1 >> 16;
        for (int i = 0; i < hashSize; i++) {
            int combinedHash = hash1 + (i + hash2);
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            //生成二进位索引
            int index = combinedHash % bitSize;
            if (!get(index)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 设置index位置的二进位为1
     * @param index
     */
    private boolean set(int index) {
        long value = bits[index / Long.SIZE];
        int bitValue = 1 << (index % Long.SIZE);
        bits[index / Long.SIZE] |= bitValue;
        return (value & bitValue) == 0;
    }

    /**
     * 查询index位置的二进位
     *
     * @param index
     * @return
     */
    private boolean get(int index) {
        long value = bits[index / Long.SIZE];
        return (value & (1 << (index % Long.SIZE))) != 0;
    }

    private void nullCheck(T value) {
        if (value == null) {
            throw new IllegalArgumentException("value must be not null");
        }
    }

}
