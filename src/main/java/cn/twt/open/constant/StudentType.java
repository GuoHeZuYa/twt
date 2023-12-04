package cn.twt.open.constant;

public enum StudentType {

    UNDERGRADUATE(3,"本科生"),
    MASTER(2,"硕士研究生"),
    PHD(1,"博士研究生");

    private StudentType(int typeNum, String typeName){
        this.typeNum = typeNum;
        this.typeName = typeName;
    }

    int typeNum;

    String typeName;

    public int getTypeNum() {
        return typeNum;
    }

    public String getTypeName() {
        return typeName;
    }
}
