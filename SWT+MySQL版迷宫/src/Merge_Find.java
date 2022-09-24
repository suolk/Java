class Merge_Find { // 并查集类
    int[] set; // 集合
    int[] value; // 集合的权重
    int size; // 集合的大小，用于生成集合
    int cnt;

    void create(int Size) { // 初始化
        size = Size; // 获得集合数量
        set = new int[Size]; // 申请内存
        value = new int[Size];
        init(); // 数据初始化
    }

    void init() { // 数据初始化
        cnt = size;
        for (int i = 0; i < size; i++) {
            set[i] = i;
            value[i] = 1;
        }
    }

    int Find_Root(int Index) { // 查找根节点
        while (set[Index] != Index) Index = set[Index];
        return Index;
    }

    int Path_Find(int Index) { // 路径压缩
        int Root = Find_Root(Index);
        while (set[Index] != Root) // 令子节点直接指向根节点
        {
            int id = Index;
            Index = set[Index];
            set[id] = Root;
        }
        return Root;
    }

    boolean Set_Union(int set1, int set2) { // 合并集合，返回值代表是否合并集合
        int i = Path_Find(set1), j = Path_Find(set2);
        if (i == j) // 说明是同一个集合无需合并
            return false;
        cnt--; // 若合并则集合数减少1
        if (value[i] < value[j]) // 将权重小的集合合并至权重大的集合中
        {
            value[j] += value[i];
            set[i] = j;
        } else {
            value[i] += value[j];
            set[j] = i;
        }
        return true;
    }

    int Set_Quantity() { // 返回剩余集合数
        return cnt;
    }
}
