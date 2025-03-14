package com.sumin.section05.compositekey.subsection02.idclass;

/* 설명. IdClass 방식도 e/h를 오버라이딩 해야 한다 */
public class MemberPK {
    private int memberNo;
    private String memberId;

    public MemberPK() {
    }

    public MemberPK(int memberNo, String memberId) {
        this.memberNo = memberNo;
        this.memberId = memberId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
