package com.lottery.web.service.dto;

import java.io.Serializable;
import com.lottery.web.domain.enumeration.State;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the UserProfile entity. This class is used in UserProfileResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /user-profiles?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserProfileCriteria implements Serializable {
    /**
     * Class for filtering State
     */
    public static class StateFilter extends Filter<State> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StateFilter state;

    private StringFilter city;

    private StringFilter address;

    private StringFilter phone;

    private StringFilter imagepath;

    private StringFilter firstname;

    private StringFilter lastname;

    private StringFilter email;

    private StringFilter username;

    private LocalDateFilter since;

    private LongFilter userId;

    private LongFilter lotteryId;

    public UserProfileCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StateFilter getState() {
        return state;
    }

    public void setState(StateFilter state) {
        this.state = state;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getImagepath() {
        return imagepath;
    }

    public void setImagepath(StringFilter imagepath) {
        this.imagepath = imagepath;
    }

    public StringFilter getFirstname() {
        return firstname;
    }

    public void setFirstname(StringFilter firstname) {
        this.firstname = firstname;
    }

    public StringFilter getLastname() {
        return lastname;
    }

    public void setLastname(StringFilter lastname) {
        this.lastname = lastname;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getUsername() {
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public LocalDateFilter getSince() {
        return since;
    }

    public void setSince(LocalDateFilter since) {
        this.since = since;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(LongFilter lotteryId) {
        this.lotteryId = lotteryId;
    }

    @Override
    public String toString() {
        return "UserProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (imagepath != null ? "imagepath=" + imagepath + ", " : "") +
                (firstname != null ? "firstname=" + firstname + ", " : "") +
                (lastname != null ? "lastname=" + lastname + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (username != null ? "username=" + username + ", " : "") +
                (since != null ? "since=" + since + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (lotteryId != null ? "lotteryId=" + lotteryId + ", " : "") +
            "}";
    }

}
