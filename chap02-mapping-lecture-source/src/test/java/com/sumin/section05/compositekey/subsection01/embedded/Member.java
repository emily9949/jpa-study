package com.sumin.section05.compositekey.subsection01.embedded;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Member {

    @EmbeddedId
    private MemberPK memberPK;
    private String phone;
    private String address;

    public Member() {
    }

    public Member(MemberPK memberPK, String phone, String address) {
        this.memberPK = memberPK;
        this.phone = phone;
        this.address = address;
    }

    public MemberPK getMemberPK() {
        return memberPK;
    }

    public void setMemberPK(MemberPK memberPK) {
        this.memberPK = memberPK;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberPK=" + memberPK +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
