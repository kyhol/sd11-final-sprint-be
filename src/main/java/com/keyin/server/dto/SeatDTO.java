package com.keyin.server.dto;

public class SeatDTO {

    private Long id;
    private Long screenId;       // we'll store the screen's ID only
    private String rowLetter;
    private Integer seatNumber;
    private String seatType;
    private Boolean available;

    public SeatDTO() {
    }

    public SeatDTO(Long id, Long screenId, String rowLetter, Integer seatNumber,
                   String seatType, Boolean available) {
        this.id = id;
        this.screenId = screenId;
        this.rowLetter = rowLetter;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public String getRowLetter() {
        return rowLetter;
    }

    public void setRowLetter(String rowLetter) {
        this.rowLetter = rowLetter;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
